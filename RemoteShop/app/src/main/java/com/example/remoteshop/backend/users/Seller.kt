package com.example.remoteshop.backend.users

data class Seller(
    var id: Int?,
    var username: String,
    var email: String,
    var company_name: String,
    val password: String,
)
