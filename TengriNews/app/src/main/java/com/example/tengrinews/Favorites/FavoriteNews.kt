package com.example.tengrinews.Favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tengrinews.databinding.ActivitySavedNewsBinding
import com.example.tengrinews.ItemDetails
import com.example.tengrinews.MainActivity

class FavoriteNews : AppCompatActivity() {
    lateinit var binding: ActivitySavedNewsBinding
    lateinit var recAdapter: FavoritesAdapter
    var backPressedTime: Long = 0
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedNewsBinding.inflate(layoutInflater)
        builder = AlertDialog.Builder(this)
        setContentView(binding.root)

        supportActionBar?.title = "Saved News"

        initRecyclerView()

    }

    private fun initRecyclerView() {
        var recyclerView  = binding.recyclerSave

        recyclerView.apply {
            val db = FavoritesDatabsae.getNewsDb(this@FavoriteNews)
            db.getNewsDao().getAllSavedNews().asLiveData().observe(this@FavoriteNews){
                if(it.size == 0){
                    Toast.makeText(this@FavoriteNews, "No saved news!!!", Toast.LENGTH_SHORT).show()
                }
                layoutManager = LinearLayoutManager(this@FavoriteNews)
                recAdapter = FavoritesAdapter(it, this@FavoriteNews)
                recAdapter.setOnItemClickListener(object : FavoritesAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        intent = Intent(this@FavoriteNews, ItemDetails::class.java)
                        intent.putExtra("title", it[position].title)
                        intent.putExtra("description", it[position].description)
                        intent.putExtra("content", it[position].content)
                        intent.putExtra("imgUrl", it[position].image)
                        startActivity(intent)
                    }
                })
                adapter = recAdapter
                recAdapter.notifyDataSetChanged()
            }
        }
    }
    override fun onBackPressed() {
        if (backPressedTime + 10 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            builder.setTitle("Exit Saves News")
                .setMessage("Do you want to log out from saved news ?")
                .setPositiveButton("Yes"){id, it ->
                    val intent = Intent(this, MainActivity::class.java)
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