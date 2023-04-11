package com.benxinm.localoss.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

inline fun Modifier.noRippleClickable(noinline onClick:()->Unit):Modifier=composed {
    clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }, onClick = onClick)
}
fun Modifier.offsetPercent(offsetPercentX:Float=0f,offsetPercentY: Float=0f)=this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val offsetX=(offsetPercentX*placeable.width).roundToInt()
    val offsetY=(offsetPercentY*placeable.height).roundToInt()
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(offsetX, offsetY)
    }
}