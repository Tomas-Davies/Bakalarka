package com.tomdev.logopadix.presentationLayer.screens.rythm.rythmScreen

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
import com.tomdev.logopadix.presentationLayer.screens.levels.IImageLevel
import com.tomdev.logopadix.presentationLayer.screens.levels.LevelsScreen
import com.tomdev.logopadix.presentationLayer.screens.rythm.rythmSyllablesScreen.RythmSyllabelsScreen
import com.tomdev.logopadix.presentationLayer.screens.rythm.rythmShelvesScreen.RythmShelvesScreen
import com.tomdev.logopadix.presentationLayer.screens.rythm.rythmRepeatScreen.RythmRepeatScreen
import com.tomdev.logopadix.theme.AppTheme


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
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_rythm)
        ) {
            var chosenExcerciseId by remember { mutableIntStateOf(-1) }
            var showDiffDialog by remember { mutableStateOf(false) }
            var selectedExcerciseImageId by remember { mutableIntStateOf(0) }
            var selectedExcerciseLabelId by remember { mutableIntStateOf(0) }

            val buttons = listOf<@Composable ()->Unit>(
                {
                    CategoryButton(
                        onClick = {
                            onDiffClicked(0, "")
                        },
                        label = stringResource(id = R.string.rythm_menu_label_1),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_1),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_1),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_1),
                        imageId = R.drawable.rythm_btn_1_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = {
                            chosenExcerciseId = 1
                            selectedExcerciseLabelId = R.string.rythm_menu_label_2
                            selectedExcerciseImageId = R.drawable.rythm_btn_2_logo
                            showDiffDialog = true
                        },
                        label = stringResource(id = R.string.rythm_menu_label_2),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_2),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_2),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_2),
                        imageId = R.drawable.rythm_btn_2_logo
                    )
                },
                {
                    CategoryButton(
                        onClick = { onDiffClicked(2, "") },
                        label = stringResource(id = R.string.rythm_menu_label_3),
                        labelLong = stringResource(id = R.string.rythm_menu_label_long_3),
                        popUpHeading = stringResource(id = R.string.rythm_menu_label_long_3),
                        popUpContent = stringResource(id = R.string.rythm_pop_up_body_3),
                        imageId = R.drawable.rythm_btn_3_logo
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


    private fun onDiffClicked(id: Int, diff: String) {
        var intent = Intent(this, RythmShelvesScreen::class.java)

        when (id) {
            1 -> {
                intent = Intent(this, LevelsScreen::class.java)
                intent.putExtra(IImageLevel.NEXT_CLASS_TAG, RythmSyllabelsScreen::class.java)
                intent.putExtra(RepositoryType.TAG, RepositoryType.RYTHM_SYLLABLES.id)
            }
            2 -> intent = Intent(this, RythmRepeatScreen::class.java)
        }
        intent.putExtra(ThemeType.TAG, ThemeType.THEME_RYTHM.id)
        intent.putExtra(DifficultyType.TAG, diff)
        startActivity(intent)
    }
}