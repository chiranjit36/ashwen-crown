package com.example

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.example.data.ManuscriptRepository
import com.example.ui.components.formatRichEpicText
import com.example.ui.components.toBengaliDigits
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ManuscriptRepositoryTest {

    @Test
    fun `loads all 20 volumes and 137 headings unabridged`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = ManuscriptRepository(context)
        val loaded = repo.loadManuscript()

        assertEquals(20, loaded.volumes.size)
        assertEquals(137, loaded.allHeadings.size)
        assertEquals(1514, loaded.blocks.size)

        val hits = repo.searchBlocks("আর্যবীর", loaded.blocks)
        assertTrue("Expected hits for আর্যবীর", hits.isNotEmpty())

        val rawTxt = repo.readOriginalTxtBytes().decodeToString()
        assertTrue("Expected full original manuscript > 200k chars", rawTxt.length > 200_000)
    }

    @Test
    fun `bengali numerals and rich text formatting work properly`() {
        assertEquals("২০", toBengaliDigits(20))
        assertEquals("৩৪১০৪", toBengaliDigits(34104))

        val formatted = formatRichEpicText(
            raw = "**আর্যবীর** রাঢ়ে ফিরলেন।",
            searchQuery = "আর্যবীর",
            isActiveHitBlock = true,
            highlightBg = Color.Yellow,
            activeHighlightBg = Color.Cyan,
            highlightText = Color.Black,
        )
        assertEquals("আর্যবীর রাঢ়ে ফিরলেন।", formatted.text)
        assertTrue(formatted.spanStyles.isNotEmpty())
    }
}
