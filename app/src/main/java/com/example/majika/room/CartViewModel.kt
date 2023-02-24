package com.example.majika.room

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majika.model.CartModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(var cartDatabase: CartDatabase): ViewModel() {
    var liveDataCart: List<CartModel>? = null
    private var repo: CartRepository = CartRepository(cartDatabase)

    fun insertData(
        name: String,
        description: String,
        currency: String,
        price: Int,
        sold: Int,
        type: String,
        added: Int
    ) {
        viewModelScope.launch {
            repo.insertData(
                name, description, currency, price, sold, type, added
            )
        }
    }

    fun getCart(): List<CartModel>?{
        liveDataCart = repo.getCart()
        return liveDataCart
    }

    fun getFood(): List<CartModel>?{
        liveDataCart = repo.getFood()
        return liveDataCart
    }

    fun getDrink(): List<CartModel>?{
        liveDataCart = repo.getDrink()
        return liveDataCart
    }

    fun getBoughtCart(): List<CartModel> {
        return repo.getBoughtCart()
    }

    fun getCartWithID(id: Int): CartModel {
        return repo.getCartWithID(id)
    }

    fun getCartofDetails(name: String, description: String, currency: String, price: Int, type: String): CartModel {
        return repo.getCartofDetails(name, description, currency, price, type)
    }

    fun clearCart() {
        repo.clearCart()
    }

    fun decreaseItem(cartModel: CartModel){
        repo.decreaseItem(cartModel)
    }

    fun addItem(cartModel: CartModel){
        repo.addItem(cartModel)
    }

    fun buyItem(cartModel: CartModel){
        repo.buyItem(cartModel)
    }
}