package com.example.remoteshop.fragments.ClientFragments.products_adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.activities.Client.ClientPage
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Cart
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.CartItemBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(val context: Context): RecyclerView.Adapter<CartAdapter.RatingHolder>() {

    var newsList = ArrayList<Cart>()

    fun setList(arr: ArrayList<Cart>){
        this.newsList = arr

    }

    class RatingHolder(item: View, context: Context): RecyclerView.ViewHolder(item){
        val binding = CartItemBinding.bind(item)
        val retrofit = api_instance.getApiInstance()
        var builder = AlertDialog.Builder(context)
        val service = retrofit.create(api_services::class.java)

        val context = context

        fun bind(cart: Cart){
            val productCall = service.getProductById(cart.product)
            productCall.enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    val product = response.body()!!
                    binding.nameCart.text =  product.name
                    binding.priceCart.text = "${product.price.toString()} tg"
                    val img = binding.imgCart
                    val url = product.imageURL
                    Glide.with(img)
                        .load(url)
                        .placeholder(R.drawable.dont_image_24)
                        .error(R.drawable.dont_image_24)
                        .fallback(R.drawable.dont_image_24)
                        .into(img)
                    val callSeller = service.getSellerById(product.seller)
                    callSeller.enqueue(object : Callback<Seller>{
                        override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                            binding.compCart.text = response.body()?.company_name!!
                        }
                        override fun onFailure(call: Call<Seller>, t: Throwable) {}
                    })
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {}
            })
            if(cart.check_order == "pending"){
                binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_clock_24))
            }
            if(cart.check_order == "cart"){
                binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_shopping_cart_24))
            }
            if(cart.check_order == "yes"){
                binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24))
            }
            if(cart.check_order == "no"){
                binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_block_24))
            }
        }


        fun deleteCart(cart: Cart){
            binding.deleteCartBtb.setOnClickListener {
                builder.setTitle("Delete Product")
                    .setMessage("Are you sure delete this product? ")
                    .setPositiveButton("Yes"){inter, it ->
                        val callDelete = service.deleteFromCart(cart.id!!)
                        callDelete.enqueue(object : Callback<ResponseBody>{
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if(response.isSuccessful){
                                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                        })
                    }
                    .setNegativeButton("No"){inter, it ->
                        inter.cancel()
                    }
                    .show()
            }
        }

        fun sent(cart:Cart){
            binding.buyBtn.setOnClickListener {
                if(cart.check_order == "cart"){
                    builder.setTitle("Sent message")
                        .setMessage("Are you want to sent message to seller")
                        .setPositiveButton("Yes"){inter, it ->
                            val callUpdate = service.updateCart(cart.id!!, Cart(
                                cart.id,
                                "pending",
                                cart.client,
                                cart.product,
                                cart.seller,
                            ))
                            callUpdate.enqueue(object : Callback<Void>{
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if(response.isSuccessful){
                                        Toast.makeText(context, "Your message send", Toast.LENGTH_SHORT).show()
                                        binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_clock_24))
                                    }
                                }
                                override fun onFailure(call: Call<Void>, t: Throwable) {}
                            })
                        }
                        .setNegativeButton("No"){inter, it ->
                            inter.cancel()
                        }
                        .show()
                }
                else if(cart.check_order == "pending"){
                    Toast.makeText(context, "Your message already sended!", Toast.LENGTH_SHORT).show()
                    binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_clock_24))
                }
                else if(cart.check_order == "yes"){
                    Toast.makeText(context, "Your message true", Toast.LENGTH_SHORT).show()
                    binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24))
                }
                else if(cart.check_order == "no"){
                    Toast.makeText(context, "Your message blocked!", Toast.LENGTH_SHORT).show()
                    binding.buyBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_block_24))
                }
            }


            if(cart.check_order == "cart"){

            }
            else if(cart.check_order == "pending"){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return RatingHolder(view, context)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.bind(newsList[position])
        holder.deleteCart(newsList[position])
        holder.sent(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}