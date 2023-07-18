package com.example.remoteshop.fragments.ClientFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
class TermsOfUse : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, Settings.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Settings"
                }
            }
        )
        return inflater.inflate(R.layout.fragment_terms_of_use, container, false)
    }

    companion object {
        @JvmStatic fun newInstance() = TermsOfUse()
    }
}