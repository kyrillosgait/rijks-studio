@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection

import app.cash.turbine.test
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetGroupedCollectionStreamUseCase
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.RequestMoreCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.feature.collection.list.CollectionListViewModel
import com.kyrillosg.rijksstudio.feature.collection.list.CollectionScreenModel
import com.kyrillosg.rijksstudio.feature.common.CoroutinesTestExtension
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class CollectionListViewModelTest {

    private val getGroupedCollectionItemsMock = mockk<GetGroupedCollectionStreamUseCase>()
    private val requestMoreCollectionItemsMock = mockk<RequestMoreCollectionItemsUseCase>(relaxed = true)

    init {
        coEvery { requestMoreCollectionItemsMock(any()) } returns true
    }

    private lateinit var viewModel: CollectionListViewModel

    @BeforeEach
    fun setup() {
        clearMocks(getGroupedCollectionItemsMock, requestMoreCollectionItemsMock)

        coEvery { requestMoreCollectionItemsMock(any()) } returns true

        viewModel = CollectionListViewModel(
            getGroupedCollectionStreamUseCase = getGroupedCollectionItemsMock,
            requestMoreCollectionItemsUseCase = requestMoreCollectionItemsMock,
        )
    }

    @Nested
    inner class SetGroupBy {

        @Test
        @DisplayName("Initial value is NONE")
        fun initialIsNone() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns false

            assertEquals(GroupField.NONE, viewModel.groupBy)
        }

        @Test
        @DisplayName("Correctly sets group field")
        fun setsGroupField() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns false

            viewModel.setGroupBy(GroupField.ARTIST_ASCENDING)

            assertEquals(GroupField.ARTIST_ASCENDING, viewModel.groupBy)
        }
    }

    @Nested
    inner class SetSearchQuery {

        @Test
        @DisplayName("Initial value is null")
        fun initialIsNone() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns false

            assertEquals(null, viewModel.searchQuery)
        }

        @Test
        @DisplayName("Correctly sets search query")
        fun setsSearchQuery() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns false

            viewModel.setSearchQuery("Van Gogh")

            assertEquals("Van Gogh", viewModel.searchQuery)
        }

        @Test
        @DisplayName("If called multiple times, sets only last query")
        fun ifCalledMultipleTimesWithinTheDebounceWindow_setsOnlyLastQuery() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns false

            viewModel.setSearchQuery("Van Gogh", debounceTimeMs = 250)
            delay(100)

            assertEquals(null, viewModel.searchQuery)

            viewModel.setSearchQuery("Pablo Picasso", debounceTimeMs = 250)
            delay(200)

            assertEquals(null, viewModel.searchQuery)

            viewModel.setSearchQuery("Salvador Dalí", debounceTimeMs = 250)
            delay(300)

            assertEquals("Salvador Dalí", viewModel.searchQuery)
        }
    }

    @Nested
    inner class ScreenState {

        @Test
        @DisplayName("Given successful request, returns successful UI state")
        fun givenSuccessfulRequest_returnsSuccessfulUiState() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns true

            viewModel.screenState.test {
                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())

                viewModel.requestCollectionItems(refreshData = true)

                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
            }
        }

        @Test
        @DisplayName("Given failed request, returns error UI state")
        fun givenFailedRequest_returnsErrorUiState() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } throws Exception("Something went wrong")

            viewModel.screenState.test {
                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())

                viewModel.requestCollectionItems(refreshData = true)

                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
                assertEquals(UiState.Error(message = "Something went wrong"), awaitItem())
            }
        }

        @Test
        @DisplayName("Given failed request, but then successful request, returns successful UI state")
        fun givenFailedRequest_andThenSuccessfulRequest_returnsSuccessfulUiState() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } throws Exception("Something went wrong")

            viewModel.screenState.test {
                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())

                viewModel.requestCollectionItems(refreshData = true)

                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
                assertEquals(UiState.Error(message = "Something went wrong"), awaitItem())

                coEvery { requestMoreCollectionItemsMock(any()) } returns true

                viewModel.requestCollectionItems(refreshData = true)

                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
                assertEquals(UiState.Success(CollectionScreenModel(emptyList())), awaitItem())
            }
        }
    }

    @Nested
    inner class CanLoadMore {

        @Test
        @DisplayName("Given we not loading anything, returns true")
        fun givenRequestStateIsSuccess_returnsTrue() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns true

            viewModel.canLoadMore.test {
                assertEquals(true, awaitItem())
            }
        }

        @Test
        @DisplayName("Given we're loading more items, returns false")
        fun givenRequestStateIsLoadingMore_returnsFalse() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns true

            viewModel.canLoadMore.test {
                assertEquals(true, awaitItem())

                viewModel.requestCollectionItems(refreshData = false)

                assertEquals(false, awaitItem())
                assertEquals(true, awaitItem())
            }
        }

        @Test
        @DisplayName("Given we're refreshing items, returns false")
        fun givenRequestStateIsRefreshing_returnsFalse() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns true

            viewModel.canLoadMore.test {
                assertEquals(true, awaitItem())

                viewModel.requestCollectionItems(refreshData = true)

                assertEquals(false, awaitItem())
                assertEquals(true, awaitItem())
            }
        }

        @Test
        @DisplayName("Given all items have been loaded, returns false")
        fun givenRequestStateIsAllItemsLoaded_returnsFalse() = runTest {
            every { getGroupedCollectionItemsMock(any()) } returns flowOf(emptyList())
            coEvery { requestMoreCollectionItemsMock(any()) } returns true

            viewModel.canLoadMore.test {
                assertEquals(true, awaitItem())

                viewModel.requestCollectionItems()

                assertEquals(false, awaitItem())
                assertEquals(true, awaitItem())
            }
        }
    }
}
