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
    secondary = speech_theme_dark_secondary,
    onSecondary = speech_theme_dark_onSecondary,
    secondaryContainer = speech_theme_dark_secondaryContainer,
    onSecondaryContainer = speech_theme_dark_onSecondaryContainer,
    tertiary = speech_theme_dark_tertiary,
    onTertiary = speech_theme_dark_onTertiary,
    tertiaryContainer = speech_theme_dark_tertiaryContainer,
    onTertiaryContainer = speech_theme_dark_onTertiaryContainer,
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
    inverseOnSurface = speech_theme_dark_inverseOnSurface,
    inverseSurface = speech_theme_dark_inverseSurface,
    inversePrimary = speech_theme_dark_inversePrimary,
    surfaceTint = speech_theme_dark_surfaceTint,
    outlineVariant = speech_theme_dark_outlineVariant,
    scrim = speech_theme_dark_scrim,
)

private val EyesightDarkColors = darkColorScheme(
    primary = eyesight_theme_dark_primary,
    onPrimary = eyesight_theme_dark_onPrimary,
    primaryContainer = eyesight_theme_dark_primaryContainer,
    onPrimaryContainer = eyesight_theme_dark_onPrimaryContainer,
    secondary = eyesight_theme_dark_secondary,
    onSecondary = eyesight_theme_dark_onSecondary,
    secondaryContainer = eyesight_theme_dark_secondaryContainer,
    onSecondaryContainer = eyesight_theme_dark_onSecondaryContainer,
    tertiary = eyesight_theme_dark_tertiary,
    onTertiary = eyesight_theme_dark_onTertiary,
    tertiaryContainer = eyesight_theme_dark_tertiaryContainer,
    onTertiaryContainer = eyesight_theme_dark_onTertiaryContainer,
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
    inverseOnSurface = eyesight_theme_dark_inverseOnSurface,
    inverseSurface = eyesight_theme_dark_inverseSurface,
    inversePrimary = eyesight_theme_dark_inversePrimary,
    surfaceTint = eyesight_theme_dark_surfaceTint,
    outlineVariant = eyesight_theme_dark_outlineVariant,
    scrim = eyesight_theme_dark_scrim,
)

private val HearingDarkColors = darkColorScheme(
    primary = hearing_theme_dark_primary,
    onPrimary = hearing_theme_dark_onPrimary,
    primaryContainer = hearing_theme_dark_primaryContainer,
    onPrimaryContainer = hearing_theme_dark_onPrimaryContainer,
    secondary = hearing_theme_dark_secondary,
    onSecondary = hearing_theme_dark_onSecondary,
    secondaryContainer = hearing_theme_dark_secondaryContainer,
    onSecondaryContainer = hearing_theme_dark_onSecondaryContainer,
    tertiary = hearing_theme_dark_tertiary,
    onTertiary = hearing_theme_dark_onTertiary,
    tertiaryContainer = hearing_theme_dark_tertiaryContainer,
    onTertiaryContainer = hearing_theme_dark_onTertiaryContainer,
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
    inverseOnSurface = hearing_theme_dark_inverseOnSurface,
    inverseSurface = hearing_theme_dark_inverseSurface,
    inversePrimary = hearing_theme_dark_inversePrimary,
    surfaceTint = hearing_theme_dark_surfaceTint,
    outlineVariant = hearing_theme_dark_outlineVariant,
    scrim = hearing_theme_dark_scrim,
)

private val RythmDarkColors = darkColorScheme(
    primary = rythm_theme_dark_primary,
    onPrimary = rythm_theme_dark_onPrimary,
    primaryContainer = rythm_theme_dark_primaryContainer,
    onPrimaryContainer = rythm_theme_dark_onPrimaryContainer,
    secondary = rythm_theme_dark_secondary,
    onSecondary = rythm_theme_dark_onSecondary,
    secondaryContainer = rythm_theme_dark_secondaryContainer,
    onSecondaryContainer = rythm_theme_dark_onSecondaryContainer,
    tertiary = rythm_theme_dark_tertiary,
    onTertiary = rythm_theme_dark_onTertiary,
    tertiaryContainer = rythm_theme_dark_tertiaryContainer,
    onTertiaryContainer = rythm_theme_dark_onTertiaryContainer,
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
    inverseOnSurface = rythm_theme_dark_inverseOnSurface,
    inverseSurface = rythm_theme_dark_inverseSurface,
    inversePrimary = rythm_theme_dark_inversePrimary,
    surfaceTint = rythm_theme_dark_surfaceTint,
    outlineVariant = rythm_theme_dark_outlineVariant,
    scrim = rythm_theme_dark_scrim,
)

private val TalesDarkColors = darkColorScheme(
    primary = tales_theme_dark_primary,
    onPrimary = tales_theme_dark_onPrimary,
    primaryContainer = tales_theme_dark_primaryContainer,
    onPrimaryContainer = tales_theme_dark_onPrimaryContainer,
    secondary = tales_theme_dark_secondary,
    onSecondary = tales_theme_dark_onSecondary,
    secondaryContainer = tales_theme_dark_secondaryContainer,
    onSecondaryContainer = tales_theme_dark_onSecondaryContainer,
    tertiary = tales_theme_dark_tertiary,
    onTertiary = tales_theme_dark_onTertiary,
    tertiaryContainer = tales_theme_dark_tertiaryContainer,
    onTertiaryContainer = tales_theme_dark_onTertiaryContainer,
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
    inverseOnSurface = tales_theme_dark_inverseOnSurface,
    inverseSurface = tales_theme_dark_inverseSurface,
    inversePrimary = tales_theme_dark_inversePrimary,
    surfaceTint = tales_theme_dark_surfaceTint,
    outlineVariant = tales_theme_dark_outlineVariant,
    scrim = tales_theme_dark_scrim,
)

@Composable
fun AppTheme(
    type: Int = ThemeType.THEME_SPEECH,
    content: @Composable () -> Unit
) {
    var colors = SpeechDarkColors

    when (type) {
        ThemeType.THEME_SPEECH -> {
            colors = SpeechDarkColors
        }

        ThemeType.THEME_EYESIGHT -> {
            colors = EyesightDarkColors
        }

        ThemeType.THEME_HEARING -> {
            colors = HearingDarkColors
        }

        ThemeType.THEME_RYTHM -> {
            colors = RythmDarkColors
        }

        ThemeType.THEME_TALES -> {
            colors = TalesDarkColors
        }
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