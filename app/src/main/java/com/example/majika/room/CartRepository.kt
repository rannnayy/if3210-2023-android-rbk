package com.example.majika.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.majika.model.CartModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CartRepository {

    companion object {
        var cartDatabase: CartDatabase? = null

        var cartModels: LiveData<List<CartModel>>? = null

        fun initializeDB(context: Context) : CartDatabase {
            return CartDatabase.getDatabaseClient(context)
        }

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
            cartDatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                val newData = CartModel(
                    name, description, currency, price, sold, type, added
                )
                cartDatabase!!.cartDAO().Insert(newData)
            }
        }

        fun getCart(context: Context) : LiveData<List<CartModel>>? {
            cartDatabase = initializeDB(context)

            cartModels = cartDatabase!!.cartDAO().getCart()

            return cartModels
        }

        fun deletecart(context: Context){
            cartDatabase = initializeDB(context)
            CoroutineScope(IO).launch{
                cartDatabase!!.cartDAO().deleteCart()
            }

        }
    }
}