package com.example.tengrinews.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API_instance {

    companion object {
        //https://newsapi.org/v2/everything?q=apple&from=2023-03-29&to=2023-03-29&sortBy=popularity&apiKey=8ad925c3ce3a4309b73812315bbc7e97
        val url = "https://newsapi.org/v2/" //https://newsapi.org/v2/everything?q=apple&from=2023-03-28&to=2023-03-28&sortBy=popularity&apiKey=8ad925c3ce3a4309b73812315bbc7e97
        fun getApiInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}