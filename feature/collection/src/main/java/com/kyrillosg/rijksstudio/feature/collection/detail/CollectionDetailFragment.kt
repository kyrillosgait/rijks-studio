package com.kyrillosg.rijksstudio.feature.collection.detail

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.feature.collection.databinding.FragmentCollectionDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionDetailFragment : ViewBindingFragment<FragmentCollectionDetailBinding>(
    bindingProvider = FragmentCollectionDetailBinding::inflate,
) {

    private val viewModel: CollectionDetailViewModel by viewModel()
    private val args: CollectionDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.getDetails(args.collectionItemId)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.screenState.collect { uiState ->
                    renderState(uiState)
                }
            }
        }

        viewModel.getDetails(args.collectionItemId)
    }

    private fun renderState(uiState: UiState<DetailScreenModel>) {
        when (uiState) {
            is UiState.Error -> {
                binding.errorMessage.isVisible = true
                binding.progressBar.isVisible = false
                binding.content.isVisible = false
                binding.pullToRefresh.isRefreshing = false

                binding.errorMessage.text = uiState.message
            }
            UiState.Loading -> {
                binding.errorMessage.isVisible = false
                binding.progressBar.isVisible = true
                binding.content.isVisible = false
            }
            is UiState.Success -> {
                binding.errorMessage.isVisible = false
                binding.progressBar.isVisible = false
                binding.content.isVisible = true
                binding.pullToRefresh.isRefreshing = false

                renderModel(uiState.data)
            }
        }
    }

    private fun renderModel(model: DetailScreenModel) {
        model.imageRatio?.let { ratio ->
            ConstraintSet().apply {
                clone(binding.content)
                setDimensionRatio(binding.image.id, ratio)
                applyTo(binding.content)
            }
        }

        model.imageUrl?.let { url ->
            binding.image.load(url) {
                crossfade(true)
            }
        }

        binding.description.text = model.description
        binding.colorView.init(model.colors)
        binding.normalizedColorView.init(model.normalizedColors)
    }
}
