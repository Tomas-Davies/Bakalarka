package com.tomdev.logopadix.presentationLayer.screens.speech.speechFilterScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.LogoApp
import com.tomdev.logopadix.R
import com.tomdev.logopadix.dataLayer.repositories.NormalizedWordContent
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.DeleteButton
import com.tomdev.logopadix.presentationLayer.components.PlaySoundButton
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme
import com.tomdev.logopadix.theme.ThemeType

class SpeechFilterScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as LogoApp
        val repo = app.speechRepository
        val viewModel: SpeechFilterViewModel by viewModels {
            SpeechFilterViewModelFactory(app, repo)
        }
        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.speech_menu_label_2),
                        onExit = { finish() }
                    ) {
                        SpeechFilterContent(viewModel, it)
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SpeechFilterContent(
        viewModel: SpeechFilterViewModel,
        pdVal: PaddingValues
    ){
        var containsFieldText by remember { mutableStateOf("") }
        var excludeFieldText by remember { mutableStateOf("") }
        var startsFieldText by remember { mutableStateOf("") }
        var endsFieldText by remember { mutableStateOf("") }
        var showFillFormError by remember { mutableStateOf(false) }
        val filteredWords = viewModel.filteredWords.collectAsStateWithLifecycle()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showSheet by rememberSaveable { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .padding(18.dp, pdVal.calculateTopPadding(), 18.dp, 18.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showFillFormError){
                Text(
                    text = stringResource(R.string.speech_filter_field_error),
                    color = Color.Red
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                FilterField(
                    modifier = Modifier.weight(1f),
                    value = containsFieldText,
                    upperText = stringResource(R.string.speech_filter_field_1),
                    placeholder = stringResource(R.string.speech_filter_field_letter_placeholder),
                    onValueChange = {
                        containsFieldText = it
                        showFillFormError = false
                    }
                )
                FilterField(
                    modifier = Modifier.weight(1f),
                    value = excludeFieldText,
                    upperText = stringResource(R.string.speech_filter_field_2),
                    placeholder = stringResource(R.string.speech_filter_field_letter_placeholder),
                    onValueChange = {
                        excludeFieldText = it
                        showFillFormError = false
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                FilterField(
                    modifier = Modifier.weight(1f),
                    value = startsFieldText,
                    upperText = stringResource(R.string.speech_filter_field_3),
                    placeholder = stringResource(R.string.speech_filter_field_begins_placeholder),
                    onValueChange = {
                        startsFieldText = it
                        showFillFormError = false
                    }
                )
                FilterField(
                    modifier = Modifier.weight(1f),
                    value = endsFieldText,
                    upperText = stringResource(R.string.speech_filter_field_4),
                    placeholder = stringResource(R.string.speech_filter_field_ends_placeholder),
                    onValueChange = {
                        endsFieldText = it
                        showFillFormError = false
                    }
                )
            }
            Button(
                modifier = Modifier.padding(vertical = 18.dp),
                onClick = {
                    val fieldContents = containsFieldText+excludeFieldText+startsFieldText+endsFieldText
                    if (fieldContents.isEmpty()){
                        showFillFormError = true
                    } else {
                        viewModel.GetWords(containsFieldText, excludeFieldText, startsFieldText, endsFieldText)
                        showSheet = true
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                    containsFieldText = ""
                    excludeFieldText = ""
                    startsFieldText = ""
                    endsFieldText = ""
                }
            ) {
                Text(text = stringResource(R.string.speech_filter_btn))
            }

            Image(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.speech_filter_decor),
                contentDescription = "decoration image"
            )
        }

        if (showSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                onDismissRequest = {
                    showSheet = false
                },
                sheetState = sheetState,
                sheetGesturesEnabled = false
            ) {
                AsyncDataWrapper(viewModel) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        DeleteButton(
                            modifier = Modifier.padding(18.dp),
                            onClick = {
                                showSheet = false
                            }
                        )
                    }
                    if (filteredWords.value.isNotEmpty()){
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(filteredWords.value){ wordContent ->
                                key(wordContent.text) {
                                    WordCard(wordContent, viewModel)
                                }
                            }
                        }
                    } else {
                        NothingToShow()
                    }
                }
            }
        }
    }


    @Composable
    fun NothingToShow(){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.speech_filter_not_found),
                    contentDescription = "decor image"
                )

                Text(
                    text = stringResource(R.string.speech_filter_no_matches),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }


    @Composable
    fun WordCard(
        wordContent: NormalizedWordContent,
        viewModel: SpeechFilterViewModel
    ){
        val imageId = viewModel.getDrawableId(wordContent.imageName)

        CustomCard(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 9.dp)
                .sizeIn(maxHeight = 200.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaySoundButton(
                    modifier = Modifier.padding(18.dp),
                    onClick = { viewModel.playSoundByName(wordContent.soundName) }
                )
                Spacer(Modifier.weight(0.1f))
                Text(
                    modifier = Modifier.border(1.dp, Color.Red),
                    text = wordContent.text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(0.1f))
                Image(
                    modifier = Modifier
                        .weight(1f),
                    painter = painterResource(imageId),
                    contentDescription = "word image",
                    contentScale = ContentScale.Fit
                )
            }

        }
    }


    @Composable
    fun FilterField(
        modifier: Modifier,
        value: String,
        upperText: String,
        placeholder: String,
        onValueChange: (newVal: String) -> Unit
    ){
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = "$upperText:",
                    fontSize = 18.sp
                )
                TextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    placeholder = { Text(text = placeholder) },
                    shape = RoundedCornerShape(25.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors().copy(
                        focusedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.outline,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Color.LightGray,
                        unfocusedPlaceholderColor = Color.LightGray
                    )
                )
            }

        }
    }
}