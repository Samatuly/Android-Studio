package com.example.assignment_3.bookData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "book_table")
data class Book(
    @PrimaryKey(autoGenerate = true)
    var bookId: Int? = null,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "author")
    var author: String,
    @ColumnInfo(name = "num_pages")
    var num_pages: Int,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "cost")
    var cost: Double
)