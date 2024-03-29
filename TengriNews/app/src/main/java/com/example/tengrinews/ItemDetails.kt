package com.example.tengrinews

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.tengrinews.Favorites.FavoriteNews
import com.example.tengrinews.Favorites.FavoritesDao
import com.example.tengrinews.databinding.ActivityItemDetailsBinding
import com.example.tengrinews.Favorites.FavoritesDatabsae
import com.example.tengrinews.Favorites.FavoritesEntity

class ItemDetails : AppCompatActivity() {
    lateinit var binding: ActivityItemDetailsBinding
    lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bundle = intent.extras as Bundle
        binding.title2.text = bundle!!.getString("title")
        binding.description2.text = bundle!!.getString("description")
        binding.content.text = bundle!!.getString("content")

        val img = binding.imageItem2
        val url = bundle!!.getString("imgUrl")
        Glide.with(img)
            .load(url)
            .placeholder(R.drawable.image)
            .error(R.drawable.image)
            .fallback(R.drawable.image)
            .into(img)

        val db = FavoritesDatabsae.getNewsDb(this)
        var saveBtn = binding.saveImgBtn
        saveBtn.setOnClickListener{
            val tempSaveNews = FavoritesEntity(
                null,
                bundle!!.getString("content") as String,
                bundle!!.getString("description") as String,
                bundle!!.getString("title") as String,
                bundle!!.getString("imgUrl") as String
            )

            db.getNewsDao().getAllFavouriteNews().asLiveData().observe(this){
                if(it.size == 0){
                    Thread{
                        db.getNewsDao().insertNews(tempSaveNews)
                    }.start()
                    Toast.makeText(this, "Successfully added to favourites", Toast.LENGTH_SHORT).show()
                }
                else{
                    for (i in it){
                        if(i.image == bundle!!.getString("imgUrl") as String){
                            Toast.makeText(this, "Successfully added to favourites", Toast.LENGTH_SHORT).show()
                            break
                        }
                        if(i == it[it.size-1]){
                            Thread{
                                db.getNewsDao().insertNews(tempSaveNews)
                            }.start()
                            Toast.makeText(this, "Successfully added to favourites", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}