package com.benxinm.localoss.net.util.network

import com.benxinm.localoss.net.service.BucketService
import com.benxinm.localoss.net.service.UserService
import com.benxinm.localoss.net.util.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object BucketNetwork {
    private val bucketService = ServiceCreator.create(BucketService::class.java)
    suspend fun addBucket(url:String,token:String,name:String,authority:Int) = bucketService.addBucket(url,token, name, authority).await()
    suspend fun updateAuthority(url:String,token: String,id:Int,authority:Int)= bucketService.updateAuthority(url,token, id, authority).await()
    suspend fun updateName(url:String,token: String,id:Int,name: String) = bucketService.updateName(url,token, id, name).await()
    suspend fun getMyBuckets(url:String,token: String) = bucketService.getBuckets(url,token).await()
    suspend fun deleteBucket(url:String,token: String,id: Int) = bucketService.deleteBucket(url,token, id).await()
    suspend fun addBucketUser(url:String,token: String,id: Int,email:String,type:Int) = bucketService.addBucketUser(url,token, id, email, type).await()
    suspend fun getUsers(url:String,token: String,bucketId:Int) = bucketService.getUsers(url,token, bucketId).await()
    suspend fun updateUserPermission(url:String,token: String,bucketId: Int,email:String,type: Int) = bucketService.updateUserPermission(url,token, bucketId, email, type).await()

    suspend fun getDeletedBucket(url:String,token: String)= bucketService.getDeletedBucket(url, token).await()
    suspend fun recoverDeletedBucket(url:String,token: String,bucketId:Int) = bucketService.recoverDeletedBucket(url, token, bucketId).await()
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