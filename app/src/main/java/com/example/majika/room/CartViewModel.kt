package com.example.majika.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.majika.model.CartModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(var cartDatabase: CartDatabase): ViewModel() {
    var liveDataCart: LiveData<List<CartModel>>? = null
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

    fun getCart(): LiveData<List<CartModel>>?{
        liveDataCart = repo.getCart()
        return liveDataCart
    }

    fun getBoughtCart(): LiveData<List<CartModel>> {
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