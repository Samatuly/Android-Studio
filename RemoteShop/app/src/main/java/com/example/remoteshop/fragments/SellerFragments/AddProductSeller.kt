package com.example.remoteshop.fragments.SellerFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.remoteshop.R
import com.example.remoteshop.activities.Seller.SellerPage
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Category
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentAddProductSellerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class AddProductSeller : Fragment() {
    lateinit var binding: FragmentAddProductSellerBinding
    val num = Pattern.compile("^[0-9]+\$").toRegex()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductSellerBinding.inflate(inflater)
        binding.progressBaraddProduct.visibility = View.INVISIBLE
        val idSeller = activity?.intent!!.getIntExtra("id", 0)

        var list = AllCategoryNames()

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_category, list)
        binding.autoComSellerAddPr.setAdapter(arrayAdapter)

        binding.addproductSellerBrn.setOnClickListener {
            addProduct(idSeller)
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

    private fun addProduct(idSeller:Int) {
        var name = binding.nameodProduct
        var description = binding.desofProduct
        var imageURL = binding.urlofProduct
        var price = binding.priceProductAdd
        var quantity = binding.quantityProduct
        var category = binding.autoComSellerAddPr

        if(name.text.isEmpty()) name.error = "Empty error"
        else if(description.text.isEmpty()) description.error = "Empty description"
        else if(imageURL.text.isEmpty()) imageURL.error = "Empty imageURL"
        else if(!price.text.matches(num)) price.error = "Price must be number"
        else if(!quantity.text.matches(num)) quantity.error = "Quantity must be number"
        else if(category.text.isEmpty()) category.error = "Select one of category"
        else{
            val retrofit = api_instance.getApiInstance()
            val service = retrofit.create(api_services::class.java)
            val call = service.getCategory(category.text.toString())
            var id_cat:Int
            binding.progressBaraddProduct.visibility = View.VISIBLE

            call.enqueue(object: Callback<Category>{
                override fun onResponse(call: Call<Category>, response: Response<Category>) {
                    id_cat = response.body()?.id!!
                    val tempProduct = Product(
                        null,
                        name.text.toString(),
                        description.text.toString(),
                        imageURL.text.toString(),
                        price.text.toString().toInt(),
                        quantity.text.toString().toInt(),
                        id_cat,
                        idSeller
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        val call = service.addProduct(tempProduct)
                        Log.d("es", "${call.isSuccessful}")
                        Log.d("me", "${call.message()}")

                        Thread(Runnable {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(activity, "Successfully added", Toast.LENGTH_SHORT).show()
                                binding.progressBaraddProduct.visibility = View.INVISIBLE
                                fragmentManager?.beginTransaction()?.replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance())?.commit()
                            })
                        }).start()
                    }
                }
                override fun onFailure(call: Call<Category>, t: Throwable) {
                    binding.progressBaraddProduct.visibility = View.INVISIBLE
                }
            })
        }

    }

    private fun AllCategoryNames(): ArrayList<String>{
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllCategories()
        var list = ArrayList<String>()
        call.enqueue(object: Callback<List<Category>>{
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                var categories = response?.body()!!

                categories.forEach {
                    list.add(it.name)
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddProductSeller()
    }
}