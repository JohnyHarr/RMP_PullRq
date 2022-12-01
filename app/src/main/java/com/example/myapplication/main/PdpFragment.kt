package com.example.myapplication.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.PdpFragmentBinding
import com.example.myapplication.interfaces.IPdp
import com.example.myapplication.models_and_DB.RealmItemData
import com.squareup.picasso.Picasso

class PdpFragment: Fragment(R.layout.pdp_fragment), IPdp {
    private var binding: PdpFragmentBinding?=null
    private val presenter by lazy {
        PdpPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding=PdpFragmentBinding.bind(view)
        binding?.imgBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        Log.d("debug","item_id: "+ arguments?.getString("itemId"))
        arguments?.getString("itemId")?.let { presenter.init(it) }
    }

    override fun onDestroy() {
        presenter.onFragmentDestroy()
        super.onDestroy()
    }

    override fun setupViewWithData(itemData: RealmItemData) {
        Picasso.get().load(itemData._id).into(binding?.pdpImg)
        binding?.tvItemTitle?.text=itemData.itemName
        binding?.tvDescription?.text=itemData.description
    }

}