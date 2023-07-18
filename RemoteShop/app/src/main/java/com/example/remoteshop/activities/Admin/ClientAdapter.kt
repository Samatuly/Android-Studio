package com.example.remoteshop.activities.Admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remoteshop.R
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.databinding.AdminClientItemBinding

class ClientAdapter: RecyclerView.Adapter<ClientAdapter.clientHolder>() {

    var newsList = ArrayList<Client>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setList(arr: ArrayList<Client>){
        this.newsList = arr
        Log.d("newsa", "${this.newsList?.size}")

    }

    class clientHolder(item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val binding = AdminClientItemBinding.bind(item)
        val login = binding.usernameClientAdmin
        val companyName = binding.cityAdmin
        val email = binding.emailClientAdmin

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindClient(client: Client){
            login.text = client.username
            email.text = client.email
            companyName.text = client.city
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): clientHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_client_item, parent, false)
        return clientHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: clientHolder, position: Int) {
        holder.bindClient(newsList[position])
    }

    override fun getItemCount(): Int {
        return  newsList.size
    }
}