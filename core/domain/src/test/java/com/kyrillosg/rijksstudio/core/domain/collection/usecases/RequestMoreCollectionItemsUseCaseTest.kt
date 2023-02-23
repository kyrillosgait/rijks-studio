@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.fakes.FakeCollectionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RequestMoreCollectionItemsUseCaseTest {

    private val repository = FakeCollectionRepository()

    @AfterEach
    fun teardown() = runTest {
        repository.invalidateCollectionItems(GroupField.NONE)
    }

    @Test
    @DisplayName("Given no refresh adds data on top")
    fun appendsData_givenNoRefresh() = runTest {
        val useCase = RequestMoreCollectionItemsUseCase(repository)
        val params = RequestMoreCollectionItemsUseCase.Params(
            groupBy = GroupField.NONE,
            refreshData = false,
            count = 4,
        )

        // Given
        val itemsBefore = repository.getCollectionItemsStream(params.groupBy).first().size

        // When
        useCase(params)
        useCase(params)
        val itemsAfter = repository.getCollectionItemsStream(params.groupBy).first().size

        assertEquals(8, itemsAfter - itemsBefore)
    }

    @Test
    @DisplayName("Given refresh replaces current data")
    fun replacesCurrentData_givenRefresh() = runTest {
        val useCase = RequestMoreCollectionItemsUseCase(repository)
        val params = RequestMoreCollectionItemsUseCase.Params(
            groupBy = GroupField.NONE,
            refreshData = true,
            count = 4,
        )

        // Given we have items
        repository.requestMoreCollectionItems(params.groupBy, params.count)
        repository.requestMoreCollectionItems(params.groupBy, params.count)
        val itemsBefore = repository.getCollectionItemsStream(params.groupBy).first().size

        // When
        useCase(params)
        val itemsAfter = repository.getCollectionItemsStream(params.groupBy).first().size

        assertEquals(4, itemsBefore - itemsAfter)
    }
}
