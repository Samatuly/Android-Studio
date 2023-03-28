package com.example.assignment_3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment_3.Fragments.*
import com.example.assignment_3.bookData.BookDatabase
import com.example.assignment_3.databinding.ActivityEditBinding
import com.google.android.material.navigation.NavigationView

class EditActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditBinding
    lateinit var builder: AlertDialog.Builder
    var backPressedTime: Long = 0
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout2: DrawerLayout

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(this)
        setContentView(binding.root)

        drawerLayout2 = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val bookDb = BookDatabase.getBookDatabase(this)
        bookDb.getBookDao().getAllBooks().asLiveData().observe(this) {
            var listBooks = it
            var adapter = bookAdapterAdmin(listBooks, this)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }

//        binding.addBookBtn.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.fragmentAddBook, AddBookFragment.newInstance()).commit()
//            supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
//        }

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AfterSignInAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_profile -> replaceFragment(ProfileFragment(), it.title.toString())
                R.id.nav_books -> replaceFragment(BookShow(), it.title.toString())
                R.id.nav_edit -> {
                    val intent = Intent(this, EditActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_favourites -> replaceFragment(FavouritesFragment(), it.title.toString())
                R.id.nav_settings -> replaceFragment(SettingsFragment(), it.title.toString())
                R.id.nav_support -> replaceFragment(SupportFragment(), it.title.toString())
                R.id.nav_rate -> replaceFragment(RateFragment(), it.title.toString())
                R.id.nav_logout -> {
                    val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setTitle("Log Out")
                    alertDialog.setMessage("Are you sure?")
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { dialog, which ->
                        val intent = Intent(this, SignInAdminActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, which ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout2.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (backPressedTime + 10 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            builder.setTitle("Exit Admin mode")
                .setMessage("Do you want to log out from admin mode,then you'll have to log in again!")
                .setPositiveButton("Yes"){id, it ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No"){id, it ->
                    id.cancel()
                }
                .show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}