package com.kyrillosg.rijksstudio.feature.collection.adapter

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem

sealed interface CollectionListModel {
    val uniqueId: String

    data class Loading(override val uniqueId: String = "Loading") : CollectionListModel

    data class Header(
        val label: String,
        override val uniqueId: String,
    ) : CollectionListModel

    data class ImageWithLabel(
        override val uniqueId: String,
        val label: String,
        val image: String?,
        val header: String,
    ) : CollectionListModel {

        companion object {
            fun from(collectionItem: CollectionItem): ImageWithLabel {
                return ImageWithLabel(
                    uniqueId = collectionItem.itemId.value,
                    label = collectionItem.title,
                    image = collectionItem.image?.url,
                    header = collectionItem.author,
                )
            }
        }
    }
}
