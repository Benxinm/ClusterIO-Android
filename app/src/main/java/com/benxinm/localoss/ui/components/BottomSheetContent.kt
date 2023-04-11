package com.benxinm.localoss.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.benxinm.localoss.ui.theme.BackgroundColor

@Composable
fun BottomSheetContent(iconInt:Int,textContent: @Composable ()->Unit,listContent:@Composable ()->Unit) {
    Surface(color = BackgroundColor) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = iconInt), contentDescription ="", modifier = Modifier.size(30.dp))
                textContent()
            }
            listContent()
        }
    }
}
@Composable
fun BottomSheetContentImg(imageInt:Int,textContent: @Composable ()->Unit,listContent:@Composable ()->Unit) {
    Surface(color = BackgroundColor) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = imageInt), contentDescription ="", modifier = Modifier.size(30.dp))
                textContent()
            }
            listContent()
        }
    }
}