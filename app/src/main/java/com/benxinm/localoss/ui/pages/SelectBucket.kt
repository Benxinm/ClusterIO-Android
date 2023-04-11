package com.benxinm.localoss.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.benxinm.localoss.ui.components.PermissionBucketView
import com.benxinm.localoss.ui.components.TopBarWithBackNoIcon
import com.benxinm.localoss.ui.model.Page
import com.benxinm.localoss.viewModel.BucketViewModel
import kotlinx.coroutines.launch

@Composable
fun SelectBucket(navController: NavController,bucketViewModel: BucketViewModel) {
    Scaffold(topBar = {
        TopBarWithBackNoIcon(text = "选择Bucket",navController)
    }) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)){
                items(bucketViewModel.bucketList) {bucket->
                    PermissionBucketView(name = bucket.name, permissionType = bucket.permissionType, onClick = {
                        bucketViewModel.currentBucket=bucket
                        if (bucketViewModel.uploadOrCamera){
                            navController.navigate(Page.Upload.name)
                        }else{
                            navController.navigate(Page.Camera.name)
                        }
                    }) {
                        bucketViewModel.currentBucket=bucket
                    }
                }
            }
        }
    }
}