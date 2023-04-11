package com.benxinm.localoss.ui.components

import android.graphics.DrawFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benxinm.localoss.ui.theme.LightMainColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.theme.Purple200
import com.benxinm.localoss.ui.theme.Purple500
import com.benxinm.localoss.ui.util.noRippleClickable

@Composable
fun FloatingButton(expanded:Boolean,onClick:()->Unit) {
    val rotateDegree :Float by animateFloatAsState(if (expanded) 45f else 0f)
    Canvas(modifier = Modifier
        .size(65.dp)
        .rotate(rotateDegree).noRippleClickable {
            onClick()
        }) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(color =if (expanded) LightMainColor
        else MainColor)
        drawLine(
            color = if (expanded) MainColor else Color.White,
            start = Offset(canvasWidth * 0.25f, 0.5f * canvasHeight),
            end = Offset(canvasWidth * 0.75f, 0.5f * canvasHeight),
            cap = StrokeCap.Round,
            strokeWidth = canvasHeight * 0.08f
        )
        drawLine(
            color = if (expanded) MainColor else Color.White,
            start = Offset(canvasWidth * 0.5f, 0.25f * canvasHeight),
            end = Offset(canvasWidth * 0.5f, 0.75f * canvasHeight),
            cap = StrokeCap.Round,
            strokeWidth = canvasHeight * 0.08f
        )
    }
}

