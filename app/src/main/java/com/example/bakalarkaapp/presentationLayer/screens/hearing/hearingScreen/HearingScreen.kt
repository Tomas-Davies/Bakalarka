package com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.CategoryButton
import com.example.bakalarkaapp.presentationLayer.components.CategoryMenu
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingFonematic.HearingFonematicScreen
import com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingMemory.HearingMemoryScreen
import com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingSynthesis.HearingSynthesisScreen

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
        val ctx = LocalContext.current
        ScreenWrapper(
            title = stringResource(id = R.string.category_hearing)
        ) {
            val buttons = listOf<@Composable () -> Unit>(
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 0) },
                        label = stringResource(id = R.string.hearing_menu_label_1),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_1),
                        imgId = R.drawable.hearing_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 1) },
                        label = stringResource(id = R.string.hearing_menu_label_2),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_2),
                        imgId = R.drawable.hearing_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 2) },
                        label = stringResource(id = R.string.hearing_menu_label_3),
                        labelLong = stringResource(id = R.string.hearing_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.hearing_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.hearing_pop_up_body_3),
                        imgId = R.drawable.hearing_btn_3_logo
                    )
                }
            )

            CategoryMenu(
                pdVal = it,
                buttons = buttons
            )
        }
    }

    private fun onCardClicked(ctx: Context, id: Int) {
        var intent = Intent(ctx, HearingFonematicScreen::class.java)
        when (id) {
            1 -> intent = Intent(ctx, HearingMemoryScreen::class.java)
            2 -> intent = Intent(ctx, HearingSynthesisScreen::class.java)
        }
        ctx.startActivity(intent)
    }
}