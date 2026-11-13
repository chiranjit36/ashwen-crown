package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.view.WindowCompat
import com.example.model.EpicThemeMode

@Immutable
data class EpicSemanticColors(
    val hairlineBorder: Color,
    val goldAccent: Color,
    val crimsonAccent: Color,
    val mutedText: Color,
    val searchHighlightBg: Color,
    val searchActiveHighlightBg: Color,
    val searchHighlightText: Color,
    val quoteBackground: Color,
    val codeBackground: Color,
    val isDark: Boolean,
)

val LocalEpicColors = staticCompositionLocalOf {
    EpicSemanticColors(
        hairlineBorder = ImperialBorder,
        goldAccent = ImperialGold,
        crimsonAccent = ImperialCrimson,
        mutedText = ImperialOnSurfaceMuted,
        searchHighlightBg = Color(0x66D4AF37),
        searchActiveHighlightBg = Color(0xFFD4AF37),
        searchHighlightText = Color(0xFF09080A),
        quoteBackground = Color(0xFF171319),
        codeBackground = Color(0xFF0D0B0F),
        isDark = true,
    )
}

private val ImperialDarkScheme = darkColorScheme(
    primary = ImperialGold,
    onPrimary = Color(0xFF181205),
    primaryContainer = Color(0xFF2B2210),
    onPrimaryContainer = ImperialGoldSoft,
    secondary = ImperialCrimson,
    onSecondary = Color(0xFFFFF5F5),
    secondaryContainer = Color(0xFF3E1016),
    onSecondaryContainer = Color(0xFFFFD8DC),
    tertiary = ImperialTeal,
    onTertiary = Color(0xFF03231E),
    background = ImperialBg,
    onBackground = ImperialOnBg,
    surface = ImperialSurface,
    onSurface = ImperialOnBg,
    surfaceVariant = ImperialSurfaceVariant,
    onSurfaceVariant = ImperialOnSurfaceMuted,
    outline = ImperialBorder,
    outlineVariant = Color(0xFF282124),
)

private val BloodVelvetScheme = darkColorScheme(
    primary = VelvetGold,
    onPrimary = Color(0xFF231605),
    primaryContainer = Color(0xFF3B2412),
    onPrimaryContainer = Color(0xFFFCE29D),
    secondary = VelvetCrimson,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF561522),
    onSecondaryContainer = Color(0xFFFFD9DF),
    tertiary = VelvetAmber,
    onTertiary = Color(0xFF2A1405),
    background = VelvetBg,
    onBackground = VelvetOnBg,
    surface = VelvetSurface,
    onSurface = VelvetOnBg,
    surfaceVariant = VelvetSurfaceVariant,
    onSurfaceVariant = VelvetOnSurfaceMuted,
    outline = VelvetBorder,
    outlineVariant = Color(0xFF391420),
)

private val ObsidianAbyssScheme = darkColorScheme(
    primary = AbyssCyan,
    onPrimary = Color(0xFF041C2B),
    primaryContainer = Color(0xFF0F2D49),
    onPrimaryContainer = Color(0xFFCDEEFF),
    secondary = AbyssGold,
    onSecondary = Color(0xFF1F1604),
    secondaryContainer = Color(0xFF312712),
    onSecondaryContainer = Color(0xFFF6E2A5),
    tertiary = AbyssIndigo,
    onTertiary = Color(0xFF0C1238),
    background = AbyssBg,
    onBackground = AbyssOnBg,
    surface = AbyssSurface,
    onSurface = AbyssOnBg,
    surfaceVariant = AbyssSurfaceVariant,
    onSurfaceVariant = AbyssOnSurfaceMuted,
    outline = AbyssBorder,
    outlineVariant = Color(0xFF192842),
)

