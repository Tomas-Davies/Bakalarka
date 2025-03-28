package com.example.logopadix.presentationLayer.screens.hearing.hearingScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.logopadix.theme.AppTheme
import com.example.logopadix.R
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.presentationLayer.components.CategoryButton
import com.example.logopadix.presentationLayer.components.CategoryMenu
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.presentationLayer.screens.hearing.hearingFonematic.HearingFonematicScreen
import com.example.logopadix.presentationLayer.screens.hearing.hearingMemory.HearingMemoryScreen
import com.example.logopadix.presentationLayer.screens.hearing.hearingSynthesis.HearingSynthesisScreen


class HearingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_HEARING.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HearingScreenContent()
                }
            }
        }
    }


    @Composable
    private fun HearingScreenContent() {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_hearing)
        ) {
            val buttons = listOf<@Composable () -> Unit>(
                {
                    CategoryButton(
                        onClick = { onCardClicked(0) },
                        label = stringResource(id = R.string.hearing_menu_label_1),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_1),
                        imageId = R.drawable.hearing_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(1) },
                        label = stringResource(id = R.string.hearing_menu_label_2),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_2),
                        imageId = R.drawable.hearing_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(2) },
                        label = stringResource(id = R.string.hearing_menu_label_3),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_3),
                        imageId = R.drawable.hearing_btn_3_logo
                    )
                }
            )

            CategoryMenu(
                pdVal = it,
                buttons = buttons
            )
        }
    }


    private fun onCardClicked(id: Int) {
        var intent = Intent(this, HearingFonematicScreen::class.java)
        when (id) {
            1 -> intent = Intent(this, HearingMemoryScreen::class.java)
            2 -> intent = Intent(this, HearingSynthesisScreen::class.java)
        }
        startActivity(intent)
    }
}