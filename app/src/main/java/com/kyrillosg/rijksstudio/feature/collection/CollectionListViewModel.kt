package com.kyrillosg.rijksstudio.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.GetCollectionItemsUseCase
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
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