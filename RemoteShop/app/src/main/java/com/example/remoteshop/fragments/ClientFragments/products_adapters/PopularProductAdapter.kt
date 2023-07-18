package com.example.remoteshop.fragments.ClientFragments.products_adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.backend.products.Product
import com.example.remoteshop.databinding.PopularItemBinding

class PopularProductAdapter: RecyclerView.Adapter<PopularProductAdapter.productHolder>() {

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
        val binding = PopularItemBinding.bind(item)
        val name = binding.namePro
        val price = binding.costPro
        val img = binding.imgPr

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindProduct(product: Product){
            name.text = product.name
            price.text = "${product.price.toString()} tg"

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
        return productHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: productHolder, position: Int) {
        holder.bindProduct(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}