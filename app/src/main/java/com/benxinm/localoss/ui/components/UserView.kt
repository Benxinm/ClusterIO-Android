package com.benxinm.localoss.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.benxinm.localoss.ui.model.UserPermissionType

@Composable
fun UserView(name:String,permissionType: UserPermissionType,onClick:()->Unit={}) {
    Row(modifier = Modifier
        .clickable {
            onClick()
        }.fillMaxWidth().background(Color.White), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = R.drawable.ic_user), contentDescription =null, modifier = Modifier
            .size(50.dp)
            .padding(15.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(text = permissionType.text, fontSize = 12.sp)
        }
    }
}