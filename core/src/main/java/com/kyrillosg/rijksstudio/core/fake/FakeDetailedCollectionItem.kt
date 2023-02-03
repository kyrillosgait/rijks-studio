package com.kyrillosg.rijksstudio.core.fake

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import java.util.*

internal data class FakeDetailedCollectionItem(
    override val itemId: CollectionItem.Id,
    override val title: String,
    override val author: String,
    override val imageUrl: String?,
    override val description: String,
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
        ): DetailedCollectionItem {
            val imageUrl = "https://via.placeholder.com/${imageWidth}x${imageHeight}.${imageFormat}?text=$imageText"
            return FakeDetailedCollectionItem(
                itemId = id,
                title = title,
                imageUrl = imageUrl,
                author = author,
                description = description
            )
        }
    }
}