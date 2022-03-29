package com.example.twowaits.homePages

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.VideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class VideoPlayer : Fragment(R.layout.video_player) {
    private lateinit var binding: VideoPlayerBinding
    private val previousPage = activity?.intent?.getStringExtra("PREVIOUS PAGE")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = VideoPlayerBinding.bind(view)
        var downloadId: Long = -1
        val lectureName = activity?.intent?.getStringExtra("LECTURE NAME")

        if (previousPage == "DOWNLOADS")
            binding.downloadBtn.hide()
        val uri =
            if (previousPage == "DOWNLOADS") Data.DOWNLOADED_LECTURE.path.toUri() else Data.VIDEO_URI
        val exoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.videoPlayer.player = exoPlayer
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.apply {
            addMediaItem(mediaItem)
            prepare()
            play()
        }

        binding.downloadBtn.setOnClickListener {
            if (checkPermission()) {
                Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
                binding.downloadBtn.isEnabled = false
            }
            val request = DownloadManager.Request(Data.VIDEO_URI)
            request.apply {
                setTitle(lectureName)
                setDescription("Downloading...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "Educool Downloads/Lectures/${lectureName}.mp4"
                )
                setAllowedOverRoaming(true)
                setAllowedOverMetered(true)
            }
            val downloadManager =
                activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)
        }
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
                    binding.downloadBtn.isEnabled = true
                }
            }
        }
        activity?.registerReceiver(
            broadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestPermission()
                return false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return false
            }
        }
        return true
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                val uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivityForResult(intent, 0)
            } catch (e: Exception) {
                e.printStackTrace()
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 0)
            }
        } else {
            val permissionArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(requireActivity(), permissionArray, 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("storagePermission", "granted")
            } else {
                Toast.makeText(
                    context,
                    "Permission denied\nSorry, the file can't be downloaded",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}