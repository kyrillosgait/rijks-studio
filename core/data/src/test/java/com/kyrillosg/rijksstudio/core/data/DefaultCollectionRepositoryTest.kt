package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository
import com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class DefaultCollectionRepositoryTest {

    private val repository = com.kyrillosg.rijksstudio.core.data.DefaultCollectionRepository(
        rijksGateway = com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway(
            collectionItems = listOf(
                com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem.create(
                    id = com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id(
                        "1"
                    )
                ),
                com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem.create(
                    id = com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id(
                        "2"
                    )
                ),
                com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem.create(
                    id = com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id(
                        "100"
                    )
                ),
            )
        ),
    )

    @Nested
    @DisplayName("Given valid CollectionItem.ID")
    inner class ValidCollectionItemId {

        @Test
        @DisplayName("The correct detailed collection item is returned")
        fun getDetailedCollectionItem_givenValidId() {
            runTest {
                val expectedId = com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id("2")
                val actual = repository.getDetailedCollectionItem(expectedId)

                assert(actual?.itemId == expectedId)
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
                val nonExistentId = com.kyrillosg.rijksstudio.core.data.model.CollectionItem.Id(UUID.randomUUID().toString())
                val actual = repository.getDetailedCollectionItem(nonExistentId)

                assert(actual == null)
            }
        }
    }
}