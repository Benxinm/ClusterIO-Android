package com.benxinm.localoss.net.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val BASE_URL = /*"http://10.0.2.2:8080/"*/"http://47.113.216.236:9739/"
    private val httpClient = OkHttpClient.Builder().callTimeout(100, TimeUnit.SECONDS)
        .connectTimeout(40, TimeUnit.SECONDS).readTimeout(400, TimeUnit.SECONDS)
        .writeTimeout(400, TimeUnit.SECONDS).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}