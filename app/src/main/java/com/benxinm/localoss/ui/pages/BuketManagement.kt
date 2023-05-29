package com.benxinm.localoss.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benxinm.localoss.net.Repository
import com.benxinm.localoss.ui.components.*
import com.benxinm.localoss.ui.model.Bucket
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.ui.model.PermissionType
import com.benxinm.localoss.ui.model.Type
import com.benxinm.localoss.ui.theme.*
import com.benxinm.localoss.ui.util.Utils
import com.benxinm.localoss.ui.util.noRippleClickable
import com.benxinm.localoss.viewModel.BucketViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@OptIn(ExperimentalPagerApi::class)
@Composable
fun BucketManagement(navController: NavController,bucketViewModel: BucketViewModel,token:String) {
    Scaffold(topBar ={
        TopBar(text = "Bucket管理")
    }, modifier = Modifier.padding(horizontal = 15.dp)) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            val scope = rememberCoroutineScope()
            val pagerState  = rememberPagerState(0)
            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current
            var searchText by remember {
                mutableStateOf("")
            }
            var files: Array<String> = context.fileList()
            LaunchedEffect(key1 = searchText){

                if (searchText.isNotEmpty()){
                    bucketViewModel.filterList.removeIf {
                        !it.name.contains(searchText)
                    }
                }else{
                    bucketViewModel.filterList.clear()
                    bucketViewModel.filterList.addAll(bucketViewModel.bucketList)
                }
            }
            LaunchedEffect(key1 = true){
                Repository.getBuckets("${Utils.HTTP+Utils.ip}/bucket/myBuckets",token).observe(lifecycleOwner){result->
                    val list = result.getOrNull()
                    if (result.isSuccess){
                        bucketViewModel.bucketList.clear()
                        list?.bucketList?.forEachIndexed {index,model->
                            var num = 0;// TODO
                            Repository.getBucketFiles("${Utils.HTTP+Utils.ip}/get/getBucket/${model.id}",token )//TODO 1
                                .observe(lifecycleOwner) {
                                    if (it.isSuccess) {
                                        it.getOrNull()?.let {list->
                                            num=list.size
                                        }
                                    } else {

                                    }
                                }
                            bucketViewModel.bucketList.add(Bucket(model.name, id = model.id, permissionType = when(model.authority){
                                1->PermissionType.PublicRead
                                2->PermissionType.PublicRW
                                3->PermissionType.Private
                                else->PermissionType.Private
                            }, num = bucketViewModel.bucketSize[index]))
                            Log.d("bucketId",model.id.toString())
                        }
                        bucketViewModel.filterList.addAll(bucketViewModel.bucketList)
                        Log.d("list",bucketViewModel.filterList.size.toString())
                    }else{
                        result.onFailure {
                            val toast = Toast.makeText(context,it.message,Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
                Repository.getDeletedBucket("${Utils.HTTP+Utils.ip}/bucket/delBuckets",token).observe(lifecycleOwner){result->
                    val list = result.getOrNull()
                    if (result.isSuccess){
                        bucketViewModel.deletedBucketList.clear()
                        list?.bucketList?.forEach {model->
                            var num = 0
                            Repository.getBucketFiles("${Utils.HTTP+Utils.ip}/get/getBucket/${model.id}",token )//TODO 1
                                .observe(lifecycleOwner) {
                                    if (it.isSuccess) {
                                        it.getOrNull()?.let {list->
                                            num=list.size
                                        }
                                    } else {

                                    }
                                }

                            bucketViewModel.deletedBucketList.add(Bucket(model.name, id = model.id, permissionType = when(model.authority){
                                1->PermissionType.PublicRead
                                2->PermissionType.PublicRW
                                3->PermissionType.Private
                                else->PermissionType.Private
                            }, num = num))
                        }
                        bucketViewModel.deleteFilterList.addAll(bucketViewModel.deletedBucketList)
                    }else{
                        result.onFailure {
                            val toast = Toast.makeText(context,it.message,Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
            }
            var total by remember {
                mutableStateOf(0)
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    SearchBar(modifier = Modifier.padding(horizontal = 3.dp),searchText) {
                        searchText = it
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Bucket概览", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,) {
                        Column(modifier = Modifier.padding(horizontal = 40.dp), verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(text = calculate(total.toLong()) , fontSize = 16.sp ,fontWeight = FontWeight.Bold)//TODO 传入数据
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "存储用量")
                        }
                        VerticalLineDiver(ratio = 0.14f)
                        Column(modifier = Modifier.padding(horizontal = 40.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = bucketViewModel.bucketList.size.toString(), fontSize = 16.sp , fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)//TODO 传入数据
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "对象存储")
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(165.dp)
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight()) {
                            MyLineChart()
                        }
                        Text(text = "4.2", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
                val typeList = Type.values()
                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.fillMaxWidth(0.1f)) {
                            Divider(thickness = 2.dp, color = Purple500, modifier = Modifier.clip(
                                RoundedCornerShape(50f)
                            ))
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "存储量", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically ,horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Bucket列表", fontSize = 19.sp ,fontWeight = FontWeight.SemiBold)
                        Text(text = "传输列表", fontSize = 15.sp, color = MainColor, modifier = Modifier.noRippleClickable {
                            navController.navigate(Page.Transport.name)
                        })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        typeList.forEachIndexed { index, type ->
                            Card(
                                backgroundColor = if (pagerState.currentPage == index) LightMainColor else BottomColor,
                                modifier = Modifier
                                    .size(width = 90.dp, height = 25.dp)
                                    .noRippleClickable {
                                        scope.launch {
                                            pagerState.scrollToPage(index)
                                        }
                                    }
                                    .padding(end = 10.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment =Alignment.CenterHorizontally) {
                                    Text(text = type.text, color = if (pagerState.currentPage == index) Color.Black else LightGreyTextColor)
                                }
                            }
                        }
                    }
                }
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)) {
                        HorizontalPager(count = 3, state = pagerState, modifier = Modifier
                            .fillMaxSize()) {page ->
                            LazyColumn(modifier = Modifier.fillMaxSize()){
                                if(page==0){
                                    if (searchText.isNotEmpty()){
                                        item {
                                            bucketViewModel.filterList .forEach{ bucket ->
                                                BucketView(num = bucket.num, name =bucket.name , size =bucket.size){
                                                    bucketViewModel.currentBucket=bucket
                                                    navController.navigate(Page.Detail.name)
                                                }
                                            }
                                        }
                                    }else{
                                        item {
                                            bucketViewModel.bucketList .forEach{ bucket ->
                                                val size = bucket.num*(700000..800000).random()
                                                LaunchedEffect(key1 = true){
                                                    total+=size
                                                }
                                                BucketView(num = bucket.num, name =bucket.name , size =size.toLong()){
                                                    bucketViewModel.currentBucket=bucket
                                                    navController.navigate(Page.Detail.name)
                                                }
                                            }
                                        }
                                    }
                                }else if (page==1){
                                    items(files){name->
                                        val file = File(context.filesDir, name)
                                        FileView(name = name, onClick = {
//                                            val file = File(context.filesDir,name)
//                                            if (file.length()<52428800){
//                                                val builder = MultipartBody.Builder().setType(
//                                                    MultipartBody.FORM)
//                                                    .addFormDataPart("originMd5", Utils.getFileMD5(file))
//                                                    .addFormDataPart("bucketId",bucketViewModel.currentBucket!!.id.toString()).addFormDataPart("isZip",false.toString())
//                                                val fileBody  = RequestBody.create(MediaType.parse("multipart/form-data"),file)
//                                                builder.addFormDataPart("file",file.name,fileBody)
//                                                val parts = builder.build().parts()
//                                                Repository.uploadSingleFile(token,parts).observe(lifecycleOwner){res->
//                                                    if (res.isSuccess){
//                                                        Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show()
//                                                    }else{
//                                                        res.onFailure {
//                                                            Log.e("Network",it.message.toString())
//                                                            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
//                                                        }
//                                                    }                                            }
//                                            }else{
//
//                                            }
                                        }, size = file.length(), extension = file.extension)
                                    }
                                }else{
                                    if (searchText.isNotEmpty()){
                                        item {
                                            bucketViewModel.deleteFilterList .forEach{ bucket ->
                                                BucketView(num = bucket.num, name =bucket.name , size =bucket.size){
                                                    bucketViewModel.currentBucket=bucket
                                                    navController.navigate(Page.Detail.name)
                                                }
                                            }
                                        }
                                    }else{
                                        item {
                                            bucketViewModel.deletedBucketList .forEach{ bucket ->
                                                BucketView(num = bucket.num, name =bucket.name , size =bucket.size){
                                                    bucketViewModel.currentBucket=bucket
                                                    navController.navigate(Page.Detail.name)
                                                }
                                            }
                                        }
                                    }                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
