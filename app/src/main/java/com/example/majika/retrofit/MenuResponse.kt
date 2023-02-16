package com.example.majika.retrofit

data class MenuResponse(
    val data: List<MenuData>
)

data class MenuData(
    val name: String,
    val description: String,
    val currency: String,
    val price: Integer,
    val sold: Integer,
    val type: String
)