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
    suspend fun uploadPrepare(token: String, md5:String, fileName:String,  num:Int,  size:Long, bucketId:Int, isZip:Boolean)=
        fileService.uploadPrepare(token, md5, fileName, num, size, bucketId, isZip).await()
    suspend fun uploadBlock(token:String,  partList: List<MultipartBody.Part>)=
        fileService.uploadBlock(token, partList).await()
    suspend fun uploadSingleFile(token: String, partList: List<MultipartBody.Part>)=
        fileService.uploadSingleFile(token, partList).await()
    suspend fun deleteFile(token: String,bucketId: Int,fileName: String,isForever:Boolean)=
        fileService.deleteFile(token, bucketId, fileName, isForever).await()
    suspend fun getBucketFiles(token: String,bucketId: Int)= fileService.getBucketFiles(token, bucketId).await()
    suspend fun downloadFile(token: String,bucketId: Int,fileName: String)= fileService.downloadFile(token, bucketId, fileName).await()
    suspend fun fileBackup(token: String,bucketId: Int,fileName: String)= fileService.fileBackup(token, bucketId, fileName).await()
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