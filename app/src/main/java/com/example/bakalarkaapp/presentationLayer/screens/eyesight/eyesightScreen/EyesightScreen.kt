package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.components.CategoryButton
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen.EyesightComparisonScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.EyesightDifferScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen.EyesightSynthesisScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.imageSearch.EyesightSearchScreen
import com.example.bakalarkaapp.presentationLayer.screens.levelsScreen.LevelsScreen
import com.example.bakalarkaapp.theme.AppTheme

class EyesightScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_EYESIGHT) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EyesightScreenContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EyesightScreenContent(){
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.category_eyesight)) },
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val cardModifier = Modifier
                val primaryColor = colorResource(id = R.color.eyesight_200)
                val secondaryColor = colorResource(id = R.color.eyesight_300)
                val textColor = colorResource(id = R.color.light)

                CategoryButton(
                    modifier = cardModifier,
                    label = stringResource(id = R.string.eyesight_menu_label_1),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_1),
                    popUpHeading = stringResource(id = R.string.eyesight_pop_up_heading_1),
                    popUpContent = stringResource(id = R.string.eyesight_pop_up_body_1),
                    imgId = R.drawable.eyesight_btn_1_logo,
                    onClick = { onCardClicked(ctx, 0) },
                    bgColorPrimary = primaryColor,
                    bgColorSecondary = secondaryColor,
                    textColor = textColor
                )
                CategoryButton(
                    modifier = cardModifier,
                    label = stringResource(id = R.string.eyesight_menu_label_2),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_2),
                    popUpHeading = stringResource(id = R.string.eyesight_pop_up_heading_2),
                    popUpContent = stringResource(id = R.string.eyesight_pop_up_body_2),
                    imgId = R.drawable.eyesight_btn_2_logo,
                    onClick = { onCardClicked(ctx, 1) },
                    bgColorPrimary = primaryColor,
                    bgColorSecondary = secondaryColor,
                    textColor = textColor
                )
                CategoryButton(
                    modifier = cardModifier,
                    label = stringResource(id = R.string.eyesight_menu_label_3),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_3),
                    popUpHeading = stringResource(id = R.string.eyesight_pop_up_heading_3),
                    popUpContent = stringResource(id = R.string.eyesight_pop_up_body_3),
                    imgId = R.drawable.eyesight_btn_3_logo,
                    onClick = { onCardClicked(ctx, 2) },
                    bgColorPrimary = primaryColor,
                    bgColorSecondary = secondaryColor,
                    textColor = textColor
                )
                CategoryButton(
                    modifier = cardModifier,
                    label = stringResource(id = R.string.eyesight_menu_label_4),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_4),
                    popUpHeading = stringResource(id = R.string.eyesight_pop_up_heading_4),
                    popUpContent = stringResource(id = R.string.eyesight_pop_up_body_4),
                    imgId = R.drawable.eyesight_btn_4_logo,
                    onClick = { onCardClicked(ctx, 3) },
                    bgColorPrimary = primaryColor,
                    bgColorSecondary = secondaryColor,
                    textColor = textColor
                )
                CategoryButton(
                    modifier = cardModifier,
                    label = stringResource(id = R.string.eyesight_menu_label_5),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_5),
                    popUpHeading = stringResource(id = R.string.eyesight_pop_up_heading_5),
                    popUpContent = stringResource(id = R.string.eyesight_pop_up_body_5),
                    imgId = R.drawable.eyesight_btn_5_logo,
                    onClick = { onCardClicked(ctx, 4) },
                    bgColorPrimary = primaryColor,
                    bgColorSecondary = secondaryColor,
                    textColor = textColor
                )
            }
        }
    }
}

private fun onCardClicked(ctx: Context, id: Int) {
    var intent = Intent(ctx, LevelsScreen::class.java)
    when(id){
        0 -> {
            intent.putExtra("NEXT_ACTIVITY_CLASS", EyesightComparisonScreen::class.java)
            intent.putExtra("REPOSITORY_TYPE", "COMPARISON")
        }
        1 -> {
            intent = Intent(ctx, LevelsScreen::class.java)
            intent.putExtra("NEXT_ACTIVITY_CLASS", EyesightSearchScreen::class.java)
            intent.putExtra("REPOSITORY_TYPE", "SEARCH")
        }
        2 -> {
            intent = Intent(ctx, LevelsScreen::class.java)
            intent.putExtra("NEXT_ACTIVITY_CLASS", EyesightDifferScreen::class.java)
            intent.putExtra("REPOSITORY_TYPE", "DIFFER")
        }
        3 -> intent = Intent(ctx, EyesightMemoryScreen::class.java)
        4 -> {
            intent = Intent(ctx, LevelsScreen::class.java)
            intent.putExtra("NEXT_ACTIVITY_CLASS", EyesightSynthesisScreen::class.java)
            intent.putExtra("REPOSITORY_TYPE", "SYNTHESIS")
        }
    }

    intent.putExtra("THEME_TYPE", ThemeType.THEME_EYESIGHT)
    ctx.startActivity(intent)
}