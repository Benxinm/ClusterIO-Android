package com.benxinm.localoss.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserViewModel:ViewModel() {
    var token by mutableStateOf("")
    var email by mutableStateOf("")
    var username by mutableStateOf("")
}