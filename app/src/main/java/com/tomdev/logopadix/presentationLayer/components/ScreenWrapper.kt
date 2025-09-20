package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


/**
 * A wrapper composable that provides Top App Bar functionality.
 *
 * @param title Title of [TopAppBar].
 * @param content A Composable displayed in the wrapper body.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWrapper(
    title: String,
    onExit: () -> Unit,
    fab: @Composable () -> Unit = {},
    content: @Composable (pdVal: PaddingValues) -> Unit
){
    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                    )
               },
                navigationIcon = {
                    IconButton(onClick = { onExit() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        },
        floatingActionButton = { fab() }
    ) { pdVal ->
        content(pdVal)
    }
}