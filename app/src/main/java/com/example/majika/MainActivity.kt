package com.example.majika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

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
    }
}