package com.example.majika.model

open class CartRecyclerViewItem
class Cart(val nameCart: String, val priceCart: Int, var numBuyCart: Int, val descCart: String, val currencyCart: String, val numSoldCart: Int, val typeCart: String) : CartRecyclerViewItem()
class Price(val price: Int) : CartRecyclerViewItem()