package com.kyrillosg.rijksstudio

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentSecondBinding

class SecondFragment : ViewBindingFragment<FragmentSecondBinding>(FragmentSecondBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}