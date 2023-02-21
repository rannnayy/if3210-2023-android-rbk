package com.example.majika.room

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.majika.model.CartModel
import kotlinx.coroutines.flow.Flow

class CartViewModel(var cartDatabase: CartDatabase): ViewModel() {
    var liveDataCart: Flow<List<CartModel>>? = null

    fun insertData(
        name: String,
        description: String,
        currency: String,
        price: Int,
        sold: Int,
        type: String,
        added: Int
    ) {
        CartRepository(cartDatabase).insertData(
            name, description, currency, price, sold, type, added
        )
    }

    fun getCart():Flow<List<CartModel>>?{
        liveDataCart = CartRepository(cartDatabase).getCart()
        return liveDataCart
    }

    fun getCartWithID(id: Int): CartModel {
        return CartRepository(cartDatabase).getCartWithID(id)
    }

    fun clearCart() {
        CartRepository(cartDatabase).clearCart()
    }

    fun decreaseItem(cartModel: CartModel){
        CartRepository(cartDatabase).decreaseItem(cartModel)
    }

    fun addItem(cartModel: CartModel){
        CartRepository(cartDatabase).addItem(cartModel)
    }
}