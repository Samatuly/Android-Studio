package com.example.remoteshop.backend.products

data class Rating(
    var id: Int?,
    var count: Int,
    var rating_number: Int,
    var comment: String,
    var client: Int,
    var product: Int,
)
