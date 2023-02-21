package com.example.majika.data

import com.example.majika.model.Loc
import com.example.majika.retrofit.BranchData

class LocDatasource {
    val listLocTitle = mutableListOf<String>()
    val listLocDesc = mutableListOf<String>()
    val listLocTel = mutableListOf<String>()
    val listPopularFood = mutableListOf<String>()
    val listCP = mutableListOf<String>()
    val listLongitude = mutableListOf<Double>()
    val listLatitude = mutableListOf<Double>()
    val listEmail = mutableListOf<String>()

    fun loadList(): List<Loc> {
        return (0..(listLocTitle.size-1)).map{ key -> Loc(listLocTitle[key], listLocDesc[key], listLocTel[key], listPopularFood[key], listCP[key], listLongitude[key], listLatitude[key], listEmail[key]) }
    }

    fun fillList(data : List<BranchData>) {
        for (branch in data) {
            listLocTitle += branch.name
            listLocDesc += branch.address
            listLocTel += branch.phone_number
            listPopularFood += branch.popular_food
            listCP += branch.contact_person
            listLongitude += branch.longitude
            listLatitude += branch.latitude
//            listEmail += branch.email
            listEmail += ""
        }
    }
}