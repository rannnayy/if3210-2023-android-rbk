package com.example.majika.data

import com.example.majika.model.Menu

class MenuDatasource {
    val listNameMenu = listOf<String>("Makanan", "Makanan A", "Makanan B", "Makanan C", "Makanan D", "Makanan E")
    val listPriceMenu = listOf<Number>(0, 50000, 100000, 10000, 25000, 5000)
    val listSoldMenu = listOf<Number>(0, 1000, 5000, 100, 200, 50)
    val listDescMenu = listOf<String>(
        "",
        "Makanan ini enaaakkkkk sekali :>",
        "Makanan ini enaaakkkkk sekali :>",
        "Makanan ini enaaakkkkk sekali :>",
        "Makanan ini enaaakkkkk sekali :>",
        "Makanan ini enaaakkkkk sekali :>",
    )
    var listNumBuyMenu = listOf<Number>(0,0,0,0,0,0)
    var itemTypeMenu = listOf<Int>(2, 1, 1, 1, 1, 1)

    fun loadList(): List<Menu> {
        return (0..(listNameMenu.size-1)).map{ key -> Menu(listNameMenu[key], listPriceMenu[key], listSoldMenu[key], listDescMenu[key], listNumBuyMenu[key], itemTypeMenu[key]) }
    }
}