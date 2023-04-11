package com.benxinm.localoss.net.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BackupService {
    @GET("/tcp/server")
    fun openServer(@Header("token") token:String):Call<String?>
    @GET("/tcp/client")
    fun openClient(@Header("token") token: String,@Query("ip")ip:String):Call<String?>
    @GET("/tcp/copy")
    fun copy(@Header("token") token: String,@Query("ip")ip: String,@Query("bucketId") bucketId:Int):Call<String?>
    @GET("/tcp/shutdownServer")
    fun shutdownServer(@Header("token")token: String):Call<String?>
    @GET("/tcp/recover")
    fun recover(@Header("token")token: String):Call<String?>
}