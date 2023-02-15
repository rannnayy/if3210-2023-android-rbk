package com.example.majika.retrofit

data class BranchResult(
    val data: List<BranchData>
)

data class BranchData(
    val name: String,
    val popular_food: String,
    val address: String,
    val contact_person: String,
    val phone_number: String,
    val longitude: Double,
    val latitude: Double
)