package com.example.remoteshop.activities.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Admin
import com.example.remoteshop.databinding.ActivityLoginAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginAdmin : AppCompatActivity() {
    lateinit var binding: ActivityLoginAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBarAdminLogin.visibility = View.INVISIBLE
        supportActionBar?.title = "Admin login"


        binding.adminEnter.setOnClickListener {

            val login = binding.adminloginIn
            val password = binding.adminPasswordIn

            if (login.text.toString().isEmpty()) login.error = "Empty login"
            else if (password.text.toString().isEmpty()) password.error = "Empty password"
            else{
                val api = api_instance.getApiInstance().create(api_services::class.java)
                val call = api.getAllAdmins()
                binding.progressBarAdminLogin.visibility = View.VISIBLE
                call.enqueue(object : Callback<List<Admin>> {
                    override fun onResponse(call: Call<List<Admin>>, response: Response<List<Admin>>) {
                        var admin = response.body()
                        Log.d("admins", "${admin?.size}")
                        var check = true

                        admin?.forEach{
                            if(it.login == login.text.toString() && it.password == password.text.toString()){
                                val intent = Intent(this@LoginAdmin, AdminPage::class.java)
                                Toast.makeText(this@LoginAdmin, "Login Successful", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                check = false
                            }
                        }
                        if(check)  Toast.makeText(this@LoginAdmin, "Login or password incorrect", Toast.LENGTH_SHORT).show()

                        binding.progressBarAdminLogin.visibility = View.INVISIBLE
                    }

                    override fun onFailure(call: Call<List<Admin>>, t: Throwable) {
                        Toast.makeText(this@LoginAdmin, "${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("clients", "${t.message}")
                        binding.progressBarAdminLogin.visibility = View.INVISIBLE

                    }
                })
            }
        }

    }
}