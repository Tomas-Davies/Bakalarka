package com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightAnalysisScreen.EyesightAnalysisScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightComparisonScreen.EyesightComparisonScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightDifferScreen.EyesightDifferScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightMemoryScreen.EyesightMemoryScreen
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthScreen.EyesightSynthScreen
import com.example.bakalarkaapp.ui.theme.AppTheme

class EyesightScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
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
                    .verticalScroll(rememberScrollState())
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EyesightMenuCard(
                    label = stringResource(id = R.string.eyesight_menu_label_1),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_1),
                    id = 0,
                    imgId = R.drawable.eyesight_btn_1_logo
                )
                EyesightMenuCard(
                    label = stringResource(id = R.string.eyesight_menu_label_2),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_2),
                    id = 1,
                    imgId = R.drawable.eyesight_btn_2_logo
                )
                EyesightMenuCard(
                    label = stringResource(id = R.string.eyesight_menu_label_3),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_3),
                    id = 2,
                    imgId = R.drawable.eyesight_btn_3_logo
                )
                EyesightMenuCard(
                    label = stringResource(id = R.string.eyesight_menu_label_4),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_4),
                    id = 3,
                    imgId = R.drawable.eyesight_btn_4_logo
                )
                EyesightMenuCard(
                    label = stringResource(id = R.string.eyesight_menu_label_5),
                    labelLong = stringResource(id = R.string.eyesight_menu_label_long_5),
                    id = 4,
                    imgId = R.drawable.eyesight_btn_5_logo
                )
            }
        }
    }

    @Composable
    private fun EyesightMenuCard(label: String, labelLong: String, id: Int, imgId: Int){
        val ctx = LocalContext.current
        ElevatedCard(
            shape = RoundedCornerShape(25.dp),
            onClick = { onCardClicked(ctx, id) },
            modifier = Modifier.fillMaxWidth(0.8f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardColors(
                contentColor = CardDefaults.cardColors().contentColor,
                containerColor = colorResource(id = R.color.eyesight_200),
                disabledContentColor = CardDefaults.cardColors().disabledContentColor,
                disabledContainerColor = CardDefaults.cardColors().disabledContainerColor
            )
        ) {
            CardContent(label, labelLong, imgId)
        }
        Spacer(modifier = Modifier.height(15.dp))
    }

    @Composable
    private fun CardContent(label:String, labelLong: String, imgId: Int){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    text = labelLong,
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    color = colorResource(id = R.color.white),
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                PlayButton()
            }

            Image(
                modifier = Modifier
                    .weight(2f)
                    .scale(0.8f),
                painter = painterResource(id = imgId),
                contentDescription = "decor image")
        }
    }

    @Composable
    fun PlayButton() {
       Row(
           modifier = Modifier
               .clip(RoundedCornerShape(25.dp))
               .background(colorResource(id = R.color.eyesight_300))
               .padding(5.dp, 2.dp),
           verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.label_show),
                color = colorResource(id = R.color.white),
                fontSize = 12.sp
            )
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "play icon",
                tint = colorResource(id = R.color.white)
            )
        }
    }
}

private fun onCardClicked(ctx: Context, id: Int){
    var intent = Intent(ctx, EyesightComparisonScreen::class.java)
    when(id){
        1 -> intent = Intent(ctx, EyesightAnalysisScreen::class.java)
        2 -> intent = Intent(ctx, EyesightDifferScreen::class.java)
        3 -> intent = Intent(ctx, EyesightMemoryScreen::class.java)
        4 -> intent = Intent(ctx, EyesightSynthScreen::class.java)
    }

    ctx.startActivity(intent)
}