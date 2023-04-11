package com.benxinm.localoss.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.benxinm.localoss.ui.model.PermissionType
import com.benxinm.localoss.ui.util.noRippleClickable

@Composable
fun BucketView(num:Int,name:String,size:Long,onClick:(String)->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .noRippleClickable { onClick(name) }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_folder), contentDescription ="folder", modifier = Modifier
                .size(40.dp)
                .padding(8.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Row {
                    Text(text = "$num"+"个对象·${calculate(size)}", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
        Icon(painter = painterResource(id = R.drawable.ic_three_dot), contentDescription ="more", modifier = Modifier.size(15.dp) )
    }
}
@Composable
fun PermissionBucketView(name:String,permissionType: PermissionType,onClick:(String)->Unit,onSelect:()->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .noRippleClickable { onClick(name) }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.ic_folder), contentDescription ="folder", modifier = Modifier
                .size(40.dp)
                .padding(8.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = permissionType.text)
            }
        }
        Icon(painter = painterResource(id = R.drawable.ic_three_dot), contentDescription ="more", modifier = Modifier
            .size(15.dp)
            .noRippleClickable {
                onSelect()
            })
    }
}
fun calculate(size: Long):String{
    var time =0
    var tmp = size
    while (tmp>1024){
        tmp /= 1024
        time++
    }
    return "$tmp"+when(time){
        1->"KB"
        2->"MB"
        3->"GB"
        4->"TB"
        else -> "Bite"
    }
}