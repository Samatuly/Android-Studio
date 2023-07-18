package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentListOfAllProductsBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListOfAllProducts : Fragment() {

    lateinit var binding: FragmentListOfAllProductsBinding
    lateinit var recyclerViewAdapterProduct: ProductsAdapterClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListOfAllProductsBinding.inflate(inflater)
        val idClient = activity?.intent!!.getIntExtra("id", 0)

        setupRecyclerViewProducts()
        createAdapterProducts()

        search(idClient)

        binding.filterBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, Filter.newInstance())?.commit()
            (activity as AppCompatActivity).supportActionBar?.title = "Filter"
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, HomePage.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Home Page"
                }
            }
        )
        return binding.root
    }

    private fun search(idClient: Int) {

        binding.searchAllProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    var filteredList = java.util.ArrayList<Product>()
                    val retrofit = api_instance.getApiInstance()
                    val service = retrofit.create(api_services::class.java)
                    val callProducts = service.getAllProducts()
                    callProducts.enqueue(object : Callback<List<Product>>{
                        override fun onResponse(
                            call: Call<List<Product>>,
                            response: Response<List<Product>>
                        ) {
                            var products = response.body() as ArrayList<Product>
                            products.forEach {
                                if(it.name.lowercase().contains(newText)){
                                    filteredList.add(it)
                                }
                            }
                            if(filteredList.size == 0){
                                Toast.makeText(activity, "Doest find book!", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                setupRecyclerViewProducts()
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
                        }

                        override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                            Toast.makeText(activity, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                return true
            }

        })

        }

    private fun createAdapterProducts() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllProducts()

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
        binding.recAllProduct.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewAdapterProduct = ProductsAdapterClient()
            adapter = recyclerViewAdapterProduct
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListOfAllProducts()
    }
}