package com.example.majika.model

open class MenuRecyclerViewItem
class Menu(val nameMenu: String, val priceMenu: Int, val numSoldMenu: Int, val descMenu: String, val numBuyMenu: Int, val currencyMenu: String, val typeMenu: String) : MenuRecyclerViewItem()
class Title(val category: String) : MenuRecyclerViewItem()