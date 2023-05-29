package com.benxinm.localoss.net.util.network

import android.app.Service
import android.util.Log
import com.benxinm.localoss.net.service.UserService
import com.benxinm.localoss.net.util.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object UserNetwork {
    private val userService = ServiceCreator.create(UserService::class.java)
    suspend fun login(url:String,email:String,password:String)= userService.login(url,email, password).await()
    suspend fun register(url:String,email: String,password: String,code:String)= userService.register(url,email, password, code).await()
    suspend fun sendCode(url:String,email:String)= userService.sendCode(url,email).await()
    suspend fun logout(url:String,token:String)= userService.logout(url,token).await()
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