package com.example.assignment_3.bookData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert
    fun insertBook(book: Book)

    @Query("Select * from book_table")
    fun getAllBooks(): Flow<List<Book>>

    @Query("delete from book_table where bookId like :Sid")
    fun deleteBookById(Sid: Int)

    @Query("update book_table set title=:title, author=:author, num_pages=:num_pages, description=:description, cost=:cost where bookId=:b_Id")
    fun updateBook(b_Id:Int, title:String, author:String, num_pages:Int, description:String, cost:Double)

    @Query("Select * from book_table order by cost desc")
    fun getAllBooksByCostDesc(): Flow<List<Book>>

    @Query("Select * from book_table order by cost")
    fun getAllBooksByCostAsc(): Flow<List<Book>>

}