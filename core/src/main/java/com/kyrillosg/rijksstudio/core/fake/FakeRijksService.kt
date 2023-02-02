package com.kyrillosg.rijksstudio.core.fake

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import kotlinx.coroutines.delay

internal class FakeRijksService : RijksService {

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