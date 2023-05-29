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
import retrofit2.http.Url

interface BucketService {
    @FormUrlEncoded
    @POST
    fun addBucket(@Url url:String,@Header("token")token:String,@Field("name")name:String,@Field("authority")authority:Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST
    fun updateAuthority(@Url url:String,@Header("token")token:String,@Field("bucketId")id:Int,@Field("authority")authority:Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST
    fun updateName(@Url url:String,@Header("token")token:String,@Field("bucketId")id:Int,@Field("newName")name:String):Call<CommonResultNoData>
    @GET
    fun getBuckets(@Url url:String,@Header("token")token:String):Call<CommonResult<BucketListModel>>
    @FormUrlEncoded
    @POST
    fun deleteBucket(@Url url:String,@Header("token")token: String,@Field("bucketId")id: Int):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST
    fun addBucketUser(@Url url:String,@Header("token")token:String,@Field("bucketId")id:Int,@Field("email")email:String,@Field("type")type:Int):Call<CommonResultNoData>
    @GET
    fun getUsers(@Url url:String,@Header("token")token: String,@Query("bucketId")bucketId:Int):Call<CommonResult<UserListModel>>
    @FormUrlEncoded
    @POST
    fun updateUserPermission(@Url url:String,@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("email")email: String,@Field("type")type: Int):Call<CommonResultNoData>
    @GET
    fun getDeletedBucket(@Url url: String,@Header("token") token: String):Call<CommonResult<BucketListModel>>
    @FormUrlEncoded
    @POST
    fun recoverDeletedBucket(@Url url: String,@Header("token")token: String,@Field("bucketId") bucketId: Int):Call<CommonResultNoData>
}