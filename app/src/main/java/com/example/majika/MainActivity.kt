package com.example.majika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.majika.retrofit.BranchApi
import com.example.majika.retrofit.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarMajika = findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Twibbon"
        toolbarMajikaText.setText(toolbarMajika.title)
        setSupportActionBar(toolbarMajika)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        navController = navHostFragment.navController

        setupWithNavController(bottomNavigationView, navController)

        val quotesApi = RetrofitClient.getInstance().create(BranchApi::class.java)

        GlobalScope.launch {
            val result = quotesApi.getBranches()
            if (result != null){
                Log.d("HASIL: ", result.body()?.data.toString())
            }
        }
    }
}