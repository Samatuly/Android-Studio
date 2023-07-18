package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteshop.R
import com.example.remoteshop.activities.Seller.ProductAdapterSeller
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.LikeProduct
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentLikedProductsBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import com.example.remoteshop.fragments.SellerFragments.Product_Item_seller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class likedProducts : Fragment() {

    lateinit var binding: FragmentLikedProductsBinding
    lateinit var recyclerViewAdapterProduct: ProductsAdapterClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikedProductsBinding.inflate(inflater)
        val idClient = activity?.intent!!.getIntExtra("id", 0)

        setupRecyclerViewProducts()
        createAdapterProducts(idClient)

        search(idClient)

        return binding.root
    }

    private fun search(idClient: Int) {
        binding.searchLiked.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    var filteredList = java.util.ArrayList<Product>()
                    val retrofit = api_instance.getApiInstance()
                    val service = retrofit.create(api_services::class.java)
                    val callLikes = service.getClientLikes(idClient)
                    var cnt = 0
                    var listOfProduct = ArrayList<Product>()

                    callLikes.enqueue(object: Callback<List<LikeProduct>>{
                        override fun onResponse(call: Call<List<LikeProduct>>, response: Response<List<LikeProduct>>) {
                            var LikeProducts = response.body() as ArrayList<LikeProduct>
                            LikeProducts.forEach {
                                val callPr = service.getProductById(it.product)
                                callPr.enqueue(object: Callback<Product>{
                                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                                        listOfProduct.add(response.body()!!)
                                        cnt++
                                        if(cnt==LikeProducts.size){

                                            listOfProduct.forEach { item->
                                                if(item.name.lowercase().contains(newText)){
                                                    filteredList.add(item)
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
                                                        Log.d("idOf" , filteredList[position].id.toString())
                                                        val fragment = Product_details()
                                                        fragment.arguments = bundle
                                                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                                                    }
                                                })
                                                recyclerViewAdapterProduct.notifyDataSetChanged()
                                            }
                                        }
                                    }
                                    override fun onFailure(call: Call<Product>, t: Throwable) {}
                                })
                            }
                        }
                        override fun onFailure(call: Call<List<LikeProduct>>, t: Throwable) {
                            Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                return true
            }

        })
    }


    private fun createAdapterProducts(idClient:Int) {
        val retrofit = api_instance.getApiInstance()
        val service = retrofit.create(api_services::class.java)
        val callLikes = service.getClientLikes(idClient)
        var cnt = 0
        callLikes.enqueue(object:Callback<List<LikeProduct>>{
            override fun onResponse(
                call: Call<List<LikeProduct>>,
                response: Response<List<LikeProduct>>
            ) {
                val likeProduct = response.body()!!
                if(likeProduct.size == 0){
                    binding.noLikesText.text = "You Don't like any Products"
                }
                else{
                    binding.noLikesText.text = ""
                }
                var listOfProduct = ArrayList<Product>()
                likeProduct.forEach {
                    val callPr = service.getProductById(it.product)
                    callPr.enqueue(object: Callback<Product>{
                        override fun onResponse(call: Call<Product>, response: Response<Product>) {
                            listOfProduct.add(response.body()!!)
                            cnt++
                            Log.d("sizeCnt", cnt.toString())
                            if(cnt==likeProduct.size){
                                recyclerViewAdapterProduct.setList(listOfProduct)
                                recyclerViewAdapterProduct.setOnItemClickListener(object : ProductsAdapterClient.onItemClickListener{
                                    override fun onItemClick(position: Int) {
                                        val bundle = Bundle()
                                        bundle.putString("id", "${listOfProduct[position].id}")
                                        Log.d("idOf" , listOfProduct[position].id.toString())
                                        val fragment = Product_details()
                                        fragment.arguments = bundle
                                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                                    }
                                })
                                recyclerViewAdapterProduct.notifyDataSetChanged()
                            }
                        }
                        override fun onFailure(call: Call<Product>, t: Throwable) {}
                    })
                }
            }
            override fun onFailure(call: Call<List<LikeProduct>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecyclerViewProducts() {
        binding.recViewLiked.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerViewAdapterProduct = ProductsAdapterClient()
            adapter = recyclerViewAdapterProduct
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = likedProducts()
    }
}