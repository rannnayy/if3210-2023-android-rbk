package com.example.majika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.CartItemAdapter
import com.example.majika.data.CartDatasource
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.room.CartDatabase
import com.example.majika.room.CartViewModel
import com.example.majika.room.CartViewModelFactory
import kotlinx.coroutines.*

class ShoppingCartFragment : Fragment() {
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private lateinit var recyclerView: RecyclerView
    private var cartsList: List<CartRecyclerViewItem> = listOf<CartRecyclerViewItem>()
    private var cartsds: CartDatasource = CartDatasource()

    lateinit var cartDatabase: CartDatabase
    lateinit var cartViewModel: CartViewModel

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
        toolbarMajikaText.setText("Shopping Cart")
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        GlobalScope.launch {
            cartsds.fillList(cartViewModel.getBoughtCart()!!)
            withContext(Dispatchers.Main) {
                val layoutManager = LinearLayoutManager(context)
                recyclerView = view.findViewById(R.id.CartRecyclerView)
                recyclerView.layoutManager = layoutManager
                recyclerView.setHasFixedSize(true)
                getData()
            }
        }

        return view
    }

    private fun getData() {
        cartsList = cartsds.loadList()
        recyclerView.adapter = CartItemAdapter(requireContext(), cartsList, cartViewModel) { String ->
            val pay = PaymentFragment.newInstance(String)
            val transaction = fragmentManager?.beginTransaction()?.setReorderingAllowed(true)
            transaction?.replace(R.id.mainContainer, pay)?.addToBackStack("prevcart")?.commit().toString()
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}