package com.tomdev.logopadix.presentationLayer.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.components.CustomCard
import com.tomdev.logopadix.presentationLayer.components.NewComersDialog
import com.tomdev.logopadix.presentationLayer.components.WhatsNewDialog
import com.tomdev.logopadix.presentationLayer.screens.achievements.AchievementScreen
import com.tomdev.logopadix.presentationLayer.screens.info.InfoScreen
import com.tomdev.logopadix.presentationLayer.screens.eyesight.eyesightScreen.EyesightScreen
import com.tomdev.logopadix.presentationLayer.screens.hearing.hearingScreen.HearingScreen
import com.tomdev.logopadix.presentationLayer.screens.rythm.rythmScreen.RythmScreen
import com.tomdev.logopadix.presentationLayer.screens.speech.speechInitialMenuScreen.SpeechInitialMenuScreen
import com.tomdev.logopadix.presentationLayer.screens.tales.talesMenu.TalesScreen
import com.tomdev.logopadix.theme.AppTheme


class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenContent()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreenContent() {
        Scaffold(
            contentWindowInsets = WindowInsets.safeContent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    actions = {
                        IconButton(onClick = { onCardClicked(5) }) {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = "info")
                        }
                    }
                )
            }
        ) { pdVal ->
            MainContent(pdVal)
        }
    }


    @Composable
    private fun MainContent(pdVal: PaddingValues) {
        BoxWithConstraints {
            if (maxWidth < 500.dp) {
                MainMenu(
                    padding = pdVal,
                    menuItemRatio = 1.3f,
                    menuItemWideRatio = 3.5f
                )
            } else {
                MainMenu(
                    padding = pdVal,
                    sidePadding = 32.dp,
                    menuItemRatio = 1.8f,
                    menuItemWideRatio = 3.3f
                )
            }
        }
    }


    @Composable
    private fun MainMenu(
        padding: PaddingValues,
        sidePadding: Dp = 18.dp,
        menuItemRatio: Float = 1f,
        menuItemWideRatio: Float = 3f,
        menuProfileRatio: Float = 4f
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        sidePadding,
                        padding.calculateTopPadding(),
                        sidePadding,
                        padding.calculateBottomPadding()
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                LazyVerticalGrid(
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    item {
                        MenuCardMedium(
                            title = stringResource(id = R.string.category_speech),
                            onClick = { onCardClicked(0) },
                            ratio = menuItemRatio,
                            imageDecorCorner = painterResource(id = R.drawable.speech_btn_decor_top_start),
                            imageDecorTopEnd = painterResource(id = R.drawable.speech_btn_decor_top_end),
                            imageDecorBottomStart = painterResource(id = R.drawable.speech_btn_decor_bottom_start),
                            textBackgroundColor = colorResource(id = R.color.speech_500),
                            color = colorResource(id = R.color.speech_200),
                            outlineColor = colorResource(id = R.color.speech_500)
                        )
                    }
                    item {
                        MenuCardMedium(
                            title = stringResource(id = R.string.category_eyesight),
                            onClick = { onCardClicked(1) },
                            ratio = menuItemRatio,
                            imageDecorCorner = painterResource(id = R.drawable.eyesight_btn_decor_top_start),
                            imageDecorTopEnd = painterResource(id = R.drawable.eyesight_btn_decor_top_end),
                            imageDecorBottomStart = painterResource(id = R.drawable.eyesight_btn_decor_bottom_start),
                            textBackgroundColor = colorResource(id = R.color.eyesight_500),
                            color = colorResource(id = R.color.eyesight_200),
                            outlineColor = colorResource(id = R.color.eyesight_500)
                        )
                    }
                    item {
                        MenuCardMedium(
                            title = stringResource(id = R.string.category_hearing),
                            onClick = { onCardClicked(2) },
                            ratio = menuItemRatio,
                            imageDecorCorner = painterResource(id = R.drawable.hearing_btn_decor_top_start),
                            imageDecorTopEnd = painterResource(id = R.drawable.hearing_btn_decor_top_end),
                            imageDecorBottomStart = painterResource(id = R.drawable.hearing_btn_decor_bottom_start),
                            textBackgroundColor = colorResource(id = R.color.hearing_500),
                            color = colorResource(id = R.color.hearing_200),
                            outlineColor = colorResource(id = R.color.hearing_500)
                        )
                    }
                    item {
                        MenuCardMedium(
                            title = stringResource(id = R.string.category_rythm),
                            onClick = { onCardClicked(3) },
                            ratio = menuItemRatio,
                            imageDecorCorner = painterResource(id = R.drawable.rythm_btn_decor_top_start),
                            imageDecorTopEnd = painterResource(id = R.drawable.rythm_btn_decor_top_end),
                            imageDecorBottomStart = painterResource(id = R.drawable.rythm_btn_decor_bottom_start),
                            textBackgroundColor = colorResource(id = R.color.rythm_500),
                            color = colorResource(id = R.color.rythm_200),
                            outlineColor = colorResource(id = R.color.rythm_500)
                        )
                    }
                    item(span = { GridItemSpan(2) }) {
                        MenuCardTales(
                            title = stringResource(id = R.string.category_tales),
                            onClick = { onCardClicked(4) },
                            ratio = menuItemWideRatio,
                            imageDecorMain = painterResource(id = R.drawable.tale_button_decor_main),
                            imageDecorLeft = painterResource(id = R.drawable.tale_button_decor_left),
                            imageDecorRight = painterResource(id = R.drawable.tale_button_decor_right),
                            textBackgroundColor = colorResource(id = R.color.tales_500),
                            color = colorResource(id = R.color.tales_200),
                            outlineColor = colorResource(id = R.color.tales_500)
                        )
                    }
                    item(span = { GridItemSpan(2) }) {
                        MenuCardProfile(
                            title = stringResource(R.string.menu_profile_label),
                            onClick = { onCardClicked(6) },
                            ratio = menuProfileRatio,
                            imageDecorMain = null,
                            color = colorResource(R.color.gold),
                            outlineColor = colorResource(R.color.gold_outline),
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .weight(4f)
                            .align(Alignment.Bottom),
                        painter = painterResource(id = R.drawable.home_decor_bubble),
                        contentDescription = "decoration",
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(Modifier.width(18.dp))
                    Image(
                        modifier = Modifier
                            .weight(3f)
                            .align(Alignment.Bottom),
                        painter = painterResource(id = R.drawable.home_screen_decor),
                        contentDescription = "decoration",
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

            val text1 = listOf(
                Triple(
                    R.string.welcome_heading_1,
                    R.string.welcome_content_1,
                    R.drawable.welcome_image_1
                ),
                Triple(
                    R.string.welcome_heading_2,
                    R.string.welcome_content_2,
                    R.drawable.welcome_image_2
                ),
                Triple(
                    R.string.welcome_heading_3,
                    R.string.welcome_content_3,
                    R.drawable.welcome_image_3
                )
            )
            NewComersDialog(
                headingsAndTexts = text1,
                btnLabelNext = stringResource(R.string.welcome_btn_label),
                onEnterClick = { }
            )

            val text2 = listOf(
                Triple(
                    R.string.update_heading_1,
                    R.string.update_content_1,
                    R.drawable.welcome_image_1
                ), // TODO - third obrazky
                Triple(
                    R.string.update_heading_2,
                    R.string.update_content_2,
                    R.drawable.welcome_image_2
                ),
                Triple(
                    R.string.update_heading_3,
                    R.string.update_content_3,
                    R.drawable.welcome_image_3
                )
            )
            WhatsNewDialog(
                headingsAndTexts = text2,
                mainHeading = stringResource(R.string.update_main_heading),
                btnLabelNext = stringResource(R.string.update_btn_label_next),
                btnLabelPrev = stringResource(R.string.update_btn_label_prev),
                onEnterClick = { }
            )

        }

    }


    @Composable
    private fun MenuCardMedium(
        title: String,
        onClick: () -> Unit,
        ratio: Float = 1f,
        imageDecorCorner: Painter,
        imageDecorTopEnd: Painter,
        imageDecorBottomStart: Painter,
        textBackgroundColor: Color,
        color: Color,
        outlineColor: Color
    ) {
        val cardColors = CardDefaults.cardColors().copy(
            containerColor = color
        )
        CustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            onClick = { onClick() },
            colors = cardColors,
            outlineColor = outlineColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.padding(bottom = 9.dp),
                    painter = imageDecorCorner,
                    contentDescription = "decor"
                )
                Image(
                    painter = imageDecorTopEnd,
                    contentDescription = "decor"
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(textBackgroundColor)
                    .wrapContentHeight(),
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = imageDecorBottomStart,
                    contentDescription = "decor"
                )
                Image(
                    modifier = Modifier
                        .rotate(180f)
                        .padding(bottom = 9.dp),
                    painter = imageDecorCorner,
                    contentDescription = "decor"
                )
            }
        }
    }


    @Composable
    private fun MenuCardProfile(
        title: String,
        onClick: () -> Unit,
        ratio: Float,
        imageDecorMain: Painter?,
        color: Color,
        outlineColor: Color
    ) {
        val cardColors = CardDefaults.cardColors().copy(containerColor = color)
        CustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            onClick = { onClick() },
            colors = cardColors,
            outlineColor = outlineColor
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(2f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .graphicsLayer(rotationZ = 33f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(25.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Yellow.copy(alpha = 0.05f),
                                            Color.Yellow.copy(alpha = 0.25f),
                                            Color.Yellow.copy(alpha = 0.5f),
                                            Color.Yellow.copy(alpha = 0.25f),
                                            Color.Yellow.copy(alpha = 0.05f)
                                        )
                                    )
                                )
                        ) {
//                                Image(
//                                    modifier = Modifier.fillMaxSize().alpha(0.4f),
//                                    painter = painterResource(R.drawable.gold_medal),
//                                    contentDescription = null
//                                )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        modifier = Modifier
                            .alpha(0.8f),
                        painter = painterResource(R.drawable.trophy_2),
                        contentDescription = "decor"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .background(
                                color = colorResource(R.color.gold_outline),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .wrapContentHeight(),
                        text = title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                    Image(
                        modifier = Modifier
                            .alpha(0.8f),
                        painter = painterResource(R.drawable.trophy_2),
                        contentDescription = "decor"
                    )
                }

            }
        }
    }

    @Composable
    private fun MenuCardTales(
        title: String,
        onClick: () -> Unit,
        ratio: Float = 1f,
        imageDecorMain: Painter,
        imageDecorLeft: Painter,
        imageDecorRight: Painter,
        textBackgroundColor: Color,
        color: Color,
        outlineColor: Color
    ) {
        val cardColors = CardDefaults.cardColors().copy(
            containerColor = color
        )
        CustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            onClick = { onClick() },
            colors = cardColors,
            outlineColor = outlineColor
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = imageDecorMain,
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "decor"
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            modifier = Modifier.alpha(0.85f),
                            painter = imageDecorLeft,
                            contentDescription = "decor"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .background(
                                    color = textBackgroundColor,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .wrapContentHeight(),
                            text = title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Image(
                            modifier = Modifier.alpha(0.85f),
                            painter = imageDecorRight,
                            contentDescription = "decor"
                        )
                    }

                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }


    private fun onCardClicked(id: Int) {
        var intent = Intent(this, SpeechInitialMenuScreen::class.java)
        when (id) {
            1 -> {
                intent = Intent(this, EyesightScreen::class.java)
            }

            2 -> {
                intent = Intent(this, HearingScreen::class.java)
            }

            3 -> {
                intent = Intent(this, RythmScreen::class.java)
            }

            4 -> {
                intent = Intent(this, TalesScreen::class.java)
            }

            5 -> {
                intent = Intent(this, InfoScreen::class.java)
            }

            6 -> {
                intent = Intent(this, AchievementScreen::class.java)
            }
        }
        startActivity(intent)
    }
}