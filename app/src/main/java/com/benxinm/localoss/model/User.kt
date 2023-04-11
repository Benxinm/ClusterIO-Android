package com.benxinm.localoss.model

import com.benxinm.localoss.ui.model.UserPermissionType

data class User(val name:String, val email:String, val type:UserPermissionType)
