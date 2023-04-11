package com.benxinm.localoss.ui.pages

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.benxinm.localoss.ui.components.TopBar
import com.benxinm.localoss.R
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.CircleButton
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.noRippleClickable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.yalantis.ucrop.UCrop
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MePage(navController: NavController, token: String,onImageCrop:(Uri)->Unit) {
    val imageList = listOf(
        R.drawable.image_editor,
        R.drawable.video_editor,
        R.drawable.power,
        R.drawable.subtract
    ) //TODO 后续根据需要改成另外的数据类型
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val imageUri = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()){ uri->
        onImageCrop(uri!!)
    }
    Scaffold(topBar = {
        TopBar(text = "我的")
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "用户: 123")
                Text(text = "公网/局域网IP: xxxxxxx")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "常用工具", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_editor),
                        contentDescription = "", modifier = Modifier.noRippleClickable {
                            imageUri.launch(
                                PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            ))
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.video_editor),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.power),
                        modifier = Modifier.noRippleClickable {
                            navController.navigate(Page.PermissionManagement.name)
                        },
                        contentDescription = ""
                    )
                    Image(
                        painter = painterResource(id = R.drawable.subtract),
                        contentDescription = "", modifier = Modifier.noRippleClickable {
                            navController.navigate(Page.BackupPage.name)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "设置", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "默认下载地址")
                Text(text = context.filesDir.absolutePath, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                val storagePermissionState = rememberPermissionState(permission = Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "访问存储空间权限")
                    CircleButton (originState = storagePermissionState.status.isGranted){
                        storagePermissionState.launchPermissionRequest()
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 50.dp, color = LineColor)
                Spacer(modifier = Modifier.height(10.dp))
                val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "访问相机权限")
                    CircleButton(originState = cameraPermissionState.status.isGranted) {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(startIndent = 50.dp, color = LineColor)
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = {
                        Repository.logout(token).observe(lifecycleOwner) {
                            if (it.isSuccess) {
                                navController.navigate(Page.Login.name)
                            }else{
                                it.onFailure { err->
                                    val toast = Toast.makeText(context,err.message,Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(contentColor = MainColor, backgroundColor = MainColor),
                    shape = RoundedCornerShape(30f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "退出登录",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

