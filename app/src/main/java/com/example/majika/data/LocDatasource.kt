package com.example.majika.data

import com.example.majika.model.Loc

class LocDatasource {
    val listLocTitle = listOf("Title1", "Title2", "Title3", "Title4", "Title5")
    val listLocDesc = listOf(
        "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh",
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"
    )
    val listLocTel = listOf(123456, 234567, 345678, 456789, 567890)

    fun loadList(): List<Loc> {
        return (0..(listLocTitle.size-1)).map{ key -> Loc(listLocTitle[key], listLocDesc[key], listLocTel[key]) }
    }
}