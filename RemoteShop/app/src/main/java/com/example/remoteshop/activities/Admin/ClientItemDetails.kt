package com.example.remoteshop.activities.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.databinding.ActivityClientItemDetailsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientItemDetails : AppCompatActivity() {

    lateinit var binding: ActivityClientItemDetailsBinding
    lateinit var bundle: Bundle
    lateinit var builder: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        builder = AlertDialog.Builder(this)
        binding.progressBarClientAdminDet.visibility = View.INVISIBLE


        bundle = intent.extras as Bundle

        val id = bundle!!.getInt("id")
        val username = bundle!!.getString("username")
        val city = bundle!!.getString("city")
        val email = bundle!!.getString("email")

        supportActionBar?.title = "$username details"

        binding.EmailClientAdminDet.text = email
        binding.usernameCleintAdminDet.text = username
        binding.cityClientAdminDet.text = city
        binding.idOfClient.text = id.toString()


        binding.clientdelelteBntAdmin.setOnClickListener {
            builder.setTitle("Delete client with email $email")
                .setMessage("Do you want to delete this client? Then restore don't available!")
                .setPositiveButton("Yes"){idd, it ->
                    val api = api_instance.getApiInstance().create(api_services::class.java)
                    val call = api.deleteClient(id)
                    binding.progressBarClientAdminDet.visibility = View.VISIBLE
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            binding.progressBarClientAdminDet.visibility = View.INVISIBLE

                            Log.d("delete", "${response.isSuccessful}")
                            Log.d("delete", "${response.message()}")
                            if(response.isSuccessful){
                                Toast.makeText(this@ClientItemDetails, "Delete success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ClientItemDetails, AdminPage::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this@ClientItemDetails, "${response.message()}", Toast.LENGTH_SHORT).show()

                            }

                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("error", "${t.message}")
                            binding.progressBarClientAdminDet.visibility = View.INVISIBLE

                            Toast.makeText(this@ClientItemDetails, "${t.message}", Toast.LENGTH_SHORT).show()

                        }

                    })
                }
                .setNegativeButton("No"){id, it ->
                    id.cancel()
                }
                .show()
        }
    }
}