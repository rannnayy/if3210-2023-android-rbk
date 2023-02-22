package com.example.majika.data

import com.example.majika.model.Cart
import com.example.majika.model.CartModel
import com.example.majika.model.CartRecyclerViewItem
import com.example.majika.model.Price

class CartDatasource {
    val listNameCart = mutableListOf<String>()
    val listPriceCart = mutableListOf<Int>()
    val listNumBuyCart = mutableListOf<Int>()
    val listDescCart = mutableListOf<String>()
    val listCurrencyCart = mutableListOf<String>()
    val listNumSoldCart = mutableListOf<Int>()
    val listTypeCart = mutableListOf<String>()

    fun loadList(): List<CartRecyclerViewItem> {
        var temp = (0..(listNameCart.size-1)).map{ key -> Cart(listNameCart[key], listPriceCart[key], listNumBuyCart[key], listDescCart[key], listCurrencyCart[key], listNumSoldCart[key], listTypeCart[key]) }
        var total = 0
        for (item in temp) {
            total += item.priceCart * item.numBuyCart
        }
        return temp + mutableListOf<CartRecyclerViewItem>(Price(total))
    }

    fun fillList(data : List<CartModel>) {
        for (menu in data) {
            listNameCart += menu.name
            listPriceCart += menu.price
            listNumBuyCart += menu.added
            listDescCart += menu.description
            listCurrencyCart += menu.currency
            listNumSoldCart += menu.sold
            listTypeCart += menu.type
        }
    }
}