package com.benxinm.localoss.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.ui.components.CircleButton
import com.benxinm.localoss.ui.components.MyInputBox
import com.benxinm.localoss.ui.components.TopBarWithBackNoIcon
import com.benxinm.localoss.ui.theme.BottomColor
import com.benxinm.localoss.ui.theme.LineColor
import com.google.accompanist.permissions.isGranted

@Composable
fun BackupPage(navController: NavController,token:String) {
    Scaffold(topBar = {
        TopBarWithBackNoIcon(text = "备份管理", navController = navController)
    }, modifier = Modifier.fillMaxSize()) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                var backupIP by remember {
                    mutableStateOf("")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "备份机IP", fontSize = 22.sp)
                    MyInputBox(value =backupIP , onValueChange ={
                                                                backupIP=it
                    }, tint = "", width = 0.45f,backgroundColor = BottomColor, roundCornerDp = 20.dp, height = 40.dp, textStyle = TextStyle(fontSize = 18.sp))
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 0.dp, color = LineColor)
                Spacer(modifier = Modifier.height(5.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "客户端开关", fontSize = 22.sp)
                    CircleButton(originState = false) {

                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 0.dp, color = LineColor)
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "开启服务端", fontSize = 22.sp)
                    CircleButton(originState = false) {

                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 0.dp, color = LineColor)
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "推送桶数据", fontSize = 22.sp)
                    CircleButton(originState = false) {

                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 0.dp, color = LineColor)
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "拉取服务端数据", fontSize = 22.sp)
                    CircleButton(originState = false) {

                    }
                }
            }

        }
    }
}