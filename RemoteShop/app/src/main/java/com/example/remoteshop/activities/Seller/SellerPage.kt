package com.example.remoteshop.activities.Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteshop.R
import com.example.remoteshop.activities.FirstWelcome
import com.example.remoteshop.databinding.ActivitySellerPageBinding
import com.example.remoteshop.fragments.SellerFragments.AddProductSeller
import com.example.remoteshop.fragments.SellerFragments.AllProductsSeller
import com.example.remoteshop.fragments.SellerFragments.MessageSeller
import com.example.remoteshop.fragments.SellerFragments.SellerProfileFragment

class SellerPage : AppCompatActivity() {

    lateinit var binding: ActivitySellerPageBinding
    lateinit var bundle: Bundle
    lateinit var builder: AlertDialog.Builder
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerPageBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(this)
        bundle = intent.extras as Bundle
        supportActionBar?.title = "My Products"
        val id = bundle!!.getInt("id")
        Log.d("id", "$id")
        setContentView(binding.root)



        supportFragmentManager.beginTransaction().replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance()).commit()
        bottomBar()
    }

    private fun bottomBar() {
        binding.bottomBarSeller.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.productsAllSeller ->{
                    supportActionBar?.title = "My Products"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance()).commit()
                }
                R.id.ddProductSeller ->{
                    supportActionBar?.title = "Add Product"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentSellerpage, AddProductSeller.newInstance()).commit()
                }
                R.id.AccountSellerBottom ->{
                    supportActionBar?.title = "My Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentSellerpage, SellerProfileFragment.newInstance()).commit()
                }
                R.id.MessageSellerBottom ->{
                    supportActionBar?.title = "My Messages"
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentSellerpage, MessageSeller.newInstance()).commit()
                }
            }
            true
        }
    }
}