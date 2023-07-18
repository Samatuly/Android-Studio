package com.example.remoteshop.backend.products

data class Product(
    var id: Int?,
    var name: String,
    var description: String,
    var imageURL: String,
    var price: Int,
    var quantity: Int,
    var category: Int,
    var seller: Int,
)