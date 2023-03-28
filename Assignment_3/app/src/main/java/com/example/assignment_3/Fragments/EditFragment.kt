package com.example.assignment_3.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment_3.R
import com.example.assignment_3.bookAdapterAdmin
import com.example.assignment_3.bookData.Book
import com.example.assignment_3.bookData.BookDatabase
import com.example.assignment_3.databinding.FragmentEditBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditFragment : Fragment() {
    lateinit var binding: FragmentEditBinding
    lateinit var listBooks: List<Book>
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditBinding.inflate(inflater)
        val context = requireContext()
        val bookDatabase = BookDatabase.getBookDatabase(context)



        bookDatabase.getBookDao().getAllBooks().asLiveData().observe(viewLifecycleOwner){
            listBooks = it
            var adapter2 = bookAdapterAdmin(listBooks, context)
            binding.recyclerView2.layoutManager = LinearLayoutManager(activity)
            binding.recyclerView2.adapter = adapter2
        }

        binding.addBookBtn2.setOnClickListener {
            val destinationFragment = AddBookFragment()
            val transction = fragmentManager?.beginTransaction()
            transction?.replace(R.id.frameLayout,destinationFragment)?.commit()
        }
        return binding.root
    }


    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
                EditFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}