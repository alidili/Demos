package tech.yangle.retrofitcache

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * AES工具
 * Created by YangLe on 2023/6/8.
 */
object AESUtils {

    /**
     * AES加密
     *
     * @param data 将要加密的内容
     * @param key  密钥
     * @return 已经加密的内容
     */
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        var finalData = data
        // 不足16字节，补齐内容为0
        val len = 16 - finalData.size % 16
        for (i in 0 until len) {
            val bytes = byteArrayOf(0.toByte())
            finalData = concatArray(finalData, bytes)
        }
        try {
            val secretKeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            return cipher.doFinal(finalData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * AES解密
     *
     * @param data 将要解密的内容
     * @param key  密钥
     * @return 已经解密的内容
     */
    fun decrypt(data: ByteArray, key: ByteArray): ByteArray {
        try {
            val secretKeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            return noPadding(cipher.doFinal(data), 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * AES解密
     *
     * @param data   将要解密的内容
     * @param key    密钥
     * @param length 数据长度
     * @return 已经解密的内容
     */
    fun decrypt(data: ByteArray, key: ByteArray, length: Int): ByteArray {
        try {
            val secretKeySpec = SecretKeySpec(key, "AES")
            val cipher = Cipher.getInstance("AES/ECB/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            return noPadding(cipher.doFinal(data), length)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    private fun concatArray(firstArray: ByteArray, secondArray: ByteArray): ByteArray {
        val bytes = ByteArray(firstArray.size + secondArray.size)
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.size)
        System.arraycopy(secondArray, 0, bytes, firstArray.size, secondArray.size)
        return bytes
    }

    /**
     * 去除数组中的补齐
     *
     * @param paddingBytes 源数组
     * @param dataLength   去除补齐后的数据长度
     * @return 去除补齐后的数组
     */
    private fun noPadding(paddingBytes: ByteArray, dataLength: Int): ByteArray {
        val noPaddingBytes: ByteArray
        if (dataLength > 0) {
            if (paddingBytes.size > dataLength) {
                noPaddingBytes = ByteArray(dataLength)
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, dataLength)
            } else {
                noPaddingBytes = paddingBytes
            }
        } else {
            val index = paddingIndex(paddingBytes)
            if (index > 0) {
                noPaddingBytes = ByteArray(index)
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, index)
            } else {
                noPaddingBytes = paddingBytes
            }
        }
        return noPaddingBytes
    }

    /**
     * 获取补齐的位置
     *
     * @param paddingBytes 源数组
     * @return 补齐的位置
     */
    private fun paddingIndex(paddingBytes: ByteArray): Int {
        for (i in paddingBytes.indices.reversed()) {
            if (paddingBytes[i].toInt() != 0) {
                return i + 1
            }
        }
        return -1
    }
}