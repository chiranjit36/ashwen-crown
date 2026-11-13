package com.example.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.FileProvider
import com.example.model.ManuscriptBlock
import com.example.model.SearchHit
import com.example.model.TocEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.File

data class LoadedManuscript(
    val blocks: List<ManuscriptBlock>,
    val allHeadings: List<TocEntry>,
    val volumes: List<TocEntry>,
)

class ManuscriptRepository(private val context: Context) {

    @Volatile
    private var cachedManuscript: LoadedManuscript? = null

    suspend fun loadManuscript(): LoadedManuscript = withContext(Dispatchers.IO) {
        cachedManuscript?.let { return@withContext it }

        val jsonText = context.assets.open("manuscript_blocks.json")
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }

        val array = JSONArray(jsonText)
        val blocks = ArrayList<ManuscriptBlock>(array.length())
        val allHeadings = ArrayList<TocEntry>(140)
        val volumes = ArrayList<TocEntry>(20)

        var currentVol = 0
        var nextParagraphIsOpening = false

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val type = obj.getString("type")
            val id = obj.optString("id", "blk-$i")
            val text = obj.optString("text", "")

            when (type) {
                "h1" -> {
                    currentVol += 1
                    val block = ManuscriptBlock.H1Volume(
                        blockIndex = i,
                        id = id,
                        text = text,
                        volumeNumber = currentVol,
                    )
                    blocks.add(block)
                    val toc = TocEntry(
                        blockIndex = i,
                        level = 1,
                        volumeNumber = currentVol,
                        title = text,
                    )
                    allHeadings.add(toc)
                    volumes.add(toc)
                    nextParagraphIsOpening = true
                }
                "h2" -> {
                    blocks.add(ManuscriptBlock.H2Title(blockIndex = i, id = id, text = text))
                    allHeadings.add(
                        TocEntry(
                            blockIndex = i,
                            level = 2,
                            volumeNumber = currentVol,
                            title = text,
                        ),
                    )
                    nextParagraphIsOpening = true
                }
                "h3" -> {
                    blocks.add(ManuscriptBlock.H3Chapter(blockIndex = i, id = id, text = text))
                    allHeadings.add(
                        TocEntry(
                            blockIndex = i,
                            level = 3,
                            volumeNumber = currentVol,
                            title = text,
                        ),
                    )
                    nextParagraphIsOpening = true
                }
                "h4" -> {
                    blocks.add(ManuscriptBlock.H4SubSection(blockIndex = i, id = id, text = text))
                    allHeadings.add(
                        TocEntry(
                            blockIndex = i,
                            level = 4,
                            volumeNumber = currentVol,
                            title = text,
                        ),
                    )
                    nextParagraphIsOpening = true
                }
                "p" -> {
                    blocks.add(
                        ManuscriptBlock.Paragraph(
                            blockIndex = i,
                            text = text,
                            isChapterOpening = nextParagraphIsOpening,
                        ),
                    )
                    nextParagraphIsOpening = false
                }
                "blockquote" -> {
                    blocks.add(ManuscriptBlock.Quote(blockIndex = i, text = text))
                    nextParagraphIsOpening = false
                }
                "ul", "ol" -> {
                    val itemsJson = obj.getJSONArray("items")
                    val items = List(itemsJson.length()) { idx -> itemsJson.getString(idx) }
                    blocks.add(
                        ManuscriptBlock.BulletList(
                            blockIndex = i,
                            items = items,
                            ordered = type == "ol",
                        ),
                    )
                    nextParagraphIsOpening = false
                }
                "pre" -> {
                    blocks.add(ManuscriptBlock.AsciiMap(blockIndex = i, diagramText = text))
                    nextParagraphIsOpening = false
                }
                "table" -> {
                    val rowsJson = obj.getJSONArray("rows")
                    val allRows = List(rowsJson.length()) { rIdx ->
                        val rowArr = rowsJson.getJSONArray(rIdx)
                        List(rowArr.length()) { cIdx -> rowArr.getString(cIdx) }
                    }
                    val headers = allRows.firstOrNull().orEmpty()
                    val bodyRows = if (allRows.size > 1) allRows.drop(1) else emptyList()
                    blocks.add(
                        ManuscriptBlock.TableBlock(
                            blockIndex = i,
                            headers = headers,
                            rows = bodyRows,
                        ),
                    )
                    nextParagraphIsOpening = false
                }
                "hr" -> {
                    blocks.add(ManuscriptBlock.Divider(blockIndex = i))
                }
            }
        }

        LoadedManuscript(blocks = blocks, allHeadings = allHeadings, volumes = volumes).also {
            cachedManuscript = it
        }
    }

    suspend fun searchBlocks(
        query: String,
        blocks: List<ManuscriptBlock>,
    ): List<SearchHit> = withContext(Dispatchers.Default) {
        val trimmed = query.trim()
        if (trimmed.length < 2) return@withContext emptyList()

        val hits = ArrayList<SearchHit>()
        var latestHeading = "প্রথম খণ্ড"

        for (block in blocks) {
            when (block) {
                is ManuscriptBlock.H1Volume -> latestHeading = block.text
                is ManuscriptBlock.H2Title -> latestHeading = block.text
                is ManuscriptBlock.H3Chapter -> latestHeading = block.text
                is ManuscriptBlock.H4SubSection -> latestHeading = block.text
                else -> Unit
            }

            val raw = block.searchableText
                .replace("**", "")
                .replace("__", "")
            if (raw.isEmpty()) continue

            var idx = raw.indexOf(trimmed, ignoreCase = true)
            if (idx >= 0) {
                var count = 0
                val firstIdx = idx
                while (idx >= 0) {
                    count++
                    idx = raw.indexOf(trimmed, startIndex = idx + trimmed.length, ignoreCase = true)
                }
                val start = (firstIdx - 42).coerceAtLeast(0)
                val end = (firstIdx + trimmed.length + 68).coerceAtMost(raw.length)
                val prefix = if (start > 0) "..." else ""
                val suffix = if (end < raw.length) "..." else ""
                val snippet = prefix + raw.substring(start, end).replace('\n', ' ') + suffix

                hits.add(
                    SearchHit(
                        blockIndex = block.blockIndex,
                        sectionTitle = latestHeading,
                        snippet = snippet,
                        matchCount = count,
                    ),
                )
            }
        }
        hits
    }

    suspend fun readOriginalTxtBytes(): ByteArray = withContext(Dispatchers.IO) {
        context.assets.open("Raktabeej_Mahakavya_Original_Manuscript.txt").use { it.readBytes() }
    }

    suspend fun writeOriginalTxtToUri(targetUri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val bytes = readOriginalTxtBytes()
            context.contentResolver.openOutputStream(targetUri)?.use { os ->
                os.write(bytes)
                os.flush()
            } ?: error("Unable to open output stream")
        }
    }

    suspend fun createShareTxtIntent(): Result<Intent> = withContext(Dispatchers.IO) {
        runCatching {
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val outFile = File(exportDir, "Raktabeej_Mahakavya_Original_Manuscript.txt")
            if (!outFile.exists()) {
                outFile.writeBytes(readOriginalTxtBytes())
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                outFile,
            )
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "রক্তবীজ মহাকাব্য: কালসর্পের পঞ্চবলয় (সম্পূর্ণ পাণ্ডুলিপি)")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            Intent.createChooser(sendIntent, "মূল পাণ্ডুলিপি TXT শেয়ার করুন")
        }
    }

    fun launchManuscriptPrint(activity: Activity, blocks: List<ManuscriptBlock>) {
        val htmlBody = buildString {
            append("<!DOCTYPE html><html lang=\"bn\"><head><meta charset=\"UTF-8\">")
            append("<style>body{font-family:serif;line-height:1.8;padding:24px;color:#111;}")
            append("h1{font-size:22px;border-bottom:2px solid #8c1d24;padding-bottom:6px;page-break-before:auto;}")
            append("h2{font-size:18px;color:#551116;} h3{font-size:16px;margin-top:18px;}")
            append("blockquote{border-left:3px solid #966e14;padding-left:12px;font-style:italic;background:#f8f3e9;padding:8px 12px;}")
            append("pre{font-family:monospace;font-size:11px;white-space:pre-wrap;background:#f4f1ea;padding:10px;}")
            append("table{width:100%;border-collapse:collapse;margin:12px 0;} th,td{border:1px solid #999;padding:6px;text-align:left;font-size:12px;}")
            append("</style></head><body>")
            append("<h1>রক্তবীজ মহাকাব্য: কালসর্পের পঞ্চবলয়</h1>")
            for (b in blocks) {
                when (b) {
                    is ManuscriptBlock.H1Volume -> append("<h1>${escapeHtml(b.text)}</h1>")
                    is ManuscriptBlock.H2Title -> append("<h2>${escapeHtml(b.text)}</h2>")
                    is ManuscriptBlock.H3Chapter -> append("<h3>${escapeHtml(b.text)}</h3>")
                    is ManuscriptBlock.H4SubSection -> append("<h4>${escapeHtml(b.text)}</h4>")
                    is ManuscriptBlock.Paragraph -> append("<p>${escapeHtml(b.text.replace("**", "").replace("__", ""))}</p>")
                    is ManuscriptBlock.Quote -> append("<blockquote>${escapeHtml(b.text.replace("**", "").replace("__", "").replace("\n", " "))}</blockquote>")
                    is ManuscriptBlock.BulletList -> {
                        val tag = if (b.ordered) "ol" else "ul"
                        append("<$tag>")
                        b.items.forEach { append("<li>${escapeHtml(it.replace("**", "").replace("__", ""))}</li>") }
                        append("</$tag>")
                    }
                    is ManuscriptBlock.AsciiMap -> append("<pre>${escapeHtml(b.diagramText)}</pre>")
                    is ManuscriptBlock.TableBlock -> {
                        append("<table><thead><tr>")
                        b.headers.forEach { append("<th>${escapeHtml(it.replace("**", "").replace("__", ""))}</th>") }
                        append("</tr></thead><tbody>")
                        b.rows.forEach { r ->
                            append("<tr>")
                            r.forEach { append("<td>${escapeHtml(it.replace("**", "").replace("__", ""))}</td>") }
                            append("</tr>")
                        }
                        append("</tbody></table>")
                    }
                    is ManuscriptBlock.Divider -> append("<hr/>")
                }
            }
            append("</body></html>")
        }

        val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                if (printManager != null) {
                    val adapter = webView.createPrintDocumentAdapter("Raktabeej_Mahakavya")
                    printManager.print(
                        "রক্তবীজ মহাকাব্য",
                        adapter,
                        PrintAttributes.Builder().build(),
                    )
                }
            }
        }
        webView.loadDataWithBaseURL(null, htmlBody, "text/html", "UTF-8", null)
    }

    private fun escapeHtml(input: String): String =
        input.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
}
