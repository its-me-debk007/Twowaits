package com.example.twowaits.ui.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.VideoPlayerBinding
import com.example.twowaits.utils.Utils
import com.example.twowaits.utils.downloadImg
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class VideoPlayer : Fragment(R.layout.video_player) {
    private lateinit var binding: VideoPlayerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = VideoPlayerBinding.bind(view)
        val previousPage = activity?.intent?.getStringExtra("PREVIOUS PAGE")
        val lectureName = activity?.intent?.getStringExtra("LECTURE NAME")

        if (previousPage == "DOWNLOADS") binding.downloadBtn.visibility = View.GONE
        val uri =
            if (previousPage == "DOWNLOADS") Utils.DOWNLOADED_LECTURE.path.toUri() else Utils.VIDEO_URI.toUri()

        val exoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.videoPlayer.player = exoPlayer
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.apply {
            addMediaItem(mediaItem)
            prepare()
            play()
        }

        binding.downloadBtn.setOnClickListener {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            binding.downloadBtn.isEnabled = false

            downloadImg(
                requireContext(), Utils.VIDEO_URI,
                "${context?.filesDir}/Downloaded Lectures/", "${lectureName}.mp4"
            )
        }
    }
}