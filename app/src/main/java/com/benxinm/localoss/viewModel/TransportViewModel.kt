package com.benxinm.localoss.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.benxinm.localoss.ui.model.File

class TransportViewModel:ViewModel() {
    val uploadedList = mutableStateListOf<File>()
    val uploadingList = mutableStateListOf<File>()
    val downloadedList = mutableStateListOf<File>() //TODO 改类型
    val downloadingList = mutableStateListOf<File>()
}