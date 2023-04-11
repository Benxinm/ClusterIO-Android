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
    suspend fun addBucket(token:String,name:String,authority:Int) = bucketService.addBucket(token, name, authority).await()
    suspend fun updateAuthority(token: String,id:Int,authority:Int)= bucketService.updateAuthority(token, id, authority).await()
    suspend fun updateName(token: String,id:Int,name: String) = bucketService.updateName(token, id, name).await()
    suspend fun getMyBuckets(token: String) = bucketService.getBuckets(token).await()
    suspend fun deleteBucket(token: String,id: Int) = bucketService.deleteBucket(token, id).await()
    suspend fun addBucketUser(token: String,id: Int,email:String,type:Int) = bucketService.addBucketUser(token, id, email, type).await()
    suspend fun getUsers(token: String,bucketId:Int) = bucketService.getUsers(token, bucketId).await()
    suspend fun updateUserPermission(token: String,bucketId: Int,email:String,type: Int) = bucketService.updateUserPermission(token, bucketId, email, type).await()
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