package com.kyrillosg.rijksstudio.feature.collection.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.core.ui.toast
import com.kyrillosg.rijksstudio.feature.collection.R
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListAdapter
import com.kyrillosg.rijksstudio.feature.collection.databinding.FragmentCollectionListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionListFragment : ViewBindingFragment<FragmentCollectionListBinding>(
    bindingProvider = FragmentCollectionListBinding::inflate
), MenuProvider {

    private val viewModel: CollectionListViewModel by viewModel()

    private val collectionListAdapter = CollectionListAdapter(
        onCollectionItemClicked = { id ->
            try {
                findNavController().navigate(
                    directions = CollectionListFragmentDirections.actionListToDetail(
                        collectionItemId = CollectionItem.Id(id)
                    )
                )
            } catch (ignored: IllegalArgumentException) {
                // Already navigating or navigated
            }
        }
    ).apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.progressBar.isVisible = true

        binding.collectionListRecyclerView.apply {
            adapter = collectionListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.collectionPagingData.collect { items ->
                        collectionListAdapter.submitData(items)
                    }
                }

                launch {
                    collectionListAdapter.loadStateFlow.collect {
                        showLoadingOrErrorState(it)
                    }
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
        ).distinct()

        errorsStates.forEach { errorState ->
            errorState.error.message?.let { toast(it) }
        }

        binding.errorText.text = errorsStates.joinToString("\n\n")
        binding.errorText.isVisible = errorsStates.isNotEmpty()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_collection_list, menu)

        val groupBy = viewModel.groupBy
        menu.forEach { menuItem ->
            if (menuItem.itemId == R.id.menu_group_by_none && groupBy == GroupBy.NONE) {
                menuItem.isChecked = true
            } else if (menuItem.itemId == R.id.menu_sort_by_artist_ascending && groupBy == GroupBy.ARTIST_ASCENDING) {
                menuItem.isChecked = true
            } else if (menuItem.itemId == R.id.menu_sort_by_artist_descending && groupBy == GroupBy.ARTIST_DESCENDING) {
                menuItem.isChecked = true
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_group_by_none -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupBy.NONE)
                collectionListAdapter.refresh()
                true
            }
            R.id.menu_sort_by_artist_ascending -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupBy.ARTIST_ASCENDING)
                collectionListAdapter.refresh()
                true
            }
            R.id.menu_sort_by_artist_descending -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupBy.ARTIST_DESCENDING)
                collectionListAdapter.refresh()
                true
            }
            else -> false
        }
    }
}