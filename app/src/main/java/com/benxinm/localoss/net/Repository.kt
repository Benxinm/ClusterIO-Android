package com.benxinm.localoss.net

import android.util.Log
import androidx.lifecycle.liveData
import com.benxinm.localoss.model.User
import com.benxinm.localoss.net.util.network.BucketNetwork
import com.benxinm.localoss.net.util.network.FileNetwork
import com.benxinm.localoss.net.util.network.UserNetwork
import com.benxinm.localoss.ui.model.UserPermissionType
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.MultipartBody

object Repository {
    /**
     * User
     */
    fun login(url:String,email:String,password:String)=liveData(Dispatchers.IO){
        val result = try {
            val loginResponse = UserNetwork.login(url,email, password)
            if (loginResponse.code==200){
                Result.success(loginResponse.data)
            }else{
                Result.failure(RuntimeException(loginResponse.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun register(url: String,email:String,password:String,code:String)=liveData(Dispatchers.IO){
        val result = try {
            val response = UserNetwork.register(url,email, password, code)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException("response status is${response.msg}"))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun sendCode(url: String,email: String)= liveData(Dispatchers.IO) {
        val result = try {
            Log.d("sendCode","1")
            val response = UserNetwork.sendCode(url,email)
            Log.d("sendCode","2")
            if (response.code==200){
                Log.d("sendCode","3")
                Result.success(true)
            }else{
                Log.d("sendCode","4")
                Result.failure(RuntimeException("response status is${response.msg}"))
            }
        }catch (e:Exception){
            Log.d("sendCode",e.message.orEmpty())
            Result.failure(e)
        }
        emit(result)
    }
    fun logout(url: String, token:String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = UserNetwork.logout(url,token)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * Bucket
     */
    fun addBucket(url: String,token: String,name:String,authority:Int)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.addBucket(url,token, name, authority)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun updateAuthority(url: String,token: String,id:Int,authority: Int) = liveData(Dispatchers.IO){
        val result = try {
            val response = BucketNetwork.updateAuthority(url,token, id, authority)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun updateName(url: String,token: String,id:Int,name: String) = liveData(Dispatchers.IO){
        val result = try {
            val response = BucketNetwork.updateName(url,token, id, name)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getBuckets(url: String,token: String)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.getMyBuckets(url,token)
            if (response.code==200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun addBucketUser(url: String,token:String,id: Int,email: String,type:Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.addBucketUser(url,token,id, email, type)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getUsers(url: String,token: String,bucketId:Int) = liveData(Dispatchers.IO) {
        val result = try {
            Log.d("getUsers","1")
            val response = BucketNetwork.getUsers(url,token,bucketId)
            Log.d("getUsers","2")
            if (response.code==200){
                Log.d("getUsers","3")
                val list = mutableListOf<User>()
                response.data.userList.forEach {
                    list.add(User(name = it["name"]?:"", email = it["user_id"]?:"", type =when(it["type"]?:"1"){
                        "1"->UserPermissionType.Read
                        "2"->UserPermissionType.ReadAndWrite
                        "3"->UserPermissionType.Forbidden
                        else->UserPermissionType.Forbidden
                    }))
                }
                Log.d("getUsers","4")
                Result.success(list.toList())
            }else{
                Log.d("getUsers","5")
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Log.d("getUsers","6")
            Log.d("getUsers",e.message.orEmpty())
            Result.failure(e)
        }
        emit(result)
    }
    fun updateUserPermission(url: String,token:String,id: Int,email: String,type:Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.updateUserPermission(url,token,id, email, type)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun deleteBucket(url: String,token: String,bucketId: Int)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.deleteBucket(url,token,bucketId)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getDeletedBucket(url: String,token: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.getDeletedBucket(url,token)
            if (response.code==200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun recoverDeletedBucket(url: String,token: String,bucketId: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.recoverDeletedBucket(url, token, bucketId)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    /**
     * File
     */
    fun uploadPrepare(url: String,token: String, md5:String, fileName:String,  num:Int,  size:Long, bucketId:Int, isZip:Boolean)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.uploadPrepare(url,token,md5,fileName,num, size, bucketId, isZip)
            if (response.code==200){
                Result.success(response.data)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun uploadBlock(url: String,token:String, partList: List<MultipartBody.Part>)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("UploadBlock","被调1")
            val response = FileNetwork.uploadBlock(url,token,partList)
            Log.d("UploadBlock","被调2")
            if (response.code==200){
                Log.d("UploadBlock","被调3")
                Result.success(response.data)
            }else{
                Log.d("UploadBlock","被调4")
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Log.d("UploadBlock","被调5")
            Result.failure(e)
        }
        emit(result)
    }
    fun uploadSingleFile(url: String,token: String, partList: List<MultipartBody.Part>)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("Upload","1")
            val response = FileNetwork.uploadSingleFile(url,token,partList )
            Log.d("Upload","2")
            if (response.code==200){
                Log.d("Upload","3")
                Log.d("Upload",response.msg)
                Result.success(true)
            }else{
                Log.d("Upload","4")
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Log.d("Upload","5")
            Result.failure(e)
        }
        emit(result)
    }
    fun deleteFile(url: String,token: String,bucketId: Int,fileName: String,isForever:Boolean)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.deleteFile(url,token,bucketId, fileName, isForever)
            if (response.code==200){
                Result.success(true)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun getBucketFiles(url: String,token: String)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.getBucketFiles(url,token)
            if (response.code==200){
                Result.success(response.data.fileSet)
            }else{
                Result.failure(RuntimeException(response.msg))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
    fun downloadFile(url: String,token: String,bucketId: Int,fileName: String)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("Download","1")
            val response = FileNetwork.downloadFile(url,token,bucketId,fileName)
            Log.d("Download","2")
            Result.success(response)
        }catch (e:Exception){
            Log.d("Download","3")
            Result.failure(e)
        }
        emit(result)
    }
    fun fileBackup(url: String,token: String,bucketId: Int,fileName: String)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.fileBackup(url,token,bucketId, fileName)
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
}