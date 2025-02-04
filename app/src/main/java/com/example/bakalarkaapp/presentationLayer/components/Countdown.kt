import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Countdown(
    from: Int = 3,
    onCountdownFinish: () -> Unit
) {
    var count by remember { mutableIntStateOf(from) }
    var isRunning by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = count, key2 = isRunning) {
        if (isRunning && count > 0) {
            delay(1000)
            count--
            if (count == 0) {
                isRunning = false
                onCountdownFinish()
            }
        }
    }
    if (isRunning) {
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                slideInVertically(
                    animationSpec = tween(500),
                    initialOffsetY = { height -> -height }
                ) + fadeIn() togetherWith
                        slideOutVertically(
                            animationSpec = tween(500),
                            targetOffsetY = { height -> height }) + fadeOut()
            },
            label = "Animated counter"
        ) { targetCount ->
            if (isRunning) {
                Text(
                    text = "$targetCount",
                    fontSize = 128.sp
                )
            }
        }
    }
}