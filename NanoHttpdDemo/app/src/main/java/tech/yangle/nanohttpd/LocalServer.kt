package tech.yangle.nanohttpd

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND
import fi.iki.elonen.NanoHTTPD.Response.Status.OK
import java.io.File
import java.io.FileInputStream

/**
 * 本地服务
 * Created by YangLe on 2022/9/2.
 */
class LocalServer private constructor() : NanoHTTPD("127.0.0.1", 8888) {

    // 资源路径
    private var mResourcePath = ""

    companion object {
        private var instance: LocalServer? = null
        private const val TAG = "LocalServer"
        private const val MIME_PLAINTEXT = "text/plain"
        private const val MIME_HTML = "text/html"
        private const val MIME_JS = "application/javascript"
        private const val MIME_CSS = "text/css"
        private const val MIME_PNG = "image/png"
        private const val MIME_JPEG = "image/jpeg"
        private const val MIME_ICON = "image/x-icon"
        private const val MIME_MP4 = "video/mp4"
        private const val MIME_GIF = "image/gif"
        private const val MIME_MP3 = "audio/mpeg"

        fun getInstance(): LocalServer {
            if (instance == null) {
                instance = LocalServer()
            }
            return instance!!
        }
    }

    override fun serve(session: IHTTPSession?): Response {
        if (session == null) {
            return newFixedLengthResponse(NOT_FOUND, MIME_PLAINTEXT, "session == null")
        }
        val url = session.uri
        try {
            val file = File(mResourcePath + url)
            val length = file.length()
            val inputStream = FileInputStream(file)
            if (url.contains(".js")) {
                return newFixedLengthResponse(OK, MIME_JS, inputStream, length)

            } else if (url.contains(".css")) {
                return newFixedLengthResponse(OK, MIME_CSS, inputStream, length)

            } else if (url.contains(".htm") || url.contains(".html")) {
                return newFixedLengthResponse(OK, MIME_HTML, inputStream, length)

            } else if (url.contains(".png")) {
                return newFixedLengthResponse(OK, MIME_PNG, inputStream, length)

            } else if (url.contains(".jpg") || url.contains(".jpeg")) {
                return newFixedLengthResponse(OK, MIME_JPEG, inputStream, length)

            } else if (url.contains(".ico")) {
                return newFixedLengthResponse(OK, MIME_ICON, inputStream, length)

            } else if (url.contains(".gif")) {
                return newFixedLengthResponse(OK, MIME_GIF, inputStream, length)

            } else if (url.contains(".mp3")) {
                return newFixedLengthResponse(OK, MIME_MP3, inputStream, length)

            } else if (url.contains(".mp4")) {
                return newFixedLengthResponse(OK, MIME_MP4, inputStream, length)

            } else if (url.contains(".json")) {
                return newFixedLengthResponse(OK, MIME_PLAINTEXT, inputStream, length)
            } else {
                return newFixedLengthResponse(OK, "*/*", inputStream, length)
            }
        } catch (e: Exception) {
            return newFixedLengthResponse(NOT_FOUND, MIME_PLAINTEXT, "${e.message}")
        }
    }

    /**
     * 启动服务
     */
    fun startServer() {
        try {
            start()
        } catch (e: Exception) {
            Log.e(TAG, "[startServer] Exception:${e.message}")
        }
    }

    /**
     * 停止服务
     */
    fun stopServer() {
        stop()
    }

    /**
     * 设置资源路径
     *
     * @param path 资源路径
     */
    fun setResourcePath(path: String) {
        this.mResourcePath = path
    }
}