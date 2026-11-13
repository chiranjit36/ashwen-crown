package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.LoreCatalog
import com.example.model.CharacterProfile
import com.example.ui.components.SectionHeader
import com.example.ui.theme.LocalEpicColors

@Composable
fun CharactersScreen(
    onSearchInManuscript: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    var selectedSegment by remember { mutableIntStateOf(0) } // 0=Central(7), 1=Vol9&Factions, 2=Diplomacy Matrix

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 860.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(key = "cast_header") {
                SectionHeader(
                    eyebrow = "THE CENTRAL CAST & FACTIONS",
                    title = "চরিত্র-পরিচয় ও প্রেত-রাজনীতি",
                    subtitle = "মূল পাণ্ডুলিপির শুরুর চরিত্র-পরিচয়, নবম খণ্ডের মহাশক্তি এবং দশম খণ্ডের পিশাচ-বৃক কূটনীতি।",
                )
            }

            item(key = "cast_tabs") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FilterChip(
                        selected = selectedSegment == 0,
                        onClick = { selectedSegment = 0 },
                        label = { Text("কেন্দ্রীয় চরিত্র (৭)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                        modifier = Modifier.testTag("cast_tab_central"),
                    )
                    FilterChip(
                        selected = selectedSegment == 1,
                        onClick = { selectedSegment = 1 },
                        label = { Text("নবম খণ্ড ও প্রেত-রাজনীতি") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                        modifier = Modifier.testTag("cast_tab_factions"),
                    )
                    FilterChip(
                        selected = selectedSegment == 2,
                        onClick = { selectedSegment = 2 },
                        label = { Text("কূটনৈতিক তালিকা (দশম খণ্ড)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                        modifier = Modifier.testTag("cast_tab_matrix"),
                    )
                }
            }

            when (selectedSegment) {
                0 -> {
                    items(LoreCatalog.centralCharacters, key = { it.id }) { character ->
                        CharacterDetailCard(
                            character = character,
                            onSearchClick = {
                                val keyToken = character.name.split(" ").lastOrNull { it.length > 2 }
                                    ?: character.name
                                val firstWord = character.name.split(" ").first()
                                val searchToken = if (firstWord in listOf("যুবরাজ", "নাগকুমারী", "সম্রাট", "গ্র্যান্ড")) {
                                    keyToken
                                } else {
                                    firstWord
                                }
                                onSearchInManuscript(searchToken)
                            },
                        )
                    }
                }
                1 -> {
                    item(key = "vol9_title") {
                        Text(
                            text = "নবম খণ্ড · নতুন রাজশক্তি ও চরিত্র-সমাবেশ",
                            style = MaterialTheme.typography.titleLarge,
                            color = epic.goldAccent,
                        )
                    }
                    items(LoreCatalog.extendedPowersVol9, key = { it.id }) { character ->
                        CharacterDetailCard(
                            character = character,
                            onSearchClick = {
                                val searchToken = character.name.split(" ").last()
                                onSearchInManuscript(searchToken)
                            },
                        )
                    }
                    item(key = "factions_title") {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "দশম খণ্ড · প্রেত ও বৃক অভ্যন্তরীণ রাজনীতি",
                            style = MaterialTheme.typography.titleLarge,
                            color = epic.goldAccent,
                        )
                    }
                    items(LoreCatalog.factionLoreCards, key = { it.title }) { faction ->
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
                                Text(
                                    text = faction.englishTag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = epic.goldAccent,
                                )
                                Text(
                                    text = faction.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                HorizontalDivider(color = epic.hairlineBorder)
                                faction.bulletPoints.forEach { point ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.Top,
                                    ) {
                                        Text(
                                            text = "◆",
                                            color = epic.crimsonAccent,
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                        Text(
                                            text = point,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {
                    item(key = "matrix_title") {
                        Text(
                            text = "নতুন রাজনৈতিক অনুঘটক ও তাদের লক্ষ্য (দশম খণ্ড)",
                            style = MaterialTheme.typography.titleLarge,
                            color = epic.goldAccent,
                        )
                    }
                    items(LoreCatalog.politicalCatalysts, key = { it.name }) { row ->
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
                                        text = row.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = epic.goldAccent,
                                    )
                                    IconButton(
                                        onClick = { onSearchInManuscript(row.name.split(" ").first()) },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "পাঠে খুঁজুন: ${row.name}",
                                            tint = epic.goldAccent,
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                ) {
                                    Text(
                                        text = "পরিচয় ও বংশ: ${row.identityAndLineage}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    )
                                }
                                Text(
                                    text = "রাজনৈতিক লক্ষ্য: ${row.politicalGoal}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                                ) {
                                    Text(
                                        text = "গোপন দুর্বলতা: ${row.hiddenWeakness}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun CharacterDetailCard(
    character: CharacterProfile,
    onSearchClick: () -> Unit,
) {
    val epic = LocalEpicColors.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, epic.hairlineBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("character_card_${character.id}"),
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = character.titleBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = epic.goldAccent,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Text(
                                text = character.realmTag,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier.testTag("search_char_${character.id}"),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "পাণ্ডুলিপিতে খুঁজুন: ${character.name}",
                        tint = epic.goldAccent,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Text(
                text = character.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
