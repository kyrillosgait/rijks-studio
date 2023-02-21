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
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.core.ui.views.ColorPaletteView
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
                viewModel.detailedCollectionItem.collect { uiState ->
                    renderState(uiState)
                }
            }
        }

        viewModel.getDetails(args.collectionItemId)
    }

    private fun renderState(uiState: UiState<DetailedCollectionItem>) {
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

                renderItem(uiState.data)
            }
        }
    }

    private fun renderItem(item: DetailedCollectionItem) {
        item.image?.let { image ->
            val ratio = "${image.width}:${image.height}"
            ConstraintSet().apply {
                clone(binding.content)
                setDimensionRatio(binding.image.id, ratio)
                applyTo(binding.content)
            }

            binding.image.load(image.url) {
                crossfade(true)
            }
        }

        binding.description.text = item.plaqueDescription ?: item.description
        binding.colorHeader.isVisible = item.colors.isNotEmpty()
        binding.colorView.init(
            colorModels = item.colors.sortedByDescending { it.percentage }.map {
                ColorPaletteView.ColorModel.from(it)
            },
        )

        binding.normalizedColorHeader.isVisible = item.normalizedColors.isNotEmpty()
        binding.normalizedColorView.init(
            colorModels = item.normalizedColors.sortedByDescending { it.percentage }.map {
                ColorPaletteView.ColorModel.from(it)
            },
        )
    }
}
