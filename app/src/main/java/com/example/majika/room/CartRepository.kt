package com.example.majika.room

import android.content.Context
import android.util.Log
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
                    name = name, description = description, currency = currency, price = price, sold = sold, type = type, added = added
                )
                cartDatabase!!.cartDAO().Insert(newData)
            }
        }

        fun getCart(context: Context) : LiveData<List<CartModel>>? {
            cartDatabase = initializeDB(context)

            cartModels = cartDatabase!!.cartDAO().getCart()

            return cartModels
        }

        fun clearCart(context: Context){
            cartDatabase = initializeDB(context)
            CoroutineScope(IO).launch{
                cartDatabase!!.cartDAO().clearCart()
            }
        }

        fun decreaseItem(context: Context, cartModel: CartModel){
            cartDatabase = initializeDB(context)
            CoroutineScope(IO).launch{
                if (cartModel.added > 1){
                    val newItem = cartModel.copy(added = cartModel.added-1)
                    cartDatabase!!.cartDAO().updateItem(newItem)
                }
                else {
                    cartDatabase!!.cartDAO().deleteItem(cartModel)
                }

            }
        }

        fun addItem(context: Context, cartModel: CartModel){
            cartDatabase = initializeDB(context)
            CoroutineScope(IO).launch{
                val newItem = cartModel.copy(added = cartModel.added+1)
                cartDatabase!!.cartDAO().updateItem(newItem)
            }
        }
    }
}