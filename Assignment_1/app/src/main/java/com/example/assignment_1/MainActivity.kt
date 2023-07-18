package com.example.assignment_1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var loginbtn: Button
    private lateinit var editemail: EditText
    private lateinit var editpword: EditText
    private lateinit var signupbtn: Button
    //private lateinit var dbh: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginbtn = findViewById(R.id.login_button)
        editemail = findViewById(R.id.email)
        editpword = findViewById(R.id.password)
        signupbtn = findViewById(R.id.signup_button)
        //dbh = DBHelper(this)

        loginbtn.setOnClickListener {
            val emailtx = editemail.text.toString()
            val pwordtx = editpword.text.toString()
            val sharedPref = getSharedPreferences("signup_pref", MODE_PRIVATE)
            val editor = sharedPref.edit()

            if (TextUtils.isEmpty(emailtx) || TextUtils.isEmpty(pwordtx)) {
                Toast.makeText(this, "Add Email or Password", Toast.LENGTH_SHORT).show()
            }
            else {
                val email_check = sharedPref.getString("email", emailtx)
                val pword_check = sharedPref.getString("password", pwordtx)
                if(email_check.equals(emailtx) && pword_check.equals(pwordtx)){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, AfterSignInScreen::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
                }
            }

            signupbtn.setOnClickListener {
                val signup_intent = Intent(this, SignUpScreen::class.java)
                startActivity(signup_intent)
            }
        }
    }
}