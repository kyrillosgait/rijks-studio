@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection.detail

import app.cash.turbine.test
import com.kyrillosg.rijksstudio.core.domain.collection.fakes.FakeDetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CollectionDetailViewModelTest {

    @Test
    @DisplayName("Initial screen state is loading")
    fun initialScreenStateIsLoading() = runTest {
        val viewModel = CollectionDetailViewModel(
            getDetailedCollectionItemUseCase = mockk(),
        )

        val initialScreenState = viewModel.screenState.first()

        assert(initialScreenState is UiState.Loading)
    }

    @Nested
    inner class GetDetails {

        @Test
        @DisplayName("Given a invalid ID, leads to error UI state")
        fun givenInvalidId_leadsToErrorUiState() = runTest {
            val useCaseMock = mockk<GetDetailedCollectionItemUseCase>()
            coEvery { useCaseMock(any()) } throws Exception("Not found")

            val viewModel = CollectionDetailViewModel(getDetailedCollectionItemUseCase = useCaseMock)

            viewModel.getDetails(CollectionItem.Id("Invalid ID"))

            viewModel.screenState.test {
                assert(awaitItem() is UiState.Error)
            }
        }

        @Test
        @DisplayName("Given a valid ID, leads to successful UI state")
        fun givenValidId_leadsToSuccessUiState() = runTest {
            val useCaseMock = mockk<GetDetailedCollectionItemUseCase>()
            coEvery { useCaseMock(any()) } returns FakeDetailedCollectionItem.create()

            val viewModel = CollectionDetailViewModel(getDetailedCollectionItemUseCase = useCaseMock)

            viewModel.getDetails(CollectionItem.Id("Valid ID"))

            viewModel.screenState.test {
                assert(awaitItem() is UiState.Loading)
                assert(awaitItem() is UiState.Success)
            }
        }
    }
}