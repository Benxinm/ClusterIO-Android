package com.benxinm.localoss.net.util.network

import com.benxinm.localoss.net.service.FileService
import com.benxinm.localoss.net.service.UserService
import com.benxinm.localoss.net.util.ServiceCreator
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Part
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FileNetwork {
    private val fileService = ServiceCreator.create(FileService::class.java)
    suspend fun uploadPrepare(url:String,token: String, md5:String, fileName:String,  num:Int,  size:Long, bucketId:Int, isZip:Boolean)=
        fileService.uploadPrepare(url,token, md5, fileName, num, size, bucketId, isZip).await()
    suspend fun uploadBlock(url:String,token:String,  partList: List<MultipartBody.Part>)=
        fileService.uploadBlock(url,token, partList).await()
    suspend fun uploadSingleFile(url:String,token: String, partList: List<MultipartBody.Part>)=
        fileService.uploadSingleFile(url,token, partList).await()
    suspend fun deleteFile(url:String,token: String,bucketId: Int,fileName: String,isForever:Boolean)=
        fileService.deleteFile(url,token, bucketId, fileName, isForever).await()
    suspend fun getBucketFiles(url:String,token: String)= fileService.getBucketFiles(url,token).await()
    suspend fun downloadFile(url:String,token: String,bucketId: Int,fileName: String)= fileService.downloadFile(url,token, bucketId, fileName).await()
    suspend fun fileBackup(url:String,token: String,bucketId: Int,fileName: String)= fileService.fileBackup(url,token, bucketId, fileName).await()
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}