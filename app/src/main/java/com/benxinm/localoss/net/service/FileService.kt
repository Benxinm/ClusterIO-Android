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

interface FileService {
    @POST("/put/uploadShard")
    @Multipart
    fun uploadBlock(@Header("token")token:String,@Part partList: List<MultipartBody.Part>): Call<CommonResult<Int>>
    @FormUrlEncoded
    @POST("/put/shardPreparation")
    fun uploadPrepare(@Header("token")token: String,@Field("originMd5") md5:String,@Field("fileName") fileName:String,@Field("shardNum") num:Int,@Field("shardSize") size:Long,@Field("bucketId")bucketId:Int,@Field("isZip") isZip:Boolean):Call<CommonResult<String>>
    @Multipart
    @POST("/put/uploadSimple")
    fun uploadSingleFile(@Header("token")token: String,@Part partList: List<MultipartBody.Part>):Call<CommonResultNoData>
    @GET("/get/getBucket/{bucketId}")
    fun getBucketFiles(@Header("token")token: String,@Path("bucketId") bucketId: Int):Call<CommonResult<BucketFiles>>
    @FormUrlEncoded
    @POST("/delete/delFile")
    fun deleteFile(@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("fileName")fileName: String,@Field("isForever") isForever:Boolean):Call<CommonResultNoData>
    @GET("/get/getFile")
    fun downloadFile(@Header("token")token: String,@Query("bucketId")bucketId: Int,@Query("fileName")fileName: String):Call<ResponseBody>
    @FormUrlEncoded
    @POST("/put/coldStore")
    fun fileBackup(@Header("token")token: String,@Field("bucketId")bucketId: Int,@Field("fileName") fileName: String):Call<String?>
}