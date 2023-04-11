package com.benxinm.localoss.ui.model

data class Bucket(val name:String, val num:Int=3, var permissionType: PermissionType=PermissionType.Private,val id:Int ,val size:Long=10000)
