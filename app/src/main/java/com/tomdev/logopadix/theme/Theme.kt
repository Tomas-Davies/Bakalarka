package com.tomdev.logopadix.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val SpeechDarkColors = darkColorScheme(
    primary = speech_theme_primary,
    onPrimary = speech_theme_onPrimary,
    primaryContainer = speech_theme_primaryContainer,
    onPrimaryContainer = speech_theme_onPrimaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = speech_theme_background,
    onBackground = speech_theme_onBackground,
    surface = speech_theme_surface,
    onSurface = speech_theme_onSurface,
    surfaceVariant = speech_theme_surfaceVariant,
    onSurfaceVariant = speech_theme_onSurfaceVariant,
    outline = speech_theme_outline,
    outlineVariant = speech_theme_outline_variant
)

private val EyesightDarkColors = darkColorScheme(
    primary = eyesight_theme_primary,
    onPrimary = eyesight_theme_onPrimary,
    primaryContainer = eyesight_theme_primaryContainer,
    onPrimaryContainer = eyesight_theme_onPrimaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = eyesight_theme_background,
    onBackground = eyesight_theme_onBackground,
    surface = eyesight_theme_surface,
    onSurface = eyesight_theme_onSurface,
    surfaceVariant = eyesight_theme_surfaceVariant,
    onSurfaceVariant = eyesight_theme_onSurfaceVariant,
    outline = eyesight_theme_outline,
    outlineVariant = eyesight_theme_outline_variant
)

private val HearingDarkColors = darkColorScheme(
    primary = hearing_theme_primary,
    onPrimary = hearing_theme_onPrimary,
    primaryContainer = hearing_theme_primaryContainer,
    onPrimaryContainer = hearing_theme_onPrimaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = hearing_theme_background,
    onBackground = hearing_theme_onBackground,
    surface = hearing_theme_surface,
    onSurface = hearing_theme_onSurface,
    surfaceVariant = hearing_theme_surfaceVariant,
    onSurfaceVariant = hearing_theme_onSurfaceVariant,
    outline = hearing_theme_outline,
    outlineVariant = hearing_theme_outline_variant
)

private val RythmDarkColors = darkColorScheme(
    primary = rythm_theme_primary,
    onPrimary = rythm_theme_onPrimary,
    primaryContainer = rythm_theme_primaryContainer,
    onPrimaryContainer = rythm_theme_onPrimaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = rythm_theme_background,
    onBackground = rythm_theme_onBackground,
    surface = rythm_theme_surface,
    onSurface = rythm_theme_onSurface,
    surfaceVariant = rythm_theme_surfaceVariant,
    onSurfaceVariant = rythm_theme_onSurfaceVariant,
    outline = rythm_theme_outline,
    outlineVariant = rythm_theme_outline_variant,
    surfaceDim = rythm_theme_surface_dim
)

private val TalesDarkColors = darkColorScheme(
    primary = tales_theme_primary,
    onPrimary = tales_theme_onPrimary,
    primaryContainer = tales_theme_primaryContainer,
    onPrimaryContainer = tales_theme_onPrimaryContainer,
    error = dark_error,
    errorContainer = dark_errorContainer,
    onError = dark_onError,
    onErrorContainer = dark_onErrorContainer,
    background = tales_theme_background,
    onBackground = tales_theme_onBackground,
    surface = tales_theme_surface,
    onSurface = tales_theme_onSurface,
    surfaceVariant = tales_theme_surfaceVariant,
    onSurfaceVariant = tales_theme_onSurfaceVariant,
    outline = tales_theme_outline,
    outlineVariant = tales_theme_outline_variant
)

@Composable
fun AppTheme(
    type: Int = ThemeType.THEME_SPEECH.id,
    content: @Composable () -> Unit
) {
    var colors = SpeechDarkColors

    when (type) {
        ThemeType.THEME_SPEECH.id -> colors = SpeechDarkColors
        ThemeType.THEME_EYESIGHT.id -> colors = EyesightDarkColors
        ThemeType.THEME_HEARING.id -> colors = HearingDarkColors
        ThemeType.THEME_RYTHM.id -> colors = RythmDarkColors
        ThemeType.THEME_TALES.id -> colors = TalesDarkColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = typography
    )
}