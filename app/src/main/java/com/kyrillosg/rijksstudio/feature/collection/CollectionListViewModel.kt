package com.kyrillosg.rijksstudio.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.GetCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.domain.GetPaginatedCollectionItems
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
    private val getPaginatedCollectionItemsUseCase: GetPaginatedCollectionItems,
) : ViewModel() {

    private val _collectionList: MutableStateFlow<List<CollectionItem>> =
        MutableStateFlow(emptyList())

    val collectionList: Flow<List<CollectionListViewData>>
        get() = _collectionList.map { items ->
            items
                .groupBy { it.author }
                .flatMap { authorWithItems ->
                    val headerItem = CollectionListViewData.Header(authorWithItems.key)
                    val imageWithLabelItems = authorWithItems.value.map { collectionItem ->
                        CollectionListViewData.ImageWithLabel.from(collectionItem)
                    }
                    listOf(headerItem) + imageWithLabelItems
                }
        }

    val collectionPagingData: Flow<PagingData<CollectionListViewData>>
        get() = getPaginatedCollectionItemsUseCase(Unit)
            .map { pagingData ->
                pagingData
                    // Kinda lame that PagingData<T> doesn't allow any kind of transformations,
                    // as we now have to keep a useless property in the `ImageWithLabel` model
                    // just to be able to map it to the header where needed
                    .map { collectionItem -> CollectionListViewData.ImageWithLabel.from(collectionItem) }
                    .insertSeparators { before, after ->
                        when {
                            before?.header != after?.header && after?.header != null -> {
                                CollectionListViewData.Header(after.header)
                            }
                            else -> null
                        }
                    }
            }
            .flowOn(Dispatchers.Default)
            .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val collectionItems = getCollectionItemsUseCase(Unit)
            _collectionList.value = collectionItems
        }
    }
}

sealed interface CollectionListUiState {
    object Loading : CollectionListUiState
    object Error : CollectionListUiState
    data class Success(val collectionItems: List<CollectionItem>) : CollectionListUiState
}