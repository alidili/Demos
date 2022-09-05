package tech.yangle.nanohttpd

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 文件工具
 * Created by YangLe on 2022/9/5.
 */
object FileUtils {

    /**
     * 将asset文件写入本地
     *
     * @param context    Context
     * @param filePath   文件路径
     * @param fileName   文件名
     * @return true：成功 false：失败
     */
    fun copyAssetAndWrite(
        context: Context,
        filePath: String,
        fileName: String
    ): Boolean {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            val fileDir = File(filePath)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val outFile = File(fileDir, fileName)
            if (outFile.exists()) {
                outFile.delete()
            }
            val res = outFile.createNewFile()
            if (!res) {
                return false
            }
            inputStream = context.assets.open(fileName)
            outputStream = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (inputStream.read(buffer).also { byteCount = it } != -1) {
                outputStream.write(buffer, 0, byteCount)
            }
            outputStream.flush()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 获取外部私有File目录
     *
     * @param context Context
     * @param type    子目录
     * @return 外部私有File目录
     */
    fun getExternalFilesDir(context: Context, type: String): String {
        return context.getExternalFilesDir(type)?.absolutePath
            ?: context.externalCacheDir?.absolutePath
            ?: context.cacheDir.absolutePath + "/" + type
    }
}