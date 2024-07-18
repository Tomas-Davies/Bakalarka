package com.example.bakalarkaapp.presentationLayer.screens.homeScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.ui.theme.AppTheme
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.eyesightScreen.EyesightScreen
import com.example.bakalarkaapp.presentationLayer.screens.hearingScreen.HearingScreen
import com.example.bakalarkaapp.presentationLayer.screens.rythmScreen.RythmScreen
import com.example.bakalarkaapp.presentationLayer.screens.speechScreen.SpeechScreen
import com.example.bakalarkaapp.presentationLayer.screens.tales.TalesScreen

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
                    },
                    actions = {
                        IconButton(onClick = { openProfileDetail() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.profile_icon),
                                contentDescription = "Profile Icon",
                                modifier = Modifier.size(32.dp))
                        }
                    }) 
            }
        ) {pdVal ->  
            MainMenu(padding = pdVal)
        }
    }

    @Composable
    private fun MainMenu( padding: PaddingValues){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp, padding.calculateTopPadding(), 18.dp, 9.dp),
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
                        imgId = R.drawable.speech_btn_img
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_eyesight),
                        id = 1,
                        imgId = R.drawable.eyesight_btn_img
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_hearing),
                        id = 2,
                        imgId = R.drawable.hearing_btn_img
                    ) }
                item {
                    MenuCard(
                        title = stringResource(id = R.string.category_rythm),
                        id = 3,
                        imgId = R.drawable.rythm_btn_img
                    ) }
                item(span = { GridItemSpan(2) }) {
                    MenuCard(
                        title = stringResource(id = R.string.category_tales),
                        id = 4,
                        ratio = 2.5f,
                        imgId = R.drawable.tales_btn_img
                    ) }
            }

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width(screenWidth / 2),
                    text = stringResource(id = R.string.home_screen_decor_label),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.home_screen_decor),
                    contentDescription = "decoration",
                    modifier = Modifier.scale(2.5f),
                )
            }

        }


    }

    @Composable
    private fun MenuCard(title: String, id: Int, ratio: Float = 1f, imgId: Int = R.drawable.dummy_img_500){
        val ctx = LocalContext.current
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                onClick = { onCardClicked(ctx, id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .paint(painterResource(id = imgId), contentScale = ContentScale.FillBounds),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.dummy_img),
//                        contentDescription = "card image",
//                        modifier = Modifier.fillMaxWidth()
//                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp
                    )
                }
            }
        }
    }

    private fun openProfileDetail(){
        
    }
    private fun onCardClicked(ctx: Context, id: Int){

        var intent = Intent(ctx, SpeechScreen::class.java)
        when(id) {
            1 -> {intent = Intent(ctx, EyesightScreen::class.java)}
            2 -> {intent = Intent(ctx, HearingScreen::class.java)}
            3 -> {intent = Intent(ctx, RythmScreen::class.java)}
            4 -> {intent = Intent(ctx, TalesScreen::class.java)}
        }
        ctx.startActivity(intent)
    }
}