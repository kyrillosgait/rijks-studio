package com.kyrillosg.rijksstudio.feature.collection

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kyrillosg.rijksstudio.R
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.core.ui.toast
import com.kyrillosg.rijksstudio.databinding.FragmentCollectionListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionListFragment : ViewBindingFragment<FragmentCollectionListBinding>(
    bindingProvider = FragmentCollectionListBinding::inflate
) {

    private val viewModel: CollectionListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Rijkscollection"
        binding.toolbar.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.collectionItems.collect { uiState ->
                    renderState(uiState)
                }
            }
        }
    }

    private fun renderState(uiState: CollectionListUiState) {
        when (uiState) {
            CollectionListUiState.Error -> {
                toast("An error occurred")
            }
            CollectionListUiState.Loading -> {
                toast("Loading...")
            }
            is CollectionListUiState.Success -> {
                toast("Success!")
            }
        }
    }
}