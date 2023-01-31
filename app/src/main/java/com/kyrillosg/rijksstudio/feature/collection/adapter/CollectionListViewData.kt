package com.kyrillosg.rijksstudio.feature.collection.adapter

import com.kyrillosg.rijksstudio.core.data.CollectionItem

sealed interface CollectionListViewData {
    val uniqueId: String

    data class Header(
        val label: String,
        override val uniqueId: String = label,
    ) : CollectionListViewData

    data class ImageWithLabel(
        override val uniqueId: String,
        val label: String,
        val image: String?,
        val header: String,
    ) : CollectionListViewData {

        companion object {
            fun from(collectionItem: CollectionItem): ImageWithLabel {
                return ImageWithLabel(
                    uniqueId = collectionItem.itemId.value,
                    label = collectionItem.title,
                    image = collectionItem.imageUrl,
                    header = collectionItem.author,
                )
            }
        }
    }
}