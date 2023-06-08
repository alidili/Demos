package tech.yangle.retrofitcache

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.Locale

/**
 * 安全工具
 * Created by YangLe on 2023/6/8.
 */
object SecurityUtils {

    /**
     * 加密
     *
     * @param content 内容
     * @param flag    标志
     * @return 加密后内容
     */
    fun encryptContent(content: String, flag: String): String {
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            val encryptData = AESUtils.encrypt(content.toByteArray(), key)
            return byteArrayToHexString(encryptData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return content
    }

    /**
     * 解密
     *
     * @param content 内容
     * @param flag    标志
     * @return 解密后内容
     */
    fun decryptContent(content: String, flag: String): String {
        var finalContent = content
        try {
            val key = MessageDigest.getInstance("MD5").digest(flag.toByteArray(charset("UTF-8")))
            var dataLength = 0
            if (finalContent.contains("|")) {
                val contentArray = finalContent.split("\\|".toRegex()).toTypedArray()
                if (contentArray.size == 2) {
                    finalContent = contentArray[0]
                    dataLength = contentArray[1].toInt()
                }
            }
            val encryptData: ByteArray = hexStr2Byte(finalContent)
            val decryptData = AESUtils.decrypt(encryptData, key, dataLength)
            return String(decryptData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return finalContent
    }

    /**
     * 获取MD5
     *
     * @param content 内容
     * @return MD5值
     */
    fun getMD5(content: String): String {
        return byteArrayToHexString(
            MessageDigest.getInstance("MD5").digest(content.toByteArray(charset("UTF-8")))
        ).lowercase()
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hex 十六进制字符串
     * @return byte[]
     */
    private fun hexStr2Byte(hex: String): ByteArray {
        var finalHex = hex
        // 奇数位补0
        if (finalHex.length % 2 != 0) {
            finalHex = "0$finalHex"
        }
        val length = finalHex.length
        val buffer = ByteBuffer.allocate(length / 2)
        var i = 0
        while (i < length) {
            var hexStr = finalHex[i].toString()
            i++
            hexStr += finalHex[i]
            val b = hexStr.toInt(16).toByte()
            buffer.put(b)
            i++
        }
        return buffer.array()
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param array byte[]
     * @return 十六进制字符串
     */
    private fun byteArrayToHexString(array: ByteArray): String {
        val buffer = StringBuffer()
        for (i in array.indices) {
            buffer.append(byteToHex(array[i]))
        }
        return buffer.toString()
    }

    /**
     * byte转十六进制字符
     *
     * @param b byte
     * @return 十六进制字符
     */
    private fun byteToHex(b: Byte): String {
        var hex = Integer.toHexString(b.toInt() and 0xFF)
        if (hex.length == 1) {
            hex = "0$hex"
        }
        return hex.uppercase(Locale.getDefault())
    }
}