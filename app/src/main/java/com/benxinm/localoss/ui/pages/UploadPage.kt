package com.benxinm.localoss.ui.pages

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.R
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.FileView
import com.benxinm.localoss.ui.components.TopBarWithBackNoIcon
import com.benxinm.localoss.ui.model.FileType
import com.benxinm.localoss.ui.util.Utils
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.viewModel.BucketViewModel
import com.benxinm.localoss.viewModel.TransportViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@Composable
fun UploadPage(navController: NavController, token: String, bucketViewModel: BucketViewModel,transportViewModel: TransportViewModel) {
    val context = LocalContext.current
    val lifecycleOwner= LocalLifecycleOwner.current
    val resolver = context.applicationContext.contentResolver
    var flag by remember {
        mutableStateOf(true)
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopBarWithBackNoIcon(text = "上传文件", navController = navController)
    }) { paddingValues ->
        val pickMultipleMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(5)
        ) { uris ->
            if (uris.isNotEmpty()) {
                uris.forEach{
                    if (!flag) {
                        var file = File.createTempFile("pic_${transferDate(System.currentTimeMillis())}", ".jpg")
                        resolver.openInputStream(it).use { stream ->
                            file.writeBytes(stream!!.readBytes())
                        }
                        Log.d("UploadMd5",Utils.getFileMD5(file).toString())
                        Log.d("UploadId",bucketViewModel.currentBucket!!.id.toString())
                        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("originMd5", Utils.getFileMD5(file))
                            .addFormDataPart("bucketId",bucketViewModel.currentBucket!!.id.toString()).addFormDataPart("isZip",false.toString())
                        val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                        builder.addFormDataPart("file",file.name,fileBody)
                        val parts = builder.build().parts()
                        Repository.uploadSingleFile(token,parts).observe(lifecycleOwner){res->
                            if (res.isSuccess){
                                Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show()
                                transportViewModel.uploadedList.add(com.benxinm.localoss.ui.model.File(file.name,file.length(),System.currentTimeMillis(),FileType.PIC))
                            }else{
                                res.onFailure {
                                    Log.e("Network",it.message.toString())
                                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        var  videoFile = File.createTempFile("vid_${transferDate(System.currentTimeMillis())}", ".mp4")
                        resolver.openInputStream(it).use { stream ->
                            videoFile.writeBytes(stream!!.readBytes())
                        }
                        //TODO 视频上传
                        /**
                         * 视频小于50M单文件上传
                         */
                        Log.d("FileSize",videoFile.length().toString())
                        if (videoFile.length()<5242880){
                            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                                .addFormDataPart("originMd5", Utils.getFileMD5(videoFile))
                                .addFormDataPart("bucketId",bucketViewModel.currentBucket!!.id.toString()).addFormDataPart("isZip",false.toString())
                            val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),videoFile)
                            builder.addFormDataPart("file",videoFile.name,fileBody)
                            val parts = builder.build().parts()
                            Repository.uploadSingleFile(token,parts).observe(lifecycleOwner){res->
                                if (res.isSuccess){
                                    Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show()
                                    transportViewModel.uploadedList.add(com.benxinm.localoss.ui.model.File(videoFile.name,videoFile.length(),System.currentTimeMillis(),FileType.VIDEO))
                                }else{
                                    res.onFailure {
                                        Log.e("Network",it.message.toString())
                                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }else{
                            val originMd5 = Utils.getFileMD5(videoFile)
                            Log.d("UploadBlockMd5",originMd5.toString())
                            val pieces = 10
                            val blockSize = videoFile.length()/pieces
                            var key:String? =null
                            Repository.uploadPrepare(token,originMd5.toString(),videoFile.name,pieces,blockSize,bucketViewModel.currentBucket!!.id,false).observe(lifecycleOwner){
                                if (it.isSuccess){
                                    Log.d("UploadBlock","已获取Key")
                                    it.getOrNull()?.let {string->
                                        key=string
                                    }
                                    for (i in (0..pieces)){
                                        val file = File.createTempFile("pieces$i","")
                                        val byteArray= Utils.getBlock(i*blockSize,videoFile, blockSize = blockSize.toInt())
                                        Log.d("UploadBlock",i.toString())
                                        file.outputStream().use {
                                            it.write(byteArray)
                                        }
                                        Log.d("UploadBlockFileSize",file.length().toString())
                                        Log.d("UploadBlockKey",key.toString())
                                        val pieceMd5 = Utils.getFileMD5(file)
                                        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("ownMd5",pieceMd5)
                                            .addFormDataPart("no","$i")
                                            .addFormDataPart("bucketId",
                                                bucketViewModel.currentBucket!!.id.toString()
                                            )
                                            .addFormDataPart("totalMd5",originMd5)
                                            .addFormDataPart("key",key!!)
                                        val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                                        builder.addFormDataPart("file",file.name,fileBody)
                                        val parts = builder.build().parts()
                                        Repository.uploadBlock(token,parts).observe(lifecycleOwner){
                                            if (it.isSuccess){
                                                it.getOrNull()?.let {num->
                                                    Log.d("UploadBlockSuccess",num.toString())
                                                    transportViewModel.uploadedList.add(com.benxinm.localoss.ui.model.File(videoFile.name,videoFile.length(),System.currentTimeMillis(),FileType.VIDEO))
                                                }
                                            }else{
                                                it.onFailure {err->
                                                    Log.d("UploadBlock","fail ${err.message}")
                                                }
                                            }
                                        }
//                                        file.delete()
                                    }
                                }else{

                                }
                            }

//                        Utils.getBlock()
                        }
                    }
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        var files: Array<String> = context.fileList()
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_image),
                                contentDescription = "", tint = Color.Black,
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(11.dp))
                            Text(text = "图片", fontSize = 18.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = "图片选取",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(8.dp)
                                    .noRippleClickable {
                                        flag = false
                                        pickMultipleMedia.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    })
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_video_pre),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(11.dp))
                            Text(text = "视频", fontSize = 18.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = "视频选取",
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(8.dp)
                                    .noRippleClickable {
                                        flag = true
                                        pickMultipleMedia.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.VideoOnly
                                            )
                                        )
                                    })
                        }
                    }
                }
                item {
                    var showLocalFile by remember {
                        mutableStateOf(false)
                    }
                    Column(modifier =Modifier.wrapContentSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_video_pre),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(8.dp)
                                )
                                Spacer(modifier = Modifier.width(11.dp))
                                Text(text = "本地文件获取", fontSize = 18.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(painter = painterResource(id = R.drawable.ic_forward),
                                    contentDescription = "本地文件选取",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(8.dp)
                                        .noRippleClickable {
                                            showLocalFile = !showLocalFile
                                        })
                            }
                        }
                        AnimatedVisibility(visible =showLocalFile ) {
                            Column(modifier = Modifier.wrapContentSize()) {
                                files.forEach { name ->
                                    val file = File(context.filesDir, name)
                                    FileView(name = name, onClick = {
                                        val file = File(context.filesDir,name)
                                        if (file.length()<52428800){
                                            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                                                .addFormDataPart("originMd5", Utils.getFileMD5(file))
                                                .addFormDataPart("bucketId",bucketViewModel.currentBucket!!.id.toString()).addFormDataPart("isZip",false.toString())
                                            val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                                            builder.addFormDataPart("file",file.name,fileBody)
                                            val parts = builder.build().parts()
                                            Repository.uploadSingleFile(token,parts).observe(lifecycleOwner){res->
                                                if (res.isSuccess){
                                                    transportViewModel.uploadedList.add(com.benxinm.localoss.ui.model.File(file.name,file.length(),System.currentTimeMillis(),FileType.TEXT))
                                                    Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show()
                                                }else{
                                                    res.onFailure {
                                                        Log.e("Network",it.message.toString())
                                                        Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                                                    }
                                                }                                            }
                                        }else{

                                        }
                                    }, size = file.length(), extension = file.extension)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun typeImage(extension: String) = if (extension == "mp3" || extension == "m4a") {
    R.drawable.ic_audio
} else {
    R.drawable.ic_text
}