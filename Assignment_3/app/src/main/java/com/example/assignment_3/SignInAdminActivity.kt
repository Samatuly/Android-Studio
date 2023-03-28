package com.example.assignment_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.assignment_3.databinding.ActivitySignInAdminBinding

class SignInAdminActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInAdminBinding
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinBtnAdmin.setOnClickListener {
            if(!binding.emailTextAdmin.text.isEmpty() && !binding.passwordTextAdmin.text.isEmpty()) {
                if (binding.emailTextAdmin.text.toString() == "admin12345@gmail.com" && binding.passwordTextAdmin.text.toString() == "admin12345") {
                    val intent = Intent(this, AfterSignInAdminActivity::class.java)
                    startActivity(intent)
                }
            }
            else if(binding.emailTextAdmin.text.isEmpty() || binding.passwordTextAdmin.text.isEmpty()){
                Toast.makeText(this, "Fill the data", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signinBtnCustomer.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 10 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Exit")
            alertDialog.setMessage("Are you sure?")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, which ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}