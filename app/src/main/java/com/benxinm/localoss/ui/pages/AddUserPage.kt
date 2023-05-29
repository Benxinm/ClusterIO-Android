package com.benxinm.localoss.ui.pages

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.BottomSheetContent
import com.benxinm.localoss.ui.components.CircleButton
import com.benxinm.localoss.ui.components.MyInputBox
import com.benxinm.localoss.ui.components.TopBarWithBack
import com.benxinm.localoss.ui.model.PermissionType
import com.benxinm.localoss.ui.model.UserPermissionType
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.Utils
import com.benxinm.localoss.ui.util.noRippleClickable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddUserPage(navController: NavController, token:String,bucketId:Int) {
    var userMail by remember {
        mutableStateOf("")
    }
    var currentIndex by remember {
        mutableStateOf(0)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    var expanded by remember {
        mutableStateOf(false)
    }
    val permissionList = UserPermissionType.values()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val modifier = Modifier.background(brush = Brush.linearGradient(listOf(
        Color.LightGray,
        Color.LightGray)), alpha = 0.6f)
    LaunchedEffect(key1 = bottomSheetScaffoldState.bottomSheetState.isCollapsed){
        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
            expanded=false
        }
    }
    BottomSheetScaffold(modifier = Modifier
        .fillMaxSize(), topBar = {
        Box(
            modifier = if (expanded) modifier else Modifier
        ) {
            TopBarWithBack(
                text = "添加用户",
                navController = navController,
                iconInt = com.benxinm.localoss.R.drawable.ic_ok
            ) {
                if (userMail.isEmpty()) {
                    val toast =
                        Toast.makeText(context, "未填写邮箱", Toast.LENGTH_SHORT)
                    toast.show()
                }else{
                    Repository.addBucketUser("${Utils.HTTP+ Utils.ip}/bucket/user/add",token, id = bucketId, type = permissionList[currentIndex].type, email = userMail).observe(lifecycleOwner){ result->
                        if (result.isSuccess){
                            val toast =
                                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT)
                            toast.show()
                            navController.popBackStack()
                        }else{
                            result.onFailure {
                                val toast =
                                    Toast.makeText(context,it.message , Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    }
                }
                //TODO 创建成功，创建失败
            }
            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color.LightGray,
                                    Color.LightGray
                                )
                            ),
                            alpha = 0.6f
                        )
                ) {
                }
            }
        }
    }, sheetContent = {
        BottomSheetContent(iconInt = com.benxinm.localoss.R.drawable.ic_permission, textContent = {
            Text(
                text = "用户权限",
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
    }, sheetPeekHeight = 0.dp, scaffoldState = bottomSheetScaffoldState
    ) { paddingValues ->
        Box(
            modifier = if (!expanded) Modifier
                .padding(paddingValues)
                .padding(horizontal = 15.dp) else modifier
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = com.benxinm.localoss.R.drawable.ic_folder),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                    MyInputBox(value = userMail, onValueChange = {
                        userMail = it
                    }, textStyle = TextStyle(fontSize = 18.sp), tint = "用户邮箱")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = com.benxinm.localoss.R.drawable.ic_permission),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(11.dp))
                        Text(text = "用户权限", fontSize = 18.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = permissionList[currentIndex].text, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        val rotateDegree: Float by animateFloatAsState(if (bottomSheetScaffoldState.bottomSheetState.isExpanded) 90f else 0f)
                        Icon(painter = painterResource(id = com.benxinm.localoss.R.drawable.ic_forward),
                            contentDescription = "权限",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(8.dp)
                                .rotate(rotateDegree)
                                .noRippleClickable {
                                    expanded = true
                                    scope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    }
                                })
                    }
                }
            }
        }
    }
}