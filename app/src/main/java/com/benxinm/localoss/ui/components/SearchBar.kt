package com.benxinm.localoss.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benxinm.localoss.R
import com.benxinm.localoss.ui.theme.SearchBarBackColor

@Composable
fun SearchBar(modifier:Modifier=Modifier,text:String,onValueChange:(String)->Unit) {
    Surface(elevation = 3.dp, modifier = modifier.wrapContentSize(), color = SearchBarBackColor ,shape = RoundedCornerShape(15.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp)
            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "搜索",
                    modifier = Modifier
                        .size(20.dp)
                )
                MyInputBox(value =text , onValueChange = onValueChange, tint = "搜索", width = 1f, height = 40.dp, textStyle = TextStyle(fontSize = 18.sp))
                Text(
                    text = "搜索",
                    textAlign = TextAlign.Start,
                    color = Color.LightGray,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        }
    }
}