package com.benxinm.localoss.ui.model

enum class UserPermissionType(val text:String,val type:Int) {
    Read("只读",1),ReadAndWrite("读写",2),Forbidden("禁止",3)
}