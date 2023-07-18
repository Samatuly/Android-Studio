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
import com.example.remoteshop.backend.products.Cart
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.FragmentCartListBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.CartAdapter
import com.example.remoteshop.fragments.ClientFragments.products_adapters.ProductsAdapterClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartList : Fragment() {

    lateinit var binding: FragmentCartListBinding
    lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartListBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Cart"


        val idClient = activity?.intent!!.getIntExtra("id", 0)

        setupRecycler()
        createAdapter(idClient)

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

    private fun createAdapter(idClient:Int) {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getClientCart(idClient)

        call.enqueue(object : Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                var carts = response.body() as ArrayList<Cart>
                if (carts.size == 0) {
                    binding.emptyCart.text = "Empty cart!"
                }
                cartAdapter.setList(carts)
                cartAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecycler() {
        binding.cartRec.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            cartAdapter = CartAdapter(requireContext())
            adapter = cartAdapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = CartList()
    }
}