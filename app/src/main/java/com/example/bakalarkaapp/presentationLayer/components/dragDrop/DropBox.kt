package com.example.bakalarkaapp.presentationLayer.components.dragDrop

import android.content.ClipDescription
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarkaapp.presentationLayer.screens.eyesight.eyesightSynthesisScreen.EyesightDragDropViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DropBox(
    index: Int,
    viewModel: EyesightDragDropViewModel,
    toggleDragSourceAbility: (index: Int, enabled: Boolean) -> Unit
) {
    val resetDropFlag = viewModel.resetDropFlag.collectAsState()
    var dragSourceIndex by remember { mutableIntStateOf(-1) }
    var label by remember { mutableStateOf(" ") }
    val hoverColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.surfaceVariant
    var dropBoxColor by remember { mutableStateOf(defaultColor) }

    LaunchedEffect(resetDropFlag.value) {
        if (label != " "){
            label = " "
            dragSourceIndex = -1
            viewModel.setResetDropFlag(false)
        }
    }

    val dropTarget = remember(resetDropFlag.value) {
        object : DragAndDropTarget {
            override fun onMoved(event: DragAndDropEvent) {
                super.onMoved(event)
                if (label == " ") dropBoxColor = hoverColor
            }
            override fun onExited(event: DragAndDropEvent) {
                super.onExited(event)
                dropBoxColor = defaultColor
            }
            override fun onDrop(event: DragAndDropEvent): Boolean {
                dropBoxColor = defaultColor
                if (label != " ") return false

                val text = event.toAndroidDragEvent().clipData
                    ?.getItemAt(0)?.text
                val dataList = text.toString().split('|')
                dragSourceIndex = dataList[0].toInt()
                toggleDragSourceAbility(dragSourceIndex, false)
                label = dataList[1]
                viewModel.addLetterAt(label[0], index)

                return true
            }
        }
    }

    Card(
        modifier = Modifier
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event
                        .mimeTypes()
                        .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = dropTarget
            ),
        onClick = {
            if (label != " "){
                label = " "
                toggleDragSourceAbility(dragSourceIndex, true)
                viewModel.removeLetterAt(index)
                dragSourceIndex = -1
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = dropBoxColor
        )
    ) {
        Text(
            modifier = Modifier.padding(15.dp),
            text = label,
            fontSize = 42.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}