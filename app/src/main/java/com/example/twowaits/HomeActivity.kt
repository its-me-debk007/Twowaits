package com.example.twowaits

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twowaits.databinding.ActivityHomeBinding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.homePages.HomePage
import com.example.twowaits.homePages.chats.ChatList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
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


        setSupportActionBar(binding.actionBar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.aboutUs -> {

                }
                R.id.privacyPolicy -> {

                }
                R.id.feedback -> {

                }
                R.id.changePassword -> {

                }
                R.id.logout -> {
                    lifecycleScope.launch {
                        CompanionObjects.saveLoginStatus("log_in_status", "false")
                    }
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

        GlobalScope.launch {
            CompanionObjects.ACCESS_TOKEN =
                CompanionObjects.readAccessToken("accessToken").toString()
            CompanionObjects.REFRESH_TOKEN =
                CompanionObjects.readRefreshToken("refreshToken").toString()
        }
        dialog = Dialog(this)
        dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
        dialog.setCancelable(false)
        isConnectedToInternet()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

//        val searchItem = menu?.findItem(R.id.SearchIcon)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.queryHint = "Search Here"
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                Toast.makeText(this@HomeActivity, "Looking for $query", Toast.LENGTH_SHORT).show()
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })

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
//                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, ChatList()).commit()
                val navGraph = navController.graph
                navGraph.startDestination = R.id.chatList
                navController.graph = navGraph
            }
            R.id.SearchIcon -> {

            }
        }
        if (toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }

//    override fun onBackPressed() {
//        AlertDialog.Builder(this@HomeActivity)
//            .setTitle("Exit")
//            .setMessage("Are you sure you want to exit?")
//            .setIcon(R.drawable.exit_warning)
//            .setPositiveButton("Yes", ){
//                    _, _ -> finish()
//            }
//            .setNegativeButton("No"){
//                    _, _ ->
//            }
//            .create()
//            .show()
//    }
}
