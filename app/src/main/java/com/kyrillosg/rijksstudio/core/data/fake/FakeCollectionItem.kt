package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import java.util.*

data class FakeCollectionItem(
    override val id: CollectionItem.Id,
    override val title: String,
    override val author: String,
    override val image: String?
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
                id = CollectionItem.Id(title),
                title = title,
                image = imageUrl,
                author = author,
            )
        }
    }
}