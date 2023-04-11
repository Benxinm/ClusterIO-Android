package com.benxinm.localoss.net.service

import com.benxinm.localoss.model.BucketListModel
import com.benxinm.localoss.model.UserListModel
import com.benxinm.localoss.net.CommonResult
import com.benxinm.localoss.net.CommonResultNoData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface BucketService {
    @FormUrlEncoded
    @POST("/bucket/add")
    fun addBucket(@Header("token")token:String,@Field("name")name:String,@Field("authority")authority:Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST("/bucket/update/authority")
    fun updateAuthority(@Header("token")token:String,@Field("bucketId")id:Int,@Field("authority")authority:Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST("/bucket/update/name")
    fun updateName(@Header("token")token:String,@Field("bucketId")id:Int,@Field("newName")name:String):Call<CommonResultNoData>
    @GET("/bucket/myBuckets")
    fun getBuckets(@Header("token")token:String):Call<CommonResult<BucketListModel>>
    @FormUrlEncoded
    @POST("/bucket/delete")
    fun deleteBucket(@Header("token")token: String,@Field("bucketId")id: Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST("/bucket/user/add")
    fun addBucketUser(@Header("token")token:String,@Field("bucketId")id:Int,@Field("email")email:String,@Field("type")type:Int):Call<CommonResultNoData>
    @GET("/bucket/user/list")
    fun getUsers(@Header("token")token: String,@Query("bucketId")bucketId:Int):Call<CommonResult<UserListModel>>
    @FormUrlEncoded
    @POST("/bucket/user/setAuth")
    fun updateUserPermission(@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("email")email: String,@Field("type")type: Int):Call<CommonResultNoData>

}