package com.example.majika.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.majika.model.CartModel

@Database(entities = arrayOf(CartModel::class), version=2, exportSchema=false)
abstract class CartDatabase: RoomDatabase(){
    abstract fun cartDAO(): CartDAO

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabaseClient(context: Context) : CartDatabase{
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, CartDatabase::class.java, "CART_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}