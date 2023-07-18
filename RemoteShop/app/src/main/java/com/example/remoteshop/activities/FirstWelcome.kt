package com.example.remoteshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.remoteshop.activities.Admin.LoginAdmin
import com.example.remoteshop.activities.Client.LoginClient
import com.example.remoteshop.activities.Seller.LoginSeller
import com.example.remoteshop.databinding.ActivityFirstWelcomeBinding

class FirstWelcome : AppCompatActivity() {

    lateinit var binding: ActivityFirstWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adminFr.setOnClickListener {
            val intent = Intent(this, LoginAdmin::class.java)
            startActivity(intent)
        }

        binding.sellerFr.setOnClickListener {
            val intent = Intent(this, LoginSeller::class.java)
            startActivity(intent)
        }

        binding.clientFr.setOnClickListener {
            val intent = Intent(this, LoginClient::class.java)
            startActivity(intent)
        }
    }
}