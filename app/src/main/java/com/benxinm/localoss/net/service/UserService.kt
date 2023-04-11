package com.benxinm.localoss.net.service

import com.benxinm.localoss.net.CommonResult
import com.benxinm.localoss.net.CommonResultNoData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @FormUrlEncoded
    @POST("/user/login")
    fun login( @Field("email")email:String,@Field("password")password:String):Call<CommonResult<Map<String,String>>>
    @POST("/user/logout")
    fun logout(@Header("token")token:String):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST("/user/logon")
    fun register(@Field("email")email: String,@Field("password")password: String,@Field("code")code:String): Call<CommonResultNoData>
    @FormUrlEncoded
    @POST("/user/sendMail")
    fun sendCode(@Field("email")email: String):Call<CommonResultNoData>
}