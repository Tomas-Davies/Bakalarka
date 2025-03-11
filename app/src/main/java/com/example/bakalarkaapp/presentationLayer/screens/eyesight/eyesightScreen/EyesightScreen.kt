package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightScreen

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
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen.EyesightComparisonScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.EyesightDifferScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen.EyesightSynthesisScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSearch.EyesightSearchScreen
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.ImageLevel
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelsScreen
import com.example.bakalarkaapp.theme.AppTheme

class EyesightScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightScreenContent()
                }
            }
        }
    }

    @Composable
    private fun EyesightScreenContent() {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_eyesight)
        ) {
            val ctx = LocalContext.current

            val buttons = listOf<@Composable () -> Unit>(
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 0) },
                        label = stringResource(id = R.string.eyesight_menu_label_1),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_1),
                        imageId = R.drawable.eyesight_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 1) },
                        label = stringResource(id = R.string.eyesight_menu_label_2),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_2),
                        imageId = R.drawable.eyesight_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 2) },
                        label = stringResource(id = R.string.eyesight_menu_label_3),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_3),
                        imageId = R.drawable.eyesight_btn_3_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 3) },
                        label = stringResource(id = R.string.eyesight_menu_label_4),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_4),
                        imageId = R.drawable.eyesight_btn_4_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(ctx, 4) },
                        label = stringResource(id = R.string.eyesight_menu_label_5),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_5),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_5),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_5),
                        imageId = R.drawable.eyesight_btn_5_logo
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
        var intent = Intent(ctx, LevelsScreen::class.java)
        when (id) {
            0 -> {
                intent.putExtra(ImageLevel.NEXT_CLASS_TAG, EyesightComparisonScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_COMPARISON.id)
            }

            1 -> {
                intent.putExtra(ImageLevel.NEXT_CLASS_TAG, EyesightSearchScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_SEARCH.id)
            }

            2 -> {
                intent.putExtra(ImageLevel.NEXT_CLASS_TAG, EyesightDifferScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_DIFFER.id)
            }

            3 -> intent = Intent(ctx, EyesightMemoryScreen::class.java)
            4 -> {
                intent.putExtra(ImageLevel.NEXT_CLASS_TAG, EyesightSynthesisScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_SYNTHESIS.id)
            }
        }
        intent.putExtra(ThemeType.TAG, ThemeType.THEME_EYESIGHT.id)
        ctx.startActivity(intent)
    }
}