package com.benxinm.localoss.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benxinm.localoss.R
import com.benxinm.localoss.ui.model.FileType
import com.benxinm.localoss.ui.pages.typeImage
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.util.noRippleClickable

@Composable
fun FileView(date:String,name:String,size:Long,type:FileType,onClick:()->Unit) {
    Column(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp).noRippleClickable { onClick() }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                , horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = typeImage(type)), contentDescription ="file", modifier = Modifier
                    .size(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Row {
                        Text(text = date+"·${calculate(size)}", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
            Icon(painter = painterResource(id = R.drawable.ic_three_dot), contentDescription ="more", modifier = Modifier.size(15.dp) )
        }
        Divider(startIndent = 48.dp, color = LineColor)
    }
}

@Composable
fun FileView(name:String,onClick:()->Unit,size: Long,extension:String) {
    Column(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .noRippleClickable { onClick() }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = typeImage(extension)), contentDescription ="file", modifier = Modifier
                    .size(40.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Row {
                        Text(text = "${calculate(size)}", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
        Divider(startIndent = 48.dp, color = LineColor)
    }
}