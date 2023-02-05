package com.kyrillosg.rijksstudio.feature.collection.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.kyrillosg.rijksstudio.feature.collection.R
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemHeaderBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemImageWithLabelBinding

internal sealed class CollectionListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Header(
        private val binding: ItemHeaderBinding,
    ) : CollectionListViewHolder(binding) {

        fun bind(item: CollectionListViewData.Header) {
            binding.label.text = item.label
        }

        companion object {
            @LayoutRes
            val LAYOUT_ID = R.layout.item_header
        }
    }

    class ImageWithLabel(
        private val binding: ItemImageWithLabelBinding,
        private val onCollectionItemClicked: OnCollectionItemClicked,
    ) : CollectionListViewHolder(binding) {

        fun bind(item: CollectionListViewData.ImageWithLabel?) {
            binding.label.text = item?.label ?: "placeholder"
            binding.image.load(item?.image) {
                crossfade(true)
            }
            binding.root.setOnClickListener {
                item?.uniqueId?.let { onCollectionItemClicked(it) }
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT_ID = R.layout.item_image_with_label
        }
    }
}