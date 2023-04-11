package com.benxinm.localoss.ui.util

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.math.BigInteger
import java.security.MessageDigest

object Utils {
    var ip:String=""
    /**
     * @param offset 偏移量
     * @param file  分块文件
     * @param blockSize 每块的大小
     * @return  这一片的数据
     */
    fun getBlock(offset: Long, file: File, blockSize: Int): ByteArray? {
        val result = ByteArray(blockSize)
        try {
            RandomAccessFile(file, "r").use { accessFile ->
                accessFile.seek(offset)
                return when (val readSize: Int = accessFile.read(result)) {
                    -1 -> {
                        null
                    }
                    blockSize -> {
                        result
                    }
                    else -> {
                        val byteArray = ByteArray(readSize)
                        System.arraycopy(result, 0, byteArray, 0, readSize)
                        byteArray
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun getFileMD5(file: File): String? {
        if (!file.isFile) {
            return null
        }
        var digest: MessageDigest? = null
        var `in`: FileInputStream? = null
        val buffer = ByteArray(1024)
        var len: Int
        try {
            digest = MessageDigest.getInstance("MD5")
            `in` = FileInputStream(file)
            while (`in`.read(buffer, 0, 1024).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            `in`.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return BigInteger(bytesToHexString(digest.digest()), 16).toString()
    }
    private fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder("")
        if (src == null || src.isEmpty()) {
            return null
        }
        for (i in src.indices) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }
}