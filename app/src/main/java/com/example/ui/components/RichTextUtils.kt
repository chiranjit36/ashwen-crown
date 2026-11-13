package com.example.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private val BENGALI_DIGITS = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

fun toBengaliDigits(number: Int): String {
    val str = number.toString()
    val sb = StringBuilder(str.length)
    for (ch in str) {
        if (ch in '0'..'9') {
            sb.append(BENGALI_DIGITS[ch - '0'])
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

fun stripInlineMarkers(raw: String): String =
    raw.replace("**", "").replace("__", "")

fun formatRichEpicText(
    raw: String,
    searchQuery: String,
    isActiveHitBlock: Boolean,
    highlightBg: Color,
    activeHighlightBg: Color,
    highlightText: Color,
    boldColor: Color? = null,
): AnnotatedString {
    val parsed = parseInlineStyles(raw, boldColor)
    val q = searchQuery.trim()
    if (q.length < 2) return parsed

    val plain = parsed.text
    val first = plain.indexOf(q, ignoreCase = true)
    if (first < 0) return parsed

    val bg = if (isActiveHitBlock) activeHighlightBg else highlightBg
    return buildAnnotatedString {
        append(parsed)
        var idx = first
        while (idx >= 0) {
            addStyle(
                style = SpanStyle(
                    background = bg,
                    color = highlightText,
                    fontWeight = FontWeight.Bold,
                ),
                start = idx,
                end = (idx + q.length).coerceAtMost(plain.length),
            )
            idx = plain.indexOf(q, startIndex = idx + q.length, ignoreCase = true)
        }
    }
}

private fun parseInlineStyles(raw: String, boldColor: Color?): AnnotatedString = buildAnnotatedString {
    var i = 0
    val n = raw.length
    while (i < n) {
        if (i + 1 < n && raw[i] == '*' && raw[i + 1] == '*') {
            val end = raw.indexOf("**", startIndex = i + 2)
            if (end > i + 2) {
                val inner = raw.substring(i + 2, end).replace("__", "")
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = boldColor ?: Color.Unspecified,
                    ),
                ) {
                    append(inner)
                }
                i = end + 2
                continue
            }
        }
        if (i + 1 < n && raw[i] == '_' && raw[i + 1] == '_') {
            val end = raw.indexOf("__", startIndex = i + 2)
            if (end > i + 2) {
                val inner = raw.substring(i + 2, end).replace("**", "")
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(inner)
                }
                i = end + 2
                continue
            }
        }
        append(raw[i])
        i++
    }
}
