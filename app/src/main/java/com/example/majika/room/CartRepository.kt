package com.example.majika.room

import androidx.lifecycle.LiveData
import com.example.majika.model.CartModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CartRepository(private val cartDatabase: CartDatabase) {
    var cartModels: LiveData<List<CartModel>>? = null

    fun insertData(
        name: String,
        description: String,
        currency: String,
        price: Int,
        sold: Int,
        type: String,
        added: Int
    ) {
        CoroutineScope(IO).launch {
            val newData = CartModel(
                name = name, description = description, currency = currency, price = price, sold = sold, type = type, added = added
            )
            cartDatabase!!.cartDAO().Insert(newData)
        }
    }

    fun getCart() : LiveData<List<CartModel>>? {
        return cartDatabase!!.cartDAO().getCart()
    }

    fun getBoughtCart() : LiveData<List<CartModel>> {
        return cartDatabase!!.cartDAO().getBoughtCart()
    }

    fun getCartWithID(id: Int) : CartModel {
        return cartDatabase!!.cartDAO().getCartWithID(id)
    }

    fun getCartofDetails(name: String, description: String, currency: String, price: Int, type: String): CartModel {
        return cartDatabase!!.cartDAO().getCartofDetails(name, description, currency, price, type)
    }

    fun clearCart(){
        CoroutineScope(IO).launch{
            cartDatabase!!.cartDAO().clearCart()
        }
    }

    fun decreaseItem(cartModel: CartModel){
        CoroutineScope(IO).launch{
            if (cartModel.added >= 1){
                val newItem = cartModel.copy(added = cartModel.added-1)
                cartDatabase!!.cartDAO().updateItem(newItem)
            }
        }
    }

    fun addItem(cartModel: CartModel){
        CoroutineScope(IO).launch{
            val newItem = cartModel.copy(added = cartModel.added+1)
            cartDatabase!!.cartDAO().updateItem(newItem)
        }
    }

    fun buyItem(cartModel: CartModel) {
        CoroutineScope(IO).launch{
            val newItem = cartModel.copy(added = 0, sold=cartModel.sold+cartModel.added)
            cartDatabase!!.cartDAO().updateItem(newItem)
        }
    }
}