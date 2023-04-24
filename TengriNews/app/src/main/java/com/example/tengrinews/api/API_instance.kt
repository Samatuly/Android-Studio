package com.example.tengrinews.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API_instance {

    companion object {
        val url = "https://gnews.io/api/v4/"
        fun getApiInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}