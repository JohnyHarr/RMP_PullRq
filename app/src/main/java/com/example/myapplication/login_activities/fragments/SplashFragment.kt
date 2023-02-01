package com.example.myapplication.login_activities.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.SplashFragmentBinding

class SplashFragment: Fragment(R.layout.splash_fragment) {

    private val binding by lazy { SplashFragmentBinding.inflate(layoutInflater)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}