package com.example.remoteshop.backend.products


data class Cart(
    var id: Int?,
    var check_order: String,
    var client: Int,
    var product: Int,
    var seller: Int,
)
