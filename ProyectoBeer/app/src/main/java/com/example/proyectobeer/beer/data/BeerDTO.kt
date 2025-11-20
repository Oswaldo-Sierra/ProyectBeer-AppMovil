package com.example.proyectobeer.beer.data

data class BeerDTO(
    var serialNumber: Int,
    var brand : String,
    var type : String,
    var ABV : String,
    var IBU : String,
    var provider : String,
    var price : Int,
    var quantity : Int
)
