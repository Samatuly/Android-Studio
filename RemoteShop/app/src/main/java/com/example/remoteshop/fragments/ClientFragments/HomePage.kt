package com.example.remoteshop.fragments.ClientFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteshop.R
import com.example.remoteshop.activities.FirstWelcome
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Category
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentHomePageBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.CategoriesAdapter
import com.example.remoteshop.fragments.ClientFragments.products_adapters.PopularProductAdapter
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import com.example.remoteshop.fragments.SellerFragments.Product_Item_seller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : Fragment() {

    lateinit var binding: FragmentHomePageBinding
    lateinit var builder: AlertDialog.Builder

    lateinit var recyclerViewAdapterPopular: PopularProductAdapter
    lateinit var recyclerViewAdapterProduct: ProductsAdapterClient
    lateinit var recyclerViewAdapterCategory: CategoriesAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater)
        builder = AlertDialog.Builder(requireContext())

        setupRecyclerViewPopular()
        createAdapterPopular()

        setupRecyclerViewCategory()
        createAdapterCategory()


        setupRecyclerViewProducts()
        createAdapterProducts()

        binding.allproductsTexxt.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, ListOfAllProducts.newInstance())?.commit()
            (activity as AppCompatActivity).supportActionBar?.title = "All products"
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    builderText()
                }
            }
        )
        return binding.root
    }

    private fun createAdapterProducts() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllProducts()

        call.enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                var products = response.body() as ArrayList<Product>
                Log.d("size", "${products.size}")
                Log.d("succ", "${response.isSuccessful}")
                Log.d("mess", "${response.message()}")

                recyclerViewAdapterProduct.setList(products)
                recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                    override fun onItemClick(position: Int) {
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
        binding.productsRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewAdapterProduct = ProductsAdapterClient()
            adapter = recyclerViewAdapterProduct
        }
    }

    private fun createAdapterCategory() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllCategories()

        call.enqueue(object: Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                var categories = response.body() as ArrayList<Category>

                recyclerViewAdapterCategory.setList(categories)
                recyclerViewAdapterCategory.setOnItemClickListener(object : CategoriesAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putString("id", "${categories[position].id}")
                        bundle.putString("name", "${categories[position].name}")
                        val fragment = CategotyProducts()
                        fragment.arguments = bundle
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                    }
                })
                recyclerViewAdapterCategory.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecyclerViewCategory() {
        binding.categoryRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewAdapterCategory = CategoriesAdapter()
            adapter = recyclerViewAdapterCategory
        }
    }

    private fun createAdapterPopular() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getPopularProducts()

        call.enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                var products = response.body() as ArrayList<Product>

                recyclerViewAdapterPopular.setList(products)
                recyclerViewAdapterPopular.setOnItemClickListener(object : PopularProductAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putString("id", "${products[position].id}")
                        val fragment = Product_details()
                        fragment.arguments = bundle
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                    }
                })
                recyclerViewAdapterPopular.notifyDataSetChanged()

            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecyclerViewPopular() {
        binding.popularRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewAdapterPopular = PopularProductAdapter()
            adapter = recyclerViewAdapterPopular
        }
    }


    private fun builderText() {
        builder.setTitle("Exit Client Account")
            .setMessage("Do you want to log out from client account,then you'll have to log in again!")
            .setPositiveButton("Yes"){id, it ->
                val intent = Intent(activity, FirstWelcome::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No"){id, it ->
                id.cancel()
            }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance()=HomePage()

    }
}