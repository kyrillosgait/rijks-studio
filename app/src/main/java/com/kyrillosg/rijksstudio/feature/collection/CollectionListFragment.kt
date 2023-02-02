package com.kyrillosg.rijksstudio.feature.collection

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.databinding.FragmentCollectionListBinding
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionListFragment : ViewBindingFragment<FragmentCollectionListBinding>(
    bindingProvider = FragmentCollectionListBinding::inflate
) {

    private val viewModel: CollectionListViewModel by viewModel()
    private val collectionListAdapter = CollectionListAdapter(
        onCollectionItemClicked = { id ->
            findNavController().navigate(
                directions = CollectionListFragmentDirections.actionListToDetail(
                    collectionItemId = CollectionItem.Id(id)
                )
            )
        }
    ).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collectionListRecyclerView.apply {
            adapter = collectionListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.collectionPagingData.collect { items ->
                    collectionListAdapter.submitData(items)
                }
            }
        }
    }
}