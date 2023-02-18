package com.example.majika.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Cart")
data class CartModel(
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0,
    @ColumnInfo(name="name")
    val name: String,
    @ColumnInfo(name="description")
    val description: String,
    @ColumnInfo(name="currency")
    val currency: String,
    @ColumnInfo(name="price")
    val price: Int,
    @ColumnInfo(name="sold")
    val sold: Int,
    @ColumnInfo(name="type")
    val type: String,
    @ColumnInfo(name="added", defaultValue = "0")
    val added: Int
) {

}
