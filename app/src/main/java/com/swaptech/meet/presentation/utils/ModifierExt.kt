package com.swaptech.meet.presentation.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.animateScaleByState(
    state: Boolean,
    from: Float = 0f,
    to: Float = 1f
): Modifier = composed {
    val fabScale by animateFloatAsState(
        targetValue = if (state) {
            to
        } else {
            from
        }
    )
    this.then(
        Modifier.graphicsLayer {
            this.scaleX = fabScale
            this.scaleY = fabScale
        }
    )
}
