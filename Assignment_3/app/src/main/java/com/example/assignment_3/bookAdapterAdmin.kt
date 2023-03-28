package com.example.assignment_3

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_3.bookData.Book
import com.example.assignment_3.bookData.BookDatabase
import com.example.assignment_3.databinding.AdminBookBinding
import java.util.regex.Pattern

class bookAdapterAdmin(val listBook:List<Book>, val contex: Context): RecyclerView.Adapter<bookAdapterAdmin.Holder>() {
    class Holder(item: View, contex: Context):RecyclerView.ViewHolder(item){
        var binding =AdminBookBinding.bind(item)
        var builder = AlertDialog.Builder(contex)
        val contextt = contex
        fun binkBooks(book: Book) = with(binding){
            titleAdmin.text = Editable.Factory.getInstance().newEditable(book.title)
            authorAdmin.text = Editable.Factory.getInstance().newEditable(book.author)
            numPagesAdmin.text = Editable.Factory.getInstance().newEditable(book.num_pages.toString())
            descriptionAdmin.text = Editable.Factory.getInstance().newEditable(book.description)
            costAdmin.text = Editable.Factory.getInstance().newEditable(book.cost.toString())
        }
        fun clickListenerDelete(book:Book){
            binding.deleteAdmin.setOnClickListener {
                val db = BookDatabase.getBookDatabase(contextt)
                val id:Int = book.bookId as Int
                builder.setTitle("Delete Book")
                    .setMessage("Are you sure delete this book? Then can not be restored!")
                    .setPositiveButton("Yes"){inter, it ->
                        Thread{
                            db.getBookDao().deleteBookById(id)
                        }.start()
                        val intent = Intent(contextt, com.example.assignment_3.EditActivity::class.java)
                        contextt.startActivity(intent)
                    }
                    .setNegativeButton("No"){inter, it ->
                        inter.cancel()
                    }
                    .show()
            }
        }
        fun clickListenerSave(book:Book){
            binding.saveAdmin.setOnClickListener {
                val db = BookDatabase.getBookDatabase(contextt)
                val id:Int = book.bookId as Int
                val num = Pattern.compile("^[0-9]+\$").toRegex()
                if(binding.titleAdmin.text.isEmpty()) binding.titleAdmin.error = "Empty title!"
                else if(binding.authorAdmin.text.isEmpty()) binding.authorAdmin.error = "Empty author!"
                else if(binding.numPagesAdmin.text.isEmpty()) binding.numPagesAdmin.error = "Empty number of pages!"
                else if(binding.descriptionAdmin.text.isEmpty()) binding.descriptionAdmin.error = "Empty description!"
                else if(binding.costAdmin.text.isEmpty()) binding.costAdmin.error = "Empty cost!"
                else if(!binding.costAdmin.text.matches(num)) binding.costAdmin.error = "Cost must be number!"
                else if(!binding.titleAdmin.text.isEmpty()){
                    val cost:Double = binding.costAdmin.text.toString().toDouble()
                    builder.setTitle("Update Book")
                        .setMessage("Are you sure save this book changes?")
                        .setPositiveButton("Yes"){inter, it ->
                            Thread{
                                db.getBookDao().updateBook(id,
                                    binding.titleAdmin.text.toString(),
                                    binding.authorAdmin.text.toString(),
                                    binding.numPagesAdmin.text.toString().toInt(),
                                    binding.descriptionAdmin.text.toString(),
                                    cost)
                            }.start()
                            val intent = Intent(contextt, com.example.assignment_3.EditActivity::class.java)
                            contextt.startActivity(intent)
                        }
                        .setNegativeButton("No"){inter, it ->
                            inter.cancel()
                        }
                        .show()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_book, parent, false)
        return Holder(view,contex)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binkBooks(listBook[position])
        holder.clickListenerDelete(listBook[position])
        holder.clickListenerSave(listBook[position])
    }

    override fun getItemCount(): Int {
        return listBook.size
    }
}