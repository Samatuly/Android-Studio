package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.backend.api_instance
import com.example.remoteshop.backend.api_services
import com.example.remoteshop.backend.products.Rating
import com.example.remoteshop.databinding.FragmentAddCommentBinding
import com.example.remoteshop.fragments.SellerFragments.AllProductsSeller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddComment : Fragment() {

    lateinit var binding: FragmentAddCommentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCommentBinding.inflate(inflater)

        val numbers = setSlist()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_category, numbers)
        binding.autoComSellerAddPrAddComment.setAdapter(arrayAdapter)

        val args = arguments
        val idProduct = args?.getString("idProduct")?.toInt()!!
        val idClient = args?.getString("idClient")?.toInt()!!


        binding.addCommentBtnCl.setOnClickListener {
            addComment(idClient, idProduct)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val bundle = Bundle()
                    bundle.putString("id", "$idProduct")
                    val fragment = Product_details()
                    fragment.arguments = bundle
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                }
            }
        )
        return binding.root
    }

    private fun addComment(idClient: Int, idProduct: Int) {
        var comment = binding.commentAdText
        var number = binding.autoComSellerAddPrAddComment

        if(comment.text.toString().isEmpty()) comment.error = "Empty comment"
        else if(number.text.isEmpty()) number.error = "Select one number"
        else{
            val retrofit = api_instance.getApiInstance()
            val service = retrofit.create(api_services::class.java)
            val callRating = service.getProductRating(idProduct)

            callRating.enqueue(object: Callback<List<Rating>>{
                override fun onResponse(call: Call<List<Rating>>, response: Response<List<Rating>>) {
                    val ratings = response.body()!!

                    val tempRating = Rating(
                        null,
                        ratings.size,
                        number.text.toString().toInt(),
                        comment.text.toString(),
                        idClient,
                        idProduct
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val call = service.addRating(tempRating)
                        Log.d("es", "${call.isSuccessful}")
                        Log.d("me", "${call.message()}")

                        Thread(Runnable {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(activity, "Successfully added", Toast.LENGTH_SHORT).show()
                                val bundle = Bundle()
                                bundle.putString("id", "$idProduct")
                                val fragment = Product_details()
                                fragment.arguments = bundle
                                fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, fragment)?.commit()
                            })
                        }).start()
                    }

                }

                override fun onFailure(call: Call<List<Rating>>, t: Throwable) {
                    Toast.makeText(activity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun setSlist(): ArrayList<Int> {
        var list = ArrayList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        return  list
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddComment()
    }
}