package com.example.remoteshop.fragments.SellerFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.FragmentSellerRegisterBinding
import com.example.remoteshop.fragments.ClientFragments.ClientSignIn
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Runnable
import java.util.regex.Pattern

class SellerRegister : Fragment() {

    lateinit var binding: FragmentSellerRegisterBinding
    val emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerRegisterBinding.inflate(inflater)
        binding.progressBarSellerReg.visibility = View.INVISIBLE

        binding.SingInReg.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.seller_frag,
                SellerSignIn.newInstance()
            )?.commit()
            (activity as AppCompatActivity).supportActionBar?.title = "Seller Sign In"
        }

        binding.sellerRegBtn.setOnClickListener {
            var username = binding.sellerusernameIn
            var email = binding.selleremailIn
            var company_name = binding.sellercompanynameIn
            var password = binding.sellerpassRegIn
            var conf_pass = binding.sellerpassRegConfIn

            if(username.text.toString().isEmpty()) username.error = "Empty username"
            else if(!email.text.toString().matches(emailPattern)) email.error = "Invalid email!"
            else if(company_name.text.toString().isEmpty()) company_name.error = "Empty Company name"
            else if(password.text.toString().length <5) password.error = "Password must be more than 6"
            else if(conf_pass.text.toString() != password.text.toString()) conf_pass.error = "Don't same passwords"
            else{

                val temp = Seller(
                    null,
                    username.text.toString(),
                    email.text.toString(),
                    company_name.text.toString(),
                    password.text.toString()
                )

                val retrofit = api_instance.getApiInstance()
                val service = retrofit.create(api_services::class.java)
                val call = service.getAllSellers()
                binding.progressBarSellerReg.visibility = View.VISIBLE

                call.enqueue(object : Callback<List<Seller>> {
                    override fun onResponse(call: Call<List<Seller>>, response: Response<List<Seller>>) {
                        var sellers = response.body()

                        Log.d("clients", "${sellers?.size}")
                        Log.d("response", "${response.isSuccessful}")
                        var check = true

                        sellers?.forEach{
                            if(it.email == email.text.toString()){
                                email.error = "This email already registered!"
                                check = false
                            }
                        }

                        Log.d("check", "$check")
                        if(check){
                            CoroutineScope(Dispatchers.IO).launch {
                                service.createSeller(temp)
                                if(response.isSuccessful){
                                    Thread(Runnable {
                                        activity?.runOnUiThread(java.lang.Runnable {
                                            Toast.makeText(activity, "Successfully registered", Toast.LENGTH_SHORT).show()
                                        })
                                    }).start()
                                    MainScope().launch {
                                        withContext(Dispatchers.Default) {}
                                        fragmentManager?.beginTransaction()?.replace(R.id.seller_frag, SellerSignIn.newInstance())?.commit()
                                    }}
                                else{
                                    Thread(Runnable {
                                        activity?.runOnUiThread(java.lang.Runnable {
                                            Toast.makeText(activity, "${response.message()}", Toast.LENGTH_SHORT).show()
                                        })
                                    }).start()
                                }
                            }
                        }
                        binding.progressBarSellerReg.visibility = View.INVISIBLE
                    }
                    override fun onFailure(call: Call<List<Seller>>, t: Throwable) {
                        Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBarSellerReg.visibility = View.INVISIBLE
                    }
                })
        }

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerRegister()
    }
}