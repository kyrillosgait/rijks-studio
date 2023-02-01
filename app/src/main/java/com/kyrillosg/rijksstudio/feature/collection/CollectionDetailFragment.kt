package com.kyrillosg.rijksstudio.feature.collection

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentCollectionDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionDetailFragment : ViewBindingFragment<FragmentCollectionDetailBinding>(
    bindingProvider = FragmentCollectionDetailBinding::inflate
) {

    private val viewModel: CollectionDetailViewModel by viewModel()
    private val args: CollectionDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.detailedCollectionItem.collect { item ->
                    binding.image.load(item.imageUrl)
                    binding.description.text = item.description
                }
            }
        }

        viewModel.getDetails(args.collectionItemId)
    }
}