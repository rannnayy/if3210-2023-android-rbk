package com.example.majika

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.CartItemAdapter
import com.example.majika.adapter.MenuItemAdapter
import com.example.majika.data.CartDatasource
import com.example.majika.model.CartModel
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.model.Menu
import com.example.majika.room.CartDatabase
import com.example.majika.room.CartViewModel
import com.example.majika.room.CartViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class ShoppingCartFragment : Fragment() {
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private lateinit var recyclerView: RecyclerView
    private var cartsList: List<CartRecyclerViewItem> = listOf<CartRecyclerViewItem>()
    private var cartsds: CartDatasource = CartDatasource()

    lateinit var cartDatabase: CartDatabase
    lateinit var cartViewModel: CartViewModel
    lateinit var cartBoughtLiveData: LiveData<List<CartModel>>
    lateinit var cartBought: List<CartModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        cartDatabase = CartDatabase.getDatabaseClient(this.requireContext())
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartDatabase)).get(CartViewModel::class.java)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Shopping Cart"
        toolbarMajikaText.setText(toolbarMajika.title)
        (activity as AppCompatActivity).setSupportActionBar(toolbarMajika)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.CartRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        getData()

        cartViewModel.getBoughtCart()!!.observe(viewLifecycleOwner, Observer{
            cartsds.fillList(it)
            getData()
            recyclerView.adapter!!.notifyDataSetChanged()
        })
        recyclerView.adapter!!.notifyDataSetChanged()

        return view
    }

    private fun getData() {
        cartsList = cartsds.loadList()
        recyclerView.adapter = CartItemAdapter(cartsList, cartViewModel) {Int ->
            val pay = PaymentFragment.newInstance(Int)
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.mainContainer, pay)?.commit()
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}