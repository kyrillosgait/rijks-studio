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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
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

        binding.progressBar.isVisible = true

        binding.collectionListRecyclerView.apply {
            adapter = collectionListAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        viewModel.requestCollectionItems()
                    }
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.collectionItems.collect { items ->
                        binding.progressBar.isVisible = items.isEmpty()

                        collectionListAdapter.submitList(items)
                    }
                }
            }
        }

        viewModel.requestCollectionItems()
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
                viewModel.requestCollectionItems()
                true
            }
            R.id.menu_sort_by_artist_ascending -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupBy.ARTIST_ASCENDING)
                viewModel.requestCollectionItems()
                true
            }
            R.id.menu_sort_by_artist_descending -> {
                menuItem.isChecked = true
                viewModel.setGroupBy(GroupBy.ARTIST_DESCENDING)
                viewModel.requestCollectionItems()
                true
            }
            else -> false
        }
    }
}
