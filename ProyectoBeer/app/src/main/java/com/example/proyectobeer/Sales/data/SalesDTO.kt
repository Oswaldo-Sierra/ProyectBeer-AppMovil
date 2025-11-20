package com.example.proyectobeer.Sales.data

data class SalesDTO(
    var salesId : Int,
    var numberSerialofBeer: Int,
    var numberofBeerSold: Int,
    var priceTotal: Int,
    var dataofSale: String,
    var userName: String,
    var customerName: String
)
