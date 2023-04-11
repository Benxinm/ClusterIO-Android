package com.benxinm.localoss.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    tint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    roundCornerDp:Dp=30.dp,
    backgroundColor: Color=Color.Transparent,
    showPassword:Boolean=true,
    singleLine:Boolean=true,
    textStyle:TextStyle=TextStyle.Default,
    width: Float = 0.8f,
    height: Dp = 55.dp,
    content:@Composable ()->Unit={}
) {
    BasicTextField(
        value = value, textStyle = textStyle, singleLine = singleLine,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(Color.Black),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        /*keyboardActions = KeyboardActions(onGo = {fo})*/ visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.background(Brush.linearGradient(listOf(backgroundColor,backgroundColor)), RoundedCornerShape(roundCornerDp), alpha = 1f),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 7.dp,
                            bottom = 7.dp,
                            end = 7.dp
                        ), contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty() && tint.isNotEmpty()) {
                        Text(text = tint, color = Color.Gray, fontSize = 18.sp)
                    }
                    innerTextField()
                }
                content()
            }
        },
        modifier = modifier
            .height(height)
            .fillMaxWidth(width)
    )
}