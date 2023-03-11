@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.core.domain.collection.usecases

import com.kyrillosg.rijksstudio.core.domain.collection.fakes.FakeCollectionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class RequestMoreCollectionItemsUseCaseTest {

    private val repository = FakeCollectionRepository()

    @AfterEach
    fun teardown() = runTest {
        GroupField.values().forEach {
            repository.invalidateCollectionItems(it)
        }
    }

    @DisplayName("Given no refresh adds data on top")
    @ParameterizedTest
    @EnumSource
    fun appendsData_givenNoRefresh(groupField: GroupField) = runTest {
        val useCase = RequestMoreCollectionItemsUseCase(repository)
        val params = RequestMoreCollectionItemsUseCase.Params(
            groupBy = groupField,
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

    @DisplayName("Given refresh replaces current data")
    @ParameterizedTest
    @EnumSource
    fun replacesCurrentData_givenRefresh(groupField: GroupField) = runTest {
        val useCase = RequestMoreCollectionItemsUseCase(repository)
        val params = RequestMoreCollectionItemsUseCase.Params(
            groupBy = groupField,
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
