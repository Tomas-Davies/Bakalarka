package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryMenu(
    pdVal: PaddingValues,
    buttons: List<@Composable () -> Unit>
){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(18.dp, pdVal.calculateTopPadding(), 18.dp, pdVal.calculateBottomPadding()+18.dp),
        columns = GridCells.Adaptive(300.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(buttons){ button ->
            button()
        }
    }
}