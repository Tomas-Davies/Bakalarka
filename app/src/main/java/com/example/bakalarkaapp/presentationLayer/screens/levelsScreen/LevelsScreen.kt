package com.example.bakalarkaapp.presentationLayer.screens.levelsScreen

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.dataLayer.repositories.ComparisonItem
import com.example.bakalarkaapp.dataLayer.repositories.DifferItem
import com.example.bakalarkaapp.dataLayer.repositories.SearchRound
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch.EyesightSearchScreen
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.utils.bundle.serializable

class LevelsScreen : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeType = intent.getIntExtra("THEME_TYPE", 0)
        val nextActivityClass = intent.serializable("NEXT_ACTIVITY_CLASS", Class::class.java)

        setContent {
            AppTheme(themeType) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as LogoApp

                    val viewModel = when(intent.getStringExtra("REPOSITORY_TYPE")) {
                        "SEARCH" -> {
                            val repo = app.eyesightSearchRepository
                            val vm: LevelsViewModel<SearchRound> by viewModels {
                                LevelsViewModelFactory(repo)
                            }
                            vm
                        }
                        "DIFFER" -> {
                            val repo = app.eyesightDifferRepository
                            val vm: LevelsViewModel<DifferItem> by viewModels {
                                LevelsViewModelFactory(repo)
                            }
                            vm
                        }
                        "COMPARISON" -> {
                            val repo = app.eyesightComparisonRepository
                            val vm: LevelsViewModel<ComparisonItem> by viewModels {
                                LevelsViewModelFactory(repo)
                            }
                            vm
                        }
                        else -> throw IllegalArgumentException("Unknown repository type")
                    }
                    LevelsScreenContent(viewModel, nextActivityClass)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LevelsScreenContent(viewModel: LevelsViewModel<out LevelWithImage>, nextActivityClass: Class<*>?) {
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.eyesight_search_level_heading)) },
                    navigationIcon = {
                        IconButton(onClick = { (ctx as Activity).finish() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back button"
                            )
                        }
                    }
                )
            }
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 0.dp),
                columns = GridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {

                itemsIndexed(viewModel.levels) { i, level ->
                    val image = resources.getIdentifier("${level}_low", "drawable", packageName)
                    LevelIcon(imageId = image, i, nextActivityClass)
                }
            }
        }
    }

    @Composable
    private fun LevelIcon(imageId: Int, i: Int, nextActivityClass: Class<*>?){
        val ctx = LocalContext.current
        val cardColor = colorResource(id = R.color.eyesight_300)
        val textColor = colorResource(id = R.color.light)

        ElevatedCard(
            onClick = {
                val intent = Intent(ctx, nextActivityClass)
                intent.putExtra("LEVEL_INDEX", i)
                startActivity(intent)
            },
            colors = CardColors(
                contentColor = CardDefaults.cardColors().contentColor,
                containerColor = Color.White,
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var alignment = Alignment.Center
                var contentScale = ContentScale.Fit
                var colorFilter: ColorFilter? = null
                if (nextActivityClass == EyesightSearchScreen::class.java) {
                    alignment = Alignment.TopCenter
                    contentScale = ContentScale.Crop
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                }

                Image(
                    modifier = Modifier
                        .height(125.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = imageId),
                    contentDescription = "Search level icon",
                    colorFilter = colorFilter,
                    contentScale = contentScale,
                    alignment = alignment
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardColor)
                        .padding(0.dp, 10.dp),
                    text = "${stringResource(id = R.string.eyesight_search_level_info_label)}: ${i + 1}",
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}