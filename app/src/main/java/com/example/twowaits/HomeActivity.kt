package com.example.twowaits

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.twowaits.databinding.ActivityHomeBinding
import com.example.twowaits.databinding.NoInternetDialogBinding
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController)

        setSupportActionBar(binding.actionBar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val navHeader = binding.navigationView.getHeaderView(0)
        navHeader.findViewById<TextView>(R.id.navHeaderName).text = "debashish.joy@gmail.com"

        val currentFragment = navController.currentDestination?.label
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("Exit")
                        .setMessage("Are you sure want to logout?")
                        .setIcon(R.drawable.exit_warning)
                        .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        CompanionObjects.saveData("log_in_status", "false")
                    }
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                    }
                        .setNegativeButton("No") { _, _ -> }
                        .create()
                        .show()
                }
                R.id.aboutEduCool -> {
                    val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.aboutEduCool)
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.privacyPolicy2 -> {
                    val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.privacyPolicy2)
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.changePassword2 -> {
                    val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.changePassword2)
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                    binding.drawerLayout.closeDrawers()
                }
                R.id.feedback2 -> {
                    val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                    navController.navigate(R.id.feedback2)
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
        dialog = Dialog(this)
        dialog.setContentView(NoInternetDialogBinding.inflate(layoutInflater).root)
        dialog.setCancelable(false)
        isConnectedToInternet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun isConnectedToInternet() {
        val checkNetworkConnection = IsConnected(application)
        checkNetworkConnection.observe(this, { isConnected ->
            if (isConnected) {
                dialog.hide()
            } else {
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
        })
    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun isConnectedToInternet(): Boolean{
//        var isConnected = false
//        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.let {
//            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
//                isConnected = when {
//                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                    else -> false
//                }
//            }
//        }
//        return isConnected
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chatList -> {
                binding.bottomNavigationView.visibility = View.INVISIBLE
//                val navGraph = navController.graph
//                navGraph.startDestination = R.id.chatList
//                navController.graph = navGraph
                val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
                navController.navigate(R.id.chatList)
            }
            R.id.SearchIcon -> {
                Log.d("KKKK", "CLicked")
                CompanionObjects.isSearchBarOpen = !CompanionObjects.isSearchBarOpen
                CompanionObjects.isSearchBarActiveLiveData.postValue(CompanionObjects.isSearchBarOpen)
            }
        }
        if (toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }


}
