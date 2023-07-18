package com.example.remoteshop.fragments.ClientFragments.products_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.remoteshop.R
import com.example.remoteshop.backend.products.Category
import com.example.remoteshop.databinding.CategoryItemBinding

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.productHolder>() {

    var newsList = ArrayList<Category>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setList(arr: ArrayList<Category>){
        this.newsList = arr

    }

    class productHolder(item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val binding = CategoryItemBinding.bind(item)
        val name = binding.contegoryName
        val img = binding.imgPro

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindProduct(category: Category){
            name.text =category.name
            val url = category.imageURL
            Glide.with(img)
                .load(url)
                .placeholder(R.drawable.dont_image_24)
                .error(R.drawable.dont_image_24)
                .fallback(R.drawable.dont_image_24)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return productHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: productHolder, position: Int) {
        holder.bindProduct(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}