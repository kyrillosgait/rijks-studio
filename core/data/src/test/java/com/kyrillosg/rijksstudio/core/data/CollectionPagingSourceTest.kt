package com.kyrillosg.rijksstudio.core.data

import androidx.paging.PagingSource
import com.kyrillosg.rijksstudio.core.data.cache.cacheOf
import com.kyrillosg.rijksstudio.core.data.fake.FakeRijksGateway
import com.kyrillosg.rijksstudio.core.data.paging.CollectionPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CollectionPagingSourceTest {

    private val pageSize = 2

    private val fakeItems = (0 until 50).map {
        com.kyrillosg.rijksstudio.core.data.fake.FakeDetailedCollectionItem.create(
            author = "Vincent van Gogh",
            imageWidth = setOf(144, 192, 256).random(),
            imageHeight = setOf(144, 192, 256).random()
        )
    }

    private val pagingSource = CollectionPagingSource(
        service = FakeRijksGateway(
            collectionItems = fakeItems,
            pageSize = pageSize
        ),
        cache = cacheOf(),
        pageSize = pageSize,
    )

    @Test
    @DisplayName("Initial load returns correct LoadResult when loadSize == pageSize")
    fun initialLoad_returnsCorrectLoadResult_whenLoadSizeIsPageSize() = runTest {
        val expected: PagingSource.LoadResult<Int, com.kyrillosg.rijksstudio.core.data.model.CollectionItem> = PagingSource.LoadResult.Page(
            data = listOf(fakeItems[0], fakeItems[1]),
            prevKey = null,
            nextKey = 1,
            itemsBefore = 0,
            itemsAfter = 48,
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = pageSize,
                placeholdersEnabled = false,
            )
        )

        println(expected)
        println(actual)

        assert(expected == actual)
    }

    @Test
    @DisplayName("Initial load returns correct LoadResult when loadSize is multiple of pageSize")
    fun initialLoad_returnsCorrectLoadResult_whenLoadSizeDiffersFromPageSize() = runTest {
        val expected: PagingSource.LoadResult<Int, com.kyrillosg.rijksstudio.core.data.model.CollectionItem> = PagingSource.LoadResult.Page(
            data = fakeItems.subList(0, 6),
            prevKey = null,
            nextKey = 3,
            itemsBefore = 0,
            itemsAfter = 44,
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3 * pageSize,
                placeholdersEnabled = false,
            )
        )

        println(expected)
        println(actual)

        assert(expected == actual)
    }

    @Test
    @DisplayName("Refreshing after invalidating returns correct LoadResult")
    fun loadAfterInvalidating_returnsCorrectLoadResult_whenLoadSizeIsPageSize() = runTest {
        val returnedItems = listOf(fakeItems[40], fakeItems[41])
        val expected: PagingSource.LoadResult<Int, com.kyrillosg.rijksstudio.core.data.model.CollectionItem> = PagingSource.LoadResult.Page(
            data = returnedItems,
            prevKey = 19,
            nextKey = 21,
            itemsBefore = 40,
            itemsAfter = 8,
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 20,
                loadSize = pageSize,
                placeholdersEnabled = false,
            )
        )

        println(expected)
        println(actual)

        assert(expected == actual)
    }

    @Test
    @DisplayName("Refreshing after invalidating returns correct LoadResult when loadSize is multiple of pageSize")
    fun loadAfterInvalidating_returnsCorrectLoadResult_whenLoadSizeDiffersFromPageSize() = runTest {
        val returnedItems = fakeItems.subList(40, 46)
        val expected: PagingSource.LoadResult<Int, com.kyrillosg.rijksstudio.core.data.model.CollectionItem> = PagingSource.LoadResult.Page(
            data = returnedItems,
            prevKey = 19,
            nextKey = 23,
            itemsBefore = 40,
            itemsAfter = 4,
        )

        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 20,
                loadSize = 3 * pageSize,
                placeholdersEnabled = false,
            )
        )

        println(expected)
        println(actual)

        assert(expected == actual)
    }
}