package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class DefaultCollectionRepositoryTest {

    private val repository = DefaultCollectionRepository(
        rijksGateway = FakeRijksGateway(
            collectionItems = listOf(
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("1")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("2")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("100")),
            ),
        ),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Nested
    @DisplayName("Given valid CollectionItem.ID")
    inner class ValidCollectionItemId {

        @Test
        @DisplayName("The correct detailed collection item is returned")
        fun getDetailedCollectionItem_givenValidId() {
            runTest {
                val expectedId = CollectionItem.Id("2")
                val actual = repository.getDetailedCollectionItem(expectedId)

                assertEquals(expectedId, actual?.itemId)
            }
        }
    }

    @Nested
    @DisplayName("Given non-existent CollectionItem.ID")
    inner class NonExistentCollectionItemId {

        @Test
        @DisplayName("No item is returned")
        fun getDetailedCollectionItem_givenNonExistentId() {
            runTest {
                val nonExistentId = CollectionItem.Id(UUID.randomUUID().toString())
                val actual = repository.getDetailedCollectionItem(nonExistentId)

                assertEquals(null, actual)
            }
        }
    }
}
