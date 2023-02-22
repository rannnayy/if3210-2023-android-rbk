package com.example.majika.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.majika.model.CartModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(cartModel: CartModel)

    @Query("SELECT * FROM Cart")
    fun getCart() : List<CartModel>

    @Query("SELECT * FROM Cart WHERE type = 'Food'")
    fun getFood() : List<CartModel>

    @Query("SELECT * FROM Cart WHERE type = 'Drink'")
    fun getDrink() : List<CartModel>

    @Query("SELECT * FROM Cart WHERE added > 0")
    fun getBoughtCart() : List<CartModel>

    @Query("SELECT * FROM Cart WHERE Id = :id")
    fun getCartWithID(id: Int): CartModel

    @Query("SELECT * FROM Cart WHERE (name = :name and description = :description and currency = :currency and price = :price and type = :type) LIMIT 1")
    fun getCartofDetails(name: String, description: String, currency: String, price: Int, type: String): CartModel

    @Query("DELETE FROM Cart")
    fun clearCart();

    @Delete
    fun deleteItem(cartModel: CartModel);

    @Update
    fun updateItem(cartModel: CartModel);
}