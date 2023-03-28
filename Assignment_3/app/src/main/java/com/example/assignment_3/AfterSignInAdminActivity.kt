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
import com.example.assignment_3.Fragments.*
import com.google.android.material.navigation.NavigationView

class AfterSignInAdminActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout2: DrawerLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_sign_in_admin)

        drawerLayout2 = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout2, R.string.open, R.string.close)
        drawerLayout2.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AfterSignInAdminActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_profile -> replaceFragment(ProfileFragment(), it.title.toString())
                R.id.nav_books -> replaceFragment(BookShow(), it.title.toString())
                R.id.nav_edit -> replaceFragment(EditFragment(), it.title.toString())
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

}