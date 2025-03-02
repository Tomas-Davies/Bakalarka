package com.example.bakalarkaapp.presentationLayer.components

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * A wrapper composable that provides Top App Bar functionality.
 *
 * @param title Title of [TopAppBar].
 * @param content The content of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWrapper(
    title: String,
    content: @Composable (pdVal: PaddingValues) -> Unit
){
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = { (ctx as Activity).finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
            )
        }
    ) {
        content(it)
    }
}