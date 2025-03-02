package com.example.bakalarkaapp.presentationLayer.screens.tales.taleDetail


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.Tale
import com.example.bakalarkaapp.dataLayer.models.TaleContent
import com.example.bakalarkaapp.presentationLayer.components.CustomDialogMenu
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.theme.AppTheme
import com.example.bakalarkaapp.presentationLayer.screens.tales.TalesViewModel
import com.example.bakalarkaapp.presentationLayer.screens.tales.TalesViewModelFactory


class TaleDetailScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel by viewModels<TalesViewModel> {
            TalesViewModelFactory(app)
        }
        val taleIdx = intent.getIntExtra("TALE_INDEX", 0)
        val tale = viewModel.getTaleByIdx(taleIdx)
        setContent {
            AppTheme(ThemeType.THEME_TALES.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaleDetailScreenContent(viewModel, tale)
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun TaleDetailScreenContent(viewModel: TalesViewModel, tale: Tale) {

        var showImagesDescription by remember { mutableStateOf(true) }

        if (showImagesDescription) {
            ImagesDescription(
                tale = tale,
                viewModel = viewModel,
                onExit = { showImagesDescription = false }
            )
        }

        ScreenWrapper(
            title = tale.name
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tale.content.forEach { content ->
                        when (content) {
                            is TaleContent.Image -> {
                                TaleImage(
                                    viewModel = viewModel,
                                    content = content
                                )
                            }

                            is TaleContent.Word -> {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = content.value,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
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
        CustomDialogMenu(
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
                val filteredContent = tale.content
                    .filterIsInstance<TaleContent.Image>()
                    .distinctBy { item -> item.imageName }

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
    private fun TaleImage(viewModel: TalesViewModel, content: TaleContent.Image) {
        val drawableId = viewModel.getDrawableId(content.imageName)
        val soundId = viewModel.getSoundId(content.soundName)
        Image(
            modifier = Modifier
                .size(56.dp)
                .clickable { viewModel.playSound(soundId) },
            painter = painterResource(id = drawableId),
            contentDescription = ""
        )
    }
}