package com.example.remoteshop.fragments.ClientFragments.products_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Rating
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.databinding.RatingItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingAdapter: RecyclerView.Adapter<RatingAdapter.RatingHolder>() {

    var newsList = ArrayList<Rating>()

    fun setList(arr: ArrayList<Rating>){
        this.newsList = arr

    }

    class RatingHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = RatingItemBinding.bind(item)

        fun bind(rating: Rating){
            val retrofit = api_instance.getApiInstance()
            val service = retrofit.create(api_services::class.java)
            val clientCall = service.getClientById(rating.client)
            clientCall.enqueue(object : Callback<Client>{
                override fun onResponse(call: Call<Client>, response: Response<Client>) {
                    val client = response.body()!!
                    binding.emailRt.text = client.email
                }

                override fun onFailure(call: Call<Client>, t: Throwable) {
                    binding.emailRt.text = "Cannot identify client name"
                }

            })
            binding.commentRt.text = rating.comment
            binding.ratingNum.text = rating.rating_number.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_item, parent, false)
        return RatingHolder(view)
    }

    override fun onBindViewHolder(holder: RatingHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}