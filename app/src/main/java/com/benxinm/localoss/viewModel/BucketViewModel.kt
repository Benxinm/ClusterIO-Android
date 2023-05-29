package com.benxinm.localoss.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.benxinm.localoss.model.User
import com.benxinm.localoss.ui.model.Bucket
import com.benxinm.localoss.ui.model.PermissionType

class BucketViewModel:ViewModel() {
    var bucketPermission by mutableStateOf(PermissionType.Private)
    val bucketList = mutableStateListOf<Bucket>()
    val deletedBucketList = mutableStateListOf<Bucket>()
    val bucketSize = mutableStateListOf<Int>(6,0,0)
    val filterList = mutableStateListOf<Bucket>()
    val deleteFilterList = mutableStateListOf<Bucket>()
    val fileList = mutableStateListOf<String>()
    val fileFilterList = mutableStateListOf<String>()
    var currentBucket by mutableStateOf<Bucket?>(null)
    val userList = mutableStateListOf<User>()
    var uploadOrCamera = true
}