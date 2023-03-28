package com.example.assignment_3.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.asLiveData
import com.example.assignment_3.R
import com.example.assignment_3.bookData.Book
import com.example.assignment_3.bookData.BookDatabase
import com.example.assignment_3.databinding.FragmentBookShowBinding
import java.util.*
import kotlin.collections.ArrayList

class BookShow : Fragment() {
    lateinit var binding: FragmentBookShowBinding
    lateinit var listBooks: List<Book>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBookShowBinding.inflate(inflater)
        val context = requireContext()
        val bookDatabase = BookDatabase.getBookDatabase(context)

        bookDatabase.getBookDao().getAllBooks().asLiveData().observe(viewLifecycleOwner){
            listBooks = it
            var adapter= BookAdapter(listBooks)
            binding.recyclerFragment.layoutManager = LinearLayoutManager(activity)
            binding.recyclerFragment.adapter = adapter
        }

        binding.desc.setOnClickListener {
            bookDatabase.getBookDao().getAllBooksByCostDesc().asLiveData().observe(viewLifecycleOwner){
                listBooks = it
                var adapter= BookAdapter(listBooks)
                binding.recyclerFragment.layoutManager = LinearLayoutManager(activity)
                binding.recyclerFragment.adapter = adapter
            }
        }

        binding.asc.setOnClickListener {
            bookDatabase.getBookDao().getAllBooksByCostAsc().asLiveData().observe(viewLifecycleOwner){
                listBooks = it
                var adapter= BookAdapter(listBooks)
                binding.recyclerFragment.layoutManager = LinearLayoutManager(activity)
                binding.recyclerFragment.adapter = adapter
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    var filteredList = ArrayList<Book>()
                    bookDatabase.getBookDao().getAllBooks().asLiveData().observe(viewLifecycleOwner){list ->
                        list.forEach{
                            if(it.title.lowercase(Locale.ROOT).contains(newText)){
                                Log.d("FilterTitle", it.title)
                                filteredList.add(it)
                            }
                            else if(it.author.lowercase(Locale.ROOT).contains(newText)){
                                Log.d("FilterTitle", it.author)
                                filteredList.add(it)
                            }
                        }
                        if(filteredList.size == 0){
                            Toast.makeText(activity, "Not Found!", Toast.LENGTH_SHORT).show()
                            var adapter= BookAdapter(filteredList)
                            binding.recyclerFragment.layoutManager = LinearLayoutManager(activity)
                            binding.recyclerFragment.adapter = adapter
                        }
                        else{
                            var adapter= BookAdapter(filteredList)
                            binding.recyclerFragment.layoutManager = LinearLayoutManager(activity)
                            binding.recyclerFragment.adapter = adapter
                        }
                    }
                }
                return true
            }

        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookShow()
    }
}