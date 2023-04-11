package com.benxinm.localoss.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.MyInputBox
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.theme.BackgroundColor
import com.benxinm.localoss.ui.theme.BottomColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.viewModel.UserViewModel

@Composable
fun LoginPage(navController: NavController, userViewModel: UserViewModel) {
    var ip by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var code by remember {
        mutableStateOf("")
    }
    var isRegister by remember {
        mutableStateOf(false)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = !isRegister) {
                MyInputBox(
                    value = ip,
                    onValueChange = {
                        ip = it
                    },
                    tint = "请输入公网/局域网ip",
                    backgroundColor = BottomColor,
                    roundCornerDp = 20.dp,
                    width = 0.85f
                )
            }
            if (!isRegister) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            MyInputBox(value = email, onValueChange = {
                email = it
            }, tint = "请输入邮箱", backgroundColor = BottomColor, roundCornerDp = 20.dp, width = 0.85f)
            Spacer(modifier = Modifier.height(8.dp))
            MyInputBox(value = password, onValueChange = {
                password = it
            }, tint = "请输入密码", backgroundColor = BottomColor, roundCornerDp = 20.dp, width = 0.85f)
            if (isRegister) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            AnimatedVisibility(visible = isRegister) {
                MyInputBox(
                    value = code,
                    onValueChange = {
                        code = it
                    },
                    tint = "请输入验证码",
                    backgroundColor = BottomColor,
                    roundCornerDp = 20.dp,
                    width = 0.85f
                ) {
                    Text(text = "获取验证码", color = MainColor, modifier = Modifier
                        .padding(end = 18.dp)
                        .noRippleClickable {
                            Log.d("sendCode", email)
                            Repository
                                .sendCode(email)
                                .observe(lifecycleOwner) { result ->
                                    val isSuccess = result.isSuccess
                                    if (isSuccess) {
                                        val toast =
                                            Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT)
                                        toast.show()
                                    } else {
                                        val toast =
                                            Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                }
                        })
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (isRegister) {
                        if (code.isNotEmpty()) {
                            Repository.register(email, password, code)
                                .observe(lifecycleOwner) { result ->
                                    if (result.isSuccess) {
                                        val toast =
                                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT)
                                        toast.show()
                                    } else {
                                        val toast =
                                            Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                }
                        } else {
                            val toast =
                                Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    } else {
                        Repository.login(email, password).observe(lifecycleOwner) {
                            val result = it.getOrNull()
                            if (it.isSuccess){
                                userViewModel.token= result?.get("tokenValue") ?: ""
                                userViewModel.email= result?.get("userMail") ?:""
                                userViewModel.username=result?.get("userName") ?:""
                                Log.d("userToken",userViewModel.token)
                                val toast =
                                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT)
                                toast.show()
                                navController.navigate(Page.Buckets.name)
                            }else{
                                it.onFailure {err->
                                    val toast =
                                        Toast.makeText(context, err.message, Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                colors = ButtonDefaults.buttonColors(contentColor = MainColor, backgroundColor = MainColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isRegister) "注册" else "登录",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = if (isRegister) "返回登录" else "新用户注册",
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.noRippleClickable {
                    isRegister = !isRegister
                })
        }
    }
}
