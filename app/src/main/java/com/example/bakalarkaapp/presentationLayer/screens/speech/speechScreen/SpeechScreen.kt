package com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen.SpeechDetailScreen
import com.example.bakalarkaapp.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.LinkedList


class SpeechScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_SPEECH) {
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
            val scrollState = rememberScrollState()
            val coroutineScope = rememberCoroutineScope()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, pdVal.calculateTopPadding(), 15.dp, 0.dp)
                    .verticalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.Top
            ) {
                val firstCol = LinkedList<Array<String>>()
                val secondCol = LinkedList<Array<String>>()
                val thirdCol = LinkedList<Array<String>>()
                val colCount = 4
                for (i in levelItems.indices){
                    if (i % colCount == 0){
                        firstCol.add(levelItems[i])
                    } else if (i % colCount == 1) {
                        secondCol.add(levelItems[i])
                    }
                    else thirdCol.add(levelItems[i])
                }
                val modifier = Modifier.weight(1f)
                DataCol(modifier = modifier, col = firstCol, coroutineScope = coroutineScope, scrollState = scrollState)
                DataCol(modifier = modifier, col = secondCol, coroutineScope = coroutineScope, scrollState = scrollState)
                DataCol(modifier = modifier, col = thirdCol, coroutineScope = coroutineScope, scrollState = scrollState)
            }
    }


    @Composable
    private fun DataCol(
        modifier: Modifier,
        col: LinkedList<Array<String>>,
        coroutineScope: CoroutineScope,
        scrollState: ScrollState
    ){

        Column(
            modifier = modifier.padding(0.dp, 0.dp, 0.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            for (levelItem in col){
                val label = levelItem[0].replace("_", "")
                SpeechScreenCard(
                    Modifier,
                    title = label,
                    levelItems = levelItem
                ) {
                    if (scrollCond(label)) {
                        coroutineScope.launch {
                            scrollState.scrollTo(scrollState.maxValue)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SpeechScreenCard(
        modifier: Modifier,
        title: String,
        levelItems: Array<String>,
        onExpand: () -> Unit
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
                .background(colorResource(id = R.color.speech_500))
                .padding(10.dp),
            onClick = {
                if (!isPrimitive){
                    isCollapsedState = !isCollapsedState
                }
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
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                if (!isPrimitive){
                    Icon(
                        modifier = Modifier.rotate(rotationState),
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "drop down arrow"
                    )
                }
            }

            AnimatedVisibility(
                visible = !isCollapsedState,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.speech_500)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.speech_menu_item_label),
                        style = MaterialTheme.typography.titleMedium
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
                onExpand()
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.light)
                )
            }
        }
    }

    private fun onCategoryClicked(ctx: Context, label: String){
        val intent = Intent(ctx, SpeechDetailScreen::class.java)
        intent.putExtra("LABEL", label)
        startActivity(intent)
    }

    private fun scrollCond(label: String): Boolean {
        return (label == "Ž" || label == "R" || label == "Ř" || label == "Š" || label == "Č" || label == "CSZ" || label == "ČŠŽ")
    }
}