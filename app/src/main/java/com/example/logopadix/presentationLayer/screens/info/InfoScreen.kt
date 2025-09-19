package com.example.logopadix.presentationLayer.screens.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.logopadix.R
import com.example.logopadix.presentationLayer.components.ScreenWrapper
import com.example.logopadix.theme.AppTheme


class InfoScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppTheme {
                    ScreenWrapper(
                        title = stringResource(id = R.string.info_heading),
                        onExit = { finish() }
                    ) { pdVal ->
                        InfoScreenContent(pdVal)
                    }

                }
            }
        }
    }


    @Composable
    private fun InfoScreenContent(pdVal: PaddingValues){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, pdVal.calculateTopPadding(), 18.dp, 18.dp)
        ) {
            Text(
                text = stringResource(id = R.string.info_content_heading),
                style = MaterialTheme.typography.titleLarge
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))
            Text(text = stringResource(id = R.string.info_content_1_heading) + " - " + stringResource(R.string.info_content_1_text))
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = stringResource(id = R.string.info_content_2_heading) + " - " + stringResource(R.string.info_content_2_text),
                modifier = Modifier.clickable {
                    openHyperlink("https://www.uppbeat.io")
                },
                textDecoration = TextDecoration.Underline
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = stringResource(id = R.string.info_content_5_heading) + " - " + stringResource(R.string.info_content_5_text),
                modifier = Modifier.clickable {
                    openHyperlink("https://www.flaticon.com/free-animated-icons/musical-note")
                },
                textDecoration = TextDecoration.Underline
            )
        }
    }

    private fun openHyperlink(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}