package com.kyrillosg.rijksstudio

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentFirstBinding

class FirstFragment : ViewBindingFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}