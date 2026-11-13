package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ManuscriptBlock
import com.example.ui.components.formatRichEpicText
import com.example.ui.components.toBengaliDigits
import com.example.ui.theme.BengaliSerifFamily
import com.example.ui.theme.LocalEpicColors
import com.example.ui.viewmodel.EpicUiState
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ReaderScreen(
    uiState: EpicUiState,
    onOpenToc: () -> Unit,
    onOpenThemePicker: () -> Unit,
    onToggleSearch: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onStepSearchHit: (Boolean) -> Unit,
    onJumpToSearchHit: (Int) -> Unit,
    onAdjustFontSize: (Float) -> Unit,
    onResetFontSize: () -> Unit,
    onToggleFocusMode: () -> Unit,
    onExitFocusMode: () -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onScrollChanged: (Int, Int) -> Unit,
    onConsumePendingScroll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = uiState.lastReadBlockIndex.coerceIn(
            0,
            (uiState.blocks.size - 1).coerceAtLeast(0),
        ),
    )

    BackHandler(enabled = uiState.focusMode) {
        onExitFocusMode()
    }

    LaunchedEffect(uiState.scrollNonce, uiState.pendingScrollToBlock) {
        val target = uiState.pendingScrollToBlock
        if (target != null && target in uiState.blocks.indices) {
            listState.scrollToItem(target)
            onConsumePendingScroll()
        }
    }

    LaunchedEffect(listState, uiState.blocks.size) {
        if (uiState.blocks.isNotEmpty()) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { firstIdx ->
                    onScrollChanged(firstIdx, uiState.blocks.size)
                }
        }
    }

    val currentHeading by remember(uiState.allHeadings, uiState.currentVisibleBlockIndex) {
        derivedStateOf {
            uiState.allHeadings.lastOrNull { it.blockIndex <= uiState.currentVisibleBlockIndex }?.title
                ?: "রক্তবীজ মহাকাব্য"
        }
    }

    val activeHitBlockIndex = uiState.searchHits.getOrNull(uiState.activeHitIndex)?.blockIndex ?: -1
    val isCurrentBookmarked = uiState.bookmarkedBlocks.contains(uiState.currentVisibleBlockIndex)

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Progress Line (always visible cleanly)
            LinearProgressIndicator(
                progress = { (uiState.readingProgressPercent / 100f).coerceIn(0f, 1f) },
                color = epic.goldAccent,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
            )

            // Reader Controls Chrome (hidden in Focus Mode)
            AnimatedVisibility(visible = !uiState.focusMode) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentHeading,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "অগ্রগতি: ${toBengaliDigits(uiState.readingProgressPercent)}% · ফন্ট: ${toBengaliDigits(uiState.fontSizeSp.toInt())}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = epic.mutedText,
                                )
                            }

                            IconButton(
                                onClick = onOpenToc,
                                modifier = Modifier.testTag("reader_toc_btn"),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FormatListBulleted,
                                    contentDescription = stringResource(R.string.action_toc),
                                    tint = epic.goldAccent,
                                )
                            }
                            IconButton(
                                onClick = onToggleSearch,
                                modifier = Modifier.testTag("reader_search_btn"),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(R.string.cd_search),
                                    tint = if (uiState.isSearchBarExpanded) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
                                )
                            }
                            IconButton(
                                onClick = { onToggleBookmark(uiState.currentVisibleBlockIndex) },
                                modifier = Modifier.testTag("reader_bookmark_btn"),
                            ) {
                                Icon(
                                    imageVector = if (isCurrentBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                    contentDescription = stringResource(R.string.cd_bookmark),
                                    tint = if (isCurrentBookmarked) epic.goldAccent else MaterialTheme.colorScheme.onSurface,
                                )
                            }
                            IconButton(
                                onClick = onToggleFocusMode,
                                modifier = Modifier.testTag("focus_mode_btn"),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Fullscreen,
                                    contentDescription = stringResource(R.string.action_focus_mode),
                                )
                            }
                        }

                        // Compact Typography & Theme Quick Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, epic.hairlineBorder),
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TextButton(
                                        onClick = { onAdjustFontSize(-1.5f) },
                                        modifier = Modifier.testTag("font_down_btn"),
                                    ) {
                                        Text("A−", fontWeight = FontWeight.Bold)
                                    }
                                    TextButton(
                                        onClick = onResetFontSize,
                                        modifier = Modifier.testTag("font_reset_btn"),
                                    ) {
                                        Text("A", fontWeight = FontWeight.Bold)
                                    }
                                    TextButton(
                                        onClick = { onAdjustFontSize(1.5f) },
                                        modifier = Modifier.testTag("font_up_btn"),
                                    ) {
                                        Text("A+", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, epic.hairlineBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(onClick = onOpenThemePicker)
                                    .testTag("reader_theme_btn"),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Palette,
                                        contentDescription = stringResource(R.string.cd_theme_picker),
                                        tint = epic.goldAccent,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = uiState.themeMode.bengaliLabel,
                                        style = MaterialTheme.typography.labelLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }

                        // Expandable Search Panel
                        AnimatedVisibility(visible = uiState.isSearchBarExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .padding(bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    OutlinedTextField(
                                        value = uiState.searchQuery,
                                        onValueChange = onSearchQueryChange,
                                        singleLine = true,
                                        placeholder = {
                                            Text(
                                                text = stringResource(R.string.search_placeholder),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        },
                                        trailingIcon = {
                                            IconButton(onClick = onToggleSearch) {
                                                Icon(
                                                    imageVector = Icons.Filled.Close,
                                                    contentDescription = stringResource(R.string.cd_clear_search),
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("search_input"),
                                    )
                                    IconButton(
                                        onClick = { onStepSearchHit(false) },
                                        enabled = uiState.searchHits.isNotEmpty(),
                                        modifier = Modifier.testTag("search_prev_btn"),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowUp,
                                            contentDescription = stringResource(R.string.cd_prev_match),
                                        )
                                    }
                                    IconButton(
                                        onClick = { onStepSearchHit(true) },
                                        enabled = uiState.searchHits.isNotEmpty(),
                                        modifier = Modifier.testTag("search_next_btn"),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.KeyboardArrowDown,
                                            contentDescription = stringResource(R.string.cd_next_match),
                                        )
                                    }
                                }

                                if (uiState.searchQuery.trim().length >= 2) {
                                    Text(
                                        text = if (uiState.searchHits.isEmpty()) {
                                            "কোনো ফলাফল পাওয়া যায়নি"
                                        } else {
                                            "ফলাফল: ${toBengaliDigits(uiState.activeHitIndex + 1)} / ${toBengaliDigits(uiState.searchHits.size)} অনুচ্ছেদ"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = epic.goldAccent,
                                    )
                                    if (uiState.searchHits.isNotEmpty()) {
                                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            itemsIndexed(
                                                uiState.searchHits.take(30),
                                                key = { idx, hit -> "${idx}_${hit.blockIndex}" },
                                            ) { idx, hit ->
                                                val isActive = idx == uiState.activeHitIndex
                                                Surface(
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = if (isActive) MaterialTheme.colorScheme.primaryContainer
                                                    else MaterialTheme.colorScheme.surfaceVariant,
                                                    border = BorderStroke(
                                                        1.dp,
                                                        if (isActive) epic.goldAccent else epic.hairlineBorder,
                                                    ),
                                                    modifier = Modifier
                                                        .width(220.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .clickable { onJumpToSearchHit(idx) },
                                                ) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Text(
                                                            text = hit.sectionTitle,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = epic.goldAccent,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                        )
                                                        Text(
                                                            text = hit.snippet,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            maxLines = 2,
                                                            overflow = TextOverflow.Ellipsis,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(color = epic.hairlineBorder)
                    }
                }
            }

            // Main Unabridged Manuscript Flow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 760.dp),
                    contentPadding = PaddingValues(
                        horizontal = if (uiState.focusMode) 22.dp else 18.dp,
                        vertical = if (uiState.focusMode) 54.dp else 18.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.blocks,
                        key = { it.blockIndex },
                    ) { block ->
                        ManuscriptBlockItem(
                            block = block,
                            fontSizeSp = uiState.fontSizeSp,
                            searchQuery = uiState.searchQuery,
                            isActiveHitBlock = block.blockIndex == activeHitBlockIndex,
                            isBookmarked = uiState.bookmarkedBlocks.contains(block.blockIndex),
                            onToggleBookmark = { onToggleBookmark(block.blockIndex) },
                        )
                    }

                    item(key = "reader_footer") {
                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = epic.hairlineBorder)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "— রক্তবীজ মহাকাব্য: কালসর্পের পঞ্চবলয় (সম্পূর্ণ পাণ্ডুলিপি সমাপ্ত) —",
                            style = MaterialTheme.typography.titleSmall,
                            color = epic.goldAccent,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                    }
                }
            }
        }

        // Floating Exit Focus Mode Pill
        if (uiState.focusMode) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                border = BorderStroke(1.dp, epic.goldAccent.copy(alpha = 0.6f)),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onExitFocusMode)
                    .testTag("exit_focus_mode_btn"),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.FullscreenExit,
                        contentDescription = stringResource(R.string.action_exit_focus),
                        tint = epic.goldAccent,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = stringResource(R.string.action_exit_focus),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun ManuscriptBlockItem(
    block: ManuscriptBlock,
    fontSizeSp: Float,
    searchQuery: String,
    isActiveHitBlock: Boolean,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
) {
    val epic = LocalEpicColors.current
    val lineHeightSp = fontSizeSp * 1.72f

    when (block) {
        is ManuscriptBlock.H1Volume -> {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, epic.goldAccent.copy(alpha = 0.7f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 4.dp),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "মহাপর্ব ${toBengaliDigits(block.volumeNumber)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = epic.goldAccent,
                        )
                        IconButton(onClick = onToggleBookmark) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                contentDescription = stringResource(R.string.cd_bookmark),
                                tint = if (isBookmarked) epic.goldAccent else epic.mutedText,
                            )
                        }
                    }
                    Text(
                        text = formatRichEpicText(
                            raw = block.text,
                            searchQuery = searchQuery,
                            isActiveHitBlock = isActiveHitBlock,
                            highlightBg = epic.searchHighlightBg,
                            activeHighlightBg = epic.searchActiveHighlightBg,
                            highlightText = epic.searchHighlightText,
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        is ManuscriptBlock.H2Title -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = formatRichEpicText(
                        raw = block.text,
                        searchQuery = searchQuery,
                        isActiveHitBlock = isActiveHitBlock,
                        highlightBg = epic.searchHighlightBg,
                        activeHighlightBg = epic.searchActiveHighlightBg,
                        highlightText = epic.searchHighlightText,
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    color = epic.goldAccent,
                )
                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = epic.goldAccent.copy(alpha = 0.4f))
            }
        }

        is ManuscriptBlock.H3Chapter -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 2.dp),
            ) {
                Text(
                    text = formatRichEpicText(
                        raw = block.text,
                        searchQuery = searchQuery,
                        isActiveHitBlock = isActiveHitBlock,
                        highlightBg = epic.searchHighlightBg,
                        activeHighlightBg = epic.searchActiveHighlightBg,
                        highlightText = epic.searchHighlightText,
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        is ManuscriptBlock.H4SubSection -> {
            Text(
                text = formatRichEpicText(
                    raw = block.text,
                    searchQuery = searchQuery,
                    isActiveHitBlock = isActiveHitBlock,
                    highlightBg = epic.searchHighlightBg,
                    activeHighlightBg = epic.searchActiveHighlightBg,
                    highlightText = epic.searchHighlightText,
                ),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = epic.crimsonAccent,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        is ManuscriptBlock.Paragraph -> {
            val formatted = formatRichEpicText(
                raw = block.text,
                searchQuery = searchQuery,
                isActiveHitBlock = isActiveHitBlock,
                highlightBg = epic.searchHighlightBg,
                activeHighlightBg = epic.searchActiveHighlightBg,
                highlightText = epic.searchHighlightText,
                boldColor = MaterialTheme.colorScheme.onBackground,
            )
            if (block.isChapterOpening && formatted.text.length > 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = formatted.text.substring(0, 1),
                        fontFamily = BengaliSerifFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = (fontSizeSp * 2.15f).sp,
                        lineHeight = (fontSizeSp * 2.2f).sp,
                        color = epic.goldAccent,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text(
                        text = formatted.subSequence(1, formatted.length),
                        fontFamily = BengaliSerifFamily,
                        fontSize = fontSizeSp.sp,
                        lineHeight = lineHeightSp.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Text(
                    text = formatted,
                    fontFamily = BengaliSerifFamily,
                    fontSize = fontSizeSp.sp,
                    lineHeight = lineHeightSp.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        is ManuscriptBlock.Quote -> {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = epic.quoteBackground,
                border = BorderStroke(1.dp, epic.goldAccent.copy(alpha = 0.55f)),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(64.dp)
                            .background(epic.goldAccent, RoundedCornerShape(2.dp)),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = formatRichEpicText(
                            raw = block.text,
                            searchQuery = searchQuery,
                            isActiveHitBlock = isActiveHitBlock,
                            highlightBg = epic.searchHighlightBg,
                            activeHighlightBg = epic.searchActiveHighlightBg,
                            highlightText = epic.searchHighlightText,
                        ),
                        fontFamily = BengaliSerifFamily,
                        fontSize = (fontSizeSp + 1f).sp,
                        lineHeight = (lineHeightSp + 2f).sp,
                        color = epic.goldAccent,
                    )
                }
            }
        }

        is ManuscriptBlock.BulletList -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                block.items.forEachIndexed { idx, itemText ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = if (block.ordered) "${toBengaliDigits(idx + 1)}." else "◆",
                            fontSize = fontSizeSp.sp,
                            color = epic.goldAccent,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = formatRichEpicText(
                                raw = itemText,
                                searchQuery = searchQuery,
                                isActiveHitBlock = isActiveHitBlock,
                                highlightBg = epic.searchHighlightBg,
                                activeHighlightBg = epic.searchActiveHighlightBg,
                                highlightText = epic.searchHighlightText,
                                boldColor = epic.goldAccent,
                            ),
                            fontFamily = BengaliSerifFamily,
                            fontSize = fontSizeSp.sp,
                            lineHeight = lineHeightSp.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        is ManuscriptBlock.AsciiMap -> {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = epic.codeBackground),
                border = BorderStroke(1.dp, epic.hairlineBorder),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp),
                ) {
                    Text(
                        text = block.diagramText,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        is ManuscriptBlock.TableBlock -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                block.rows.forEach { row ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, epic.hairlineBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            row.forEachIndexed { cIdx, cell ->
                                val header = block.headers.getOrNull(cIdx).orEmpty()
                                val combined = if (header.isNotBlank() && cIdx > 0) "**$header:** $cell" else cell
                                Text(
                                    text = formatRichEpicText(
                                        raw = combined,
                                        searchQuery = searchQuery,
                                        isActiveHitBlock = isActiveHitBlock,
                                        highlightBg = epic.searchHighlightBg,
                                        activeHighlightBg = epic.searchActiveHighlightBg,
                                        highlightText = epic.searchHighlightText,
                                        boldColor = epic.goldAccent,
                                    ),
                                    style = if (cIdx == 0) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                                    color = if (cIdx == 0) epic.goldAccent else MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }

        is ManuscriptBlock.Divider -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                HorizontalDivider(
                    color = epic.hairlineBorder,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "  ◈  ",
                    color = epic.goldAccent.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelMedium,
                )
                HorizontalDivider(
                    color = epic.hairlineBorder,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
