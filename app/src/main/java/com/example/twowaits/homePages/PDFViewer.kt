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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.PdfViewerBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PDFViewer : Fragment(R.layout.pdf_viewer) {
    private lateinit var binding: PdfViewerBinding
    private val askPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (!it) Toast.makeText(context, "Please give storage permission", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PdfViewerBinding.bind(view)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        var downloadId: Long = -1
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(
            R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (Data.PREVIOUS_PAGE == "DOWNLOADS") {
            binding.downloadBtn.visibility = View.INVISIBLE
            binding.pdf.fromFile(Data.DOWNLOADED_NOTE).load()
        } else {
            binding.pdf.fromUri(Data.PDF_URI).load()
        }

        binding.downloadBtn.setOnClickListener {
            if (checkPermission()) {
                Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
                binding.downloadBtn.isEnabled = false
            } else return@setOnClickListener
            val request = DownloadManager.Request(Data.PDF_URI)
            request.apply {
                setTitle(Data.NOTE_NAME)
                setDescription("Downloading...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setMimeType("application/pdf")
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "Educool Downloads/Notes/${Data.NOTE_NAME}.pdf")
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
        activity?.registerReceiver(broadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (Data.PREVIOUS_PAGE) {
                    "DOWNLOADS" -> findNavController().navigate(R.id.action_PDFViewer_to_downloads)
                    "BOOKMARK" -> findNavController().navigate(R.id.action_PDFViewer_to_library)
                    "HOME" -> findNavController().navigate(R.id.action_PDFViewer_to_homePage)
                }
                val bottomNavigationView =
                    activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
                val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                val actionBar = (activity as AppCompatActivity).supportActionBar
                actionBar?.show()
            }
        })
    }
}
