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
    fun login(email:String,password:String)=liveData(Dispatchers.IO){
        val result = try {
            val loginResponse = UserNetwork.login(email, password)
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
    fun register(email:String,password:String,code:String)=liveData(Dispatchers.IO){
        val result = try {
            val response = UserNetwork.register(email, password, code)
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
    fun sendCode(email: String)= liveData(Dispatchers.IO) {
        val result = try {
            Log.d("sendCode","1")
            val response = UserNetwork.sendCode(email)
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
    fun logout(token:String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = UserNetwork.logout(token)
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
    fun addBucket(token: String,name:String,authority:Int)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.addBucket(token, name, authority)
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
    fun updateAuthority(token: String,id:Int,authority: Int) = liveData(Dispatchers.IO){
        val result = try {
            val response = BucketNetwork.updateAuthority(token, id, authority)
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
    fun updateName(token: String,id:Int,name: String) = liveData(Dispatchers.IO){
        val result = try {
            val response = BucketNetwork.updateName(token, id, name)
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
    fun getBuckets(token: String)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.getMyBuckets(token)
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
    fun addBucketUser(token:String,id: Int,email: String,type:Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.addBucketUser(token,id, email, type)
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
    fun getUsers(token: String,bucketId:Int) = liveData(Dispatchers.IO) {
        val result = try {
            Log.d("getUsers","1")
            val response = BucketNetwork.getUsers(token,bucketId)
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
    fun updateUserPermission(token:String,id: Int,email: String,type:Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.updateUserPermission(token,id, email, type)
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
    fun deleteBucket(token: String,bucketId: Int)= liveData(Dispatchers.IO) {
        val result = try {
            val response = BucketNetwork.deleteBucket(token,bucketId)
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
    fun uploadPrepare(token: String, md5:String, fileName:String,  num:Int,  size:Long, bucketId:Int, isZip:Boolean)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.uploadPrepare(token,md5,fileName,num, size, bucketId, isZip)
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
    fun uploadBlock(token:String, partList: List<MultipartBody.Part>)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("UploadBlock","被调1")
            val response = FileNetwork.uploadBlock(token,partList)
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
    fun uploadSingleFile(token: String, partList: List<MultipartBody.Part>)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("Upload","1")
            val response = FileNetwork.uploadSingleFile(token,partList )
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
    fun deleteFile(token: String,bucketId: Int,fileName: String,isForever:Boolean)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.deleteFile(token,bucketId, fileName, isForever)
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
    fun getBucketFiles(token: String,bucketId: Int)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.getBucketFiles(token,bucketId)
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
    fun downloadFile(token: String,bucketId: Int,fileName: String)=liveData(Dispatchers.IO) {
        val result = try {
            Log.d("Download","1")
            val response = FileNetwork.downloadFile(token,bucketId,fileName)
            Log.d("Download","2")
            Result.success(response)
        }catch (e:Exception){
            Log.d("Download","3")
            Result.failure(e)
        }
        emit(result)
    }
    fun fileBackup(token: String,bucketId: Int,fileName: String)=liveData(Dispatchers.IO) {
        val result = try {
            val response = FileNetwork.fileBackup(token,bucketId, fileName)
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
        emit(result)
    }
}