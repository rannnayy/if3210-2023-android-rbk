package com.example.majika.adapter

import android.util.Log
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
import com.example.majika.model.CartModel
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.model.Price
import com.example.majika.room.CartViewModel

class CartItemAdapter(private var carts: List<CartRecyclerViewItem>, private val cartViewModel: CartViewModel, private val onBtPayClicked: (String) -> String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var curr: String

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
        fun bind(item: Cart, cartViewModel: CartViewModel) {
            cartNameText.text = item.nameCart
            cartPriceText.text = item.priceCart.toString()
            cartBuyText.text = item.numBuyCart.toString()
            cartBtPlus.setOnClickListener{v: View ->
                Toast.makeText(v.context, "Adding 1 "+item.nameCart, Toast.LENGTH_SHORT).show()
                Thread {
                    cartViewModel.addItem(
                        cartViewModel.getCartofDetails(
                            name = item.nameCart,
                            description = item.descCart,
                            currency = item.currencyCart,
                            price = item.priceCart,
                            type = item.typeCart
                        )
                    )
                }.start()
                item.numBuyCart += 1
                cartBuyText.text = item.numBuyCart.toString()
            }
            cartBtMin.setOnClickListener{v: View ->
                if (item.numBuyCart > 0) {
                    Toast.makeText(v.context, "Removing 1 " + item.nameCart, Toast.LENGTH_SHORT).show()
                    Thread {
                        cartViewModel.decreaseItem(
                            cartViewModel.getCartofDetails(
                                name = item.nameCart,
                                description = item.descCart,
                                currency = item.currencyCart,
                                price = item.priceCart,
                                type = item.typeCart
                            )
                        )
                    }.start()
                    item.numBuyCart -= 1
                    cartBuyText.text = item.numBuyCart.toString()
                }
                else {
                    Toast.makeText(
                        v.context,
                        "Removing " + item.nameCart + " from cart.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Thread {
                        cartViewModel.decreaseItem(
                            cartViewModel.getCartofDetails(
                                name = item.nameCart,
                                description = item.descCart,
                                currency = item.currencyCart,
                                price = item.priceCart,
                                type = item.typeCart
                            )
                        )
                    }.start()
                    cartCard.visibility = View.INVISIBLE
                }
            }
        }
    }
    class PriceViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val priceTotalText: TextView = view.findViewById(R.id.price_total_text)
        val priceBtToPay: Button = view.findViewById(R.id.price_bt_to_pay)
        fun bind(item: Price) {
            priceTotalText.text = item.price.toString()
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
        var item = carts[position]
        if (holder is ItemViewHolder && item is Cart) {
            curr = item.currencyCart
            holder.bind(item, cartViewModel)
        }
        if (holder is PriceViewHolder && item is Price) {
            holder.bind(item)
            holder.priceBtToPay.setOnClickListener{
                onBtPayClicked(curr + " " + item.price.toString())
            }
        }
    }
}