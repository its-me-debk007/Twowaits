package com.example.twowaits.ui.activity.home

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.example.twowaits.R
import com.example.twowaits.connectivity.ConnectivityLiveData
import com.example.twowaits.databinding.ActivityHomeBinding
import com.example.twowaits.databinding.NoInternetDialogBinding
import com.example.twowaits.ui.activity.auth.AuthActivity
import com.example.twowaits.utils.Datastore
import com.example.twowaits.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.io.File

@DelicateCoroutinesApi
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var dialog: Dialog
    private lateinit var toggle: ActionBarDrawerToggle
    private val dataStore by lazy { Datastore(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val exit = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            addTarget(android.R.id.content)
        }
        window.exitTransition = exit
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityHomeActionBar)
        supportActionBar?.title = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.findNavController()

        setupWithNavController(binding.bottomNavigationView, navController)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        isConnectedToInternet()
        navDrawerItemClicks()
        dialog = Dialog(this).apply {
            setContentView(NoInternetDialogBinding.inflate(layoutInflater).root)
            setCancelable(false)
        }

//        Utils().saveAccessTokenLiveData.observe(this) {
//            lifecycleScope.launch {
//                dataStore.saveTokens("accessToken", it)
//            }
//        }
    }

    private fun navDrawerItemClicks() {
        binding.navigationView.setNavigationItemSelectedListener {
            val intent = Intent(this, NavDrawerActivity::class.java)
            binding.drawerLayout.closeDrawers()
            when (it.itemId) {
                R.id.logout -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logoutConfirmation)
                        .setIcon(R.drawable.exit_warning)
                        .setPositiveButton("Logout") { _, _ ->
                            lifecycleScope.launch {
                                dataStore.saveLoginData("")
                            }
                            Utils.ACCESS_TOKEN = null
                            startActivity(Intent(this, AuthActivity::class.java))
                            finish()
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .setBackground(ContextCompat.getDrawable(this, R.drawable.exit_dialog))
                        .show()
                }
                R.id.aboutEduCool ->
                    intent.putExtra("navDrawerFragment", "About us")

                R.id.privacyPolicy2 ->
                    intent.putExtra("navDrawerFragment", "Privacy Policy")

                R.id.changePassword2 ->
                    intent.putExtra("navDrawerFragment", "Change Password")

                R.id.feedback2 ->
                    intent.putExtra("navDrawerFragment", "Feedback")
            }
            if (it.itemId != R.id.logout) startActivity(intent)

            true
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    private fun isConnectedToInternet() {
        val checkNetworkConnection = ConnectivityLiveData(this)
        checkNetworkConnection.observe(this) {
            if (it) dialog.hide()
            else {
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.chatList) {
            binding.bottomNavigationView.visibility = View.INVISIBLE
            val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
            navController.navigate(R.id.chatList)
            item.icon = ContextCompat.getDrawable(this, R.drawable.chat_icon_active)
        }
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.closeDrawers()
        else super.onBackPressed()
    }

    fun showProfileInNavDrawer() {
        val navHeader = binding.navigationView.getHeaderView(0)
        val profilePic = navHeader.findViewById<ShapeableImageView>(R.id.shapeableImageView)
        val name = navHeader.findViewById<MaterialTextView>(R.id.navHeaderName)
        val email = navHeader.findViewById<MaterialTextView>(R.id.navHeaderEmail)

        lifecycleScope.launch {
            email.text = dataStore.readUserDetails("EMAIL")
            name.text = dataStore.readUserDetails("NAME")
        }

        val file = File(getExternalFilesDir(null), "Profile/profile_pic.jpg")
        val path = if (file.exists()) file.absolutePath
        else {

        }

        Glide.with(this).load(path).into(profilePic)
    }
}