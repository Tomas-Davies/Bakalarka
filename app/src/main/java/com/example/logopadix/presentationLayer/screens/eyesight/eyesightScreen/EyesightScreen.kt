package com.example.logopadix.presentationLayer.screens.eyesight.eyesightScreen

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
import com.example.logopadix.R
import com.example.logopadix.dataLayer.RepositoryType
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.presentationLayer.components.CategoryButton
import com.example.logopadix.presentationLayer.components.CategoryMenu
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.presentationLayer.screens.eyesight.eyesightComparisonScreen.EyesightComparisonScreen
import com.example.logopadix.presentationLayer.screens.eyesight.eyesightDifferScreen.EyesightDifferScreen
import com.example.logopadix.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryScreen
import com.example.logopadix.presentationLayer.screens.eyesight.eyesightSynthesisScreen.EyesightSynthesisScreen
import com.example.logopadix.presentationLayer.screens.eyesight.eyesightSearch.EyesightSearchScreen
import com.example.logopadix.presentationLayer.screens.levelsScreen.IImageLevel
import com.example.logopadix.presentationLayer.screens.levelsScreen.LevelsScreen
import com.example.logopadix.theme.AppTheme


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
            val buttons = listOf<@Composable () -> Unit>(
                {
                    CategoryButton(
                        onClick = { onCardClicked(0) },
                        label = stringResource(id = R.string.eyesight_menu_label_1),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_1),
                        imageId = R.drawable.eyesight_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(1) },
                        label = stringResource(id = R.string.eyesight_menu_label_2),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_2),
                        imageId = R.drawable.eyesight_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(2) },
                        label = stringResource(id = R.string.eyesight_menu_label_3),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_3),
                        imageId = R.drawable.eyesight_btn_3_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(3) },
                        label = stringResource(id = R.string.eyesight_menu_label_4),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_4),
                        imageId = R.drawable.eyesight_btn_4_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onCardClicked(4) },
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


    private fun onCardClicked(id: Int) {
        var intent = Intent(this, LevelsScreen::class.java)
        when (id) {
            0 -> {
                intent.putExtra(IImageLevel.NEXT_CLASS_TAG, EyesightComparisonScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_COMPARISON.id)
                intent.putExtra(IImageLevel.LEVEL_ITEM_LABEL_ID_TAG, R.string.eyesight_comparison_levels_label)
            }

            1 -> {
                intent.putExtra(IImageLevel.NEXT_CLASS_TAG, EyesightSearchScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_SEARCH.id)
            }

            2 -> {
                intent.putExtra(IImageLevel.NEXT_CLASS_TAG, EyesightDifferScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_DIFFER.id)
                intent.putExtra(IImageLevel.LEVEL_ITEM_LABEL_ID_TAG, R.string.eyesight_differ_levels_label)
            }

            3 -> intent = Intent(this, EyesightMemoryScreen::class.java)
            4 -> {
                intent.putExtra(IImageLevel.NEXT_CLASS_TAG, EyesightSynthesisScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.EYESIGHT_SYNTHESIS.id)
            }
        }
        intent.putExtra(ThemeType.TAG, ThemeType.THEME_EYESIGHT.id)
        startActivity(intent)
    }
}