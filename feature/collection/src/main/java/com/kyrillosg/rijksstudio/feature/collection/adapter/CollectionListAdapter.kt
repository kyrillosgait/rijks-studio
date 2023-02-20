package com.kyrillosg.rijksstudio.feature.collection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemHeaderBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemImageWithLabelBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemLoadingBinding

internal typealias OnCollectionItemClicked = (id: String) -> Unit

internal class CollectionListAdapter(
    private val onCollectionItemClicked: OnCollectionItemClicked,
) : ListAdapter<CollectionListViewData, CollectionListViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CollectionListViewData.Loading -> CollectionListViewHolder.Loading.LAYOUT_ID
        is CollectionListViewData.Header -> CollectionListViewHolder.Header.LAYOUT_ID
        is CollectionListViewData.ImageWithLabel -> CollectionListViewHolder.ImageWithLabel.LAYOUT_ID
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CollectionListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CollectionListViewHolder.Loading.LAYOUT_ID -> {
                CollectionListViewHolder.Loading(
                    binding = ItemLoadingBinding.inflate(inflater, parent, false),
                )
            }
            CollectionListViewHolder.Header.LAYOUT_ID -> {
                CollectionListViewHolder.Header(
                    binding = ItemHeaderBinding.inflate(inflater, parent, false),
                )
            }
            CollectionListViewHolder.ImageWithLabel.LAYOUT_ID -> {
                CollectionListViewHolder.ImageWithLabel(
                    binding = ItemImageWithLabelBinding.inflate(inflater, parent, false),
                    onCollectionItemClicked = onCollectionItemClicked,
                )
            }
            else -> error("viewType $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: CollectionListViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is CollectionListViewHolder.Loading -> {
                // Nothing to bind
            }
            is CollectionListViewHolder.Header -> {
                holder.bind(item as CollectionListViewData.Header)
            }
            is CollectionListViewHolder.ImageWithLabel -> {
                holder.bind(item as CollectionListViewData.ImageWithLabel?)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CollectionListViewData>() {
        override fun areItemsTheSame(
            oldItem: CollectionListViewData,
            newItem: CollectionListViewData,
        ): Boolean = oldItem.uniqueId == newItem.uniqueId

        override fun areContentsTheSame(
            oldItem: CollectionListViewData,
            newItem: CollectionListViewData,
        ): Boolean = oldItem == newItem
    }
}
