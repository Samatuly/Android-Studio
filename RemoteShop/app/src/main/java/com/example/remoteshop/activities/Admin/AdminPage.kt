package com.example.remoteshop.activities.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.remoteshop.activities.FirstWelcome
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.users.Client
import com.example.remoteshop.backend.users.Seller
import com.example.remoteshop.databinding.ActivityAdminPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPage : AppCompatActivity() {

    lateinit var binding: ActivityAdminPageBinding
    lateinit var recyclerViewAdapter: SellersAdapter
    lateinit var recyclerViewAdapterClient: ClientAdapter
    lateinit var builder: AlertDialog.Builder
    var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPageBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(this)
        setContentView(binding.root)

        supportActionBar?.title = "Sellers"
        initRecyclerView()
        createData()

        binding.listofclients.setOnClickListener {
            supportActionBar?.title = "Clients"
            initRecyclerViewClient()
            createDataClient()
        }

        binding.listofsellers.setOnClickListener {
            supportActionBar?.title = "Sellers"
            initRecyclerView()
            createData()
        }
    }

    private fun createDataClient() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllClients()

        call.enqueue(object : Callback<List<Client>> {
            override fun onResponse(call: Call<List<Client>>, response: Response<List<Client>>) {
                var clients = response.body() as ArrayList<Client>
                Log.d("is","${response.isSuccessful}")
                Log.d("is","${clients?.size}")
                recyclerViewAdapterClient.setList(clients)
                recyclerViewAdapterClient.setOnItemClickListener(object : ClientAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        intent = Intent(this@AdminPage, ClientItemDetails::class.java)
                        intent.putExtra("id", clients[position].id)
                        intent.putExtra("username", clients[position].username)
                        intent.putExtra("email", clients[position].email)
                        intent.putExtra("city", clients[position].city)
                        startActivity(intent)
                    }
                })
                recyclerViewAdapterClient.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Client>>, t: Throwable) {
                Toast.makeText(this@AdminPage, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.d("clients", "${t.message}")

            }
        })
    }

    private fun initRecyclerViewClient() {
        var recyclerView  = binding.recvAdminPage

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminPage)
            recyclerViewAdapterClient = ClientAdapter()
            adapter = recyclerViewAdapterClient
        }
    }

    private fun createData() {
        val api = api_instance.getApiInstance().create(api_services::class.java)
        val call = api.getAllSellers()

        call.enqueue(object : Callback<List<Seller>> {
            override fun onResponse(call: Call<List<Seller>>, response: Response<List<Seller>>) {
                var sellers = response.body() as ArrayList<Seller>
                Log.d("is","${response.isSuccessful}")
                Log.d("is","${sellers?.size}")
                recyclerViewAdapter.setList(sellers)
                recyclerViewAdapter.setOnItemClickListener(object : SellersAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        intent = Intent(this@AdminPage, SellerItemDetails::class.java)
                        intent.putExtra("id", sellers[position].id)
                        intent.putExtra("username", sellers[position].username)
                        intent.putExtra("email", sellers[position].email)
                        intent.putExtra("companyName", sellers[position].company_name)
                        startActivity(intent)
                    }
                })
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Seller>>, t: Throwable) {
                Toast.makeText(this@AdminPage, "${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.d("sellers", "${t.message}")

            }
        })
    }

    private fun initRecyclerView() {
        var recyclerView  = binding.recvAdminPage

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminPage)
            recyclerViewAdapter = SellersAdapter()
            adapter = recyclerViewAdapter
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 10 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            builder.setTitle("Exit Admin Account")
                .setMessage("Do you want to log out from admin account,then you'll have to log in again!")
                .setPositiveButton("Yes"){id, it ->
                    val intent = Intent(this, FirstWelcome::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No"){id, it ->
                    id.cancel()
                }
                .show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}