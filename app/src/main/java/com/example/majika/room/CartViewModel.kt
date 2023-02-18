package com.example.majika.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.majika.model.CartModel

class CartViewModel: ViewModel() {
    var liveDataCart: LiveData<List<CartModel>>? = null

    fun insertData(
        context: Context,
        name: String,
        description: String,
        currency: String,
        price: Int,
        sold: Int,
        type: String,
        added: Int
    ) {
        CartRepository.insertData(
            context, name, description, currency, price, sold, type, added
        )
    }

    fun getCart(context: Context):LiveData<List<CartModel>>?{
        liveDataCart = CartRepository.getCart(context)
        return liveDataCart
    }

    fun deleteCart(context: Context) {
        CartRepository.deletecart(context)
    }
}