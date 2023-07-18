package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.databinding.FragmentPrivacyPolicyBinding


class PrivacyPolicy : Fragment() {

    lateinit var binding: FragmentPrivacyPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater)



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, Settings.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Settings"
                }
            }
        )
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = PrivacyPolicy()
    }
}