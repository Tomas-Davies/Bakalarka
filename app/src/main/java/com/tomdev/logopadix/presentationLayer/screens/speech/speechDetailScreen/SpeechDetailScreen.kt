package com.tomdev.logopadix.presentationLayer.screens.speech.speechDetailScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomdev.logopadix.R
import com.tomdev.logopadix.theme.ThemeType
import com.tomdev.logopadix.dataLayer.UserSentence
import com.tomdev.logopadix.presentationLayer.components.AsyncDataWrapper
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.CustomDialog
import com.tomdev.logopadix.presentationLayer.components.OptionsMenu
import com.tomdev.logopadix.presentationLayer.components.PlaySoundButton
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme


class SpeechDetailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wordsChosen = intent.getBooleanExtra("SHOW_WORDS", true)
        val letterLabel = intent.getStringExtra("LETTER_LABEL") ?: ""
        val posLabel = intent.getStringExtra("POS_LABEL") ?: ""

        val app = application as com.tomdev.logopadix.LogoApp
        val repo = app.speechRepository
        val viewModel: SpeechDetailViewModel by viewModels {
            SpeechDetailViewModelFactory(repo, letterLabel, posLabel, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SpeechDetailContent(
                        viewModel = viewModel,
                        wordsChosen = wordsChosen,
                        letterLabel = letterLabel,
                        posLabel = posLabel
                    )
                }
            }
        }
    }


    @Composable
    private fun SpeechDetailContent(
        viewModel: SpeechDetailViewModel,
        wordsChosen: Boolean,
        letterLabel: String,
        posLabel: String
    ) {
        val category = if (posLabel.isNotEmpty() && posLabel != "NONE") posLabel else letterLabel
        val label = if (wordsChosen) stringResource(id = R.string.speech_options_words) else stringResource(id = R.string.speech_options_sentences)
        var clickedButtonIdx by remember { mutableIntStateOf(0) }

        ScreenWrapper(
            onExit = { finish() },
            title = "$label - $category",
            fab = {
                if (clickedButtonIdx == 1) {
                    FloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = { viewModel.addUserSentence("") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "add answer"
                        )
                    }
                }
            }
        ) { pdVal ->
            AsyncDataWrapper(viewModel = viewModel) {
                if (wordsChosen) {
                    SpeechWordsContent(
                        pdVal = pdVal,
                        viewModel = viewModel
                    )
                } else {
                    SpeechSentencesContent(
                        pdVal = pdVal,
                        viewModel = viewModel,
                        clickedButtonIdx = clickedButtonIdx,
                        onCategoryClicked = { idx -> clickedButtonIdx = idx }
                    )
                }
            }
        }
    }


    @Composable
    private fun SpeechSentencesContent(
        pdVal: PaddingValues,
        viewModel: SpeechDetailViewModel,
        clickedButtonIdx: Int,
        onCategoryClicked: (idx: Int) -> Unit
    ) {
        val userSentences by viewModel.userSentences.collectAsStateWithLifecycle()
        val items = if (clickedButtonIdx == 0) viewModel.defaultSentences
        else userSentences.reversed()

        Column(
            modifier = Modifier.padding(top = pdVal.calculateTopPadding(), bottom = pdVal.calculateBottomPadding())
        ) {
            OptionsMenu(
                onOptionClick = { idx -> onCategoryClicked(idx) },
                itemLabels = listOf(
                    stringResource(id = R.string.speech_sentences_category_1),
                    stringResource(id = R.string.speech_sentences_category_2),
                )
            )
            if (items.isEmpty()) {
                EmptyDataScreen()
            } else {
                Sentences(
                    items = items,
                    viewModel = viewModel,
                    clickedButtonIdx = clickedButtonIdx
                )
            }
        }
    }


    @Composable
    private fun Sentences(
        items: List<Any>,
        viewModel: SpeechDetailViewModel,
        clickedButtonIdx: Int
    ) {
        var showDeletePopUp by remember { mutableStateOf(false) }
        var sentenceToDelete by remember { mutableStateOf<UserSentence?>(null) }

        if (showDeletePopUp) {
            DeleteSentencePopUp(
                onDismiss = { showDeletePopUp = false },
                onConfirm = {
                    sentenceToDelete?.let { viewModel.deleteUserSentence(it) }
                    sentenceToDelete = null
                    showDeletePopUp = false
                }
            )
        }
        LazyVerticalGrid(
            modifier = Modifier.padding(18.dp),
            columns = GridCells.Adaptive(minSize = 300.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { _, sentence -> sentence.toString() }) { idx, sentence ->
                if (clickedButtonIdx == 0) {
                    if (sentence is String) {
                        DefaultSentenceCard(
                            number = idx + 1,
                            sentence = sentence
                        )
                    }
                } else {
                    if (sentence is UserSentence) {
                        UserSentenceCard(
                            number = idx + 1,
                            userSentence = sentence,
                            viewModel = viewModel,
                            onDeleteClick = {
                                sentenceToDelete = sentence
                                showDeletePopUp = true
                            },
                            onEditClick = {}
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun EmptyDataScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val id = "inlineContent"
            val txt = buildAnnotatedString {
                append("Kliknutím na ")
                appendInlineContent(id, "[ICON]")
                append(" přidejte své věty.")
            }
            val inlineContent = mapOf(
                id to InlineTextContent(
                    Placeholder(
                        width = 22.sp,
                        height = 22.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = ""
                    )
                }
            )
            Text(
                text = txt,
                inlineContent = inlineContent
            )
        }
    }


    @Composable
    private fun DefaultSentenceCard(
        number: Int,
        sentence: String
    ) {
        CustomCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(9.dp),
                text = "$number"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TextFieldDefaults.contentPaddingWithoutLabel()),
                text = sentence,
                fontSize = 22.sp
            )
        }
    }


    @Composable
    private fun UserSentenceCard(
        number: Int,
        userSentence: UserSentence,
        viewModel: SpeechDetailViewModel,
        onDeleteClick: () -> Unit,
        onEditClick: () -> Unit
    ) {
        var textFieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    text = userSentence.sentence,
                    selection = TextRange(userSentence.sentence.length)
                )
            )
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        var readOnlyFieldState by remember { mutableStateOf(true) }

        CustomCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$number")
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    if (readOnlyFieldState) {
                        EditButton(
                            onClick = {
                                onEditClick()
                                readOnlyFieldState = false
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                        )
                    } else {
                        SaveButton(
                            onClick = {
                                readOnlyFieldState = true
                                keyboardController?.hide()
                                viewModel.onSaveButtonClick(textFieldValue.text, userSentence)
                            }
                        )
                    }
                    DeleteButton(
                        onClick = { onDeleteClick() }
                    )
                }
            }
            TextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                textStyle = TextStyle().copy(fontSize = 22.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                readOnly = readOnlyFieldState
            )
        }
        LaunchedEffect(Unit) {
            if (number == 1 && userSentence.sentence.isBlank()) {
                readOnlyFieldState = false
                focusRequester.requestFocus()
                keyboardController?.show()
            }
        }
    }


    @Composable
    fun DeleteButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) = CircleIconButton(
        modifier = modifier,
        onClick = { onClick() },
        color = MaterialTheme.colorScheme.errorContainer,
        icon = Icons.Filled.Clear
    )


    @Composable
    fun EditButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) = CircleIconButton(
        modifier = modifier,
        onClick = { onClick() },
        color = Color.Gray,
        icon = Icons.Filled.Edit
    )


    @Composable
    fun SaveButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) = CircleIconButton(
        modifier = modifier,
        onClick = { onClick() },
        color = Color.Green,
        icon = Icons.Filled.Check
    )


    @Composable
    fun CircleIconButton(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        color: Color,
        icon: ImageVector
    ) {
        Surface(
            modifier = modifier.size(28.dp),
            onClick = { onClick() },
            shape = CircleShape,
            color = color
        ) {
            Icon(
                modifier = Modifier
                    .padding(5.dp),
                imageVector = icon,
                contentDescription = "delete button"
            )
        }
    }


    @Composable
    private fun DeleteSentencePopUp(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit
    ){
        CustomDialog(
            onExit = { onDismiss() },
            showExitButton = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Delete warning"
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.speechDeletePopUpText),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        onClick = { onConfirm() }
                    ) {
                        Text(text = stringResource(id = R.string.confirmButtonLabel))
                    }
                    Button(
                        onClick = { onDismiss() }
                    ) {
                        Text(text = stringResource(id = R.string.dismissButtonLabel))
                    }
                }
            }
        }
    }


    @Composable
    private fun SpeechWordsContent(
        pdVal: PaddingValues,
        viewModel: SpeechDetailViewModel
    ) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp, pdVal.calculateTopPadding(), 18.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val drawableName = uiState.currentWord.imageName ?: ""
            val drawableId = viewModel.getDrawableId(drawableName)

            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    painter = painterResource(id = drawableId),
                    contentDescription = "image"
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    text = uiState.currentWord.text ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = Modifier
                    .weight(0.2f)
                    .wrapContentHeight(),
                text = "${uiState.index + 1} / ${viewModel.count}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.scale(1.5f),
                    onClick = { viewModel.previous() },
                    enabled = !uiState.isOnFirstWord
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "button previous"
                    )
                }
                val soundName = uiState.currentWord.soundName ?: ""
                val soundId = viewModel.getSoundId(soundName)

                PlaySoundButton(
                    onClick = { viewModel.playSound(soundId) }
                )
                IconButton(
                    modifier = Modifier.scale(1.5f),
                    onClick = { viewModel.next() },
                    enabled = !uiState.isOnLastWord
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "button next"
                    )
                }
            }
        }
    }
}