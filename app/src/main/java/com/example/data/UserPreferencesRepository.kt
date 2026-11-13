package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.EpicThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.raktabeejDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "raktabeej_reader_prefs",
)

data class UserReaderPreferences(
    val themeMode: EpicThemeMode = EpicThemeMode.IMPERIAL_DARK,
    val fontSizeSp: Float = 17f,
    val lastReadBlockIndex: Int = 0,
    val readingProgressPercent: Int = 0,
    val bookmarkedBlockIndices: Set<Int> = emptySet(),
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FONT_SIZE_SP = floatPreferencesKey("font_size_sp")
        val LAST_READ_BLOCK = intPreferencesKey("last_read_block_index")
        val READING_PROGRESS = intPreferencesKey("reading_progress_pct")
        val BOOKMARKS = stringSetPreferencesKey("bookmarked_blocks")
    }

    val preferencesFlow: Flow<UserReaderPreferences> = context.raktabeejDataStore.data.map { prefs ->
        UserReaderPreferences(
            themeMode = EpicThemeMode.fromId(prefs[Keys.THEME_MODE]),
            fontSizeSp = (prefs[Keys.FONT_SIZE_SP] ?: 17f).coerceIn(14f, 26f),
            lastReadBlockIndex = prefs[Keys.LAST_READ_BLOCK] ?: 0,
            readingProgressPercent = (prefs[Keys.READING_PROGRESS] ?: 0).coerceIn(0, 100),
            bookmarkedBlockIndices = prefs[Keys.BOOKMARKS]
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()
                ?: emptySet(),
        )
    }

    suspend fun setThemeMode(mode: EpicThemeMode) {
        context.raktabeejDataStore.edit { prefs ->
            prefs[Keys.THEME_MODE] = mode.id
        }
    }

    suspend fun setFontSize(fontSizeSp: Float) {
        context.raktabeejDataStore.edit { prefs ->
            prefs[Keys.FONT_SIZE_SP] = fontSizeSp.coerceIn(14f, 26f)
        }
    }

    suspend fun updateReadingPosition(blockIndex: Int, progressPercent: Int) {
        context.raktabeejDataStore.edit { prefs ->
            prefs[Keys.LAST_READ_BLOCK] = blockIndex.coerceAtLeast(0)
            prefs[Keys.READING_PROGRESS] = progressPercent.coerceIn(0, 100)
        }
    }

    suspend fun toggleBookmark(blockIndex: Int) {
        context.raktabeejDataStore.edit { prefs ->
            val current = prefs[Keys.BOOKMARKS]?.toMutableSet() ?: mutableSetOf()
            val key = blockIndex.toString()
            if (current.contains(key)) {
                current.remove(key)
            } else {
                current.add(key)
            }
            prefs[Keys.BOOKMARKS] = current
        }
    }
}
