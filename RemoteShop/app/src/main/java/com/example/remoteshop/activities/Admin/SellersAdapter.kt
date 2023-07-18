package com.example.remoteshop.activities.Admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteshop.R
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.AdminSellerItemBinding

class SellersAdapter: RecyclerView.Adapter<SellersAdapter.sellerHolder>() {

    var newsList = ArrayList<Seller>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setList(arr: ArrayList<Seller>){
        this.newsList = arr
        Log.d("newsa", "${this.newsList?.size}")

    }

    class sellerHolder(item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val binding = AdminSellerItemBinding.bind(item)
        val login = binding.usernameSellerAdmin
        val companyName = binding.companynameadmin
        val email = binding.emailSelelradmin

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindSeller(seller: Seller){
            login.text = seller.username
            email.text = seller.email
            companyName.text = seller.company_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sellerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_seller_item, parent, false)
        return sellerHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: sellerHolder, position: Int) {
        holder.bindSeller(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}