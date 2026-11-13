package com.example.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ManuscriptRepository
import com.example.data.UserPreferencesRepository
import com.example.model.EpicThemeMode
import com.example.model.MainTab
import com.example.model.ManuscriptBlock
import com.example.model.SearchHit
import com.example.model.TocEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EpicUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val activeTab: MainTab = MainTab.OVERVIEW,
    val themeMode: EpicThemeMode = EpicThemeMode.IMPERIAL_DARK,
    val fontSizeSp: Float = 17f,
    val focusMode: Boolean = false,
    val blocks: List<ManuscriptBlock> = emptyList(),
    val allHeadings: List<TocEntry> = emptyList(),
    val volumes: List<TocEntry> = emptyList(),
    val lastReadBlockIndex: Int = 0,
    val currentVisibleBlockIndex: Int = 0,
    val readingProgressPercent: Int = 0,
    val bookmarkedBlocks: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val isSearchBarExpanded: Boolean = false,
    val searchHits: List<SearchHit> = emptyList(),
    val activeHitIndex: Int = -1,
    val showTocSheet: Boolean = false,
    val showThemeSheet: Boolean = false,
    val pendingScrollToBlock: Int? = null,
    val scrollNonce: Long = 0L,
)

class EpicViewModel(application: Application) : AndroidViewModel(application) {

    private val manuscriptRepo = ManuscriptRepository(application)
    private val prefsRepo = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(EpicUiState())
    val uiState: StateFlow<EpicUiState> = _uiState.asStateFlow()

    private val _snackbarEvents = MutableSharedFlow<String>()
    val snackbarEvents: SharedFlow<String> = _snackbarEvents.asSharedFlow()

    private val _shareIntentEvents = MutableSharedFlow<Intent>()
    val shareIntentEvents: SharedFlow<Intent> = _shareIntentEvents.asSharedFlow()

    private var searchJob: Job? = null
    private var progressSaveJob: Job? = null

