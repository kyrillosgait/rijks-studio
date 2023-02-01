package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.*
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import kotlinx.coroutines.delay

class FakeRijksService : RijksService {

    private val fakeAuthors = setOf("Salvador Dalí", "Vincent van Gogh")
    private val fakeImageDimensions = setOf(48, 72, 108, 144, 192, 256)

    override suspend fun getCollection(filter: CollectionFilter): List<CollectionItem> {
        delay(1000)

        val author = if (filter.page <= 5) fakeAuthors.first() else fakeAuthors.last()

        return (0..filter.pageSize).map {
            FakeDetailedCollectionItem.create(
                author = author,
                imageWidth = fakeImageDimensions.random(),
                imageHeight = fakeImageDimensions.random()
            )
        }
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem {
        return FakeDetailedCollectionItem.create(
            author = fakeAuthors.random(),
            imageWidth = fakeImageDimensions.random(),
            imageHeight = fakeImageDimensions.random()
        )
    }
}