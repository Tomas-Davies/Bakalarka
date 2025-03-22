package com.example.logopadix.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.logopadix.R
import com.example.logopadix.utils.typography.withFontFamily


val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, style = FontStyle.Italic),

    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_light_italic, FontWeight.Light, style = FontStyle.Italic),

    Font(R.font.roboto_extra_light, FontWeight.ExtraLight),
    Font(R.font.roboto_extra_light_italic, FontWeight.ExtraLight, style = FontStyle.Italic),

    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_medium_italic, FontWeight.Medium, style = FontStyle.Italic),

    Font(R.font.roboto_semi_bold, FontWeight.SemiBold),
    Font(R.font.roboto_semi_bold_italic, FontWeight.SemiBold, style = FontStyle.Italic),

    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bold_italic, FontWeight.Bold, style = FontStyle.Italic),

    Font(R.font.roboto_extra_bold, FontWeight.ExtraBold),
    Font(R.font.roboto_extra_bold_italic, FontWeight.ExtraBold, style = FontStyle.Italic),

    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_black_italic, FontWeight.Black, style = FontStyle.Italic)
)

val typography = Typography().withFontFamily(robotoFamily)