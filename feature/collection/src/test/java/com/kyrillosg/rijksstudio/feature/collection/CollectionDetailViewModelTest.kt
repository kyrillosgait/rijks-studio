@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection

import app.cash.turbine.test
import com.kyrillosg.rijksstudio.core.domain.collection.fakes.FakeDetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.feature.collection.detail.CollectionDetailViewModel
import com.kyrillosg.rijksstudio.feature.common.CoroutinesTestExtension
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class CollectionDetailViewModelTest {

    private val getDetailedCollectionItemMock = mockk<GetDetailedCollectionItemUseCase>()

    private lateinit var viewModel: CollectionDetailViewModel

    @BeforeEach
    fun setup() {
        clearMocks(getDetailedCollectionItemMock)

        viewModel = CollectionDetailViewModel(
            getDetailedCollectionItemUseCase = getDetailedCollectionItemMock,
        )
    }

    @Test
    @DisplayName("Initial screen state is loading")
    fun initialScreenStateIsLoading() = runTest {
        viewModel.screenState.test {
            assert(awaitItem() is UiState.Loading)
        }
    }

    @Nested
    inner class GetDetails {

        @Test
        @DisplayName("Given a invalid ID, leads to error UI state")
        fun givenInvalidId_leadsToErrorUiState() = runTest {
            coEvery { getDetailedCollectionItemMock(any()) } throws Exception("Not found")

            viewModel.screenState.test {
                assert(awaitItem() is UiState.Loading)

                viewModel.getDetails(CollectionItem.Id("Invalid ID"))

                assert(awaitItem() is UiState.Error)
            }
        }

        @Test
        @DisplayName("Given a valid ID, leads to successful UI state")
        fun givenValidId_leadsToSuccessUiState() = runTest {
            coEvery { getDetailedCollectionItemMock(any()) } returns FakeDetailedCollectionItem.create()

            viewModel.screenState.test {
                assert(awaitItem() is UiState.Loading)

                viewModel.getDetails(CollectionItem.Id("Valid ID"))

                assert(awaitItem() is UiState.Success)
            }
        }
    }
}
