package com.example.remoteshop.fragments.SellerFragments

import android.os.Bundle
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
import com.example.remoteshop.databinding.FragmentMessageSellerBinding
import com.example.remoteshop.fragments.ClientFragments.products_adapters.CartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageSeller : Fragment() {

    lateinit var binding: FragmentMessageSellerBinding
    lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageSellerBinding.inflate(inflater)

        val idSeller = activity?.intent!!.getIntExtra("id", 0)


        setupRecycler()
        createAdapter(idSeller)


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

    private fun createAdapter(idSeller: Int) {

        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getSellerOrder(idSeller)

        call.enqueue(object: Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: Response<List<Cart>>) {
                var carts = response.body() as ArrayList<Cart>
                var filterCart = ArrayList<Cart>()
                carts.forEach {
                    if(it.check_order == "pending"){
                        filterCart.add(it)
                    }
                }
                if(filterCart.size == 0){
                    binding.emptyMessage.text = "Empty message!"
                }
                messageAdapter.setList(filterCart)
                messageAdapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setupRecycler() {
        binding.recVMess.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            messageAdapter = MessageAdapter(requireContext())
            adapter = messageAdapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MessageSeller()
    }
}