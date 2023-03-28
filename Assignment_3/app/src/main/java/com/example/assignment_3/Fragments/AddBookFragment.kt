package com.example.assignment_3.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.assignment_3.R
import com.example.assignment_3.bookData.Book
import com.example.assignment_3.bookData.BookDatabase
import com.example.assignment_3.databinding.FragmentAddBookBinding
import java.util.regex.Pattern


class AddBookFragment : Fragment() {
    lateinit var binding: FragmentAddBookBinding
    val num = Pattern.compile("^[0-9]+\$").toRegex()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddBookBinding.inflate(inflater)
        val context = requireActivity()
        val database = BookDatabase.getBookDatabase(context)

        binding.addBook2Btn.setOnClickListener{
            if(binding.title.text.isEmpty()) binding.title.error = "Empty title!"
            else if(binding.author.text.isEmpty()) binding.author.error = "Empty author!"
            else if(binding.numPages.text.isEmpty()) binding.author.error = "Empty number of pages!"
            else if(binding.description.text.isEmpty()) binding.description.error = "Empty description!"
            else if(binding.cost.text.isEmpty()) binding.cost.error = "Empty cost!"
            else if(binding.cost.text.matches(num)) binding.cost.error = "Cost must be number!"
            else{
                var temp = Book(
                    null,
                    binding.title.text.toString(),
                    binding.author.text.toString(),
                    binding.numPages.text.toString().toInt(),
                    binding.description.text.toString(),
                    binding.cost.text.toString().toDouble(),
                )
                Thread{
                    database.getBookDao().insertBook(temp)
                }.start()
                Toast.makeText(context, "Book succesfully added!", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, com.example.assignment_3.AfterSignInAdminActivity::class.java)
                startActivity(intent)
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddBookFragment()
    }
}