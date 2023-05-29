package com.benxinm.localoss.net.service

import com.benxinm.localoss.model.BucketFiles
import com.benxinm.localoss.net.CommonResult
import com.benxinm.localoss.net.CommonResultNoData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface FileService {
    @POST
    @Multipart
    fun uploadBlock(@Url url:String,@Header("token")token:String,@Part partList: List<MultipartBody.Part>): Call<CommonResult<Int>>
    @FormUrlEncoded
    @POST
    fun uploadPrepare(@Url url:String,@Header("token")token: String,@Field("originMd5") md5:String,@Field("fileName") fileName:String,@Field("shardNum") num:Int,@Field("shardSize") size:Long,@Field("bucketId")bucketId:Int,@Field("isZip") isZip:Boolean):Call<CommonResult<String>>
    @Multipart
    @POST
    fun uploadSingleFile(@Url url:String,@Header("token")token: String,@Part partList: List<MultipartBody.Part>):Call<CommonResultNoData>
    @GET
    fun getBucketFiles(@Url url:String,@Header("token")token: String):Call<CommonResult<BucketFiles>>
    @FormUrlEncoded
    @POST
    fun deleteFile(@Url url:String,@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("fileName")fileName: String,@Field("isForever") isForever:Boolean):Call<CommonResultNoData>
    @GET
    fun downloadFile(@Url url:String,@Header("token")token: String,@Query("bucketId")bucketId: Int,@Query("fileName")fileName: String):Call<ResponseBody>
    @FormUrlEncoded
    @POST
    fun fileBackup(@Url url:String,@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("fileName") fileName: String):Call<String?>
}