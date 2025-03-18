package com.example.bakalarkaapp.presentationLayer.screens.levelsScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.RepositoryType
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.DifferData
import com.example.bakalarkaapp.dataLayer.models.ComparisonItem
import com.example.bakalarkaapp.dataLayer.models.DifferItem
import com.example.bakalarkaapp.dataLayer.models.SearchData
import com.example.bakalarkaapp.dataLayer.models.SearchRound
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthData
import com.example.bakalarkaapp.dataLayer.models.EyesightSynthRound
import com.example.bakalarkaapp.dataLayer.models.ComparisonData
import com.example.bakalarkaapp.dataLayer.models.IModel
import com.example.bakalarkaapp.dataLayer.models.RythmSyllabData
import com.example.bakalarkaapp.dataLayer.models.RythmSyllabRound
import com.example.bakalarkaapp.presentationLayer.components.AsyncDataWrapper
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSearch.EyesightSearchScreen
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.utils.bundle.getSdkBasedSerializableExtra

class LevelsScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repoTypeName = intent.getIntExtra(RepositoryType.TAG, 0)

        // Chooses correct viewModel initialization based on repository type,
        // as it supports more types of exercises
        val viewModel = when(repoTypeName) {
            RepositoryType.EYESIGHT_SEARCH.id -> {
                val repo = app.eyesightSearchRepository
                val tmpViewModel: LevelsViewModel<SearchData, SearchRound> by viewModels {
                    LevelsViewModelFactory(app, repo, R.string.eyesight_menu_label_2)
                }
                tmpViewModel
            }
            RepositoryType.EYESIGHT_DIFFER.id -> {
                val repo = app.eyesightDifferRepository
                val tmpViewModel: LevelsViewModel<DifferData, DifferItem> by viewModels {
                    LevelsViewModelFactory(app, repo, R.string.eyesight_menu_label_3)
                }
                tmpViewModel
            }
            RepositoryType.EYESIGHT_COMPARISON.id -> {
                val repo = app.eyesightComparisonRepository
                val tmpViewModel: LevelsViewModel<ComparisonData, ComparisonItem> by viewModels {
                    LevelsViewModelFactory(app, repo, R.string.eyesight_menu_label_1)
                }
                tmpViewModel
            }
            RepositoryType.EYESIGHT_SYNTHESIS.id -> {
                val repo = app.eyesightSynthesisRepository
                val tmpViewModel: LevelsViewModel<EyesightSynthData, EyesightSynthRound> by viewModels {
                    LevelsViewModelFactory(app, repo, R.string.eyesight_menu_label_5)
                }
                tmpViewModel
            }
            RepositoryType.RYTHM_SYLLABLES.id -> {
                val repo = app.rythmSyllablesRepository
                val tmpViewModel: LevelsViewModel<RythmSyllabData, RythmSyllabRound> by viewModels {
                    LevelsViewModelFactory(app, repo, R.string.rythm_menu_label_2)
                }
                tmpViewModel
            }
            else -> throw IllegalArgumentException("Unknown repository type")
        }
        val themeType = intent.getIntExtra(ThemeType.TAG, 0)

        setContent {
            AppTheme(themeType) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LevelsScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    fun <T : IModel<S>, S : IImageLevel> LevelsScreenContent(viewModel: LevelsViewModel<T, S>) {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(viewModel.headingId)
        ) {
            AsyncDataWrapper(viewModel) {
                val levels by viewModel.levels.collectAsStateWithLifecycle()

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Adaptive(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
                ) {
                    itemsIndexed(levels) { i, level ->
                        val image = viewModel.getDrawableId("${level}_low")
                        LevelImageIcon(imageId = image, i)
                    }
                }
            }
        }
    }


    @Composable
    private fun LevelImageIcon(imageId: Int, i: Int){
        val nextActivityClass = intent.getSdkBasedSerializableExtra(IImageLevel.NEXT_CLASS_TAG, Class::class.java)
        val labelId = intent.getIntExtra(IImageLevel.LEVEL_ITEM_LABEL_ID_TAG, R.string.round)

        ElevatedCard(
            onClick = {
                val intent = Intent(this, nextActivityClass)
                intent.putExtra(IImageLevel.TAG, i)
                startActivity(intent)
            },
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.White
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var alignment = Alignment.Center
                var contentScale = ContentScale.Fit
                if (nextActivityClass == EyesightSearchScreen::class.java) {
                    alignment = Alignment.TopCenter
                    contentScale = ContentScale.Crop
                }
                Image(
                    modifier = Modifier
                        .height(125.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = imageId),
                    contentDescription = "Search level icon",
                    contentScale = contentScale,
                    alignment = alignment
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(0.dp, 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.weight(3f),
                        text = stringResource(id = labelId) + ": " + "${i + 1}",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        modifier = Modifier.weight(1f),
                        imageVector =  Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}