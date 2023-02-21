package com.example.majika.room

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
    fun getCart() : Flow<List<CartModel>>

    @Query("SELECT * FROM Cart WHERE id = :id")
    fun getCartWithID(id: Int): CartModel

    @Query("DELETE FROM Cart")
    fun clearCart();

    @Delete
    fun deleteItem(cartModel: CartModel);

    @Update
    fun updateItem(cartModel: CartModel);
}