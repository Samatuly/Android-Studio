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
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentCategotyProductsBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategotyProducts : Fragment() {

    lateinit var binding: FragmentCategotyProductsBinding
    lateinit var recyclerViewAdapterProduct: ProductsAdapterClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategotyProductsBinding.inflate(inflater)

        val args = arguments
        val categoryId = args?.getString("id")!!.toInt()
        val catName = args?.getString("name")!!
        (activity as AppCompatActivity).supportActionBar?.title = catName


        setupRecyclerViewProducts()
        createAdapterProducts(categoryId)

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

    private fun createAdapterProducts(categoryId:Int) {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getProductsByCategory(categoryId)

        call.enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                var products = response.body() as ArrayList<Product>
                recyclerViewAdapterProduct.setList(products)
                recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        Toast.makeText(activity, "$position clicked", Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        bundle.putString("id", "${products[position].id}")
                        val fragment = Product_details()
                        fragment.arguments = bundle
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                    }
                })
                recyclerViewAdapterProduct.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecyclerViewProducts() {
        binding.catRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewAdapterProduct = ProductsAdapterClient()
            adapter = recyclerViewAdapterProduct
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategotyProducts()
    }
}