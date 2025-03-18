package com.example.bakalarkaapp.presentationLayer.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bakalarkaapp.viewModels.BaseViewModel
import com.example.bakalarkaapp.viewModels.ScreenState

/**
 * A wrapper Composable that shows a [CircularProgressIndicator] or [content] based
 * on a state of asynchronous data loading.
 *
 * @param viewModel Provides a state which indicates that data are loaded.
 * @param content A Composable displayed in the wrapper body.
 */
@Composable
fun AsyncDataWrapper(
    viewModel: BaseViewModel,
    content: @Composable () -> Unit
){
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    if (screenState is ScreenState.Loading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    } else {
        content()
    }
}