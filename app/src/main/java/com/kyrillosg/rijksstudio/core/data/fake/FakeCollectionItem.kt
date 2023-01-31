package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import java.util.*

data class FakeCollectionItem(
    override val itemId: CollectionItem.Id,
    override val title: String,
    override val author: String,
    override val imageUrl: String?
) : CollectionItem {

    companion object {
        fun create(
            author: String = "Salvador Dalí",
            title: String = UUID.randomUUID().toString(),
            imageText: String = title,
            imageHeight: Int = 720,
            imageWidth: Int = 480,
            imageFormat: String = "png",
        ): CollectionItem {
            val imageUrl = "https://via.placeholder.com/${imageWidth}x${imageHeight}.${imageFormat}?text=$imageText"
            return FakeCollectionItem(
                itemId = CollectionItem.Id(title),
                title = title,
                imageUrl = imageUrl,
                author = author,
            )
        }
    }
}