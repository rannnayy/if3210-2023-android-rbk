package com.example.majika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.majika.retrofit.MajikaAPI
import com.example.majika.retrofit.MenuData
import com.example.majika.retrofit.RetrofitClient
import com.example.majika.room.CartDatabase
import com.example.majika.room.CartViewModel
import com.example.majika.room.CartViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    lateinit var cartDatabase: CartDatabase
    lateinit var cartViewModel: CartViewModel

    lateinit var results: List<MenuData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cartDatabase = CartDatabase.getDatabaseClient(this@MainActivity)
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartDatabase)).get(
            CartViewModel::class.java)

        val quotesApi = RetrofitClient.getInstance().create(MajikaAPI::class.java)

        GlobalScope.launch {
            val result = quotesApi.getMenu()
            if (result != null) {
                results = result.body()!!.data

                for (res in results) {
                    var temp = cartViewModel.getCartofDetails(
                        name = res.name,
                        description = res.description,
                        currency = res.currency,
                        price = res.price,
                        type = res.type
                    )
                    if (temp == null) {
                        cartViewModel.insertData(
                            name = res.name,
                            description = res.description,
                            currency = res.currency,
                            price = res.price,
                            sold = res.sold,
                            type = res.type,
                            added = 0
                        )
                    }
                }
            }
        }

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