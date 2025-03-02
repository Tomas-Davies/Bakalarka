package com.example.bakalarkaapp.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.bakalarkaapp.ThemeType


private val SpeechDarkColors = darkColorScheme(
    primary = speech_theme_dark_primary,
    onPrimary = speech_theme_dark_onPrimary,
    primaryContainer = speech_theme_dark_primaryContainer,
    onPrimaryContainer = speech_theme_dark_onPrimaryContainer,
    error = speech_theme_dark_error,
    errorContainer = speech_theme_dark_errorContainer,
    onError = speech_theme_dark_onError,
    onErrorContainer = speech_theme_dark_onErrorContainer,
    background = speech_theme_dark_background,
    onBackground = speech_theme_dark_onBackground,
    surface = speech_theme_dark_surface,
    onSurface = speech_theme_dark_onSurface,
    surfaceVariant = speech_theme_dark_surfaceVariant,
    onSurfaceVariant = speech_theme_dark_onSurfaceVariant,
    outline = speech_theme_dark_outline,
)

private val EyesightDarkColors = darkColorScheme(
    primary = eyesight_theme_dark_primary,
    onPrimary = eyesight_theme_dark_onPrimary,
    primaryContainer = eyesight_theme_dark_primaryContainer,
    onPrimaryContainer = eyesight_theme_dark_onPrimaryContainer,
    error = eyesight_theme_dark_error,
    errorContainer = eyesight_theme_dark_errorContainer,
    onError = eyesight_theme_dark_onError,
    onErrorContainer = eyesight_theme_dark_onErrorContainer,
    background = eyesight_theme_dark_background,
    onBackground = eyesight_theme_dark_onBackground,
    surface = eyesight_theme_dark_surface,
    onSurface = eyesight_theme_dark_onSurface,
    surfaceVariant = eyesight_theme_dark_surfaceVariant,
    onSurfaceVariant = eyesight_theme_dark_onSurfaceVariant,
    outline = eyesight_theme_dark_outline,
)

private val HearingDarkColors = darkColorScheme(
    primary = hearing_theme_dark_primary,
    onPrimary = hearing_theme_dark_onPrimary,
    primaryContainer = hearing_theme_dark_primaryContainer,
    onPrimaryContainer = hearing_theme_dark_onPrimaryContainer,
    error = hearing_theme_dark_error,
    errorContainer = hearing_theme_dark_errorContainer,
    onError = hearing_theme_dark_onError,
    onErrorContainer = hearing_theme_dark_onErrorContainer,
    background = hearing_theme_dark_background,
    onBackground = hearing_theme_dark_onBackground,
    surface = hearing_theme_dark_surface,
    onSurface = hearing_theme_dark_onSurface,
    surfaceVariant = hearing_theme_dark_surfaceVariant,
    onSurfaceVariant = hearing_theme_dark_onSurfaceVariant,
    outline = hearing_theme_dark_outline,
)

private val RythmDarkColors = darkColorScheme(
    primary = rythm_theme_dark_primary,
    onPrimary = rythm_theme_dark_onPrimary,
    primaryContainer = rythm_theme_dark_primaryContainer,
    onPrimaryContainer = rythm_theme_dark_onPrimaryContainer,
    error = rythm_theme_dark_error,
    errorContainer = rythm_theme_dark_errorContainer,
    onError = rythm_theme_dark_onError,
    onErrorContainer = rythm_theme_dark_onErrorContainer,
    background = rythm_theme_dark_background,
    onBackground = rythm_theme_dark_onBackground,
    surface = rythm_theme_dark_surface,
    onSurface = rythm_theme_dark_onSurface,
    surfaceVariant = rythm_theme_dark_surfaceVariant,
    onSurfaceVariant = rythm_theme_dark_onSurfaceVariant,
    outline = rythm_theme_dark_outline,
    surfaceDim = rythm_theme_dark_surface_dim
)

private val TalesDarkColors = darkColorScheme(
    primary = tales_theme_dark_primary,
    onPrimary = tales_theme_dark_onPrimary,
    primaryContainer = tales_theme_dark_primaryContainer,
    onPrimaryContainer = tales_theme_dark_onPrimaryContainer,
    error = tales_theme_dark_error,
    errorContainer = tales_theme_dark_errorContainer,
    onError = tales_theme_dark_onError,
    onErrorContainer = tales_theme_dark_onErrorContainer,
    background = tales_theme_dark_background,
    onBackground = tales_theme_dark_onBackground,
    surface = tales_theme_dark_surface,
    onSurface = tales_theme_dark_onSurface,
    surfaceVariant = tales_theme_dark_surfaceVariant,
    onSurfaceVariant = tales_theme_dark_onSurfaceVariant,
    outline = tales_theme_dark_outline,
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