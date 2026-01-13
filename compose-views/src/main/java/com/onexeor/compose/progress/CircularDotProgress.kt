package com.onexeor.compose.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A circular progress indicator with animated dots that pulse around a center point.
 *
 * @param modifier Modifier to be applied to the layout
 * @param dotCount Number of dots to display around the circle
 * @param dotColor Color of the dots
 * @param radius Radius of the circular path in pixels
 * @param minDotSize Minimum dot radius when at lowest intensity
 * @param maxDotSize Maximum dot radius when at highest intensity
 * @param animationDurationMs Duration of one complete animation cycle in milliseconds
 */
@Composable
fun CircularDotProgress(
    modifier: Modifier = Modifier,
    dotCount: Int = 8,
    dotColor: Color = Color(0xFF3399FF),
    radius: Float = 120f,
    minDotSize: Float = 8f,
    maxDotSize: Float = 16f,
    animationDurationMs: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circular_progress")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = dotCount.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size((radius * 2 + maxDotSize * 2).dp)) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            repeat(dotCount) { index ->
                val angle = (2 * PI * index / dotCount).toFloat()
                val distance = (animationProgress - index + dotCount) % dotCount
                val normalizedDistance = (distance / dotCount) * 2 * PI
                val wave = (cos(normalizedDistance).toFloat() + 1f) / 2f
                val scale = wave
                val dotSize = minDotSize + (maxDotSize - minDotSize) * scale
                val x = centerX + radius * cos(angle - PI.toFloat() / 2)
                val y = centerY + radius * sin(angle - PI.toFloat() / 2)

                drawCircle(
                    color = dotColor.copy(alpha = 0.3f + 0.7f * scale),
                    radius = dotSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}
