package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionService

class FakeCollectionService : CollectionService {

    override suspend fun getCollectionItems(
        page: Int,
        pageSize: Int,
    ): List<CollectionItem> {
        val testAuthors = setOf("Salvador Dalí", "Vincent van Gogh")
        val author = if (page <= 5) testAuthors.first() else testAuthors.last()

        val testImageWidths = setOf(72, 108, 192, 256)
        val testImageHeights = setOf(48, 72, 108, 144)

        return (0..pageSize).map {
            FakeCollectionItem.create(
                author = author,
                imageWidth = testImageWidths.random(),
                imageHeight = testImageHeights.random()
            )
        }
    }
}