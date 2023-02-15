package com.example.majika

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.adapter.CartItemAdapter
import com.example.majika.data.CartDatasource
import com.example.majika.model.CartRecyclerViewItem

class ShoppingCartFragment : Fragment() {
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartsList: List<CartRecyclerViewItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Shopping Cart"
        toolbarMajikaText.setText(toolbarMajika.title)
        (activity as AppCompatActivity).setSupportActionBar(toolbarMajika)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        cartsList = CartDatasource().loadList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.CartRecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CartItemAdapter(view.context, cartsList) {Int ->
            val pay = PaymentFragment.newInstance(Int)
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.mainContainer, pay)?.commit()
        }

        return view
    }

    companion object {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}