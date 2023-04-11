package com.benxinm.localoss.ui.components

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.benxinm.localoss.R
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.theme.CircleButtonColor
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.ui.util.offsetPercent
import com.benxinm.localoss.viewModel.BucketViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AnimatedCircleButton(expanded:Boolean,bucketViewModel: BucketViewModel,navController: NavController,onClick:()->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
        AnimatedVisibility(visible = expanded , enter = scaleIn(), exit = scaleOut(), modifier = Modifier.offsetPercent(offsetPercentX = -0.3f)) {
            Card(
                shape = CircleShape,
                backgroundColor = CircleButtonColor,
                modifier = Modifier
                    .size(85.dp)
                    .noRippleClickable {
                        onClick()
                        navController.navigate(Page.CreateBucket.name)
                    }
            ) {
                Column (modifier = Modifier.fillMaxSize(0.9f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Icon(painter = painterResource(id = R.drawable.ic_ok), contentDescription = "创建Bucket", modifier = Modifier.size(30.dp), tint = Color.White)
                    Text(text =  "创建", color = Color.White)
                    Text(text =  "Bucket", color = Color.White)
                }
            }
        }
        AnimatedVisibility(visible = expanded, enter = scaleIn(), exit = scaleOut(), modifier = Modifier.offsetPercent(offsetPercentY = -0.8f)) {
            Card(
                shape = CircleShape,
                backgroundColor = CircleButtonColor,
                modifier = Modifier
                    .size(85.dp)
                    .noRippleClickable {
                        onClick()
                        bucketViewModel.uploadOrCamera=true
                        navController.navigate(Page.SelectBucket.name)
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize(0.9f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    Icon(painter = painterResource(id = R.drawable.ic_upload), contentDescription = "上传文件" ,modifier = Modifier.size(35.dp), tint = Color.White)
                    Text(text = "上传文件", color = Color.White)
                }
            }
        }
        AnimatedVisibility(visible = expanded, enter = scaleIn(), exit = scaleOut(), modifier = Modifier.offsetPercent(offsetPercentX = 0.3f)) {
            val permissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
            Card(
                shape = CircleShape,
                backgroundColor = CircleButtonColor,
                modifier = Modifier
                    .size(85.dp)
                    .noRippleClickable {
                        onClick()
                        permissionState.launchPermissionRequest()
                        bucketViewModel.uploadOrCamera=false
                        navController.navigate(Page.SelectBucket.name)
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize(0.9f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(painter = painterResource(id = R.drawable.camera), contentDescription = "拍照上传", modifier = Modifier.size(35.dp), tint = Color.White)
                    Text(text =  "拍照上传", color = Color.White)
                }
            }
        }
    }
}