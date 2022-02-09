package com.example.twowaits

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.databinding.ActivityHomeBinding
import com.example.twowaits.databinding.NoInternetDialogBinding
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var dialog: Dialog
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        getProfile()
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Are you sure want to logout?")
                        .setIcon(R.drawable.exit_warning)
                        .setPositiveButton("Yes") { _, _ ->
                            lifecycleScope.launch {
                                Data.saveData("log_in_status", "false")
                            }
                            Data.ACCESS_TOKEN = null
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("No") { _, _ -> }
                        .create()
                        .show()
                }
                R.id.aboutEduCool -> {
                    val navController =
                        Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.aboutEduCool)
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.privacyPolicy2 -> {
                    val navController =
                        Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.privacyPolicy2)
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.changePassword2 -> {
                    val navController =
                        Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.changePassword2)
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.feedback2 -> {
                    val navController =
                        Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.feedback2)
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
        dialog = Dialog(this)
        dialog.setContentView(NoInternetDialogBinding.inflate(layoutInflater).root)
        dialog.setCancelable(false)
//        isConnectedToInternet()
        Data.removeActionBarLiveData.observe(this) {
            if (it) supportActionBar?.hide() else supportActionBar?.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun isConnectedToInternet() {
        val checkNetworkConnection = IsConnected(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (isConnected) {
                dialog.hide()
            } else {
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chatList -> {
                binding.bottomNavigationView.visibility = View.INVISIBLE
                val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                navController.navigate(R.id.chatList)
                item.icon = ContextCompat.getDrawable(this, R.drawable.chat_icon_active)
            }
            R.id.SearchIcon -> {
                Data.isSearchBarOpen = !Data.isSearchBarOpen
                Data.isSearchBarActiveLiveData.postValue(Data.isSearchBarOpen)
            }
        }
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    private fun getProfile() {
        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
        val navHeader = binding.navigationView.getHeaderView(0)
        if (Data.USER == "FACULTY") {
            viewModel.profileDetailsFaculty()
            viewModel.profileFacultyLiveData.observe(this) {
                navHeader.findViewById<ShapeableImageView>(R.id.shapeableImageView)
                    .load(it.profile_pic_firebase) {
                        transformations(CircleCropTransformation())
                    }
                navHeader.findViewById<TextView>(R.id.navHeaderName).text = it.name
                navHeader.findViewById<TextView>(R.id.navHeaderEmail).text =
                    Data.USER_EMAIL
            }
            viewModel.errorFacultyLiveData.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        } else {
            viewModel.profileDetailsStudent()
            viewModel.profileStudentLiveData.observe(this) {
                navHeader.findViewById<ShapeableImageView>(R.id.shapeableImageView)
                    .load(it.profile_pic_firebase) {
                        transformations(CircleCropTransformation())
                    }
                navHeader.findViewById<TextView>(R.id.navHeaderName).text = it.name
                navHeader.findViewById<TextView>(R.id.navHeaderEmail).text =
                    Data.USER_EMAIL
            }
            viewModel.errorStudentLiveData.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
