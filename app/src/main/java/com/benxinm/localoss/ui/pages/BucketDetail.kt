package com.benxinm.localoss.ui.pages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.ui.model.File
import com.benxinm.localoss.ui.model.FileType
import com.benxinm.localoss.viewModel.BucketViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.benxinm.localoss.R
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.*
import com.benxinm.localoss.ui.theme.LineColor
import com.benxinm.localoss.ui.theme.MainColor
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.ui.util.saveMediaToStorage
import com.benxinm.localoss.viewModel.TransportViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BucketDetail(navController: NavController, token: String, bucketViewModel: BucketViewModel,transportViewModel: TransportViewModel) {
    val tempList = listOf(
        File("文本文件.txt", 100000, Date().time, FileType.TEXT),
        File("音频文件.mp3", 100000, Date().time, FileType.AUDIO),
        File("图片文件.jpg", 100000, Date().time, FileType.PIC),
        File("视频文件.mp4", 100000, Date().time, FileType.VIDEO),
        File("文本文件.xxx", 100000, Date().time, FileType.OTHER)
    )
    var isExpand by remember {
        mutableStateOf(false)
    }
    var currentFile: File? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val modifier = Modifier.background(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray,
                Color.LightGray
            )
        ), alpha = 0.6f
    )
    var flag by remember {
        mutableStateOf(false)
    }
    var showDrawDownMenu by remember {
        mutableStateOf(false)
    }
    var changeName by remember {
        mutableStateOf(false)
    }
    var bucketName by remember {
        mutableStateOf(bucketViewModel.currentBucket!!.name)
    }
    LaunchedEffect(key1 = flag) {
        Repository.getBucketFiles(token, bucketViewModel.currentBucket!!.id)
            .observe(lifecycleOwner) {
                if (it.isSuccess) {
                    it.getOrNull()?.let { fileList ->
                        bucketViewModel.fileList.clear()
                        bucketViewModel.fileList.addAll(fileList)
                    }
                } else {
                    it.onFailure { err ->
                        Toast.makeText(context, err.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    LaunchedEffect(key1 = bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
            isExpand = false
        }
    }
    BottomSheetScaffold(
        topBar = {
            Box(
                modifier = if (isExpand) modifier else Modifier
            ) {
                TopBarWithBack(
                    text = bucketName,
                    navController,
                    if (changeName) R.drawable.ic_ok else R.drawable.ic_three_dot, changeName = changeName ,iconSize = if (changeName) 40.dp else 30.dp
                ) {newName->
                    if (changeName){
                        Repository.updateName(token,bucketViewModel.currentBucket!!.id,newName).observe(lifecycleOwner){
                            if (it.isSuccess){
                                Toast.makeText(context,"改名成功",Toast.LENGTH_SHORT).show()
                                bucketName=newName
                            }else{
                                Toast.makeText(context,"改名失败",Toast.LENGTH_SHORT).show()
                            }
                        }
                        changeName=false

                    }else{
                        showDrawDownMenu=!showDrawDownMenu
                    }
                }
            }
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (currentFile != null) {
                BottomSheetContentImg(imageInt = typeImage(currentFile!!.type), textContent = {
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = currentFile!!.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row {
                            Text(
                                text = transferDate(currentFile!!.date) + "·${calculate(currentFile!!.size)}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }) {
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
                                    //下载请求
                                    scope.launch {
                                        Repository
                                            .downloadFile(
                                                token,
                                                bucketViewModel.currentBucket!!.id,
                                                currentFile!!.name
                                            )
                                            .observe(lifecycleOwner) {
                                                if (it.isSuccess) {
                                                    val tempFileName =
                                                        "localOss_${currentFile!!.name}"
                                                    val body = it.getOrNull()
                                                    context
                                                        .openFileOutput(
                                                            tempFileName,
                                                            Context.MODE_PRIVATE
                                                        )
                                                        .use { fos ->
                                                            body?.let {
                                                                fos.write(body.bytes())
                                                            }
                                                        }
                                                    val file =
                                                        java.io.File(context.filesDir, tempFileName)
                                                    if (tempFileName.endsWith(
                                                            ".jpg",
                                                            ignoreCase = true
                                                        ) || tempFileName.endsWith(
                                                            ".png",
                                                            ignoreCase = true
                                                        ) || tempFileName.endsWith(".jpeg")
                                                    ) {
                                                        val bitmap: Bitmap? =
                                                            BitmapFactory.decodeFile(file.path)
                                                        bitmap?.let { bitmap ->
                                                            bitmap.saveMediaToStorage(
                                                                context,
                                                                bitmap
                                                            )
                                                        }
//                                                        file.delete()
                                                    } else {

                                                    }
                                                    transportViewModel.downloadedList.add(File(file.name,file.length(),System.currentTimeMillis(),
                                                        typeConvert(file.name)
                                                    ))
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "完成下载",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                } else {
                                                    it.onFailure { err->
                                                        Log.d("DownloadFail",err.message.toString())
                                                    }
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "开始下载失败",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                            }
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_download),
                                contentDescription = "下载",
                                modifier = Modifier
                                    .size(25.dp)
                            )
                            Text(
                                text = "下载",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(startIndent = 18.dp, color = LineColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .noRippleClickable {
                                    // 删除请求
                                    scope.launch {
                                        Repository
                                            .fileBackup(
                                                token,
                                                bucketViewModel.currentBucket!!.id,
                                                currentFile!!.name
                                            )
                                            .observe(lifecycleOwner) {
                                                if (it.isSuccess) {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "备份成功",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "备份失败",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()

                                                }
                                            }
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_backup),
                                contentDescription = "备份",
                                modifier = Modifier
                                    .size(25.dp)
                            )
                            Text(
                                text = "备份",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(startIndent = 18.dp, color = LineColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .noRippleClickable {
                                    // 删除请求
                                    scope.launch {
                                        Repository
                                            .deleteFile(
                                                token,
                                                bucketViewModel.currentBucket!!.id,
                                                currentFile!!.name,
                                                true
                                            )
                                            .observe(lifecycleOwner) {
                                                if (it.isSuccess) {
                                                    flag = !flag
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "删除成功",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "删除失败",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                            }
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "删除",
                                modifier = Modifier
                                    .size(25.dp)
                            )
                            Text(
                                text = "删除",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(startIndent = 18.dp, color = LineColor)
                        Button(
                            onClick = {
                                scope.launch {
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
                                text = "取消",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

        },
        sheetPeekHeight = 0.dp
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            var searchText by remember {
                mutableStateOf("")
            }

            LaunchedEffect(key1 = searchText){
                if (searchText.isNotEmpty()){
                    bucketViewModel.fileFilterList.removeIf{
                        !it.contains(searchText)
                    }
                }else{
                    bucketViewModel.fileFilterList.clear()
                    bucketViewModel.fileFilterList.addAll(bucketViewModel.fileList)
                }
            }
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 15.dp)
            ) {
                LazyColumn {
                    item {
                        SearchBar(text = searchText){
                            searchText=it
                        }
                    }

                    items(if (searchText.isNotEmpty()) bucketViewModel.fileFilterList else bucketViewModel.fileList ) { name ->
                        val file = File(
                            date = System.currentTimeMillis(),
                            name = name,
                            size = 10000,
                            type = typeConvert(name)
                        )
                        FileView(
                            date = transferDate(date = file.date),
                            name = name,
                            size = file.size,
                            type = file.type
                        ) {
                            currentFile = file
                            isExpand = true
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        }
                    }
                }
            }
            Box(modifier = if (isExpand) modifier.fillMaxSize() else Modifier)
            AnimatedVisibility(visible = showDrawDownMenu, enter = expandVertically(), exit = shrinkVertically()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.End) {
                    Surface(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.4f)
                        , shape = RoundedCornerShape(15.dp), elevation = 3.dp
                    ) {
                        Box(modifier = Modifier.padding(15.dp)) {
                            Column {
                                Text(text = "Bucket重命名", fontSize = 18.sp ,modifier = Modifier.clickable {
                                    showDrawDownMenu=false
                                    changeName=true
                                })
                                Spacer(modifier = Modifier.height(7.dp))
                                Text(text = "删除Bucket", fontSize = 18.sp ,modifier = Modifier.clickable {
                                    Repository.deleteBucket(token,bucketViewModel.currentBucket!!.id).observe(lifecycleOwner){
                                        navController.popBackStack()
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

fun transferDate(date: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    return sdf.format(date)
}

fun typeImage(type: FileType) = when (type) {
    FileType.PIC -> R.drawable.ic_pic
    FileType.AUDIO -> R.drawable.ic_audio
    FileType.VIDEO -> R.drawable.ic_video
    FileType.TEXT -> R.drawable.ic_text
    FileType.OTHER -> R.drawable.ic_other
}

fun typeConvert(name: String) =
    if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
        FileType.PIC
    } else if (name.endsWith(".mp3") || name.endsWith(".m4a")) {
        FileType.AUDIO
    } else if (name.endsWith(".mp4") || name.endsWith(".mkv")) {
        FileType.VIDEO
    } else if (name.endsWith(".txt")) {
        FileType.TEXT
    } else {
        FileType.OTHER
    }

