package com.tomdev.logopadix.presentationLayer.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = CircleIconButton(
    modifier = modifier,
    onClick = { onClick() },
    color = MaterialTheme.colorScheme.errorContainer,
    icon = Icons.Filled.Clear
)


@Composable
fun EditButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = CircleIconButton(
    modifier = modifier,
    onClick = { onClick() },
    color = Color.Gray,
    icon = Icons.Filled.Edit
)


@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = CircleIconButton(
    modifier = modifier,
    onClick = { onClick() },
    color = Color.Green,
    icon = Icons.Filled.Check
)


@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector
) {
    Surface(
        modifier = modifier.size(28.dp),
        onClick = { onClick() },
        shape = CircleShape,
        color = color
    ) {
        Icon(
            modifier = Modifier
                .padding(5.dp),
            imageVector = icon,
            contentDescription = "delete button"
        )
    }
}