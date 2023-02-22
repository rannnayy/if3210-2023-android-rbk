package com.example.majika

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.MenuItemAdapter
import com.example.majika.data.MenuDatasource
import com.example.majika.model.CartModel
import com.example.majika.model.Menu
import com.example.majika.model.MenuRecyclerViewItem
import com.example.majika.retrofit.MajikaAPI
import com.example.majika.retrofit.MenuData
import com.example.majika.retrofit.RetrofitClient
import com.example.majika.room.CartDatabase
import com.example.majika.room.CartViewModel
import com.example.majika.room.CartViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class FoodBankFragment : Fragment() {
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var menusList: List<MenuRecyclerViewItem>
    private var menuds: MenuDatasource = MenuDatasource()
    private lateinit var menusListName: List<String>

    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchedMenu: ArrayList<MenuRecyclerViewItem>
    private lateinit var searchedMenuName: ArrayList<String>

    lateinit var cartDatabase: CartDatabase
    lateinit var cartViewModel: CartViewModel

    companion object {
        fun newInstance() = FoodBankFragment().apply {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_bank, container, false)

        cartDatabase = CartDatabase.getDatabaseClient(this.requireContext())
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartDatabase)).get(CartViewModel::class.java)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Food Bank"
        toolbarMajikaText.setText(toolbarMajika.title)
        (activity as AppCompatActivity).setSupportActionBar(toolbarMajika)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        searchView = view.findViewById(R.id.search)

        val quotesApi = RetrofitClient.getInstance().create(MajikaAPI::class.java)

        GlobalScope.launch {
            menuds.fillList(listOf(CartModel(name = "Makanan", description = "", currency = "", price = 0, sold = 0, type = "text", added = 0)))
            var foodResult = cartViewModel.getFood()!!
            for (res in foodResult) {
                foodResult += CartModel(
                    name = res.name, description = res.description, currency = res.currency, price = res.price, sold = res.sold, type = res.type, added = res.added
                )
            }
            menuds.fillList(foodResult)
            menuds.fillList(listOf(CartModel(name = "Minuman", description = "", currency = "", price = 0, sold = 0, type = "text", added = 0)))
            var drinkResult = cartViewModel.getDrink()!!
            for (res in drinkResult) {
                drinkResult += CartModel(
                    name = res.name, description = res.description, currency = res.currency, price = res.price, sold = res.sold, type = res.type, added = res.added
                )
            }
            menuds.fillList(foodResult)
            withContext(Dispatchers.Main) {
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.FoodBankRecyclerView)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)

                menusList = arrayListOf<MenuRecyclerViewItem>()
                menusListName = arrayListOf<String>()
                searchedMenu = arrayListOf<MenuRecyclerViewItem>()
                searchedMenuName = arrayListOf<String>()
                getData()

                searchView.clearFocus()
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String): Boolean {
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(p0: String): Boolean {
                        searchedMenu.clear()
                        searchedMenuName.clear()
                        val searchText = p0!!.toLowerCase(Locale.getDefault())
                        if (searchText.isNotEmpty()) {
                            for (i in (0..menusList.size-1)) {
                                if ((menusListName[i] != "Makanan" || menusListName[i] != "Minuman") && menusListName[i].toLowerCase(Locale.getDefault()).contains(searchText)) {
                                    searchedMenu.add(menusList[i])
                                    searchedMenuName.add(menusListName[i])
                                }
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        } else {
                            searchedMenu.clear()
                            searchedMenuName.clear()
                            searchedMenu.addAll(menusList)
                            searchedMenuName.addAll(menusListName)
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                        return false
                    }
                })
            }
        }

        return view
    }

    private fun getData() {
        menusList = menuds.loadList()
        menusListName = menuds.loadName()
        searchedMenu.addAll(menusList)
        searchedMenuName.addAll(menusListName)
        recyclerView.adapter = MenuItemAdapter(menusList, cartViewModel)
    }
}