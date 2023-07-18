package com.example.remoteshop.fragments.SellerFragments

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Cart
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.databinding.MessageItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageAdapter (val context: Context): RecyclerView.Adapter<MessageAdapter.RatingHolder>() {

    var newsList = ArrayList<Cart>()

    fun setList(arr: ArrayList<Cart>){
        this.newsList = arr

    }

    class RatingHolder(item: View, context: Context): RecyclerView.ViewHolder(item){
        val binding = MessageItemBinding.bind(item)

        val retrofit = api_instance.getApiInstance()
        var builder = AlertDialog.Builder(context)
        val service = retrofit.create(api_services::class.java)

        val context = context

        fun bind(cart: Cart){
            val productCall = service.getProductById(cart.product)
            productCall.enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    val product = response.body()!!
                    binding.prNameMess.text =  product.name
                    binding.idPrMess.text = product.id.toString()
                    val callClient = service.getClientById(cart.client)
                    callClient.enqueue(object : Callback<Client> {
                        override fun onResponse(call: Call<Client>, response: Response<Client>) {
                            binding.usernamePrMess.text = response.body()?.username!!
                        }
                        override fun onFailure(call: Call<Client>, t: Throwable) {}
                    })
                }
                override fun onFailure(call: Call<Product>, t: Throwable) {}
            })
        }


        fun noBtn(cart: Cart){
            binding.noBtn.setOnClickListener {
                builder.setTitle("Block order")
                    .setMessage("Are you sure block this order?")
                    .setPositiveButton("Yes"){inter, it ->
                        val callUpdate = service.updateCart(cart.id!!, Cart(
                            cart.id,
                            "no",
                            cart.client,
                            cart.product,
                            cart.seller,
                        )
                        )
                        callUpdate.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Toast.makeText(context, "You block this order", Toast.LENGTH_SHORT).show()
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
        }

        fun yesBtn(cart: Cart){
            binding.yesBtn.setOnClickListener {
                builder.setTitle("Accept order")
                    .setMessage("Are you sure accept this order?")
                    .setPositiveButton("Yes"){inter, it ->
                        val callUpdate = service.updateCart(cart.id!!, Cart(
                            cart.id,
                            "yes",
                            cart.client,
                            cart.product,
                            cart.seller,
                        )
                        )
                        callUpdate.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if(response.isSuccessful){
                                    Toast.makeText(context, "You accept this order", Toast.LENGTH_SHORT).show()
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


            if(cart.check_order == "cart"){

            }
            else if(cart.check_order == "pending"){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return RatingHolder(view, context)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.bind(newsList[position])
        holder.noBtn(newsList[position])
        holder.yesBtn(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}