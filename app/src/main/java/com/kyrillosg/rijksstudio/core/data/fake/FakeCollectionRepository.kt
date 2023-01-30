package com.kyrillosg.rijksstudio.core.data.fake

import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.data.CollectionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeCollectionRepository(
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : CollectionRepository {

    override suspend fun getCollectionItems(): List<CollectionItem> {
        return withContext(backgroundDispatcher) {
            delay(1_000)

            val testAuthors = setOf("Salvador Dalí", "Vincent van Gogh")
            val testImageWidths = setOf(720, 1080, 1920, 2560)
            val testImageHeights = setOf(480, 720, 1080, 1440)

            (1..1000).map {
                FakeCollectionItem.create(
                    author = testAuthors.random(),
                    imageWidth = testImageWidths.random(),
                    imageHeight = testImageHeights.random()
                )
            }
        }
    }
}