    init {
        loadAllData()
        observePreferences()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            runCatching {
                manuscriptRepo.loadManuscript()
            }.onSuccess { loaded ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        blocks = loaded.blocks,
                        allHeadings = loaded.allHeadings,
                        volumes = loaded.volumes,
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = err.localizedMessage ?: "পাণ্ডুলিপি লোড করতে সমস্যা হয়েছে",
                    )
                }
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            prefsRepo.preferencesFlow.collect { prefs ->
                _uiState.update { state ->
                    state.copy(
                        themeMode = prefs.themeMode,
                        fontSizeSp = prefs.fontSizeSp,
                        lastReadBlockIndex = prefs.lastReadBlockIndex,
                        readingProgressPercent = prefs.readingProgressPercent,
                        bookmarkedBlocks = prefs.bookmarkedBlockIndices,
                    )
                }
            }
        }
    }

    fun selectTab(tab: MainTab) {
        _uiState.update {
            it.copy(
                activeTab = tab,
                focusMode = if (tab != MainTab.READER) false else it.focusMode,
            )
        }
    }

    fun openReaderAtBlock(blockIndex: Int) {
        _uiState.update {
            it.copy(
                activeTab = MainTab.READER,
                pendingScrollToBlock = blockIndex.coerceAtLeast(0),
                scrollNonce = it.scrollNonce + 1L,
                showTocSheet = false,
            )
        }
    }

    fun consumePendingScroll() {
        _uiState.update { it.copy(pendingScrollToBlock = null) }
    }

    fun setThemeMode(mode: EpicThemeMode) {
        _uiState.update { it.copy(themeMode = mode, showThemeSheet = false) }
        viewModelScope.launch { prefsRepo.setThemeMode(mode) }
    }

    fun adjustFontSize(delta: Float) {
        val updated = (_uiState.value.fontSizeSp + delta).coerceIn(14f, 26f)
        _uiState.update { it.copy(fontSizeSp = updated) }
        viewModelScope.launch { prefsRepo.setFontSize(updated) }
    }

    fun resetFontSize() {
        _uiState.update { it.copy(fontSizeSp = 17f) }
        viewModelScope.launch { prefsRepo.setFontSize(17f) }
    }

    fun toggleFocusMode() {
        _uiState.update { it.copy(focusMode = !it.focusMode) }
    }

    fun exitFocusMode() {
        _uiState.update { it.copy(focusMode = false) }
    }

    fun setTocSheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showTocSheet = visible) }
    }

    fun setThemeSheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showThemeSheet = visible) }
    }

    fun toggleSearchBar() {
        val nextExpanded = !_uiState.value.isSearchBarExpanded
        _uiState.update {
            if (!nextExpanded) {
                it.copy(
                    isSearchBarExpanded = false,
                    searchQuery = "",
                    searchHits = emptyList(),
                    activeHitIndex = -1,
                )
            } else {
                it.copy(isSearchBarExpanded = true)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(220)
            val hits = manuscriptRepo.searchBlocks(query, _uiState.value.blocks)
            _uiState.update {
                it.copy(
                    searchHits = hits,
                    activeHitIndex = if (hits.isNotEmpty()) 0 else -1,
                )
            }
            if (hits.isNotEmpty()) {
                openReaderAtBlock(hits.first().blockIndex)
            }
        }
    }

    fun stepSearchHit(forward: Boolean) {
        val state = _uiState.value
        val hits = state.searchHits
        if (hits.isEmpty()) return
        val nextIdx = if (forward) {
            (state.activeHitIndex + 1) % hits.size
        } else {
            if (state.activeHitIndex <= 0) hits.lastIndex else state.activeHitIndex - 1
        }
        _uiState.update { it.copy(activeHitIndex = nextIdx) }
        openReaderAtBlock(hits[nextIdx].blockIndex)
    }

    fun jumpToSearchHit(hitListIndex: Int) {
        val hits = _uiState.value.searchHits
        if (hitListIndex !in hits.indices) return
        _uiState.update { it.copy(activeHitIndex = hitListIndex) }
        openReaderAtBlock(hits[hitListIndex].blockIndex)
    }

    fun onReaderScrollChanged(firstVisibleIndex: Int, totalCount: Int) {
        if (totalCount <= 1) return
        val pct = ((firstVisibleIndex.toFloat() / (totalCount - 1).coerceAtLeast(1)) * 100f)
            .toInt()
            .coerceIn(0, 100)
        _uiState.update {
            it.copy(
                currentVisibleBlockIndex = firstVisibleIndex,
                lastReadBlockIndex = firstVisibleIndex,
                readingProgressPercent = pct,
            )
        }
        progressSaveJob?.cancel()
        progressSaveJob = viewModelScope.launch {
            delay(500)
            prefsRepo.updateReadingPosition(firstVisibleIndex, pct)
        }
    }

    fun toggleBookmarkAt(blockIndex: Int) {
        val wasBookmarked = _uiState.value.bookmarkedBlocks.contains(blockIndex)
        viewModelScope.launch {
            prefsRepo.toggleBookmark(blockIndex)
            _snackbarEvents.emit(
                if (wasBookmarked) "বুকমার্ক সরানো হয়েছে" else "অধ্যায়টি বুকমার্ক করা হয়েছে",
            )
        }
    }

    fun saveTxtToUri(uri: Uri) {
        viewModelScope.launch {
            manuscriptRepo.writeOriginalTxtToUri(uri)
                .onSuccess { _snackbarEvents.emit("মূল পাণ্ডুলিপি TXT সংরক্ষিত হয়েছে") }
                .onFailure { _snackbarEvents.emit("TXT সংরক্ষণ করা যায়নি") }
        }
    }

    fun shareOriginalTxt() {
        viewModelScope.launch {
            manuscriptRepo.createShareTxtIntent()
                .onSuccess { intent -> _shareIntentEvents.emit(intent) }
                .onFailure { _snackbarEvents.emit("TXT শেয়ার তৈরি করা যায়নি") }
        }
    }

    fun printManuscript(activity: Activity) {
        val blocks = _uiState.value.blocks
        if (blocks.isEmpty()) return
        manuscriptRepo.launchManuscriptPrint(activity, blocks)
    }
}
