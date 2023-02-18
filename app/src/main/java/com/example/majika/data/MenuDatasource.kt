package com.example.majika.data

import com.example.majika.model.Menu
import com.example.majika.model.MenuRecyclerViewItem
import com.example.majika.model.Title
import com.example.majika.retrofit.MenuData

class MenuDatasource {
    val listNameMenu = mutableListOf<String>()
    var listPriceMenu = mutableListOf<Int>()
    var listSoldMenu = mutableListOf<Int>()
    val listDescMenu = mutableListOf<String>()
    val listNumBuyMenu = mutableListOf<Int>()
    val listCurrencyMenu = mutableListOf<String>()
    val listTypeMenu = mutableListOf<String>()

    fun loadList(): List<MenuRecyclerViewItem> {
        val temp = mutableListOf<MenuRecyclerViewItem>()
        for (i in (0 .. (listNameMenu.size-1))) {
            if (listPriceMenu[i] == 0 && listSoldMenu[i] == 0 && listDescMenu[i] == "" && listCurrencyMenu[i] == "" && listTypeMenu[i] == "text") {
                temp += Title(listNameMenu[i])
            }
            else {
                temp += Menu(listNameMenu[i], listPriceMenu[i], listSoldMenu[i], listDescMenu[i], listNumBuyMenu[i], listCurrencyMenu[i], listTypeMenu[i])
            }
        }
        return temp
    }

    fun loadName(): List<String> {
        return listNameMenu
    }

    fun fillList(data : List<MenuData>) {
        for (menu in data) {
            listNameMenu += menu.name
            listPriceMenu += menu.price
            listSoldMenu += menu.sold
            listDescMenu += menu.description
            listNumBuyMenu += 0
            listCurrencyMenu += menu.currency
            listTypeMenu += menu.type
        }
    }
}