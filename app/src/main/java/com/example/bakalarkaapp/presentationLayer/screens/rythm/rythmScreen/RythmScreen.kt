package com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmScreen

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
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.RepositoryType
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.CategoryButton
import com.example.bakalarkaapp.presentationLayer.components.CategoryMenu
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelsScreen
import com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmSyllablesScreen.RythmSyllabelsScreen
import com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmShelvesScreen.RythmShelvesScreen
import com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmRepeatScreen.RythmRepeatScreen
import com.example.bakalarkaapp.theme.AppTheme

class RythmScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_RYTHM.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RythmScreenContent()
                }
            }
        }
    }

    @Composable
    private fun RythmScreenContent() {
        val ctx = LocalContext.current
        ScreenWrapper(
            onExit = { this.finish() },
            title = stringResource(id = R.string.category_rythm)
        ) {
            val buttons = listOf<@Composable ()->Unit>(
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 0) },
                        label = stringResource(id = R.string.rythm_menu_label_1),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_1),
                        imageId = R.drawable.rythm_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 1) },
                        label = stringResource(id = R.string.rythm_menu_label_2),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_2),
                        imageId = R.drawable.rythm_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 2) },
                        label = stringResource(id = R.string.rythm_menu_label_3),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_3),
                        imageId = R.drawable.rythm_btn_3_logo
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
        var intent = Intent(ctx, RythmShelvesScreen::class.java)

        when (id) {
            1 -> {
                intent = Intent(ctx, LevelsScreen::class.java)
                intent.putExtra(ImageLevel.NEXT_CLASS_TAG, RythmSyllabelsScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.RYTHM_SYLLABLES.id)
            }
            2 -> intent = Intent(ctx, RythmRepeatScreen::class.java)
        }
        intent.putExtra(ThemeType.TAG, ThemeType.THEME_RYTHM)
        ctx.startActivity(intent)
    }
}