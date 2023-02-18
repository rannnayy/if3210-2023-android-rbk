package com.example.majika.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.majika.model.CartModel

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(cartModel: CartModel)

    @Query("SELECT * FROM Cart")
    fun getCart() : LiveData<List<CartModel>>

    @Query("DELETE FROM Cart")
    fun clearCart();

    @Delete
    fun deleteItem(cartModel: CartModel);

    @Update
    fun updateItem(cartModel: CartModel);
}