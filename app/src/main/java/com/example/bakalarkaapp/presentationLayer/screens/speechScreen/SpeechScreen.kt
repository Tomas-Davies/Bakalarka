package com.example.bakalarkaapp.presentationLayer.screens.speechScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ui.theme.AppTheme


class SpeechScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SpeechScreenContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SpeechScreenContent(){
        val ctx = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.category_speech)) },
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
        ) {pdVal ->
            val arrIds = resources.obtainTypedArray(R.array.speech_levels)
            val len = arrIds.length()
            val levelItems = mutableListOf<Array<String>>()
            for (i in 0 until len){
                val id = arrIds.getResourceId(i, 0)
                val item = resources.getStringArray(id)
                levelItems.add(item)
            }
            arrIds.recycle()
            SpeechScreenMenu(pdVal, levelItems)
        }
    }

    @Composable
    private fun SpeechScreenMenu(pdVal: PaddingValues, levelItems: MutableList<Array<String>>){
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(pdVal),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            verticalItemSpacing = 15.dp,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(levelItems){levelItem ->
                val label = levelItem[0]
                SpeechScreenCard(Modifier,title = label, levelItems = levelItem)
            }
        }
    }

    @Composable
    private fun SpeechScreenCard(
        modifier: Modifier,
        title: String,
        levelItems: Array<String>
    ){
        val ctx = LocalContext.current
        var isCollapsedState by remember { mutableStateOf(true) }

        val rotationState by animateFloatAsState(
            targetValue = if (!isCollapsedState) 180f else 0f, label = ""
        )

        val isPrimitive = levelItems.size == 1

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .background(colorResource(id = R.color.speech_500))
                .padding(10.dp),
            onClick = {
                if (!isPrimitive){ isCollapsedState = !isCollapsedState }
                else onCategoryClicked(ctx, title)
            }
        ) {
            val horizontalArr = if (!isPrimitive) Arrangement.SpaceBetween else Arrangement.Start
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.speech_500))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArr
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

                if (!isPrimitive){
                    Icon(
                        modifier = Modifier.rotate(rotationState),
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "drop down arrow"
                    )
                }
            }

            if (!isCollapsedState){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.speech_500)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.speech_menu_item_label),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    levelItems.forEach { str ->
                        Spacer(modifier = Modifier.height(5.dp))
                        LevelItem(
                            label = str,
                            Modifier
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun LevelItem(
        label: String,
        modifier: Modifier
    ){
        val ctx = LocalContext.current
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(5.dp, colorResource(id = R.color.speech_200)),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.speech_500)
            ),
            onClick = {
                onCategoryClicked(ctx, label)
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
    private fun onCategoryClicked(ctx: Context, label: String){
        Toast.makeText(ctx, "To be done", Toast.LENGTH_SHORT).show()
    }
}