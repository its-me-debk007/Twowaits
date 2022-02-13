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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.VideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.Exception

@DelicateCoroutinesApi
class VideoPlayer : Fragment() {
    private var _binding: VideoPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoPlayerBinding.inflate(inflater, container, false)
        var downloadId: Long = -1
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        Data.removeActionBarLiveData.postValue(true)
        if (Data.PREV_PAGE_FOR_PLAYER == "DOWNLOADS")
            binding.downloadBtn.hide()

        val uri = if (Data.PREV_PAGE_FOR_PLAYER == "DOWNLOADS") Data.DOWNLOADED_LECTURE.path.toUri() else Data.VIDEO_URI
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
                setTitle(Data.LECTURE_NAME)
                setDescription("Downloading...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Educool Downloads/Lectures/${Data.LECTURE_NAME}.mp4")
                setAllowedOverRoaming(true)
                setAllowedOverMetered(true)
            }
            val downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)
        }
        val broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
                    binding.downloadBtn.isEnabled = true
                }
            }
        }
        activity?.registerReceiver(broadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (Data.PREV_PAGE_FOR_PLAYER) {
                    "DOWNLOADS" -> findNavController().navigate(R.id.action_videoPlayer2_to_downloads)
                    "PROFILE" -> findNavController().navigate(R.id.action_videoPlayer2_to_profile)
                    "HOME" -> findNavController().navigate(R.id.action_videoPlayer2_to_homePage)
                }
                Data.removeActionBarLiveData.postValue(false)
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
            }
        })
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestPermission()
                return false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(context, "Permission denied\nSorry, the file can't be downloaded", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        _binding = null
    }
}