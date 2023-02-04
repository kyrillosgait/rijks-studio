package com.kyrillosg.rijksstudio.feature.collection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kyrillosg.rijksstudio.databinding.ItemHeaderBinding
import com.kyrillosg.rijksstudio.databinding.ItemImageWithLabelBinding

typealias OnCollectionItemClicked = (id: String) -> Unit

class CollectionListAdapter(
    private val onCollectionItemClicked: OnCollectionItemClicked,
) : PagingDataAdapter<CollectionListViewData, CollectionListViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CollectionListViewData.Header -> CollectionListViewHolder.Header.LAYOUT_ID
        is CollectionListViewData.ImageWithLabel -> CollectionListViewHolder.ImageWithLabel.LAYOUT_ID
        // Support for paging3 placeholders
        null -> CollectionListViewHolder.ImageWithLabel.LAYOUT_ID
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CollectionListViewHolder.Header.LAYOUT_ID -> {
                CollectionListViewHolder.Header(
                    binding = ItemHeaderBinding.inflate(inflater, parent, false)
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
            newItem: CollectionListViewData
        ): Boolean = oldItem.uniqueId == newItem.uniqueId

        override fun areContentsTheSame(
            oldItem: CollectionListViewData,
            newItem: CollectionListViewData
        ): Boolean = oldItem == newItem
    }
}