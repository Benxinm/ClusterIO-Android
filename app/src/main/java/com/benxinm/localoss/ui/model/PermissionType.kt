package com.benxinm.localoss.ui.model

enum class PermissionType(val text:String,val authority:Int) {
    Private("私有",3),PublicRead("公共读",1),PublicRW("公共读写",2)
}