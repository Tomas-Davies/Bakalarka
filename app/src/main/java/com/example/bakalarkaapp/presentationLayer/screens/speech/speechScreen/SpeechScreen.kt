package com.example.bakalarkaapp.presentationLayer.screens.speech.speechScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.SpeechLetter
import com.example.bakalarkaapp.presentationLayer.components.CustomAlertDialog
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.speech.speechDetailScreen.SpeechDetailScreen
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.theme.typography


class SpeechScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel: SpeechViewModel by viewModels {
            SpeechViewModelFactory(app)
        }
        setContent {
            AppTheme(ThemeType.THEME_SPEECH) {
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
            headerLabel = stringResource(id = R.string.category_speech)
        ) { pdVal ->
            val letters = viewModel.lettersAndPositions
            SpeechScreenMenu(pdVal, letters)
        }
    }


    @Composable
    private fun SpeechScreenMenu(pdVal: PaddingValues, letters: List<SpeechLetter>) {
        var showLetterOptions by remember { mutableStateOf(false) }
        var letterOptionsData by remember { mutableStateOf(SpeechLetter()) }
        val ctx = LocalContext.current

        LazyVerticalGrid(
            modifier = Modifier.padding(top = pdVal.calculateTopPadding(), bottom = 18.dp),
            columns = GridCells.Adaptive(100.dp)
        ) {
            items(letters) { letter ->
                SpeechButton(
                    text = letter.label,
                    onClick = {
                        if (letter.isPrimitive){
                            onCategoryClicked(ctx, letter.label, letter.positions[0].label)
                        } else {
                            letterOptionsData = letter
                            showLetterOptions = true
                        }
                    },
                    isPrimitive = letter.isPrimitive
                )
            }
        }
        if (showLetterOptions) {
            CustomAlertDialog(
                heading = stringResource(id = R.string.speech_options_label),
                onExit = { showLetterOptions = false }
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.weight(1f, fill = false),
                    columns = GridCells.Adaptive(100.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(letterOptionsData.positions) { pos ->
                        SpeechButton(
                            onClick = {
                                onCategoryClicked(ctx, letterOptionsData.label, pos.label)
                            },
                            text = pos.label
                        )
                    }
                }
            }
        }
    }



    @Composable
    fun SpeechButton(
        onClick: () -> Unit,
        text: String,
        isPrimitive: Boolean = true
    ) {
        val cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = CardDefaults.cardColors().contentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor
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


    private fun onCategoryClicked(ctx: Context, letterLabel: String, posLabel: String) {
        val intent = Intent(ctx, SpeechDetailScreen::class.java)
        intent.putExtra("LETTER_LABEL", letterLabel)
        intent.putExtra("POS_LABEL", posLabel)
        startActivity(intent)
    }
}