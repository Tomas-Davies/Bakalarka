package com.tomdev.logopadix.presentationLayer.screens.speech.speechInitialMenuScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.components.CategoryButton
import com.tomdev.logopadix.presentationLayer.components.CategoryMenu
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.presentationLayer.screens.speech.speechFilterScreen.SpeechFilterScreen
import com.tomdev.logopadix.presentationLayer.screens.speech.speechScreen.SpeechScreen
import com.tomdev.logopadix.presentationLayer.screens.speech.speechTongueScreen.SpeechTongueScreen
import com.tomdev.logopadix.theme.AppTheme
import com.tomdev.logopadix.theme.ThemeType

// todo dodelat ikony u category buttonu ( CANVA )
class SpeechInitialMenuScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.category_speech),
                        onExit = { finish() }
                    ) {
                        val buttons = listOf<@Composable () -> Unit>(
                            {
                                CategoryButton(
                                    onClick = { onCardClicked(0) },
                                    label = stringResource(id = R.string.speech_menu_label_1),
                                    labelLong = stringResource(id = R.string.speech_menu_label_long_1),
                                    popUpHeading = stringResource(id = R.string.speech_menu_label_long_1),
                                    popUpContent = stringResource(id = R.string.speech_pop_up_body_1),
                                    imageId = R.drawable.speech_btn_1_logo
                                )
                            },
                            {
                                CategoryButton(
                                    onClick = { onCardClicked(1) },
                                    label = stringResource(id = R.string.speech_menu_label_2),
                                    labelLong = stringResource(id = R.string.speech_menu_label_long_2),
                                    popUpHeading = stringResource(id = R.string.speech_menu_label_long_2),
                                    popUpContent = stringResource(id = R.string.speech_pop_up_body_2),
                                    imageId = R.drawable.speech_btn_2_logo
                                )
                            },
                            {
                                CategoryButton(
                                    onClick = { onCardClicked(2) },
                                    label = stringResource(id = R.string.speech_menu_label_3),
                                    labelLong = stringResource(id = R.string.speech_menu_label_long_3),
                                    popUpHeading = stringResource(id = R.string.speech_menu_label_long_3),
                                    popUpContent = stringResource(id = R.string.speech_pop_up_body_3),
                                    imageId = R.drawable.speech_btn_3_logo
                                )
                            }
                        )

                        CategoryMenu(
                            pdVal = it,
                            buttons = buttons
                        )
                    }
                }
            }
        }
    }

    private fun onCardClicked(id: Int){
        var intent = Intent(this, SpeechScreen::class.java)

        when(id){
            1 -> {intent = Intent(this, SpeechFilterScreen::class.java)}
            2 -> {intent = Intent(this, SpeechTongueScreen::class.java)}
        }
        startActivity(intent)
    }
}