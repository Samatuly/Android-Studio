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
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.ActivitySelletItemDetailsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerItemDetails : AppCompatActivity() {

    lateinit var binding: ActivitySelletItemDetailsBinding
    lateinit var bundle: Bundle
    lateinit var builder: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelletItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBarSellerAdminDet.visibility = View.INVISIBLE
        bundle = intent.extras as Bundle
        builder = AlertDialog.Builder(this)


        val id = bundle!!.getInt("id")
        val username = bundle!!.getString("username")
        val companyName = bundle!!.getString("companyName")
        val email = bundle!!.getString("email")

        supportActionBar?.title = "${username} details"

        binding.EmailSellerDet.text = email
        binding.usernameSellerDetails.text = username
        binding.companyNameSellerDet.text = companyName
        binding.idSellerAdminDet.text = id.toString()


        binding.deleteCompanyBtn.setOnClickListener {

            builder.setTitle("Delete seller with email $email")
                .setMessage("Do you want to delete this seller? Then restore don't available!")
                .setPositiveButton("Yes"){idd, it -> val api = api_instance.getApiInstance().create(api_services::class.java)
                    val call = api.deleteSeller(id)
                    binding.progressBarSellerAdminDet.visibility = View.VISIBLE
                    call.enqueue(object : Callback<ResponseBody>{
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            binding.progressBarSellerAdminDet.visibility = View.INVISIBLE

                            Log.d("delete", "${response.isSuccessful}")
                            Log.d("delete", "${response.message()}")
                            if(response.isSuccessful){
                                Toast.makeText(this@SellerItemDetails, "Delete success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SellerItemDetails, AdminPage::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this@SellerItemDetails, "${response.message()}", Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("error", "${t.message}")
                            binding.progressBarSellerAdminDet.visibility = View.INVISIBLE

                            Toast.makeText(this@SellerItemDetails, "${t.message}", Toast.LENGTH_SHORT).show()

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