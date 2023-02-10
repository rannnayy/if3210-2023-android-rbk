package com.example.majika.data

import com.example.majika.model.Cart

class CartDatasource {
    val listNameCart = listOf<String>("Makanan A", "Makanan B", "Makanan C", "Makanan D", "Makanan E")
    val listPriceCart = listOf<Number>(50000, 100000, 10000, 25000, 5000)
    val listNumBuyCart = listOf<Number>(1000, 5000, 100, 200, 50)

    fun loadList(): List<Cart> {
        return (0..(listNameCart.size-1)).map{ key -> Cart(listNameCart[key], listPriceCart[key], listNumBuyCart[key]) }
    }
}