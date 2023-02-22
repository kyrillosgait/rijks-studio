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
) : ListAdapter<CollectionListModel, CollectionListViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CollectionListModel.Loading -> CollectionListViewHolder.Loading.LAYOUT_ID
        is CollectionListModel.Header -> CollectionListViewHolder.Header.LAYOUT_ID
        is CollectionListModel.ImageWithLabel -> CollectionListViewHolder.ImageWithLabel.LAYOUT_ID
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
                holder.bind(item as CollectionListModel.Header)
            }
            is CollectionListViewHolder.ImageWithLabel -> {
                holder.bind(item as CollectionListModel.ImageWithLabel?)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CollectionListModel>() {
        override fun areItemsTheSame(
            oldItem: CollectionListModel,
            newItem: CollectionListModel,
        ): Boolean = oldItem.uniqueId == newItem.uniqueId

        override fun areContentsTheSame(
            oldItem: CollectionListModel,
            newItem: CollectionListModel,
        ): Boolean = oldItem == newItem
    }
}
