package com.example.remoteshop.fragments.SellerFragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Category
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.FragmentProductItemSellerBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class Product_Item_seller : Fragment() {

    lateinit var binding: FragmentProductItemSellerBinding
    lateinit var builder: AlertDialog.Builder

    val num = Pattern.compile("^[0-9]+\$").toRegex()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductItemSellerBinding.inflate(inflater)
        builder = AlertDialog.Builder(requireContext())


        val args = arguments
        val id = args?.getString("id")?.toInt()!!
        val idSeller = activity?.intent!!.getIntExtra("id", 0)


        var list = AllCategoryNames()

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_category, list)
        binding.autoComSellerAddPrUpdate.setAdapter(arrayAdapter)

        createPage(id)

        binding.saveChangesBtn.setOnClickListener {
            updateProduct(id, idSeller)
        }

        binding.deleteProductBtn.setOnClickListener {

            deleteProduct(id)
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

    private fun deleteProduct(id: Int) {
        val retrofit = api_instance.getApiInstance()
        val service = retrofit.create(api_services::class.java)
        val call = service.deleteProduct(id)

        builder.setTitle("Exit Admin mode")
            .setMessage("Do you want to log out from admin mode,then you'll have to log in again!")
            .setPositiveButton("Yes"){idd, it ->
                call.enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if(response.isSuccessful){
                            Toast.makeText(activity, "This product deleted successfully", Toast.LENGTH_SHORT).show()
                            fragmentManager?.beginTransaction()?.replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance())?.commit()
                        }
                        else{
                            Toast.makeText(activity, "${response.message()}", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            .setNegativeButton("No"){id, it ->
                id.cancel()
            }
            .show()
    }

    private fun createPage(productID: Int) {

        val retrofit = api_instance.getApiInstance()
        val service = retrofit.create(api_services::class.java)
        val call = service.getProductById(productID)

        var product: Product

        call.enqueue(object: Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                product = response.body()!!
                binding.nameProductSeller.text = Editable.Factory.getInstance().newEditable(product.name)
                binding.descriptionSellerPr.text = Editable.Factory.getInstance().newEditable(product.description)
                binding.imageURLSeller.text = Editable.Factory.getInstance().newEditable(product.imageURL)
                binding.priceProduct.text = Editable.Factory.getInstance().newEditable(product.price.toString())
                binding.quantitySellerPr.text = Editable.Factory.getInstance().newEditable(product.quantity.toString())

                (activity as AppCompatActivity).supportActionBar?.title = "${product.name} details"


                val img = binding.imageProductSeller
                val url = product.imageURL
                Glide.with(img)
                    .load(url)
                    .placeholder(R.drawable.dont_image_24)
                    .error(R.drawable.dont_image_24)
                    .fallback(R.drawable.dont_image_24)
                    .into(img)

            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun updateProduct(product_id: Int, idseller:Int) {
        var name = binding.nameProductSeller
        var description = binding.descriptionSellerPr
        var imageURL = binding.imageURLSeller
        var price = binding.priceProduct
        var quantity = binding.quantitySellerPr
        var category = binding.autoComSellerAddPrUpdate

        if(name.text!!.isEmpty()) name.error = "Empty error"
        else if(description.text!!.isEmpty()) description.error = "Empty description"
        else if(imageURL.text!!.isEmpty()) imageURL.error = "Empty imageURL"
        else if(!price.text!!.matches(num)) price.error = "Price must be number"
        else if(!quantity.text!!.matches(num)) quantity.error = "Quantity must be number"
        else if(category.text.isEmpty()) category.error = "Select one of category"
        else{
            val retrofit = api_instance.getApiInstance()
            val service = retrofit.create(api_services::class.java)
            val call = service.getCategory(category.text.toString())
            var id_cat:Int

            call.enqueue(object: Callback<Category>{
                override fun onResponse(call: Call<Category>, response: Response<Category>) {
                    id_cat = response.body()?.id!!
                    Log.d("id_cat", "$id_cat")
                    Log.d("id_pro", "$product_id")
                    val tempProduct = Product(
                        product_id,
                        name.text.toString(),
                        description.text.toString(),
                        imageURL.text.toString(),
                        price.text.toString().toInt(),
                        quantity.text.toString().toInt(),
                        id_cat,
                        idseller,
                    )
                    val callUpdate = service.updateProduct(product_id, tempProduct)
                    callUpdate.enqueue(object :Callback<Void>{
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if(response.isSuccessful){
                                Toast.makeText(activity, "Your changes saved!", Toast.LENGTH_SHORT).show()
                                fragmentManager?.beginTransaction()?.replace(R.id.fragmentSellerpage, AllProductsSeller.newInstance())?.commit()
                            }
                            else{
                                Toast.makeText(activity, "${response.message()} onRes", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(activity, "${t.message} on fail", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                override fun onFailure(call: Call<Category>, t: Throwable) {
                    Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }



    private fun AllCategoryNames(): ArrayList<String>{
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllCategories()
        var list = ArrayList<String>()
        call.enqueue(object: Callback<List<Category>> {
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
        fun newInstance() = Product_Item_seller()
    }


}