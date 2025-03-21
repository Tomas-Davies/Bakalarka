package com.example.bakalarkaapp.presentationLayer.screens.homeScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightScreen.EyesightScreen
import com.example.bakalarkaapp.presentationLayer.screens.hearing.hearingScreen.HearingScreen
import com.example.bakalarkaapp.presentationLayer.screens.rythm.rythmScreen.RythmScreen
import com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen.SpeechScreen
import com.example.bakalarkaapp.presentationLayer.screens.tales.talesMenu.TalesScreen
import com.example.bakalarkaapp.theme.AppTheme


class HomeScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreenContent(){
        Scaffold(
            topBar = { 
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                )
            }
        ) { pdVal ->
            MainContent(pdVal)
        }
    }


    @Composable
    private fun MainContent(pdVal: PaddingValues){
        BoxWithConstraints {
            if (maxWidth < 500.dp){
                MainMenu(padding = pdVal)
            } else {
                MainMenu(
                    padding = pdVal,
                    sidePadding = 32.dp,
                    menuItemRatio = 1.5f,
                    menuItemWideRatio = 3f
                )
            }
        }
    }


    @Composable
    private fun MainMenu(
        padding: PaddingValues,
        sidePadding: Dp = 18.dp,
        menuItemRatio: Float = 1f,
        menuItemWideRatio: Float = 2.5f
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(sidePadding, padding.calculateTopPadding(), sidePadding, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LazyVerticalGrid(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                columns = GridCells.Fixed(2),
            ) {
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_speech),
                        id = 0,
                        ratio = menuItemRatio,
                        imageId = R.drawable.home_speech_btn_image,
                        colorId = R.color.speech_200
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_eyesight),
                        id = 1,
                        ratio = menuItemRatio,
                        imageId = R.drawable.home_eyesight_btn_image,
                        colorId = R.color.eyesight_200
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_hearing),
                        id = 2,
                        ratio = menuItemRatio,
                        imageId = R.drawable.home_hearing_btn_image,
                        colorId = R.color.hearing_200
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_rythm),
                        id = 3,
                        ratio = menuItemRatio,
                        imageId = R.drawable.home_rythm_btn_image,
                        colorId = R.color.rythm_200
                    ) }
                item(span = { GridItemSpan(2) }) {
                    MenuCard(
                        title = stringResource(id = R.string.category_tales),
                        id = 4,
                        ratio = menuItemWideRatio,
                        imageId = R.drawable.home_tales_btn_image,
                        colorId = R.color.tales_200
                    ) }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .weight(4f)
                        .align(Alignment.Bottom),
                    painter = painterResource(id = R.drawable.home_decor_bubble),
                    contentDescription = "decoration",
                    contentScale = ContentScale.FillWidth
                )
                Image(
                    modifier = Modifier
                        .weight(3f)
                        .align(Alignment.Bottom),
                    painter = painterResource(id = R.drawable.home_screen_decor),
                    contentDescription = "decoration",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }


    @Composable
    private fun MenuCard(
        title: String,
        id: Int,
        ratio: Float = 1f,
        imageId: Int = R.drawable.dummy_image_500,
        colorId: Int
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                onClick = { onCardClicked(id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = colorId))
                        .paint(
                            painterResource(id = imageId),
                            contentScale = ContentScale.Fit
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = colorResource(id = R.color.white),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


    private fun onCardClicked(id: Int){
        var intent = Intent(this, SpeechScreen::class.java)
        when(id) {
            1 -> {intent = Intent(this, EyesightScreen::class.java)}
            2 -> {intent = Intent(this, HearingScreen::class.java)}
            3 -> {intent = Intent(this, RythmScreen::class.java)}
            4 -> {intent = Intent(this, TalesScreen::class.java)}
        }
        startActivity(intent)
    }
}