package com.kyrillosg.rijksstudio.feature.collection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemImageWithLabelBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemLabelBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemProgressBarBinding

internal typealias OnCollectionItemClicked = (id: String) -> Unit

internal class CollectionListAdapter(
    private val onCollectionItemClicked: OnCollectionItemClicked,
) : ListAdapter<CollectionListModel, CollectionListViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CollectionListModel.ProgressBar -> CollectionListViewHolder.Loading.LAYOUT_ID
        is CollectionListModel.ImageWithLabel -> CollectionListViewHolder.ImageWithLabel.LAYOUT_ID
        is CollectionListModel.Label -> CollectionListViewHolder.Label.LAYOUT_ID
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CollectionListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CollectionListViewHolder.Loading.LAYOUT_ID -> {
                CollectionListViewHolder.Loading(
                    binding = ItemProgressBarBinding.inflate(inflater, parent, false),
                )
            }
            CollectionListViewHolder.Label.LAYOUT_ID -> {
                CollectionListViewHolder.Label(
                    binding = ItemLabelBinding.inflate(inflater, parent, false),
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
            is CollectionListViewHolder.Label -> {
                holder.bind(item as CollectionListModel.Label)
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
