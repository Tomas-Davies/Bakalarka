package com.example.logopadix.presentationLayer.screens.tales.talesMenu

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.logopadix.LogoApp
import com.example.logopadix.R
import com.example.logopadix.theme.ThemeType
import com.example.logopadix.dataLayer.repositories.Tale
import com.example.logopadix.presentationLayer.components.AsyncDataWrapper
import com.example.logopadix.presentationLayer.components.CustomCard
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.presentationLayer.screens.tales.TalesViewModel
import com.example.logopadix.presentationLayer.screens.tales.TalesViewModelFactory
import com.example.logopadix.presentationLayer.screens.tales.taleDetail.TaleDetailScreen
import com.example.logopadix.theme.AppTheme


class TalesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val repo = app.talesRepository
        val viewModel by viewModels<TalesViewModel> {
            TalesViewModelFactory(repo, app)
        }
        setContent {
            AppTheme(ThemeType.THEME_TALES.id) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TalesScreenContent(viewModel)
                }
            }
        }
    }


    @Composable
    private fun TalesScreenContent(viewModel: TalesViewModel) {
        ScreenWrapper(
            onExit = { finish() },
            title = stringResource(id = R.string.category_tales)
        ) { pdVal ->
            AsyncDataWrapper(viewModel = viewModel) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp, pdVal.calculateTopPadding(), 18.dp, 18.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp)
                ) {
                    itemsIndexed(viewModel.tales){ idx, tale ->
                        TaleCard(
                            tale = tale,
                            viewModel = viewModel,
                            onClick = { openTale(idx) }
                        )
                    }
                }
            }
        }
    }


    @Composable
    private fun TaleCard(tale: Tale, viewModel: TalesViewModel, onClick: () -> Unit){
        val cardColors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        )
        CustomCard(
            modifier = Modifier.padding(9.dp),
            colors = cardColors,
            onClick = { onClick() }
        ) {
            val drawable = viewModel.getDrawableId(tale.taleImageName)
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = drawable),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(9.dp),
                text = tale.name,
                fontWeight = FontWeight.SemiBold,
                minLines = 2
            )
        }
    }


    private fun openTale(idx: Int){
        val intent = Intent(this, TaleDetailScreen::class.java)
        intent.putExtra("TALE_INDEX", idx)
        startActivity(intent)
    }
}