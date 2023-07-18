package com.example.remoteshop.activities.Seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.remoteshop.R
import com.example.remoteshop.databinding.ActivityLoginSellerBinding
import com.example.remoteshop.fragments.SellerFragments.SellerSignIn

class LoginSeller : AppCompatActivity() {
    lateinit var binding: ActivityLoginSellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Seller sign in"

        supportFragmentManager.beginTransaction().replace(R.id.seller_frag, SellerSignIn.newInstance()).commit()
    }
}