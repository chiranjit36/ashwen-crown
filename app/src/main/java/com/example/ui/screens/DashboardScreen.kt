package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.LoreCatalog
import com.example.model.TocEntry
import com.example.ui.components.SectionHeader
import com.example.ui.components.toBengaliDigits
import com.example.ui.theme.LocalEpicColors
import com.example.ui.viewmodel.EpicUiState

@Composable
fun DashboardScreen(
    uiState: EpicUiState,
    onContinueReading: () -> Unit,
    onOpenToc: () -> Unit,
    onOpenThemePicker: () -> Unit,
    onSaveTxt: () -> Unit,
    onShareTxt: () -> Unit,
    onPrintManuscript: () -> Unit,
    onJumpToVolume: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    var expandedCodexId by remember { mutableStateOf<String?>(LoreCatalog.quickCodexEntries.first().id) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 860.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // 1. Imperial Hero Banner
            item(key = "hero_banner") {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, epic.hairlineBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_hero_banner),
                                contentDescription = stringResource(R.string.cd_hero_banner),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.15f),
                                                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                                                MaterialTheme.colorScheme.surface,
                                            ),
                                        ),
                                    ),
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, epic.goldAccent.copy(alpha = 0.45f)),
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(14.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.hero_badge),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = epic.goldAccent,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 18.dp, vertical = 10.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = stringResource(R.string.epic_subtitle),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = epic.goldAccent,
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.hero_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ) {
                                    Text(
                                        text = "প্রথম খণ্ড থেকে একবিংশ খণ্ড",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                ) {
                                    Text(
                                        text = "সম্পূর্ণ উৎসক্রম অক্ষুণ্ণ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    )
                                }
                            }

                            // Primary CTA row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Button(
                                    onClick = onContinueReading,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("continue_reading_button"),
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (uiState.lastReadBlockIndex > 0) {
                                            "পাঠ চালিয়ে যান (${toBengaliDigits(uiState.readingProgressPercent)}%)"
                                        } else {
                                            "পাঠ শুরু করুন"
                                        },
                                    )
                                }

                                OutlinedButton(
                                    onClick = onOpenToc,
                                    border = BorderStroke(1.dp, epic.hairlineBorder),
                                    modifier = Modifier.testTag("dashboard_toc_button"),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FormatListNumbered,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("২০ খণ্ড")
                                }
                            }

                            // Secondary manuscript actions (TXT Save, Share, Print, Theme)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                CompactActionChip(
                                    label = "মূল TXT",
                                    icon = Icons.Filled.Download,
                                    onClick = onSaveTxt,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("export_txt_btn"),
                                )
                                CompactActionChip(
                                    label = "শেয়ার",
                                    icon = Icons.Filled.Share,
                                    onClick = onShareTxt,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("share_txt_btn"),
                                )
                                CompactActionChip(
                                    label = "প্রিন্ট",
                                    icon = Icons.Filled.Print,
                                    onClick = onPrintManuscript,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("print_btn"),
                                )
                                CompactActionChip(
                                    label = "থিম",
                                    icon = Icons.Filled.Palette,
                                    onClick = onOpenThemePicker,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("dashboard_theme_btn"),
                                )
                            }
                        }
                    }
                }
            }

            // 2. Reading Progress Bar Card
            item(key = "progress_card") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, epic.hairlineBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "পাঠের অগ্রগতি (Reading Progress)",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "${toBengaliDigits(uiState.readingProgressPercent)}%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = epic.goldAccent,
                            )
                        }
                        LinearProgressIndicator(
                            progress = { (uiState.readingProgressPercent / 100f).coerceIn(0f, 1f) },
                            color = epic.goldAccent,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                        )
                        Text(
                            text = "বর্তমান থিম: ${uiState.themeMode.bengaliLabel} · বুকমার্ক: ${toBengaliDigits(uiState.bookmarkedBlocks.size)}টি",
                            style = MaterialTheme.typography.bodySmall,
                            color = epic.mutedText,
                        )
                    }
                }
            }

            // 3. Manuscript Stats (2x2 Grid)
            item(key = "stats_grid") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val stats = LoreCatalog.heroStats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        StatMetricCard(stats[0].label, stats[0].value, stats[0].caption, Modifier.weight(1f))
                        StatMetricCard(stats[1].label, stats[1].value, stats[1].caption, Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        StatMetricCard(stats[2].label, stats[2].value, stats[2].caption, Modifier.weight(1f))
                        StatMetricCard(stats[3].label, stats[3].value, stats[3].caption, Modifier.weight(1f))
                    }
                }
            }

            // 4. The 5 Cosmic Rings (পঞ্চ-বলয়)
            item(key = "cosmic_rings_section") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(
                        eyebrow = "THE COSMIC ENGINE",
                        title = "পঞ্চ-বলয় · পাঁচ মহাজাগতিক শক্তি",
                        subtitle = "সৃষ্টির সূচনায় শেষনাগ অনন্ত সমুদ্রের গভীরে যে পাঁচটি স্ফটিক অঙ্গুরীয় নিক্ষেপ করেছিলেন, পাণ্ডুলিপির বিশ্ব-ভিত্তিতে তাদের শক্তির নাম এভাবেই দেওয়া হয়েছে।",
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(LoreCatalog.cosmicRings, key = { it.indexLabel }) { ring ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, ring.accentColor.copy(alpha = 0.45f)),
                                modifier = Modifier.width(235.dp),
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(ring.accentColor.copy(alpha = 0.18f)),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = ring.indexLabel,
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = ring.accentColor,
                                            )
                                        }
                                        Text(
                                            text = ring.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }
                                    Text(
                                        text = ring.powerDescription,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Twin Dragons Spotlight + Quick Codex Accordions
            item(key = "quick_codex_section") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(
                        eyebrow = "QUICK CODEX",
                        title = "চরিত্র ও রহস্য কোডেক্স",
                        subtitle = "মহাকাব্যের মূল তিন স্তম্ভ ও দ্বৈত ড্রাগন-রহস্যের সংক্ষিপ্ত কোডেক্স।",
                    )

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, epic.hairlineBorder),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column {
                            Image(
                                painter = painterResource(id = R.drawable.img_dragon_duality),
                                contentDescription = stringResource(R.string.cd_dragons),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                            )
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "কালান্তক (লাভা) ও নীল-নাগ (বরফ) — সৃষ্টির দুই মেরু শক্তি",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = epic.goldAccent,
                                )
                            }
                        }
                    }

                    LoreCatalog.quickCodexEntries.forEach { entry ->
                        val expanded = expandedCodexId == entry.id
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(
                                1.dp,
                                if (expanded) epic.goldAccent.copy(alpha = 0.6f) else epic.hairlineBorder,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    expandedCodexId = if (expanded) null else entry.id
                                }
                                .testTag("codex_card_${entry.id}"),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = entry.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                        Text(
                                            text = entry.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = epic.goldAccent,
                                        )
                                    }
                                    Icon(
                                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                        contentDescription = entry.title,
                                        tint = epic.mutedText,
                                    )
                                }
                                AnimatedVisibility(visible = expanded) {
                                    Column {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(color = epic.hairlineBorder)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = entry.summary,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 6. Quick Volume Jump Strip (20 Volumes)
            if (uiState.volumes.isNotEmpty()) {
                item(key = "volume_quick_jump") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeader(
                            eyebrow = "TWENTY VOLUMES",
                            title = "খণ্ড সূচিপত্র (২০টি মহাপর্ব)",
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(uiState.volumes, key = { it.blockIndex }) { vol: TocEntry ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, epic.hairlineBorder),
                                    modifier = Modifier
                                        .width(230.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onJumpToVolume(vol.blockIndex) }
                                        .testTag("quick_vol_${vol.volumeNumber}"),
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Text(
                                            text = "পর্ব ${toBengaliDigits(vol.volumeNumber)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = epic.goldAccent,
                                        )
                                        Text(
                                            text = vol.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 2,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 7. Editorial Preservation Note
            item(key = "editorial_note") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    ),
                    border = BorderStroke(1.dp, epic.hairlineBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = epic.goldAccent,
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "সম্পাদনা ও সংরক্ষণ-নীতি",
                                style = MaterialTheme.typography.titleSmall,
                                color = epic.goldAccent,
                            )
                            Text(
                                text = LoreCatalog.EDITORIAL_POLICY_NOTE,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun StatMetricCard(
    label: String,
    value: String,
    caption: String,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, epic.hairlineBorder),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = epic.mutedText,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = epic.goldAccent,
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CompactActionChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, epic.hairlineBorder),
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = epic.goldAccent,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
    }
}
