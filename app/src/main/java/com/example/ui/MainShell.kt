package com.example.ui

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.model.MainTab
import com.example.ui.components.ThemePickerModalSheet
import com.example.ui.components.TocBottomSheet
import com.example.ui.screens.CharactersScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.WorldScreen
import com.example.ui.theme.LocalEpicColors
import com.example.ui.theme.RaktabeejTheme
import com.example.ui.viewmodel.EpicViewModel

private data class NavDestination(
    val tab: MainTab,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String,
)

private val NAV_ITEMS = listOf(
    NavDestination(
        tab = MainTab.OVERVIEW,
        labelRes = R.string.nav_overview,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        testTag = "nav_dashboard",
    ),
    NavDestination(
        tab = MainTab.WORLD,
        labelRes = R.string.nav_world,
        selectedIcon = Icons.Filled.Public,
        unselectedIcon = Icons.Outlined.Public,
        testTag = "nav_world",
    ),
    NavDestination(
        tab = MainTab.CAST,
        labelRes = R.string.nav_cast,
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        testTag = "nav_cast",
    ),
    NavDestination(
        tab = MainTab.READER,
        labelRes = R.string.nav_reader,
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
        testTag = "nav_reader",
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaktabeejAppRoot(viewModel: EpicViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val activity = context as? Activity

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/plain"),
    ) { uri ->
        if (uri != null) {
            viewModel.saveTxtToUri(uri)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.shareIntentEvents.collect { shareIntent ->
            context.startActivity(shareIntent)
        }
    }

    BackHandler(enabled = !uiState.focusMode && uiState.activeTab != MainTab.OVERVIEW) {
        viewModel.selectTab(MainTab.OVERVIEW)
    }

    RaktabeejTheme(themeMode = uiState.themeMode) {
        val epic = LocalEpicColors.current

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useNavigationRail = maxWidth >= 600.dp

            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    if (!uiState.focusMode) {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            title = {
                                Column {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        style = MaterialTheme.typography.titleLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Text(
                                        text = stringResource(R.string.epic_subtitle),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = epic.goldAccent,
                                        maxLines = 1,
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.setTocSheetVisible(true) },
                                    modifier = Modifier.testTag("topbar_toc_btn"),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FormatListNumbered,
                                        contentDescription = stringResource(R.string.action_toc),
                                        tint = epic.goldAccent,
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.setThemeSheetVisible(true) },
                                    modifier = Modifier.testTag("topbar_theme_btn"),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Palette,
                                        contentDescription = stringResource(R.string.cd_theme_picker),
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                            },
                        )
                    }
                },
                bottomBar = {
                    if (!useNavigationRail && !uiState.focusMode) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ) {
                            NAV_ITEMS.forEach { item ->
                                val selected = uiState.activeTab == item.tab
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(item.tab) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = stringResource(item.labelRes),
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(item.labelRes),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = epic.goldAccent,
                                        selectedTextColor = epic.goldAccent,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                    modifier = Modifier.testTag(item.testTag),
                                )
                            }
                        }
                    }
                },
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    if (useNavigationRail && !uiState.focusMode) {
                        NavigationRail(
                            containerColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            Spacer(modifier = Modifier.weight(0.2f))
                            NAV_ITEMS.forEach { item ->
                                val selected = uiState.activeTab == item.tab
                                NavigationRailItem(
                                    selected = selected,
                                    onClick = { viewModel.selectTab(item.tab) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = stringResource(item.labelRes),
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(item.labelRes),
                                            maxLines = 1,
                                        )
                                    },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = epic.goldAccent,
                                        selectedTextColor = epic.goldAccent,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                    modifier = Modifier.testTag(item.testTag),
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                    ) {
                        when {
                            uiState.isLoading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(
                                            color = epic.goldAccent,
                                            modifier = Modifier.size(42.dp),
                                        )
                                        Spacer(modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "পাণ্ডুলিপি প্রস্তুত হচ্ছে...",
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                    }
                                }
                            }
                            uiState.errorMessage != null -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = uiState.errorMessage ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                            else -> {
                                when (uiState.activeTab) {
                                    MainTab.OVERVIEW -> DashboardScreen(
                                        uiState = uiState,
                                        onContinueReading = {
                                            viewModel.openReaderAtBlock(uiState.lastReadBlockIndex)
                                        },
                                        onOpenToc = { viewModel.setTocSheetVisible(true) },
                                        onOpenThemePicker = { viewModel.setThemeSheetVisible(true) },
                                        onSaveTxt = {
                                            createDocumentLauncher.launch("Raktabeej_Mahakavya_Original_Manuscript.txt")
                                        },
                                        onShareTxt = { viewModel.shareOriginalTxt() },
                                        onPrintManuscript = {
                                            if (activity != null) {
                                                viewModel.printManuscript(activity)
                                            }
                                        },
                                        onJumpToVolume = { blockIndex ->
                                            viewModel.openReaderAtBlock(blockIndex)
                                        },
                                    )
                                    MainTab.WORLD -> WorldScreen(
                                        onSearchInManuscript = { token ->
                                            viewModel.selectTab(MainTab.READER)
                                            if (!uiState.isSearchBarExpanded) {
                                                viewModel.toggleSearchBar()
                                            }
                                            viewModel.onSearchQueryChange(token)
                                        },
                                    )
                                    MainTab.CAST -> CharactersScreen(
                                        onSearchInManuscript = { token ->
                                            viewModel.selectTab(MainTab.READER)
                                            if (!uiState.isSearchBarExpanded) {
                                                viewModel.toggleSearchBar()
                                            }
                                            viewModel.onSearchQueryChange(token)
                                        },
                                    )
                                    MainTab.READER -> ReaderScreen(
                                        uiState = uiState,
                                        onOpenToc = { viewModel.setTocSheetVisible(true) },
                                        onOpenThemePicker = { viewModel.setThemeSheetVisible(true) },
                                        onToggleSearch = { viewModel.toggleSearchBar() },
                                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                                        onStepSearchHit = { viewModel.stepSearchHit(it) },
                                        onJumpToSearchHit = { viewModel.jumpToSearchHit(it) },
                                        onAdjustFontSize = { viewModel.adjustFontSize(it) },
                                        onResetFontSize = { viewModel.resetFontSize() },
                                        onToggleFocusMode = { viewModel.toggleFocusMode() },
                                        onExitFocusMode = { viewModel.exitFocusMode() },
                                        onToggleBookmark = { viewModel.toggleBookmarkAt(it) },
                                        onScrollChanged = { idx, total ->
                                            viewModel.onReaderScrollChanged(idx, total)
                                        },
                                        onConsumePendingScroll = { viewModel.consumePendingScroll() },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.showTocSheet) {
                TocBottomSheet(
                    volumes = uiState.volumes,
                    allHeadings = uiState.allHeadings,
                    bookmarkedBlocks = uiState.bookmarkedBlocks,
                    blocks = uiState.blocks,
                    currentBlockIndex = uiState.currentVisibleBlockIndex,
                    onJumpToBlock = { idx -> viewModel.openReaderAtBlock(idx) },
                    onDismiss = { viewModel.setTocSheetVisible(false) },
                )
            }

            if (uiState.showThemeSheet) {
                ThemePickerModalSheet(
                    currentTheme = uiState.themeMode,
                    onSelectTheme = { mode -> viewModel.setThemeMode(mode) },
                    onDismiss = { viewModel.setThemeSheetVisible(false) },
                )
            }
        }
    }
}
