package com.example.assignment_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.assignment_3.Data.User
import com.example.assignment_3.Data.UserDatabase
import com.example.assignment_3.databinding.ActivityMainBinding
import com.example.assignment_3.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = UserDatabase.getUserDatabase(this)
        binding.signupBtn.setOnClickListener {
            var name = binding.nameText2.text.toString()
            var surname = binding.surnameText2.text.toString()
            var email = binding.emailText2.text.toString()
            var password = binding.passwordText2.text.toString()
            var confirmPassword = binding.confirmPasswordText2.text.toString()

            if(!password.equals(confirmPassword)) binding.confirmPasswordText2.setError("Password Not Match")
            else if(name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                val user = User(
                    null, name, surname, email, password
                )
                Thread{
                    db.getDao().insertUser(user)
                }.start()
                Toast.makeText(applicationContext, "Signed Up Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Fill the empty data", Toast.LENGTH_SHORT).show()
            }
        }
    }

}