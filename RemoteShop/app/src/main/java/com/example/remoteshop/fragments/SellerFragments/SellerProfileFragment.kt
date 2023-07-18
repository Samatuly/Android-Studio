package com.example.remoteshop.fragments.SellerFragments

import android.os.Bundle
import android.text.Editable
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
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.FragmentSellerProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SellerProfileFragment : Fragment() {

    lateinit var binding: FragmentSellerProfileBinding
    lateinit var builder: AlertDialog.Builder
    private val emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+").toRegex()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerProfileBinding.inflate(inflater)
        builder = AlertDialog.Builder(requireContext())
        val idSeller = activity?.intent!!.getIntExtra("id", 0)


        fillDatas(idSeller)

        binding.saveProfileSeller.setOnClickListener {
            btnClick(idSeller)
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "My products"
                }
            }
        )
        return binding.root
    }

    private fun btnClick(idSeller:Int) {

        builder.setTitle("Save changes")
            .setMessage("Do you want to save changes")
            .setPositiveButton("Yes"){idd, it ->
                var username = binding.sellerusernameProfileIn
                var email = binding.selleremailInProfile
                var company_name = binding.sellercompanynameInProfile
                var password = binding.sellerpassRegInProfile

                if(username.text.toString().isEmpty()) username.error = "Empty username"
                else if(!email.text.toString().matches(emailPattern)) email.error = "Invalid email!"
                else if(company_name.text.toString().isEmpty()) company_name.error = "Empty Company name"
                else if(password.text.toString().length <5) password.error = "Password must be more than 6"
                else{

                    var tempSeller = Seller(
                        idSeller,
                        username.text.toString(),
                        email.text.toString(),
                        company_name.text.toString(),
                        password.text.toString()
                    )

                    val retrofit = api_instance.getApiInstance()
                    val service = retrofit.create(api_services::class.java)
                    val call = service.updateSeller(idSeller, tempSeller)

                    call.enqueue(object :Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                Toast.makeText(activity, "Your changes saved!", Toast.LENGTH_SHORT).show()
                                fragmentManager?.beginTransaction()?.replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance())?.commit()
                                (activity as AppCompatActivity).supportActionBar?.title = "My products"
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

    private fun fillDatas(idSeller: Int) {
        val retrofit = api_instance.getApiInstance()
        val service = retrofit.create(api_services::class.java)
        val call = service.getSellerById(idSeller)

        var seller:Seller

        call.enqueue(object: Callback<Seller>{
            override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                seller = response.body()!!
                binding.sellerusernameProfileIn.text = Editable.Factory.getInstance().newEditable(seller.username)
                binding.selleremailInProfile.text = Editable.Factory.getInstance().newEditable(seller.email)
                binding.sellercompanynameInProfile.text = Editable.Factory.getInstance().newEditable(seller.company_name)
                binding.sellerpassRegInProfile.text = Editable.Factory.getInstance().newEditable(seller.password)
            }

            override fun onFailure(call: Call<Seller>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
    companion object {
        @JvmStatic
        fun newInstance() =SellerProfileFragment()
    }
}