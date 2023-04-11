package com.benxinm.localoss.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.R
import com.benxinm.localoss.ui.theme.BackgroundColor
import com.benxinm.localoss.ui.theme.LightMainColor
import com.benxinm.localoss.ui.util.noRippleClickable

@Composable
fun TopBar(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                .padding(10.dp)
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
fun TopBarWithBack(text: String, navController: NavController, iconInt: Int, iconSize: Dp =70.dp,changeName:Boolean=false ,onClick:(String)->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var changingName by remember {
            mutableStateOf(text)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "返回",
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable { navController.popBackStack() }
            )
            if (!changeName){
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .padding(start = 20.dp)
                        .padding(vertical = 10.dp)
                )
            }else{
                MyInputBox(value = changingName, onValueChange = {
                                                                 changingName=it
                }, modifier = Modifier.padding(10.dp)
                    .padding(start = 20.dp)
                    .padding(vertical = 10.dp) ,tint ="请输入Bucket名", backgroundColor = BackgroundColor, roundCornerDp = 15.dp ,textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold) )
            }
        }
        Icon(
            painter = painterResource(id = iconInt),
            contentDescription = "更多",
            modifier = Modifier
                .size(iconSize)
                .noRippleClickable {
                    onClick(changingName)
                }
        )
    }
}
@Composable
fun TopBarWithBackNoIcon(text: String,navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "返回",
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable { navController.popBackStack() }
            )
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .padding(start = 20.dp)
                    .padding(vertical = 10.dp)
            )
        }
    }
}