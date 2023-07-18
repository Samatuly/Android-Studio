package com.example.remoteshop.activities.Seller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.ProductItemSellerBinding

class ProductAdapterSeller: RecyclerView.Adapter<ProductAdapterSeller.productHolder>() {

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
        val binding = ProductItemSellerBinding.bind(item)
        val name = binding.namePrSeller
        val price = binding.pricePrSeller
        val quantity = binding.QuntitySeller
        val img = binding.imageSeller

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindProduct(product: Product){
            name.text = product.name
            price.text = product.price.toString()
            quantity.text = product.quantity.toString()

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_seller, parent, false)
        return productHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: productHolder, position: Int) {
        holder.bindProduct(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}