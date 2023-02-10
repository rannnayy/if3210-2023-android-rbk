package com.example.majika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Cart

class CartItemAdapter (private val carts: List<Cart>) : RecyclerView.Adapter<CartItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cartNameText: TextView = view.findViewById(R.id.item_name_cart)
        val cartPriceText: TextView = view.findViewById(R.id.item_price_cart)
        val cartCard: CardView = view.findViewById(R.id.cardViewCart)
        val cartBtPlus: Button = view.findViewById(R.id.item_bt_cart_plus)
        val cartBuyText: TextView = view.findViewById(R.id.item_num_buy_cart)
        val cartBtMin: Button = view.findViewById(R.id.item_bt_cart_min)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_cart_model, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.cartNameText.text = carts[position].nameCart
        holder.cartPriceText.text = carts[position].priceCart.toString()
        holder.cartBuyText.text = carts[position].numBuyCart.toString()
        holder.cartBtPlus.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Adding 1 "+carts[position].nameCart, Toast.LENGTH_SHORT).show()
            holder.cartBuyText.text = (carts[position].numBuyCart.toInt()+1).toString()
        }
        holder.cartBtMin.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Removing 1 "+carts[position].nameCart, Toast.LENGTH_SHORT).show()
            holder.cartBuyText.text = (carts[position].numBuyCart.toInt()-1).toString()
        }

    }
}