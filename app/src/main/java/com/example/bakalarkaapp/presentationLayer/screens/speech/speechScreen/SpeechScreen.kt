package com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.LetterPosition
import com.example.bakalarkaapp.dataLayer.models.SpeechLetter
import com.example.bakalarkaapp.presentationLayer.components.AsyncDataWrapper
import com.example.bakalarkaapp.presentationLayer.components.CustomDialog
import com.example.bakalarkaapp.presentationLayer.components.OptionsMenu
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen.SpeechDetailScreen
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.theme.typography


class SpeechScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.speechRepository
        val viewModel: SpeechViewModel by viewModels {
            SpeechViewModelFactory(repo, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SpeechScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun SpeechScreenContent(viewModel: SpeechViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_speech)
        ) { pdVal ->
            AsyncDataWrapper(viewModel = viewModel) {
                val letters = viewModel.lettersAndPositions
                SpeechScreenMenu(pdVal, letters)
            }
        }
    }


    @Composable
    private fun SpeechScreenMenu(pdVal: PaddingValues, letters: List<SpeechLetter>) {
        var showLetterOptions by remember { mutableStateOf(false) }
        var letterOptionsData by remember { mutableStateOf(SpeechLetter()) }

        LazyVerticalGrid(
            contentPadding = PaddingValues(top = pdVal.calculateTopPadding(), bottom = 18.dp),
            columns = GridCells.Adaptive(100.dp)
        ) {
            items(letters) { letter ->
                SpeechButton(
                    text = letter.label,
                    onClick = {
                        letterOptionsData = letter
                        showLetterOptions = true
                    },
                    isPrimitive = letter.isPrimitive
                )
            }
        }
        if (showLetterOptions) {
            LetterOptionsDialog(
                letterOptionsData = letterOptionsData,
                onExit = { showLetterOptions = false  }
            )
        }
    }


    @Composable
    private fun LetterOptionsDialog(
        letterOptionsData: SpeechLetter,
        onExit: () -> Unit
    ){
        var clickedItemIdx by remember { mutableIntStateOf(0) }

        CustomDialog(
            onExit = { onExit() }
        ) {
            OptionsMenu(
                activeIndex = clickedItemIdx,
                onOptionClick = { itemIdx -> clickedItemIdx = itemIdx },
                itemLabels = listOf(
                    stringResource(id = R.string.speech_options_words),
                    stringResource(id = R.string.speech_options_sentences)
                )
            )
            Spacer(modifier = Modifier.height(18.dp))

            if (clickedItemIdx == 0){
                if (letterOptionsData.isPrimitive){
                    PrimitiveLetterOptions(
                        letterLabel = letterOptionsData.label
                    )
                } else {
                    NonPrimitiveLetterOptions(
                        letterLabel = letterOptionsData.label,
                        textModifier = Modifier.align(Alignment.CenterHorizontally),
                        gridModifier = Modifier.weight(1f, fill = false),
                        positions = letterOptionsData.positions
                    )
                }
            } else {
                SentencesOption(
                    letterLabel = letterOptionsData.label
                )
            }
        }
    }


    @Composable
    private fun PrimitiveLetterOptions(
        letterLabel: String,
    ){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            OutlinedButton(
                modifier = Modifier.padding(18.dp),
                onClick = {
                    onCategoryClicked(
                        letterLabel,
                        true
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(text = stringResource(id = R.string.speech_options_button_words))
            }
        }
    }


    @Composable
    private fun NonPrimitiveLetterOptions(
        letterLabel: String,
        textModifier: Modifier = Modifier,
        gridModifier: Modifier = Modifier,
        positions: List<LetterPosition>
    ){
        Text(
            modifier = textModifier,
            text = stringResource(id = R.string.speech_options_label),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(18.dp))
        LazyVerticalGrid(
            modifier = gridModifier,
            columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(positions) { pos ->
                SpeechButton(
                    onClick = {
                        onCategoryClicked(
                            letterLabel,
                            true,
                            pos.label
                        )
                    },
                    text = pos.label
                )
            }
        }
    }


    @Composable
    private fun SentencesOption(
        letterLabel: String
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedButton(
                modifier = Modifier.padding(18.dp),
                onClick = {
                    onCategoryClicked(
                        letterLabel,
                        false
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(text = stringResource(id = R.string.speech_options_button_sentences))
            }
        }
    }


    @Composable
    fun SpeechButton(
        onClick: () -> Unit,
        text: String,
        isPrimitive: Boolean = true
    ) {
        val cardColors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = Modifier
                .padding(9.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CardDefaults.shape
                ),
            onClick = { onClick() },
            colors = cardColors
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                text = text,
                style = typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }


    private fun onCategoryClicked(letterLabel: String, showWords: Boolean, posLabel: String = "") {
        val intent = Intent(this, SpeechDetailScreen::class.java)
        if (showWords) intent.putExtra("POS_LABEL", posLabel)
        intent.putExtra("LETTER_LABEL", letterLabel)
        intent.putExtra("SHOW_WORDS", showWords)
        startActivity(intent)
    }
}