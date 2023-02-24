package com.kyrillosg.rijksstudio.feature.collection.adapter

import android.graphics.Typeface
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.kyrillosg.rijksstudio.feature.collection.R
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemImageWithLabelBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemLabelBinding
import com.kyrillosg.rijksstudio.feature.collection.databinding.ItemProgressBarBinding

internal sealed class CollectionListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class Loading(binding: ItemProgressBarBinding) : CollectionListViewHolder(binding) {

        companion object {
            @LayoutRes
            val LAYOUT_ID = R.layout.item_progress_bar
        }
    }

    class Label(
        private val binding: ItemLabelBinding,
    ) : CollectionListViewHolder(binding) {

        fun bind(item: CollectionListModel.Label) {
            binding.label.text = item.label

            when (item.style) {
                CollectionListModel.Label.Style.BOLD -> {
                    binding.label.setTypeface(null, Typeface.BOLD)
                    binding.label.textSize = 20f
                }
                CollectionListModel.Label.Style.ITALIC -> {
                    binding.label.setTypeface(null, Typeface.ITALIC)
                    binding.label.textSize = 16f
                }
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT_ID = R.layout.item_label
        }
    }

    class ImageWithLabel(
        private val binding: ItemImageWithLabelBinding,
        private val onCollectionItemClicked: OnCollectionItemClicked,
    ) : CollectionListViewHolder(binding) {

        fun bind(item: CollectionListModel.ImageWithLabel?) {
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
