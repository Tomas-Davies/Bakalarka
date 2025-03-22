package com.example.logopadix.presentationLayer.screens.tales.taleDetail


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logopadix.LogoApp
import com.example.logopadix.R
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.dataLayer.repositories.Tale
import com.example.logopadix.dataLayer.repositories.TaleImage
import com.example.logopadix.presentationLayer.components.AsyncDataWrapper
import com.example.logopadix.presentationLayer.components.CustomDialog
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.theme.AppTheme
import com.example.logopadix.presentationLayer.screens.tales.TalesViewModel
import com.example.logopadix.presentationLayer.screens.tales.TalesViewModelFactory


class TaleDetailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.talesRepository
        val viewModel by viewModels<TalesViewModel> {
            TalesViewModelFactory(repo, app)
        }
        val taleIdx = intent.getIntExtra("TALE_INDEX", 0)
        val taleAndAnnotatedString = viewModel.getTaleAndAnnotatedString(taleIdx)
        setContent {
            AppTheme(ThemeType.THEME_TALES.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaleDetailScreenContent(viewModel, taleAndAnnotatedString)
                }
            }
        }
    }


    @Composable
    private fun TaleDetailScreenContent(viewModel: TalesViewModel, taleAndAnnotatedString: Pair<Tale, AnnotatedString>) {
        var showImagesDescription by remember { mutableStateOf(true) }
        val tale = taleAndAnnotatedString.first
        val annotatedString = taleAndAnnotatedString.second

        if (showImagesDescription) {
            ImagesDescription(
                tale = tale,
                viewModel = viewModel,
                onExit = { showImagesDescription = false }
            )
        }
        val inlineContent = mutableMapOf<String, InlineTextContent>()

        tale.images.forEachIndexed { idx, image ->
            val key = "${Tale.ANNOTATION_KEY}$idx"
            inlineContent[key] = InlineTextContent(
                Placeholder(
                    width = 80.sp,
                    height = 80.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                ),
            ) {
                TaleImage(
                    viewModel = viewModel,
                    image = image
                )
            }
        }
        ScreenWrapper(
            onExit = { finish() },
            title = tale.name
        ) { pdVal ->
            AsyncDataWrapper(viewModel = viewModel) {
                Column(
                    modifier = Modifier
                        .padding(18.dp, pdVal.calculateTopPadding(), 18.dp, 18.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = annotatedString,
                        inlineContent = inlineContent,
                        fontSize = 18.sp,
                        lineHeight = 75.sp
                    )
                }
            }
        }
    }


    @Composable
    private fun ImagesDescription(
        tale: Tale,
        viewModel: TalesViewModel,
        onExit: () -> Unit
    ) {
        CustomDialog(
            heading = stringResource(id = R.string.tales_images_description_heading),
            onExit = { onExit() }
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.tales_images_description_label)
            )

            LazyVerticalGrid(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(18.dp, 18.dp, 18.dp, 0.dp),
                columns = GridCells.Adaptive(minSize = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                val filteredContent = tale.images.distinctBy { item -> item.imageName }

                items(filteredContent) { content ->
                    val soundId = viewModel.getSoundId(content.nounFormSoundName)
                    val drawableId = viewModel.getDrawableId(content.imageName)
                    Card(
                        modifier = Modifier.aspectRatio(1f),
                        onClick = { viewModel.playSound(soundId) }
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(9.dp),
                            painter = painterResource(id = drawableId),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TaleImage(viewModel: TalesViewModel, image: TaleImage) {
        val drawableId = viewModel.getDrawableId(image.imageName)
        val soundId = viewModel.getSoundId(image.soundName)
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 9.dp)
                .clickable { viewModel.playSound(soundId) },
            painter = painterResource(id = drawableId),
            contentDescription = ""
        )
    }
}