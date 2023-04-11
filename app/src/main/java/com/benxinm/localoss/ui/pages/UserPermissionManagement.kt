package com.benxinm.localoss.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.R
import com.benxinm.localoss.model.User
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.BottomSheetContent
import com.benxinm.localoss.ui.components.CircleButton
import com.benxinm.localoss.ui.components.TopBarWithBack
import com.benxinm.localoss.ui.components.UserView
import com.benxinm.localoss.ui.model.Bucket
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.model.UserPermissionType
import com.benxinm.localoss.ui.theme.LightMainColor
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.viewModel.BucketViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserPermissionManagement(navController: NavController,token:String,bucketViewModel: BucketViewModel) {
    var changePermission by remember {
        mutableStateOf(false)
    }
    var currentIndex by remember {
        mutableStateOf(0)
    }
    var userName by remember {
        mutableStateOf("")
    }
    var currentEmail by remember {
        mutableStateOf("")
    }
    val userPermissionType = UserPermissionType.values()
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var flag by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = flag){
        if (bucketViewModel.currentBucket!=null){
            Repository.getUsers(token,bucketViewModel.currentBucket!!.id).observe(lifecycleOwner){
                if (it.isSuccess){
                    val result = it.getOrNull()
                    bucketViewModel.userList.clear()
                    bucketViewModel.userList.addAll(result!!)
                }
            }
        }
    }
    BottomSheetScaffold(topBar = {
        TopBarWithBack(text ="权限管理", navController =navController, iconInt = R.drawable.ic_add) {
            navController.navigate(Page.AddUser.name)
        }
    }, scaffoldState = bottomSheetScaffoldState, sheetPeekHeight = 0.dp ,sheetContent = {
        BottomSheetContent(iconInt = R.drawable.ic_user, textContent = {
            Text(text = userName, fontWeight = FontWeight.SemiBold)
        }) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if (!changePermission){

                    Button(
                        onClick = {
                            changePermission=true
                            //TODO 调接口+跳提示窗确认

                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 10.dp), colors = ButtonDefaults.buttonColors(
                            MainColor
                        ), shape = RoundedCornerShape(30f)
                    ) {
                        Text(
                            text = "修改用户权限",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = {
                            //TODO 调接口+跳提示窗确认

                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 10.dp), colors = ButtonDefaults.buttonColors(
                            MainColor
                        ), shape = RoundedCornerShape(30f)
                    ) {
                        Text(
                            text = "删除用户",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }else{

                    userPermissionType.forEachIndexed { index, permissionType ->
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .background(Color.White)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .noRippleClickable {
                                        currentIndex = index
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = permissionType.text, fontSize = 15.sp)
                                CircleButton(currentIndex == index)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(startIndent = 18.dp, color = LineColor)
                        }
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                //TODO 加接口
                                Repository.updateUserPermission(token,bucketViewModel.currentBucket!!.id,currentEmail,userPermissionType[currentIndex].type).observe(lifecycleOwner){
                                    if (it.isSuccess){
                                        flag=!flag
                                        val toast = Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT)
                                        toast.show()
                                    }else{
                                        val toast = Toast.makeText(context,"修改失败,请重试",Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                }
                                changePermission=false
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }, modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally)
                            .padding(10.dp), colors = ButtonDefaults.buttonColors(
                            MainColor
                        ), shape = RoundedCornerShape(30f)
                    ) {
                        Text(
                            text = "确认",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(LightMainColor)) {
                Text(text = "${bucketViewModel.currentBucket?.name} (${bucketViewModel.currentBucket?.permissionType?.text})", modifier = Modifier.padding(start = 15.dp))
            }
            LazyColumn{
                items(bucketViewModel.userList){
                    UserView(name = it.name, permissionType = it.type){
                        scope.launch {
                            userName=it.name
                            currentEmail=it.email
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                }
            }
        }
    }
}