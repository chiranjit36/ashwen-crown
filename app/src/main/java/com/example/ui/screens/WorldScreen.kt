package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LoreCatalog
import com.example.model.WorldRegion
import com.example.ui.components.SectionHeader
import com.example.ui.theme.LocalEpicColors
import kotlin.math.roundToInt

@Composable
fun WorldScreen(
    onSearchInManuscript: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val epic = LocalEpicColors.current
    var selectedRegion by remember { mutableStateOf(LoreCatalog.worldRegions[2]) } // Default: Gaur

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 860.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item(key = "world_header") {
                SectionHeader(
                    eyebrow = "THE CONTINENT",
                    title = "বঙ্গ-আবর্ত · ভূগোলের মানচিত্র",
                    subtitle = "উত্তরের তুষারাবৃত অরণ্য থেকে দক্ষিণের ম্যানগ্রোভ, সমুদ্রের অতল ঘূর্ণি থেকে পাঁচ মাইল গভীর রসাতল—মানচিত্রের পিনে চাপ দিয়ে প্রতিটি অঞ্চল দেখুন।",
                )
            }

            // Interactive Continent Map with 7 Pins
            item(key = "interactive_map") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, epic.goldAccent.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_world_continent),
                                contentDescription = stringResource(R.string.cd_world_map),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )

                            val density = androidx.compose.ui.platform.LocalDensity.current
                            val widthPx = with(density) { maxWidth.toPx() }
                            val heightPx = with(density) { maxHeight.toPx() }
                            val pinHalfPx = with(density) { 24.dp.toPx() }

                            LoreCatalog.worldRegions.forEach { region ->
                                val isSelected = region.id == selectedRegion.id
                                Box(
                                    modifier = Modifier
                                        .offset {
                                            IntOffset(
                                                x = (region.mapX * widthPx - pinHalfPx).roundToInt(),
                                                y = (region.mapY * heightPx - pinHalfPx).roundToInt(),
                                            )
                                        }
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .clickable { selectedRegion = region }
                                        .testTag("map_pin_${region.id}"),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(if (isSelected) 34.dp else 26.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) epic.goldAccent
                                                else MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                else epic.goldAccent,
                                                shape = CircleShape,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.LocationOn,
                                            contentDescription = region.name,
                                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else epic.goldAccent,
                                            modifier = Modifier.size(if (isSelected) 20.dp else 15.dp),
                                        )
                                    }
                                }
                            }
                        }

                        // Selected Region Detail Panel
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selectedRegion.zoneLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = epic.goldAccent,
                                    )
                                    Text(
                                        text = selectedRegion.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                        val token = selectedRegion.name.split(" ", "—", "-").first()
                                        onSearchInManuscript(token)
                                    },
                                    border = BorderStroke(1.dp, epic.hairlineBorder),
                                    modifier = Modifier.testTag("search_selected_region_btn"),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("পাঠে খুঁজুন")
                                }
                            }
                            Text(
                                text = selectedRegion.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Text(
                                    text = "প্রধান শক্তি: ${selectedRegion.dominantForce}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                )
                            }
                        }
                    }
                }
            }

            // All 7 Region Cards
            items(LoreCatalog.worldRegions, key = { it.id }) { region ->
                RegionLoreCard(
                    region = region,
                    isSelected = region.id == selectedRegion.id,
                    onClick = { selectedRegion = region },
                )
            }

            // Volume 10 Geopolitics ASCII Diagram
            item(key = "geopolitics_diagram") {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = epic.codeBackground),
                    border = BorderStroke(1.dp, epic.hairlineBorder),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = "দশম খণ্ড · ক্ষমতার ভৌগোলিক ও কূটনৈতিক মানচিত্র",
                            style = MaterialTheme.typography.titleMedium,
                            color = epic.goldAccent,
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                        ) {
                            Text(
                                text = LoreCatalog.geopoliticsAsciiMap,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
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
private fun RegionLoreCard(
    region: WorldRegion,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val epic = LocalEpicColors.current
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            else MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) epic.goldAccent else epic.hairlineBorder,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("region_card_${region.id}"),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = region.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Text(
                        text = region.zoneLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = epic.goldAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
            Text(
                text = region.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "প্রধান শক্তি: ${region.dominantForce}",
                style = MaterialTheme.typography.bodySmall,
                color = epic.mutedText,
            )
        }
    }
}
