package com.kyrillosg.rijksstudio

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentCollectionListBinding

class CollectionListFragment : ViewBindingFragment<FragmentCollectionListBinding>(
    bindingProvider = FragmentCollectionListBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Rijkscollection"
        binding.toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}