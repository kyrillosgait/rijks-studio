package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItemColor
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItemImage
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import java.util.*

internal data class FakeDetailedCollectionItem(
    override val itemId: CollectionItem.Id,
    override val title: String,
    override val author: String,
    override val image: CollectionItemImage?,
    override val description: String,
    override val plaqueDescription: String?,
    override val colors: List<CollectionItemColor>,
    override val normalizedColors: List<CollectionItemColor>,
) : DetailedCollectionItem {

    companion object {
        fun create(
            author: String = "Salvador Dalí",
            title: String = UUID.randomUUID().toString(),
            id: CollectionItem.Id = CollectionItem.Id(title),
            description: String = title.repeat(10),
            imageText: String = title,
            imageHeight: Int = 720,
            imageWidth: Int = 480,
            imageFormat: String = "png",
            colors: List<CollectionItemColor> = emptyList(),
        ): DetailedCollectionItem {
            val imageUrl = "https://via.placeholder.com/${imageWidth}x$imageHeight.$imageFormat?text=$imageText"
            return FakeDetailedCollectionItem(
                itemId = id,
                title = title,
                image = object : CollectionItemImage {
                    override val url = imageUrl
                    override val width = imageWidth
                    override val height = imageHeight
                },
                author = author,
                description = description,
                plaqueDescription = description,
                colors = colors,
                normalizedColors = colors,
            )
        }
    }
}
