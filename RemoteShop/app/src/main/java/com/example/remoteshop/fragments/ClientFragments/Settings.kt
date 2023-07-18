package com.example.remoteshop.fragments.ClientFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.remoteshop.R
import com.example.remoteshop.activities.FirstWelcome
import com.example.remoteshop.databinding.FragmentSettingsBinding

class Settings : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        builder = AlertDialog.Builder(requireContext())

        binding.termstouse.setOnClickListener { terms()}
        binding.logOutClient.setOnClickListener {logout()}
        binding.help.setOnClickListener {help()}
        binding.PrivacyPolicy.setOnClickListener {privacy()}


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, HomePage.newInstance())?.commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Home page"
                }
            }
        )
        return binding.root
    }



    private fun privacy() {
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, PrivacyPolicy.newInstance())?.commit()
        (activity as AppCompatActivity).supportActionBar?.title = "Privacy Policy"
    }

    private fun help() {
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, Help.newInstance())?.commit()
        (activity as AppCompatActivity).supportActionBar?.title = "Help"
    }

    private fun logout() {
        builder.setTitle("Exit Client Account")
            .setMessage("Do you want to log out from client account,then you'll have to log in again!")
            .setPositiveButton("Yes"){id, it ->
                val intent = Intent(activity, FirstWelcome::class.java)
                startActivity(intent)
            }
            .setNegativeButton("No"){id, it ->
                id.cancel()
            }
            .show()
    }

    private fun payment() {
        Toast.makeText(activity, "Payment", Toast.LENGTH_SHORT).show()    }

    private fun terms() {
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_client_page, TermsOfUse.newInstance())?.commit()
        (activity as AppCompatActivity).supportActionBar?.title = "Terms of Use"
    }

    companion object {
        @JvmStatic
        fun newInstance() = Settings()
    }
}