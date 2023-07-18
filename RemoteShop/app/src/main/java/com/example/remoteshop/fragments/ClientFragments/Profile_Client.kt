package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.databinding.FragmentPlorileClientBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class Profile_Client : Fragment() {

    lateinit var binding: FragmentPlorileClientBinding
    lateinit var builder: AlertDialog.Builder
    private val emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlorileClientBinding.inflate((inflater))
        builder = AlertDialog.Builder(requireContext())

        val idClient = activity?.intent!!.getIntExtra("id", 0)

        binding.savePr.setOnClickListener {
            btnClick(idClient)
        }

        fillDatas(idClient)


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, HomePage.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Home page"
                }
            }
        )
        return binding.root
    }

    private fun btnClick(idClient:Int) {

        builder.setTitle("Save changes")
            .setMessage("Do you want to save changes?")
            .setPositiveButton("Yes"){idd, it ->
                var username = binding.usernamePr
                var email = binding.emailPr
                var city = binding.cityPr
                var password = binding.passwordPr

                if(username.text.toString().isEmpty()) username.error = "Empty username"
                else if(!email.text.toString().matches(emailPattern)) email.error = "Invalid email!"
                else if(city.text.toString().isEmpty()) city.error = "Empty City"
                else if(password.text.toString().length <5) password.error = "Password must be more than 6"
                else{

                    var tempClient = Client(
                        idClient,
                        username.text.toString(),
                        email.text.toString(),
                        city.text.toString(),
                        password.text.toString()
                    )

                    val retrofit = api_instance.getApiInstance()
                    val service = retrofit.create(api_services::class.java)
                    val call = service.updateClient(idClient, tempClient)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                Toast.makeText(activity, "Your changes saved!", Toast.LENGTH_SHORT).show()
                                fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, HomePage.newInstance())?.commit()
                                (activity as AppCompatActivity).supportActionBar?.title = "Home page"
                            }
                            else{
                                Toast.makeText(activity, "${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
            .setNegativeButton("No"){id, it ->
                id.cancel()
            }
            .show()
    }

    private fun fillDatas(idClient: Int) {
        val retrofit = api_instance.getApiInstance()
        val service = retrofit.create(api_services::class.java)
        val call = service.getClientById(idClient)

        var client: Client

        call.enqueue(object: Callback<Client>{
            override fun onResponse(call: Call<Client>, response: Response<Client>) {
                client = response.body()!!
                binding.usernamePr.text = Editable.Factory.getInstance().newEditable(client.username)
                binding.emailPr.text = Editable.Factory.getInstance().newEditable(client.email)
                binding.cityPr.text = Editable.Factory.getInstance().newEditable(client.city)
                binding.passwordPr.text = Editable.Factory.getInstance().newEditable(client.password)
            }

            override fun onFailure(call: Call<Client>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = Profile_Client()
    }
}