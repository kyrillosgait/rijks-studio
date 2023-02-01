package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.data.network.*
import kotlinx.coroutines.delay

class FakeRijksService : RijksService {

    private val fakeAuthors = setOf("Salvador Dalí", "Vincent van Gogh")
    private val fakeImageDimensions = setOf(48, 72, 108, 144, 192, 256)

    override suspend fun getCollection(filter: RijksService.CollectionFilter): CollectionResponse {
        delay(1000)

        val author = if (filter.page <= 5) fakeAuthors.first() else fakeAuthors.last()

        return CollectionResponse(
            artObjects = (0..filter.pageSize).map {
                val fakeItem = FakeCollectionItem.create(
                    author = author,
                    imageWidth = fakeImageDimensions.random(),
                    imageHeight = fakeImageDimensions.random()
                )

                NetworkCollectionItem(
                    objectNumber = fakeItem.itemId.value,
                    title = fakeItem.title,
                    principalOrFirstMaker = fakeItem.author,
                    webImage = NetworkCollectionImage(
                        url = fakeItem.imageUrl.orEmpty(),
                    ),
                )
            },
        )
    }

    override suspend fun getCollectionDetails(objectNumber: String): CollectionDetailsResponse {
        val fakeItem = FakeCollectionItem.create(
            author = fakeAuthors.random(),
            imageWidth = fakeImageDimensions.random(),
            imageHeight = fakeImageDimensions.random()
        )

        return CollectionDetailsResponse(
            artObject = NetworkCollectionDetailsItem(
                objectNumber = fakeItem.itemId.value,
                title = fakeItem.title,
                description = (0..100).map { fakeItem.title }.toString(),
                principalOrFirstMaker = fakeItem.author,
                webImage = NetworkCollectionImage(
                    url = fakeItem.imageUrl.orEmpty(),
                ),
            )
        )
    }
}