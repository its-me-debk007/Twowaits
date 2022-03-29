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
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.PdfViewerBinding

class PDFViewer : Fragment(R.layout.pdf_viewer) {
    private lateinit var binding: PdfViewerBinding
    private val askPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) Toast.makeText(context, "Please give storage permission", Toast.LENGTH_SHORT)
                .show() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PdfViewerBinding.bind(view)
        var downloadId: Long = -1
        val noteName = activity?.intent?.getStringExtra("NOTE NAME")
        val previousPage = activity?.intent?.getStringExtra("PREVIOUS PAGE")

        if (previousPage == "DOWNLOADS") {
            binding.downloadBtn.visibility = View.INVISIBLE
            binding.pdf.fromFile(Data.DOWNLOADED_NOTE).load()
        }

        binding.downloadBtn.setOnClickListener {
            if (checkPermission()) {
                Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
                binding.downloadBtn.isEnabled = false
            } else return@setOnClickListener
            val request = DownloadManager.Request(Data.PDF_URI)
            request.apply {
                setTitle(noteName)
                setDescription("Downloading...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setMimeType("application/pdf")
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "Educool Downloads/Notes/${noteName}.pdf")
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
                askPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermission() {
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("StoragePermission", "granted")
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
