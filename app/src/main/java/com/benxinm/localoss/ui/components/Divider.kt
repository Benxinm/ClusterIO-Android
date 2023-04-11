package com.benxinm.localoss.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineDivider(margin:Dp=0.dp) {
    Spacer(modifier = Modifier.height(margin))
    Canvas(modifier = Modifier.fillMaxWidth()){
        val canvasWidth=size.width
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = canvasWidth, y = 0f),
            color = Color.LightGray
        )
    }
}

@Composable
fun VerticalLineDiver(margin:Dp=0.dp,ratio:Float=1f) {
    Spacer(modifier = Modifier.width(margin))
    Canvas(modifier = Modifier.fillMaxHeight(ratio)){
        val canvasHeight = size.height
        drawLine(
            start = Offset(0f,0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.LightGray, strokeWidth = 3f
        )
    }
}