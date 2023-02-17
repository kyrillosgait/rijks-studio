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
import com.kyrillosg.rijksstudio.core.ui.toast
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
                toast(uiState.message)
                binding.progressBar.isVisible = false
                binding.scrollView.isVisible = false
            }
            UiState.Loading -> {
                binding.progressBar.isVisible = true
                binding.scrollView.isVisible = false
            }
            is UiState.Success -> {
                uiState.data.image?.let { image ->
                    val ratio = "${image.width}:${image.height}"
                    ConstraintSet().apply {
                        clone(binding.constraintLayout)
                        setDimensionRatio(binding.image.id, ratio)
                        applyTo(binding.constraintLayout)
                    }

                    binding.image.load(image.url) {
                        crossfade(true)
                    }
                }

                binding.description.text = uiState.data.description
                binding.colorView.init(
                    colorModels = uiState.data.colors.sortedByDescending { it.percentage }.map {
                        ColorPaletteView.ColorModel.from(it)
                    },
                )
                binding.colorPaletteHeader.isVisible = uiState.data.colors.isNotEmpty()
                binding.normalizedColorView.init(
                    colorModels = uiState.data.normalizedColors.sortedByDescending { it.percentage }.map {
                        ColorPaletteView.ColorModel.from(it)
                    },
                )
                binding.colorPaletteNormalizedHeader.isVisible = uiState.data.normalizedColors.isNotEmpty()

                binding.progressBar.isVisible = false
                binding.scrollView.isVisible = true
            }
        }
    }
}
