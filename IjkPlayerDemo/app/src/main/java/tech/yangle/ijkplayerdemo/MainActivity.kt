package tech.yangle.ijkplayerdemo

import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.yangle.ijkplayerdemo.databinding.ActivityMainBinding
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * IjkPlayer视频播放器
 * <p>
 * Created by yangle on 2024/7/18.
 * Website：http://www.yangle.tech
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        val ijkMediaPlayer = IjkMediaPlayer()
        binding.svVideo.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                ijkMediaPlayer.setDisplay(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {

            }
        })
        ijkMediaPlayer.setDataSource("http://vjs.zencdn.net/v/oceans.mp4")
        ijkMediaPlayer.prepareAsync()
        ijkMediaPlayer.start()
    }
}