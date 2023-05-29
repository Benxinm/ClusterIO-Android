package com.benxinm.localoss.net.service

import com.benxinm.localoss.net.CommonResult
import com.benxinm.localoss.net.CommonResultNoData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface UserService {
    @FormUrlEncoded
    @POST
    fun login(@Url url:String ,@Field("email")email:String,@Field("password")password:String):Call<CommonResult<Map<String,String>>>
    @POST
    fun logout(@Url url:String,@Header("token")token:String):Call<CommonResultNoData>
    @FormUrlEncoded
    @POST
    fun register(@Url url:String,@Field("email")email: String,@Field("password")password: String,@Field("code")code:String): Call<CommonResultNoData>
    @FormUrlEncoded
    @POST
    fun sendCode(@Url url:String,@Field("email")email: String):Call<CommonResultNoData>
}