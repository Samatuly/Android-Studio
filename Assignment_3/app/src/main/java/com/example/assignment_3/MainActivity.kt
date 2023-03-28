package com.example.assignment_3

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.asLiveData
import com.example.assignment_3.Data.User
import com.example.assignment_3.Data.UserDatabase
import com.example.assignment_3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup2Btn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signinBtn.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()
            val email2 = binding.emailText.text.clear()
            val password2 = binding.passwordText.text.clear()
            val db = UserDatabase.getUserDatabase(this)

            if(!email.isEmpty() && !password.isEmpty()){
                db.getDao().getAllUser().asLiveData().observe(this){list ->
                    var check:Boolean = true
                    list.forEach{
                        if(email.equals(it.email) && password.equals(it.password)){
                            Log.i(TAG, "${it.password} and ${password}")
                            val intent = Intent(this, AfterSignInActivity::class.java)
                            startActivity(intent)
                            check = true
                            email.equals(email2)
                            password.equals(password2)
                            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                            return@forEach
                        }
                        else{
                            check = false
                        }
                    }
                    if(!check){
                        Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(this, "Fill the empty data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signinAdmin.setOnClickListener {
            val intent = Intent(this, SignInAdminActivity::class.java)
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