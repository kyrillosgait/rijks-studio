package com.kyrillosg.rijksstudio.feature.collection.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.core.ui.ViewBindingFragment
import com.kyrillosg.rijksstudio.core.ui.addOnEndlessScrollListener
import com.kyrillosg.rijksstudio.feature.collection.R
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListAdapter
import com.kyrillosg.rijksstudio.feature.collection.databinding.FragmentCollectionListBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionListFragment :
    ViewBindingFragment<FragmentCollectionListBinding>(FragmentCollectionListBinding::inflate),
    MenuProvider,
    SearchView.OnQueryTextListener {

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

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.collectionListRecyclerView.apply {
            adapter = collectionListAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addOnEndlessScrollListener(
                predicate = { runBlocking { viewModel.canLoadMore.first() } },
                onTrigger = { viewModel.requestCollectionItems(refreshData = false) },
            )
        }

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.setSearchQuery(null).also {
                searchView.setQuery(null, false)
                searchView.onActionViewCollapsed()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.screenState.collect { uiState ->
                        renderState(uiState)
                    }
                }
            }
        }
    }

    private fun renderState(uiState: UiState<CollectionScreenModel>) {
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
                binding.pullToRefresh.isRefreshing = uiState.data.items.isEmpty()

                collectionListAdapter.submitList(uiState.data.items)
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

        searchView = menu.findItem(R.id.menu_search).actionView as? SearchView ?: return

        viewModel.searchQuery?.let { query ->
            searchView.setQuery(query, false)
        }

        searchView.setOnQueryTextListener(this@CollectionListFragment)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.setSearchQuery(newText, debounceTimeMs = 500L)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_group_by_none -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.NONE)
                viewModel.requestCollectionItems(refreshData = true)
                true
            }
            R.id.menu_group_by_artist_asc -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.ARTIST_ASCENDING)
                viewModel.requestCollectionItems(refreshData = true)
                true
            }
            R.id.menu_group_by_artist_desc -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupField.ARTIST_DESCENDING)
                viewModel.requestCollectionItems(refreshData = true)
                true
            }
            else -> false
        }
    }
}
