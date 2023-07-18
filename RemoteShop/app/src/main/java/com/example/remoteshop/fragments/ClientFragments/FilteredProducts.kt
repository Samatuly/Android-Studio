package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Category
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.FragmentFilteredProductsBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilteredProducts : Fragment() {

    lateinit var binding: FragmentFilteredProductsBinding
    lateinit var recyclerViewAdapterProduct: ProductsAdapterClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilteredProductsBinding.inflate((inflater))
        (activity as AppCompatActivity).supportActionBar?.title = "Filtered Products"
        val args = arguments
        val category = args?.getString("category")!!
        val company = args?.getString("company")!!
        val priceFrom = args?.getString("priceFrom")?.toInt()!!
        val priceTo = args?.getString("priceTo")?.toInt()!!
        val sortAsc = args?.getBoolean("sortAsc")!!
        val sortDesc = args?.getBoolean("sortDesc")!!

        Log.d("cat", category)
        Log.d("com", company)
        Log.d("pF", priceFrom.toString())
        Log.d("pT", priceTo.toString())
        Log.d("sA", sortAsc.toString())
        Log.d("sD", sortDesc.toString())

        setupRecyclerViewProducts()
        createAdapterProducts(category, company, priceFrom, priceTo, sortAsc, sortDesc)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, ListOfAllProducts.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "All Products"
                }
            }
        )
        return binding.root
    }

    private fun createAdapterProducts(category:String, company : String, priceFrom:Int,
                                      priceTo:Int, sortAsc:Boolean, sortDesc:Boolean) {
        val api = api_instance.getApiInstance().create(api_services::class.java)

        if(category != "no" && company != "no"){
            val callCategory = api.getCategory(category)
            callCategory.enqueue(object: Callback<Category>{
                override fun onResponse(call: Call<Category>, response: Response<Category>){
                    val categoryId = response.body()?.id!!
                    val callCompanyName = api.getCompanyName(company)
                    callCompanyName.enqueue(object : Callback<Seller>{
                        override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                            val companyId = response.body()?.id!!
                            val callComCat = api.getComCat(companyId, categoryId)
                            callComCat.enqueue(object : Callback<List<Product>>{
                                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                                    val list = response.body()!! as ArrayList<Product>
                                    var filteredList = ArrayList<Product>()
                                    list.forEach {
                                        if(it.price in priceFrom..priceTo){
                                            filteredList.add(it)
                                        }
                                    }
                                    if(filteredList.size == 0){
                                        binding.noListFilter.text = "Don't match any product"
                                    }
                                    if(sortDesc){
                                        filteredList.sortByDescending { it.price }
                                    }
                                    else if(sortAsc) {
                                        filteredList.sortBy { it.price }
                                    }
                                    recyclerViewAdapterProduct.setList(filteredList)
                                    recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                                        override fun onItemClick(position: Int) {
                                            val bundle = Bundle()
                                            bundle.putString("id", "${filteredList[position].id}")
                                            val fragment = Product_details()
                                            fragment.arguments = bundle
                                            fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                                        }
                                    })
                                    recyclerViewAdapterProduct.notifyDataSetChanged()
                                }
                                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                            })
                        }
                        override fun onFailure(call: Call<Seller>, t: Throwable) {}
                    })
                }
                override fun onFailure(call: Call<Category>, t: Throwable) {}
            })
        }
        else if(category == "no"  && company != "no"){
            val callCompanyName = api.getCompanyName(company)
            callCompanyName.enqueue(object : Callback<Seller>{
                override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                    val companyId = response.body()?.id!!
                    val callComCat = api.getSelletProducts(companyId)
                    callComCat.enqueue(object : Callback<List<Product>>{
                        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                            val list = response.body()!! as ArrayList<Product>
                            var filteredList = ArrayList<Product>()
                            list.forEach {
                                if(it.price in priceFrom..priceTo){
                                    filteredList.add(it)
                                }
                            }
                            if(filteredList.size == 0){
                                binding.noListFilter.text = "Don't match any product"
                            }
                            if(sortDesc){
                                filteredList.sortByDescending { it.price }
                            }
                            else if(sortAsc) {
                                filteredList.sortBy { it.price }
                            }
                            recyclerViewAdapterProduct.setList(filteredList)
                            recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle()
                                    bundle.putString("id", "${filteredList[position].id}")
                                    val fragment = Product_details()
                                    fragment.arguments = bundle
                                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                                }
                            })
                            recyclerViewAdapterProduct.notifyDataSetChanged()
                        }
                        override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                    })
                }
                override fun onFailure(call: Call<Seller>, t: Throwable) {}
            })
        }
        else if(category != "no"  && company == "no"){
            val callCategory = api.getCategory(category)
            callCategory.enqueue(object: Callback<Category>{
                override fun onResponse(call: Call<Category>, response: Response<Category>){
                    val categoryId = response.body()?.id!!

                    val callCategoryList = api.getProductsByCategory(categoryId)

                    callCategoryList.enqueue(object : Callback<List<Product>>{
                        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                            val list = response.body()!! as ArrayList<Product>
                            var filteredList = ArrayList<Product>()
                            list.forEach {
                                if(it.price in priceFrom..priceTo){
                                    filteredList.add(it)
                                }
                            }
                            if(filteredList.size == 0){
                                binding.noListFilter.text = "Don't match any product"
                            }
                            if(sortDesc){
                                filteredList.sortByDescending { it.price }
                            }
                            else if(sortAsc) {
                                filteredList.sortBy { it.price }
                            }
                            recyclerViewAdapterProduct.setList(filteredList)
                            recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle()
                                    bundle.putString("id", "${filteredList[position].id}")
                                    val fragment = Product_details()
                                    fragment.arguments = bundle
                                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                                }
                            })
                            recyclerViewAdapterProduct.notifyDataSetChanged()
                        }
                        override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                    })
                }
                override fun onFailure(call: Call<Category>, t: Throwable) {}
            })
        }
        else{
            val call = api.getAllProducts()
            call.enqueue(object : Callback<List<Product>>{
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    var list = response.body()!! as ArrayList<Product>
                    var filteredList = ArrayList<Product>()
                    list.forEach {
                        if(it.price in priceFrom..priceTo){
                            filteredList.add(it)
                        }
                    }
                    if(filteredList.size == 0){
                        binding.noListFilter.text = "Don't match any product"
                    }
                    if(sortDesc){
                        filteredList.sortByDescending { it.price }
                    }
                    else if(sortAsc) {
                        filteredList.sortBy { it.price }
                    }
                    recyclerViewAdapterProduct.setList(filteredList)
                    recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val bundle = Bundle()
                            bundle.putString("id", "${filteredList[position].id}")
                            val fragment = Product_details()
                            fragment.arguments = bundle
                            fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                        }
                    })
                    recyclerViewAdapterProduct.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
            })
        }
    }

    private fun setupRecyclerViewProducts() {
        binding.filteredRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewAdapterProduct = ProductsAdapterClient()
            adapter = recyclerViewAdapterProduct
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FilteredProducts()
    }
}