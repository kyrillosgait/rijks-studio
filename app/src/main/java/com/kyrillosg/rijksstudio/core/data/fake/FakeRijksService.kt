package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.data.network.CollectionItemResponse
import com.kyrillosg.rijksstudio.core.data.network.NetworkCollectionImage
import com.kyrillosg.rijksstudio.core.data.network.NetworkCollectionItem

class FakeRijksService : RijksService {

    override suspend fun getCollectionItems(
        page: Int,
        pageSize: Int,
    ): CollectionItemResponse {
        val testAuthors = setOf("Salvador Dalí", "Vincent van Gogh")
        val author = if (page <= 5) testAuthors.first() else testAuthors.last()

        val testImageWidths = setOf(72, 108, 192, 256)
        val testImageHeights = setOf(48, 72, 108, 144)

        return CollectionItemResponse(
            artObjects = (0..pageSize).map {
                val fakeItem = FakeCollectionItem.create(
                    author = author,
                    imageWidth = testImageWidths.random(),
                    imageHeight = testImageHeights.random()
                )

                NetworkCollectionItem(
                    id = fakeItem.itemId.value,
                    title = fakeItem.title,
                    principalOrFirstMaker = fakeItem.author,
                    webImage = NetworkCollectionImage(
                        url = fakeItem.imageUrl.orEmpty(),
                    ),
                )
            },
        )
    }
}