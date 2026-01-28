package com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.RepositoryType
import com.tomdev.logopadix.presentationLayer.DifficultyType
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.presentationLayer.components.CategoryButton
import com.tomdev.logopadix.presentationLayer.components.CategoryMenu
import com.tomdev.logopadix.presentationLayer.components.DifficultyDialog
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightComparisonScreen.EyesightComparisonScreen
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightDifferScreen.EyesightDifferScreen
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryScreen
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightSynthesisScreen.EyesightSynthesisScreen
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightSearch.EyesightSearchScreen
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.tomdev.logopadix.presentationLayer.screens.levels.LevelsScreen
import com.tomdev.logopadix.theme.AppTheme


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
            var chosenExcerciseId by remember { mutableIntStateOf(-1) }
            var showDiffDialog by remember { mutableStateOf(false) }
            var selectedExcerciseImageId by remember { mutableIntStateOf(0) }
            var selectedExcerciseLabelId by remember { mutableIntStateOf(0) }

            val buttons = listOf<@Composable () -> Unit>(
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 0
                            selectedExcerciseLabelId = R.string.eyesight_menu_label_1
                            selectedExcerciseImageId = R.drawable.eyesight_btn_1_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.eyesight_menu_label_1),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_1),
                        imageId = R.drawable.eyesight_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 1
                            selectedExcerciseLabelId = R.string.eyesight_menu_label_2
                            selectedExcerciseImageId = R.drawable.eyesight_btn_2_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.eyesight_menu_label_2),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_2),
                        imageId = R.drawable.eyesight_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 2
                            selectedExcerciseLabelId = R.string.eyesight_menu_label_3
                            selectedExcerciseImageId = R.drawable.eyesight_btn_3_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.eyesight_menu_label_3),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_3),
                        imageId = R.drawable.eyesight_btn_3_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 3
                            selectedExcerciseLabelId = R.string.eyesight_menu_label_4
                            selectedExcerciseImageId = R.drawable.eyesight_btn_4_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.eyesight_menu_label_4),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_4),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_4),
                        imageId = R.drawable.eyesight_btn_4_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 4
                            selectedExcerciseLabelId = R.string.eyesight_menu_label_5
                            selectedExcerciseImageId = R.drawable.eyesight_btn_5_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.eyesight_menu_label_5),
                        labelLong = stringResource(id = R.string.eyesight_menu_label_long_5),
                        popUpHeading = stringResource(id = R.string.eyesight_menu_label_long_5),
                        popUpContent = stringResource(id = R.string.eyesight_pop_up_body_5),
                        imageId = R.drawable.eyesight_btn_5_logo
                    )
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ){
                CategoryMenu(
                    pdVal = it,
                    buttons = buttons
                )

                DifficultyDialog(
                    isVisible = showDiffDialog,
                    setIsVisible = { visible -> showDiffDialog = visible },
                    selectedExcerciseImage = selectedExcerciseImageId,
                    selectedExcerciseLabel = selectedExcerciseLabelId,
                    difficultyHeading = stringResource(R.string.difficulty_heading),
                    easyLabel = stringResource(R.string.difficulty_easy),
                    mediumLabel = stringResource(R.string.difficulty_medium),
                    hardLabel = stringResource(R.string.difficulty_hard),
                    easyColor = R.color.correct,
                    mediumColor = R.color.hearing_500,
                    hardColor = R.color.diff_hard,
                    onEasyClick = { onDiffClicked(chosenExcerciseId, DifficultyType.EASY.name) },
                    onMediumClick = { onDiffClicked(chosenExcerciseId, DifficultyType.MEDIUM.name) },
                    onHardClick = { onDiffClicked(chosenExcerciseId, DifficultyType.HARD.name) },
                    easyPainter = painterResource(R.drawable.difficulty_beginner),
                    mediumPainter = painterResource(R.drawable.difficulty_medium),
                    hardPainter = painterResource(R.drawable.difficulty_hard)
                )
            }
        }
    }


    private fun onDiffClicked(excerciseId: Int, diffType: String) {
        var intent = Intent(this, LevelsScreen::class.java)
        when (excerciseId) {
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
        intent.putExtra(DifficultyType.TAG, diffType)
        startActivity(intent)
    }
}