package com.benxinm.localoss.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.noRippleClickable

@Composable
fun CircleButton(originState: Boolean,onClick:(Boolean)->Unit) {
    var state by remember {
        mutableStateOf(originState)
    }
    Canvas(modifier = Modifier
        .size(23.dp)
        .noRippleClickable {
            state=!state
            onClick(state)
        }){
        if (state){
            drawCircle(color = MainColor )
            drawCircle(color = Color.White, radius = size.minDimension/4.0f)
        }else{
            drawCircle(color = Color.LightGray, style =Stroke(width = 3f, cap = StrokeCap.Round) )
        }
    }
}
@Composable
fun CircleButton(state:Boolean) {
    Canvas(modifier = Modifier
        .size(23.dp)
        ){
        if (state){
            drawCircle(color = MainColor )
            drawCircle(color = Color.White, radius = size.minDimension/4.0f)
        }else{
            drawCircle(color = Color.LightGray, style =Stroke(width = 3f, cap = StrokeCap.Round) )
        }
    }
}

