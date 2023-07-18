package com.example.remoteshop.fragments.ClientFragments.products_adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.ClientProductItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsAdapterClient: RecyclerView.Adapter<ProductsAdapterClient.productHolder>() {

    var newsList = ArrayList<Product>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setList(arr: ArrayList<Product>){
        this.newsList = arr
        Log.d("newsa", "${this.newsList?.size}")

    }

    class productHolder(item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val binding = ClientProductItemBinding.bind(item)
        val name = binding.nameProd
        val price = binding.costProd
        val des = binding.desPro
        val company = binding.companyProd
        val img = binding.imgProd

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindProduct(product: Product){
            name.text = product.name
            price.text = "${product.price.toString()} tg"
            des.text = product.description

            val retrofit = api_instance.getApiInstance()
            val service = retrofit.create(api_services::class.java)
            val callSeller = service.getSellerById(product.seller)
            callSeller.enqueue(object : Callback<Seller> {
                override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                    company.text = response.body()!!.company_name
                }
                override fun onFailure(call: Call<Seller>, t: Throwable) {}
            })

            val url = product.imageURL
            Glide.with(img)
                .load(url)
                .placeholder(R.drawable.dont_image_24)
                .error(R.drawable.dont_image_24)
                .fallback(R.drawable.dont_image_24)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.client_product_item, parent, false)
        return productHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: productHolder, position: Int) {
        holder.bindProduct(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}