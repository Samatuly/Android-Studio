package com.example.tengrinews.api

import com.example.tengrinews.News
import retrofit2.Call
import retrofit2.http.GET

interface API_service {
    //everything?q=apple&from=2023-03-29&to=2023-03-29&sortBy=popularity&apiKey=13f499722e060b8c2ae15cde76d1cbbf
    //https://gnews.io/api/v4/search?q=example&lang=en&country=us&max=10&apikey=13f499722e060b8c2ae15cde76d1cbbf
    @GET("search?q=example&lang=en&country=us&max=10&apikey=13f499722e060b8c2ae15cde76d1cbbf")
    fun getDataFromAPI(): Call<News>

}