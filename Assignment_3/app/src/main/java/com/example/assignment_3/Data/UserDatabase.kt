package com.example.assignment_3.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getDao(): UserDao

    companion object{
        fun getUserDatabase(context: Context): UserDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "app_database"
            ).build()
        }
    }
}