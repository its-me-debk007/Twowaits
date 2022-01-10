package com.example.twowaits

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.twowaits.databinding.ActivityHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        GlobalScope.launch {
            CompanionObjects.accessToken = CompanionObjects.readAccessToken("accessToken").toString()
            CompanionObjects.refreshToken = CompanionObjects.readRefreshToken("refreshToken").toString()
//            Log.d("AccessToken", CompanionObjects.accessToken)
        }

        if (!isConnectedToInternet()){
            Toast.makeText(this@HomeActivity, "No Internet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.SearchIcon)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search Here"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                Toast.makeText(this@HomeActivity, "Looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnectedToInternet(): Boolean{
        var isConnected = false
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    else -> false
                }
            }
        }
        return isConnected
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.ChatIcon -> Toast.makeText(this@HomeActivity, "Look", Toast.LENGTH_SHORT).show()
//        }

//        return super.onOptionsItemSelected(item)
//    }

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
