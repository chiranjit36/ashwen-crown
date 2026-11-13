package com.example.model

import androidx.compose.ui.graphics.Color

enum class EpicThemeMode(
    val id: String,
    val bengaliLabel: String,
    val englishTag: String,
    val swatchBg: Color,
    val swatchAccent: Color,
) {
    IMPERIAL_DARK(
        id = "imperial_dark",
        bengaliLabel = "রাজকীয় স্বর্ণ",
        englishTag = "Imperial Dark",
        swatchBg = Color(0xFF09080A),
        swatchAccent = Color(0xFFD4AF37),
    ),
    BLOOD_VELVET(
        id = "blood_velvet",
        bengaliLabel = "রক্তবর্ণ মখমল",
        englishTag = "Blood Velvet",
        swatchBg = Color(0xFF220B12),
        swatchAccent = Color(0xFFE1364F),
    ),
    OBSIDIAN_ABYSS(
        id = "obsidian_abyss",
        bengaliLabel = "অতলেন্দু নীল",
        englishTag = "Obsidian Abyss",
        swatchBg = Color(0xFF0D1526),
        swatchAccent = Color(0xFF38BDF8),
    ),
    ANCIENT_PARCHMENT(
        id = "ancient_parchment",
        bengaliLabel = "প্রাচীন পাণ্ডুলিপি",
        englishTag = "Parchment Light",
        swatchBg = Color(0xFFF7F1E5),
        swatchAccent = Color(0xFF8C1D24),
    );

    companion object {
        fun fromId(id: String?): EpicThemeMode =
            entries.firstOrNull { it.id == id } ?: IMPERIAL_DARK
    }
}

enum class MainTab(val route: String) {
    OVERVIEW("overview"),
    WORLD("world"),
    CAST("cast"),
    READER("reader"),
}

data class HeroStat(
    val label: String,
    val value: String,
    val caption: String,
)

data class CosmicRing(
    val indexLabel: String,
    val name: String,
    val powerDescription: String,
    val accentColor: Color,
)

data class WorldRegion(
    val id: String,
    val name: String,
    val zoneLabel: String,
    val description: String,
    val dominantForce: String,
    val mapX: Float,
    val mapY: Float,
)

data class CharacterProfile(
    val id: String,
    val name: String,
    val titleBadge: String,
    val description: String,
    val realmTag: String,
    val isCentralCast: Boolean = true,
)

data class QuickCodexEntry(
    val id: String,
    val title: String,
    val subtitle: String,
    val summary: String,
)

data class PoliticalCatalystRow(
    val name: String,
    val identityAndLineage: String,
    val politicalGoal: String,
    val hiddenWeakness: String,
)

data class FactionLoreCard(
    val title: String,
    val englishTag: String,
    val bulletPoints: List<String>,
)

sealed interface ManuscriptBlock {
    val blockIndex: Int
    val searchableText: String

    data class H1Volume(
        override val blockIndex: Int,
        val id: String,
        val text: String,
        val volumeNumber: Int,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class H2Title(
        override val blockIndex: Int,
        val id: String,
        val text: String,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class H3Chapter(
        override val blockIndex: Int,
        val id: String,
        val text: String,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class H4SubSection(
        override val blockIndex: Int,
        val id: String,
        val text: String,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class Paragraph(
        override val blockIndex: Int,
        val text: String,
        val isChapterOpening: Boolean = false,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class Quote(
        override val blockIndex: Int,
        val text: String,
    ) : ManuscriptBlock {
        override val searchableText: String get() = text
    }

    data class BulletList(
        override val blockIndex: Int,
        val items: List<String>,
        val ordered: Boolean,
    ) : ManuscriptBlock {
        override val searchableText: String by lazy { items.joinToString(" ") }
    }

    data class AsciiMap(
        override val blockIndex: Int,
        val diagramText: String,
    ) : ManuscriptBlock {
        override val searchableText: String get() = diagramText
    }

    data class TableBlock(
        override val blockIndex: Int,
        val headers: List<String>,
        val rows: List<List<String>>,
    ) : ManuscriptBlock {
        override val searchableText: String by lazy {
            (listOf(headers) + rows).flatten().joinToString(" ")
        }
    }

    data class Divider(
        override val blockIndex: Int,
    ) : ManuscriptBlock {
        override val searchableText: String get() = ""
    }
}

data class TocEntry(
    val blockIndex: Int,
    val level: Int,
    val volumeNumber: Int,
    val title: String,
)

data class SearchHit(
    val blockIndex: Int,
    val sectionTitle: String,
    val snippet: String,
    val matchCount: Int,
)
