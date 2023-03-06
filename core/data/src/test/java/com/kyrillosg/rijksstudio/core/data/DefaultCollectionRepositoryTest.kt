@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.core.data

import com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway
import com.kyrillosg.rijksstudio.core.domain.collection.fakes.FakeDetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.util.*

class DefaultCollectionRepositoryTest {

    private val pageSize = 4

    private val repository = DefaultCollectionRepository(
        rijksGateway = FakeRijksGateway(
            collectionItems = listOf(
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("1")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("2")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("3")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("4")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("5")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("6")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("7")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("8")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("97")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("98")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("99")),
                FakeDetailedCollectionItem.create(id = CollectionItem.Id("100")),
            ),
            pageSize = pageSize,
        ),
    )

    @AfterEach
    fun teardown() = runTest {
        GroupField.values().forEach {
            repository.invalidateCollectionItems(it)
        }
    }

    @Nested
    inner class GetDetailedCollectionItem {

        @Test
        @DisplayName("Given valid CollectionItem.ID returns the correct item")
        fun getDetailedCollectionItem_givenValidId() = runTest {
            val expectedId = CollectionItem.Id("2")
            val actual = repository.getDetailedCollectionItem(expectedId)

            assertEquals(expectedId, actual.itemId)
        }

        @Test
        @DisplayName("Given non-existent CollectionItem.ID returns no item")
        fun getDetailedCollectionItem_givenNonExistentId() = runTest {
            val nonExistentId = CollectionItem.Id(UUID.randomUUID().toString())
            val actual = runCatching {
                repository.getDetailedCollectionItem(nonExistentId)
            }.getOrNull()

            assertEquals(null, actual)
        }
    }

    @Nested
    inner class GetCollectionItemsStream {

        @DisplayName("Is empty if no request was made")
        @ParameterizedTest
        @EnumSource
        fun isEmpty_ifNoRequestWasMade(groupField: GroupField) = runTest {
            val items = repository.getCollectionItemsStream(groupField).first()

            assertEquals(0, items.size)
        }

        @DisplayName("Can return same number of items if there are no more items to load")
        @ParameterizedTest
        @EnumSource
        fun canReturnSameNumberOfItems_givenThereAreNoMoreItemsToLoad(groupField: GroupField) = runTest {
            // Given there are no more available items
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            val firstCollectionCall = repository.getCollectionItemsStream(groupField).first()

            // When
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            val secondCollectionCall = repository.getCollectionItemsStream(groupField).first()

            assertEquals(firstCollectionCall.size, secondCollectionCall.size)
        }
    }

    @Nested
    inner class InvalidateCollectionItems {

        @DisplayName("Given a group field, clears all collection items for that group field")
        @ParameterizedTest
        @EnumSource
        fun clearsAllCollectionItems_givenAGroupField(groupField: GroupField) = runTest {
            // Given some items exist
            repository.requestMoreCollectionItems(groupField, pageSize)

            // When
            repository.invalidateCollectionItems(groupField)
            val itemsAfterInvalidating = repository.getCollectionItemsStream(groupField).first()

            assertEquals(0, itemsAfterInvalidating.size)
        }
    }

    @Nested
    inner class RequestMoreCollectionItems {

        @DisplayName("Given a group field, requests more items for that group field")
        @ParameterizedTest
        @EnumSource
        fun requestsMoreItems_givenAGroupField(groupField: GroupField) = runTest {
            val itemsBeforeLoadingMore = repository.getCollectionItemsStream(groupField).first()
            repository.requestMoreCollectionItems(groupField, count = pageSize)
            repository.requestMoreCollectionItems(groupField, count = pageSize)

            val itemsAfterLoadingMore = repository.getCollectionItemsStream(groupField).first()
            val expected = 2 * pageSize
            val actual = itemsAfterLoadingMore.size - itemsBeforeLoadingMore.size

            assertEquals(expected, actual)
        }
    }
}
