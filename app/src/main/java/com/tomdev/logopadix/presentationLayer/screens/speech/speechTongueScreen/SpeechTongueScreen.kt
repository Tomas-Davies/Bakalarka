package com.tomdev.logopadix.presentationLayer.screens.speech.speechTongueScreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tomdev.logopadix.R
import com.tomdev.logopadix.presentationLayer.components.ScreenWrapper
import com.tomdev.logopadix.theme.AppTheme
import com.tomdev.logopadix.theme.ThemeType

class SpeechTongueScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme(ThemeType.THEME_SPEECH.id) {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenWrapper(
                        title = stringResource(R.string.speech_menu_label_3),
                        onExit = { finish() }
                    ) {

                    }
                }
            }
        }
    }
}