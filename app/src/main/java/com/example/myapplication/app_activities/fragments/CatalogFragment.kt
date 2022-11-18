package com.example.myapplication.app_activities.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.PresenterCatalog
import com.example.myapplication.R
import com.example.myapplication.databinding.CatalogFragmentBinding
import com.example.myapplication.interfaces.IToastRealmSessionErrors
import com.example.myapplication.objects.SharedPrefsIDs

class CatalogFragment : Fragment(R.layout.catalog_fragment), IToastRealmSessionErrors {
    private var binding: CatalogFragmentBinding? = null
    private val presenter by lazy {
        PresenterCatalog(
            this, activity?.getSharedPreferences(
                SharedPrefsIDs.sharedPrefLogInData,
                Context.MODE_PRIVATE
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter.init()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = CatalogFragmentBinding.bind(view)
        binding?.logOutBut?.text = "logOut"
        binding?.logOutBut?.setOnClickListener {
            Log.d("debug", "Start logout")
            presenter.logOut()
            returnToLoginScreen()
        }
        super.onViewCreated(view, savedInstanceState)
        Log.d("debug", "onCreateCatalog completed")
    }

    fun returnToLoginScreen() {
        findNavController().popBackStack(R.id.logInFragment, inclusive = false)
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(
            requireContext(),
            getString(R.string.logInErrorConnectionIssue),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(requireContext(), getString(R.string.unknownRealmError), Toast.LENGTH_LONG)
            .show()
    }

}