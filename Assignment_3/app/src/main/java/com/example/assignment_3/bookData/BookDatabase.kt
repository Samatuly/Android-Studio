package com.example.assignment_3.bookData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Book::class], version = 1)
abstract class BookDatabase: RoomDatabase() {

    abstract fun getBookDao(): BookDao

    companion object{
        fun getBookDatabase(context: Context): BookDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BookDatabase::class.java,
                "books_database"
            ).build()
        }
    }
}