package com.example.majika.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.majika.model.CartModel

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(cartModel: CartModel)

    @Query("SELECT * FROM Cart WHERE added > 0")
    fun getCart() : LiveData<List<CartModel>>

    @Query("DELETE FROM Cart")
    fun deleteCart();
}