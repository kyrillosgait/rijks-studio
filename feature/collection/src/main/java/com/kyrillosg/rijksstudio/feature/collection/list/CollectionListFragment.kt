package com.kyrillosg.rijksstudio.feature.collection.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.feature.collection.R
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListAdapter
import com.kyrillosg.rijksstudio.feature.collection.databinding.FragmentCollectionListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionListFragment :
    ViewBindingFragment<FragmentCollectionListBinding>(
        bindingProvider = FragmentCollectionListBinding::inflate,
    ),
    MenuProvider {

    private val viewModel: CollectionListViewModel by viewModel()

    private val collectionListAdapter = CollectionListAdapter(
        onCollectionItemClicked = { id ->
            try {
                findNavController().navigate(
                    directions = CollectionListFragmentDirections.actionListToDetail(
                        collectionItemId = CollectionItem.Id(id),
                    ),
                )
            } catch (ignored: IllegalArgumentException) {
                // Already navigating or navigated
            }
        },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.collectionListRecyclerView.apply {
            adapter = collectionListAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val isScrolledToBottom = !recyclerView.canScrollVertically(1) && dy > 0
                    if (isScrolledToBottom) {
                        viewModel.requestCollectionItems()
                    }
                }
            })
        }

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.requestCollectionItems(refreshData = true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.collectionItems.collect { items ->
                        binding.pullToRefresh.isRefreshing = items.isEmpty()
                        collectionListAdapter.submitList(items)
                    }
                }

                launch {
                    viewModel.requestCollectionItemsState.collect { uiState ->
                        renderState(uiState)
                    }
                }
            }
        }
    }

    private fun renderState(uiState: UiState<Unit>) {
        when (uiState) {
            is UiState.Error -> {
                binding.errorMessage.text = uiState.message
                binding.errorMessage.isVisible = true
                binding.pullToRefresh.isRefreshing = false
            }
            UiState.Loading -> {
                binding.errorMessage.isVisible = false
            }
            is UiState.Success -> {
                binding.errorMessage.isVisible = false
                binding.pullToRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_collection_list, menu)

        val groupBy = viewModel.groupBy
        menu.forEach { menuItem ->
            if (menuItem.itemId == R.id.menu_group_by_none && groupBy == GroupField.NONE) {
                menuItem.isChecked = true
            } else if (menuItem.itemId == R.id.menu_group_by_artist_asc && groupBy == GroupField.ARTIST_ASCENDING) {
                menuItem.isChecked = true
            } else if (menuItem.itemId == R.id.menu_group_by_artist_desc && groupBy == GroupField.ARTIST_DESCENDING) {
                menuItem.isChecked = true
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_group_by_none -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.NONE)
                viewModel.requestCollectionItems()
                true
            }
            R.id.menu_group_by_artist_asc -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.ARTIST_ASCENDING)
                viewModel.requestCollectionItems()
                true
            }
            R.id.menu_group_by_artist_desc -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.ARTIST_DESCENDING)
                viewModel.requestCollectionItems()
                true
            }
            else -> false
        }
    }
}
