package com.example.assignment_3.Fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_3.R
import com.example.assignment_3.bookData.Book
import com.example.assignment_3.databinding.BookBoxBinding

class BookAdapter(var bookList:List<Book>): RecyclerView.Adapter<BookAdapter.BookHolder>() {
    class BookHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = BookBoxBinding.bind(item)
        fun binkBooks(book:Book) = with(binding){
            titleBook.text = book.title
            authorBook.text = book.author
            costBook.text = book.cost.toString() + " tenge"
            descBook.text = book.description
        }
    }

    fun setFilteredBooks(bookList:List<Book>){
        this.bookList = bookList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_box, parent, false)
        return BookHolder(view)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.binkBooks(bookList[position])
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

}