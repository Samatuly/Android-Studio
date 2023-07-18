package com.example.assignment_1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUpScreen : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var pword: EditText
    private lateinit var cpword: EditText
    private lateinit var signupbtn: Button
    //private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_screen)

        email = findViewById(R.id.email_2)
        pword = findViewById(R.id.password_2)
        cpword = findViewById(R.id.confirm_password)
        signupbtn = findViewById(R.id.signup_button_2)
        //db = DBHelper(this)

        signupbtn.setOnClickListener{
            val emailtext = email.text.toString()
            val pwordtext = pword.text.toString()
            val cpwordtext = cpword.text.toString()
            //val savedata = db.insertdata(emailtext, pwordtext)
            val savePref = getSharedPreferences("signup_pref", MODE_PRIVATE)
            val editor2 = savePref.edit()
            val emailcheck = editor2.putString(emailtext, null)
            val pwordcheck = editor2.putString(pwordtext, null)


            if(TextUtils.isEmpty(emailtext) || TextUtils.isEmpty(pwordtext) || TextUtils.isEmpty(cpwordtext)){
                Toast.makeText(this, "Add Email, Password & Confirm Password", Toast.LENGTH_SHORT).show()
            }
            else{
                if (pwordtext == cpwordtext){
                    if(emailcheck == null){
                        emailcheck?.apply()
                        pwordcheck.apply()
                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, "User Exist", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "Password Not Match", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}