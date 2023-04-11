package com.benxinm.localoss.ui.model

import com.benxinm.localoss.R

enum class Page(val text: String, val iconInt: Int) {
    Buckets("Bucket管理", R.drawable.ic_home), Me("我的", R.drawable.ic_me),
    Transport("传输", 0), Detail("BucketDetail", 0), CreateBucket(
        "创建Bucket",
        0
    ),
    PermissionManagement("bucket权限管理", 0), UserPermissionManagement("用户权限管理", 0), Login(
        "登录",
        0
    ),
    AddUser("添加用户", 0), Camera("相机", 0), Upload("上传", 0), SelectBucket("选择桶",0),BackupPage("备份管理",0);

    companion object {
        fun fromRoute(route: String?): Page =
            when (route?.substringBefore("/")) {
                Buckets.name -> Buckets
                Me.name -> Me
                Transport.name -> Transport
                Detail.name -> Detail
                CreateBucket.name -> CreateBucket
                PermissionManagement.name -> PermissionManagement
                UserPermissionManagement.name -> UserPermissionManagement
                Login.name -> Login
                AddUser.name -> AddUser
                Camera.name -> Camera
                Upload.name -> Upload
                SelectBucket.name->SelectBucket
                BackupPage.name->BackupPage
                null -> Buckets
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}