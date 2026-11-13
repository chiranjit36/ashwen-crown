package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.EpicThemeMode
import com.example.model.ManuscriptBlock
import com.example.model.TocEntry
import com.example.ui.theme.LocalEpicColors

@Composable
fun SectionHeader(
    eyebrow: String,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = eyebrow.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = epic.goldAccent,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePickerModalSheet(
    currentTheme: EpicThemeMode,
    onSelectTheme: (EpicThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val epic = LocalEpicColors.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "ইম্পেরিয়াল থিম নির্বাচন করুন",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "দিন বা রাতের পাঠের সুবিধামতো রঙ ও আবহ পরিবর্তন করুন",
                style = MaterialTheme.typography.bodySmall,
                color = epic.mutedText,
            )

            EpicThemeMode.entries.forEach { mode ->
                val selected = mode == currentTheme
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) MaterialTheme.colorScheme.primary else epic.hairlineBorder,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectTheme(mode) }
                        .testTag("theme_option_${mode.id}"),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(mode.swatchBg)
                                .border(2.5.dp, mode.swatchAccent, CircleShape),
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mode.bengaliLabel,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = mode.englishTag,
                                style = MaterialTheme.typography.bodySmall,
                                color = epic.mutedText,
                            )
                        }
                        if (selected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = mode.bengaliLabel,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TocBottomSheet(
    volumes: List<TocEntry>,
    allHeadings: List<TocEntry>,
    bookmarkedBlocks: Set<Int>,
    blocks: List<ManuscriptBlock>,
    currentBlockIndex: Int,
    onJumpToBlock: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val epic = LocalEpicColors.current
    var selectedFilter by remember { mutableIntStateOf(0) } // 0=Volumes(20), 1=All(137), 2=Bookmarks
    var tocQuery by remember { mutableStateOf("") }

    val filteredList = remember(selectedFilter, tocQuery, volumes, allHeadings, bookmarkedBlocks) {
        val base = when (selectedFilter) {
            0 -> volumes
            1 -> allHeadings
            else -> {
                bookmarkedBlocks.sorted().map { idx ->
                    val blk = blocks.getOrNull(idx)
                    val preview = when (blk) {
                        is ManuscriptBlock.H1Volume -> blk.text
                        is ManuscriptBlock.H2Title -> blk.text
                        is ManuscriptBlock.H3Chapter -> blk.text
                        is ManuscriptBlock.H4SubSection -> blk.text
                        is ManuscriptBlock.Paragraph -> stripInlineMarkers(blk.text).take(75)
                        else -> "ব্লক #${toBengaliDigits(idx + 1)}"
                    }
                    TocEntry(blockIndex = idx, level = 1, volumeNumber = 0, title = preview)
                }
            }
        }
        val q = tocQuery.trim()
        if (q.isBlank()) base else base.filter { it.title.contains(q, ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.86f)
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AutoStories,
                        contentDescription = null,
                        tint = epic.goldAccent,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "খণ্ড ও অধ্যায় সূচিপত্র",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_toc_button"),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_back),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedFilter == 0,
                    onClick = { selectedFilter = 0 },
                    label = { Text("২০টি মহাপর্ব") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.testTag("toc_filter_volumes"),
                )
                FilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = { Text("সকল শিরোনাম (${toBengaliDigits(allHeadings.size)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.testTag("toc_filter_all"),
                )
                FilterChip(
                    selected = selectedFilter == 2,
                    onClick = { selectedFilter = 2 },
                    label = { Text("বুকমার্ক (${toBengaliDigits(bookmarkedBlocks.size)})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.testTag("toc_filter_bookmarks"),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = tocQuery,
                onValueChange = { tocQuery = it },
                singleLine = true,
                placeholder = { Text("সূচিপত্রে খণ্ড বা অধ্যায় খুঁজুন...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (tocQuery.isNotEmpty()) {
                        IconButton(onClick = { tocQuery = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cd_clear_search))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("toc_search_input"),
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = epic.hairlineBorder)

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (selectedFilter == 2) {
                            "এখনও কোনো বুকমার্ক যোগ করা হয়নি। পাঠকের যেকোনো অংশে বুকমার্ক আইকনে চাপ দিন।"
                        } else {
                            "কোনো শিরোনাম পাওয়া যায়নি।"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = epic.mutedText,
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(filteredList, key = { "${selectedFilter}_${it.blockIndex}_${it.level}" }) { entry ->
                        val isNearCurrent = currentBlockIndex >= entry.blockIndex &&
                            (filteredList.firstOrNull { it.blockIndex > entry.blockIndex }?.blockIndex
                                ?: Int.MAX_VALUE) > currentBlockIndex

                        val startIndent = when (entry.level) {
                            1 -> 0.dp
                            2 -> 10.dp
                            3 -> 22.dp
                            else -> 32.dp
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isNearCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (entry.level == 1) 0.75f else 0.35f),
                            border = if (entry.level == 1) BorderStroke(1.dp, epic.hairlineBorder) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = startIndent)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onJumpToBlock(entry.blockIndex) }
                                .testTag("toc_item_${entry.blockIndex}"),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = if (selectedFilter == 2) "★" else when (entry.level) {
                                        1 -> "◈"
                                        2 -> "◆"
                                        else -> "•"
                                    },
                                    color = if (entry.level == 1) epic.goldAccent else epic.crimsonAccent,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = entry.title,
                                    style = if (entry.level <= 2) {
                                        MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    } else {
                                        MaterialTheme.typography.bodyMedium
                                    },
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f),
                                )
                                if (bookmarkedBlocks.contains(entry.blockIndex)) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Filled.Bookmark,
                                        contentDescription = null,
                                        tint = epic.goldAccent,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
