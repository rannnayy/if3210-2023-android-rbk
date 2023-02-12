package com.example.majika.data

import com.example.majika.model.Cart
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.model.Price
import kotlinx.coroutines.flow.merge

class CartDatasource {
    val listNameCart = listOf<String>("Makanan A", "Makanan B", "Makanan C", "Makanan D", "Makanan E")
    val listPriceCart = listOf<Int>(50000, 100000, 10000, 25000, 5000)
    val listNumBuyCart = listOf<Int>(1000, 5000, 100, 200, 50)

    fun loadList(): List<CartRecyclerViewItem> {
        var temp = (0..(listNameCart.size-1)).map{ key -> Cart(listNameCart[key], listPriceCart[key], listNumBuyCart[key]) }
        var total = 0
        for (item in temp) {
            total += item.priceCart * item.numBuyCart
        }
        return temp + listOf<CartRecyclerViewItem>(Price(total))
    }
}