package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.databinding.FragmentClientRegisterBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Runnable
import java.util.regex.Pattern


class ClientRegister : Fragment() {
    lateinit var  binding: FragmentClientRegisterBinding
    val emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientRegisterBinding.inflate(inflater)
        binding.progressBarClientReg.visibility = View.INVISIBLE

        binding.SingInRegClient.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.client_frag,
                ClientSignIn.newInstance()
            )?.commit()
            (activity as AppCompatActivity).supportActionBar?.title = "Client Sign In"
        }

        binding.clientRegBtn.setOnClickListener {
            var username = binding.clientusernameIn
            var email = binding.clientemailIn
            var city = binding.clientcitynameIn
            var password = binding.clientpassRegIn
            var conf_pass = binding.clientpassRegConfIn

//            username.text = Editable.Factory.getInstance().newEditable("Era")
//            email.text = Editable.Factory.getInstance().newEditable("erdaulet03@gmail.com")
//            city.text = Editable.Factory.getInstance().newEditable("Shymkent")
//            password.text = Editable.Factory.getInstance().newEditable("123456")
//            conf_pass.text = Editable.Factory.getInstance().newEditable("123456")

            if(username.text.toString().isEmpty()) username.error = "Empty username"
            else if(!email.text.toString().matches(emailPattern)) email.error = "Invalid email!"
            else if(city.text.toString().isEmpty()) city.error = "Empty city"
            else if(password.text.toString().length <5) password.error = "Password must be more than 6"
            else if(conf_pass.text.toString() != password.text.toString()) conf_pass.error = "Don't same passwords"
            else{

                val temp = Client(
                    null,
                    username.text.toString(),
                    email.text.toString(),
                    city.text.toString(),
                    password.text.toString()
                )

                val retrofit = api_instance.getApiInstance()
                val service = retrofit.create(api_services::class.java)
                val call = service.getAllClients()
                binding.progressBarClientReg.visibility = View.VISIBLE

                call.enqueue(object : Callback<List<Client>> {
                    override fun onResponse(call: Call<List<Client>>, response: Response<List<Client>>) {
                        var clients = response.body()

                        Log.d("clients", "${clients?.size}")
                        var check = true

                        clients?.forEach{
                            if(it.email == email.text.toString()){
                                email.error = "This email already registered!"
                                check = false
                            }
                        }

                        Log.d("check", "$check")
                        if(check){
                            CoroutineScope(Dispatchers.IO).launch {
                                service.createClient(temp)
                                if(response.isSuccessful){
                                    Thread(Runnable {
                                        activity?.runOnUiThread(java.lang.Runnable {
                                            Toast.makeText(activity, "You are successfully registered", Toast.LENGTH_SHORT).show()
                                        })
                                    }).start()
                                    MainScope().launch {
                                        withContext(Dispatchers.Default) {}
                                        fragmentManager?.beginTransaction()?.replace(R.id.client_frag, ClientSignIn.newInstance())?.commit()
                                    }
                                }
                                else{
                                    Thread(Runnable {
                                        activity?.runOnUiThread(java.lang.Runnable {
                                            Toast.makeText(activity, "${response.message()}", Toast.LENGTH_SHORT).show()
                                        })
                                    }).start()
                                }
                            }
                        }
                        binding.progressBarClientReg.visibility = View.INVISIBLE
                    }
                    override fun onFailure(call: Call<List<Client>>, t: Throwable) {
                        Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBarClientReg.visibility = View.INVISIBLE
                    }
                })
            }

        }

        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = ClientRegister()
    }
}