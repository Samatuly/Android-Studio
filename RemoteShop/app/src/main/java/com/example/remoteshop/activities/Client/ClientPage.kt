package com.example.remoteshop.activities.Client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.remoteshop.R
import com.example.remoteshop.databinding.ActivityClientPageBinding
import com.example.remoteshop.fragments.ClientFragments.*

class ClientPage : AppCompatActivity() {

    lateinit var binding: ActivityClientPageBinding
    lateinit var bundle: Bundle
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        builder = AlertDialog.Builder(this)
        bundle = intent.extras as Bundle
        supportActionBar?.title = "Home page"
        val id = bundle!!.getInt("id")
        Log.d("id", "$id")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, HomePage.newInstance()).commit()

        bottomBar()
    }

    private fun bottomBar() {
        binding.bottomBarClient.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeClinet ->{
                    supportActionBar?.title = "Home page"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, HomePage.newInstance()).commit()

                }
                R.id.likes_client ->{
                    supportActionBar?.title = "Liked products"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, likedProducts.newInstance()).commit()
                }
                R.id.cart_client ->{
                    supportActionBar?.title = "Cart"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, CartList.newInstance()).commit()
                }
                R.id.profile_client ->{
                    supportActionBar?.title = "Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, Profile_Client.newInstance()).commit()
                }
                R.id.settings_clietn ->{
                    supportActionBar?.title = "Settings"
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_client_page, Settings.newInstance()).commit()
                }
            }
            true
        }
    }
}