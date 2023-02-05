package com.kyrillosg.rijksstudio.feature.collection

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.core.ui.toast
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

        binding.progressBar.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                collectionListAdapter.loadStateFlow.collect {
                    showLoadingOrErrorState(it)
                }
            }
        }
    }

    private fun showLoadingOrErrorState(state: CombinedLoadStates) {
        binding.progressBar.isVisible = state.refresh is LoadState.Loading

        val errorsStates = listOfNotNull(
            state.source.refresh as? LoadState.Error,
            state.source.prepend as? LoadState.Error,
            state.source.append as? LoadState.Error,
            state.refresh as? LoadState.Error,
            state.prepend as? LoadState.Error,
            state.append as? LoadState.Error,
        )

        errorsStates.forEach { errorState ->
            errorState.error.message?.let { toast(it) }
        }

        binding.errorText.text = errorsStates.joinToString("\n\n")
    }
}