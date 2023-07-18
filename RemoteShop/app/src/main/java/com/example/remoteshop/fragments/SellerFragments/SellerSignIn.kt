package com.example.remoteshop.fragments.SellerFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.activities.Seller.SellerPage
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.FragmentSellerSignInBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SellerSignIn : Fragment() {
    lateinit var binding: FragmentSellerSignInBinding
    val emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerSignInBinding.inflate(inflater)
        binding.progressBarSellerSingin.visibility = View.INVISIBLE

        binding.registerSeller.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.seller_frag, SellerRegister.newInstance())?.commit()
            (activity as AppCompatActivity).supportActionBar?.title = "Seller Registration"
        }

        binding.sellerEnter.setOnClickListener {
            var email = binding.sellerloginIn
            var password = binding.sellerPasswordIn

            if(!email.text.toString().matches(emailPattern)) email.error = "Invalid email!"
            else if(password.text.toString().isEmpty()) password.error = "Empty password!"
            else{
                val api = api_instance.getApiInstance().create(api_services::class.java)
                val call = api.getAllSellers()
                binding.progressBarSellerSingin.visibility = View.VISIBLE
                call.enqueue(object : Callback<List<Seller>> {
                    override fun onResponse(call: Call<List<Seller>>, response: Response<List<Seller>>) {
                        var sellers = response.body()

                        Log.d("sellers", "${sellers?.size}")
                        var check = true

                        sellers?.forEach {
                            if(it.email == email.text.toString() && it.password == password.text.toString()){
                                val intent = Intent(activity, SellerPage::class.java)
                                Toast.makeText(activity, "Login successful", Toast.LENGTH_SHORT).show()

                                intent.putExtra("id", it.id)
                                startActivity(intent)
                                check = false
                            }
                        }

                        if(check) Toast.makeText(activity, "Incorrect password or email", Toast.LENGTH_SHORT).show()

                        binding.progressBarSellerSingin.visibility = View.INVISIBLE
                    }

                    override fun onFailure(call: Call<List<Seller>>, t: Throwable) {
                        Toast.makeText(activity, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                        Log.d("sellers", "${t.message}")
                        binding.progressBarSellerSingin.visibility = View.INVISIBLE

                    }
                })
            }
        }

        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance() = SellerSignIn()
    }
}