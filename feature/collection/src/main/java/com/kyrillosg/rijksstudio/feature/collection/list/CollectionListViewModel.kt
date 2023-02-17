@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import com.kyrillosg.rijksstudio.core.domain.GetPaginatedCollectionItems
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class CollectionListViewModel(
    private val getPaginatedCollectionItemsUseCase: GetPaginatedCollectionItems,
) : ViewModel() {

    private val _groupBy = MutableStateFlow(GroupBy.ARTIST_ASCENDING)

    val groupBy: GroupBy
        get() = _groupBy.value

    val collectionPagingData: Flow<PagingData<CollectionListViewData>>
        get() = _groupBy.flatMapLatest {
            getPaginatedCollectionItemsUseCase(it)
                .distinctUntilChanged()
                .map { pagingData ->
                    pagingData
                        // Kinda lame that PagingData<T> doesn't allow any kind of transformations,
                        // as we now have to keep a useless property in the `ImageWithLabel` model
                        // just to be able to map it to the header where needed
                        .map { collectionItem ->
                            CollectionListViewData.ImageWithLabel.from(collectionItem)
                        }
                        .insertSeparators { before, after ->
                            if (it == GroupBy.NONE) return@insertSeparators null

                            // Inserting separators dynamically don't play very well with list -> detail
                            // and then back to list
                            when {
                                before?.header != after?.header && after?.header != null -> {
                                    CollectionListViewData.Header(
                                        label = after.header,
                                        uniqueId = after.header + after.label + after.image,
                                    )
                                }
                                else -> null
                            }
                        }
                }
                .flowOn(Dispatchers.Default)
                .cachedIn(viewModelScope)
        }

    fun setGroupBy(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }
}
