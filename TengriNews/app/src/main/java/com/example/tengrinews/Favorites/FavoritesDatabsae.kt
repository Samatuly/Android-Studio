package com.example.tengrinews.Favorites

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FavoritesEntity::class], version = 1)
abstract class FavoritesDatabsae: RoomDatabase() {

    abstract fun getNewsDao():FavoritesDao

    companion object{
        fun getNewsDb(context: Context):FavoritesDatabsae{
            return Room.databaseBuilder(
                context.applicationContext,
                FavoritesDatabsae::class.java,
                "FavoritesEntity.db2"
            ).build()
        }
    }

}