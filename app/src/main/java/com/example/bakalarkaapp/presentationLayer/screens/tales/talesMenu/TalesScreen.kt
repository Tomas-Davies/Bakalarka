package com.example.bakalarkaapp.presentationLayer.screens.tales.talesMenu

import android.content.Context
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bakalarkaapp.LogoApp
import com.example.bakalarkaapp.R
import com.example.bakalarkaapp.ThemeType
import com.example.bakalarkaapp.dataLayer.models.Tale
import com.example.bakalarkaapp.presentationLayer.components.ScreenWrapper
import com.example.bakalarkaapp.presentationLayer.screens.tales.TalesViewModel
import com.example.bakalarkaapp.presentationLayer.screens.tales.TalesViewModelFactory
import com.example.bakalarkaapp.presentationLayer.screens.tales.taleDetail.TaleDetailScreen
import com.example.bakalarkaapp.theme.AppTheme

class TalesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as LogoApp
        val viewModel by viewModels<TalesViewModel> {
            TalesViewModelFactory(app)
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
        val ctx = LocalContext.current
        ScreenWrapper(
            onExit = { this.finish() },
            title = stringResource(id = R.string.category_tales)
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp, it.calculateTopPadding(), 18.dp, 18.dp),
                columns = GridCells.Adaptive(minSize = 150.dp)
            ) {
                itemsIndexed(viewModel.tales){ idx, tale ->
                    TaleCard(
                        tale = tale,
                        viewModel = viewModel,
                        onClick = { openTale(ctx, idx) }
                    )
                }
            }
        }
    }

    @Composable
    private fun TaleCard(tale: Tale, viewModel: TalesViewModel, onClick: () -> Unit){
        val cardColors = CardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = CardDefaults.cardColors().contentColor,
            disabledContainerColor = CardDefaults.cardColors().disabledContainerColor,
            disabledContentColor = CardDefaults.cardColors().disabledContentColor
        )
        Card(
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

    private fun openTale(ctx: Context, idx: Int){
        val intent = Intent(ctx, TaleDetailScreen::class.java)
        intent.putExtra("TALE_INDEX", idx)
        ctx.startActivity(intent)
    }
}