package com.example.remoteshop.backend.users


data class Client(
    var id: Int?,
    var username: String,
    var email: String,
    var city: String,
    val password: String,
)