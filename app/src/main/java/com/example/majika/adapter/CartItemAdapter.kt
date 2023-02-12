package com.example.majika.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Cart
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.model.Price

class CartItemAdapter (private val context: Context, private val carts: List<CartRecyclerViewItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_CART = 1
        const val VIEW_PRICE = 2
    }
    override fun getItemViewType(position: Int): Int {
        if (carts[position] is Cart) {
            return VIEW_CART
        } else {
            return VIEW_PRICE
        }
    }
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cartNameText: TextView = view.findViewById(R.id.item_name_cart)
        val cartPriceText: TextView = view.findViewById(R.id.item_price_cart)
        val cartCard: CardView = view.findViewById(R.id.cardViewCart)
        val cartBtPlus: Button = view.findViewById(R.id.item_bt_cart_plus)
        val cartBuyText: TextView = view.findViewById(R.id.item_num_buy_cart)
        val cartBtMin: Button = view.findViewById(R.id.item_bt_cart_min)
        fun bind(item: Cart) {
            cartNameText.text = item.nameCart.toString()
            cartPriceText.text = item.priceCart.toString()
            cartBuyText.text = item.numBuyCart.toString()
            cartBtPlus.setOnClickListener{v: View ->
                Toast.makeText(v.context, "Adding 1 "+item.nameCart, Toast.LENGTH_SHORT).show()
                cartBuyText.text = (item.numBuyCart.toInt()+1).toString()
            }
            cartBtMin.setOnClickListener{v: View ->
                Toast.makeText(v.context, "Removing 1 "+item.nameCart, Toast.LENGTH_SHORT).show()
                cartBuyText.text = (item.numBuyCart.toInt()-1).toString()
            }
        }
    }
    class PriceViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val priceTotalText: TextView = view.findViewById(R.id.price_total_text)
        val priceBtToPay: Button = view.findViewById(R.id.price_bt_to_pay)
        fun bind(item: Price) {
            priceTotalText.text = item.price.toString()
            priceBtToPay.setOnClickListener{v: View ->
                Toast.makeText(v.context, "Paid "+item.price.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_CART) {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_cart_model, parent, false))
        } else {
            return PriceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_price_model, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = carts[position]
        if (holder is ItemViewHolder && item is Cart) {
            holder.bind(item)
        }
        if (holder is PriceViewHolder && item is Price) {
            holder.bind(item)
        }
    }
}