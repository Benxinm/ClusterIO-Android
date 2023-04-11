package com.benxinm.localoss.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Test() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        var text by remember {
            mutableStateOf("")
        }
        TextField(value =text , onValueChange = {
            text=it
        }, modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
fun TestMY() {
    Test()
}