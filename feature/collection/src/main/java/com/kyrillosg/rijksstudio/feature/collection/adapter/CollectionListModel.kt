package com.kyrillosg.rijksstudio.feature.collection.adapter

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem

internal sealed interface CollectionListModel {
    val uniqueId: String

    data class ProgressBar(override val uniqueId: String = "Loading") : CollectionListModel

    data class Label(
        val label: String,
        override val uniqueId: String,
        val style: Style = Style.BOLD,
    ) : CollectionListModel {

        enum class Style { BOLD, ITALIC }
    }

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
