package com.kyrillosg.rijksstudio

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentCollectionDetailBinding

class CollectionDetailFragment : ViewBindingFragment<FragmentCollectionDetailBinding>(
    bindingProvider = FragmentCollectionDetailBinding::inflate
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Rijksdetail"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}