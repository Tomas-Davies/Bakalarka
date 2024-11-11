package com.example.bakalarkaapp.presentationLayer.components.dragDrop

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DragBox(
    letter: Char,
    enabled: Boolean,
    index: Int
) {
    Card(
        modifier = Modifier
            .then(
                if (enabled) {
                    Modifier.dragAndDropSource(
//                    drawDragDecoration = {
//
//                    }
                    ) {
                        detectTapGestures(
                            onPress = {
                                tryAwaitRelease()
                                startTransfer(
                                    transferData = DragAndDropTransferData(
                                        ClipData.newPlainText(
                                            "text",
                                            "$index|$letter"
                                        )
                                    )
                                )
                            }
                        )
                    }
                } else Modifier.alpha(0.5f)
            )
            .border(
                BorderStroke(2.dp, if (enabled) Color.Green else Color.Gray),
                CardDefaults.shape
            )
    ) {
        Text(
            modifier = Modifier
                .padding(15.dp),
            text = letter.toString(),
            fontSize = 28.sp
        )
    }
}