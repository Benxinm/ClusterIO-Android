package com.benxinm.localoss.ui.pages

import android.widget.Toast
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
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.*
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.model.PermissionType
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.Utils
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.viewModel.BucketViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PermissionManagement(navController: NavController,bucketViewModel: BucketViewModel,token:String) {
    val permissionList = PermissionType.values()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    var currentIndex by remember {
        mutableStateOf(0)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val lifecycleOwner= LocalLifecycleOwner.current
    val context = LocalContext.current
    BottomSheetScaffold(topBar = {
        TopBarWithBackNoIcon(text = "权限管理",navController)
    }, scaffoldState = bottomSheetScaffoldState ,sheetPeekHeight = 0.dp, sheetContent = {
        BottomSheetContent(iconInt = R.drawable.ic_permission, textContent = {
            Text(
                text = "读写权限",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                permissionList.forEachIndexed { index, permissionType ->
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
                            Repository.updateAuthority("${Utils.HTTP+ Utils.ip}/bucket/update/authority",token,bucketViewModel.currentBucket!!.id,permissionList[currentIndex].authority).observe(lifecycleOwner){
                                if (it.isSuccess){
                                    val toast= Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT)
                                    toast.show()
                                }else{
                                    val toast= Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            expanded=false
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
    }) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)){
                items(bucketViewModel.bucketList) {bucket->
                    PermissionBucketView(name = bucket.name, permissionType = bucket.permissionType, onClick = {
                        bucketViewModel.currentBucket=bucket
                        navController.navigate(Page.UserPermissionManagement.name)
                    }) {
                        bucketViewModel.currentBucket=bucket
                        scope.launch {
                            currentIndex=permissionList.indexOf(bucket.permissionType)
                            bottomSheetScaffoldState.bottomSheetState.expand()
                            expanded=true
                        }
                    }
                }
            }
        }
    }
}