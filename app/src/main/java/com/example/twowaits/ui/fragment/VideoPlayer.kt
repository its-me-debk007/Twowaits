package com.example.twowaits.ui.fragment

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.example.twowaits.R
import com.example.twowaits.databinding.VideoPlayerBinding
import com.example.twowaits.util.DownloaderInterface
import com.example.twowaits.util.Utils
import com.example.twowaits.util.downloadImg
import com.example.twowaits.util.hideSystemUi

class VideoPlayer : Fragment(R.layout.video_player), DownloaderInterface {
    private lateinit var binding: VideoPlayerBinding
    private var player: ExoPlayer? = null
    private val previousPage by lazy { activity?.intent?.getStringExtra("PREVIOUS PAGE") }
    private val lectureName by lazy { activity?.intent?.getStringExtra("LECTURE NAME") }
    val uri by lazy { if (previousPage == "DOWNLOADS") Utils.DOWNLOADED_LECTURE.path.toUri() else Utils.VIDEO_URI.toUri() }
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        binding = VideoPlayerBinding.bind(view)
        if (previousPage == "DOWNLOADS") binding.downloadBtn.visibility = View.GONE

        binding.downloadBtn.setOnClickListener {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            binding.downloadBtn.hide()

            downloadImg(
                requireContext(), Utils.VIDEO_URI,
                "${context?.filesDir}/Downloaded Lectures/", "${lectureName}.mp4",
                this
            )
        }
    }

    private fun initializePlayer(uri: Uri) {
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(uri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentItem, playbackPosition)
                exoPlayer.prepare()
            }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer(uri)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.hideSystemUi(binding.videoView)
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(uri)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

    override fun onDownloadError() {
        binding.downloadBtn.show()
    }
}