private val AncientParchmentScheme = lightColorScheme(
    primary = ParchmentCrimson,
    onPrimary = Color(0xFFFFF8F2),
    primaryContainer = Color(0xFFF0D8D4),
    onPrimaryContainer = Color(0xFF4D0B10),
    secondary = ParchmentGold,
    onSecondary = Color(0xFFFFFBF2),
    secondaryContainer = Color(0xFFEDE0BE),
    onSecondaryContainer = Color(0xFF402C03),
    tertiary = ParchmentTeal,
    onTertiary = Color(0xFFFFFFFF),
    background = ParchmentBg,
    onBackground = ParchmentOnBg,
    surface = ParchmentSurface,
    onSurface = ParchmentOnBg,
    surfaceVariant = ParchmentSurfaceVariant,
    onSurfaceVariant = ParchmentOnSurfaceMuted,
    outline = ParchmentBorder,
    outlineVariant = Color(0xFFDCCCB2),
)

private val EpicShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(26.dp),
)

@Composable
fun RaktabeejTheme(
    themeMode: EpicThemeMode = EpicThemeMode.IMPERIAL_DARK,
    content: @Composable () -> Unit,
) {
    val colorScheme: ColorScheme
    val semantic: EpicSemanticColors

    when (themeMode) {
        EpicThemeMode.IMPERIAL_DARK -> {
            colorScheme = ImperialDarkScheme
            semantic = EpicSemanticColors(
                hairlineBorder = ImperialBorder,
                goldAccent = ImperialGold,
                crimsonAccent = ImperialCrimson,
                mutedText = ImperialOnSurfaceMuted,
                searchHighlightBg = Color(0x66D4AF37),
                searchActiveHighlightBg = ImperialGold,
                searchHighlightText = Color(0xFF09080A),
                quoteBackground = Color(0xFF171319),
                codeBackground = Color(0xFF0D0B0F),
                isDark = true,
            )
        }
        EpicThemeMode.BLOOD_VELVET -> {
            colorScheme = BloodVelvetScheme
            semantic = EpicSemanticColors(
                hairlineBorder = VelvetBorder,
                goldAccent = VelvetGold,
                crimsonAccent = VelvetCrimson,
                mutedText = VelvetOnSurfaceMuted,
                searchHighlightBg = Color(0x66E5BD64),
                searchActiveHighlightBg = VelvetGold,
                searchHighlightText = Color(0xFF15060A),
                quoteBackground = Color(0xFF280E16),
                codeBackground = Color(0xFF19080D),
                isDark = true,
            )
        }
        EpicThemeMode.OBSIDIAN_ABYSS -> {
            colorScheme = ObsidianAbyssScheme
            semantic = EpicSemanticColors(
                hairlineBorder = AbyssBorder,
                goldAccent = AbyssGold,
                crimsonAccent = AbyssCyan,
                mutedText = AbyssOnSurfaceMuted,
                searchHighlightBg = Color(0x5538BDF8),
                searchActiveHighlightBg = AbyssCyan,
                searchHighlightText = Color(0xFF060A13),
                quoteBackground = Color(0xFF111C31),
                codeBackground = Color(0xFF09101E),
                isDark = true,
            )
        }
        EpicThemeMode.ANCIENT_PARCHMENT -> {
            colorScheme = AncientParchmentScheme
            semantic = EpicSemanticColors(
                hairlineBorder = ParchmentBorder,
                goldAccent = ParchmentGold,
                crimsonAccent = ParchmentCrimson,
                mutedText = ParchmentOnSurfaceMuted,
                searchHighlightBg = Color(0x55D4AF37),
                searchActiveHighlightBg = Color(0xFF966E14),
                searchHighlightText = Color(0xFFFFFBF2),
                quoteBackground = Color(0xFFE7DAC2),
                codeBackground = Color(0xFFE4D8C1),
                isDark = false,
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !semantic.isDark
                isAppearanceLightNavigationBars = !semantic.isDark
            }
            @Suppress("DEPRECATION")
            run {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
            }
        }
    }

    CompositionLocalProvider(LocalEpicColors provides semantic) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = EpicShapes,
            content = content,
        )
    }
}
