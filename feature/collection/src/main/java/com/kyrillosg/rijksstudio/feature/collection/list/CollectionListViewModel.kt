@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupedCollection
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetGroupedCollectionStreamUseCase
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.RequestMoreCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getGroupedCollectionStreamUseCase: GetGroupedCollectionStreamUseCase,
    private val requestMoreCollectionItemsUseCase: RequestMoreCollectionItemsUseCase,
) : ViewModel() {

    private val _groupBy = MutableStateFlow(GroupField.ARTIST_ASCENDING)
    private val _requestCollectionItemsState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))

    val groupBy: GroupField
        get() = _groupBy.value

    val requestCollectionItemsState: Flow<UiState<Unit>>
        get() = _requestCollectionItemsState

    val collectionItems: Flow<List<CollectionListViewData>>
        get() = _groupBy
            .combine(_requestCollectionItemsState) { groupBy, requestState ->
                Pair(groupBy, requestState)
            }
            .flatMapLatest { pair ->
                val groupBy = pair.first
                val isLoading = pair.second is UiState.Loading

                getGroupedCollectionStreamUseCase(groupBy)
                    .distinctUntilChanged()
                    .map { groupedCollectionItems ->
                        groupedCollectionItems.toViewData()
                    }
                    .map { items ->
                        items.appendLoadingItem { isLoading && items.isNotEmpty() }
                    }
                    .flowOn(Dispatchers.Default)
            }

    init {
        requestCollectionItems()
    }

    fun requestCollectionItems(refreshData: Boolean = false) {
        if (_requestCollectionItemsState.value == UiState.Loading) return

        viewModelScope.launch {
            _requestCollectionItemsState.value = UiState.Loading

            runCatching {
                requestMoreCollectionItemsUseCase(
                    input = RequestMoreCollectionItemsUseCase.Params(groupBy, refreshData),
                )
            }.onSuccess {
                _requestCollectionItemsState.value = UiState.Success(Unit)
            }.onFailure {
                _requestCollectionItemsState.value = UiState.Error(it.message.toString())
            }
        }
    }

    fun setGroupBy(groupBy: GroupField) {
        _groupBy.value = groupBy
    }

    private fun List<GroupedCollection>.toViewData(): List<CollectionListViewData> {
        return flatMap { groupedCollectionItems ->
            val items = groupedCollectionItems.items.map {
                CollectionListViewData.ImageWithLabel.from(it)
            }
            val header = groupedCollectionItems.label?.let { label ->
                CollectionListViewData.Header(
                    label = label,
                    uniqueId = label + items.map { it.uniqueId },
                )
            }

            listOfNotNull(header) + items
        }
    }

    private fun List<CollectionListViewData>.appendLoadingItem(predicate: () -> Boolean): List<CollectionListViewData> {
        return if (predicate()) this + CollectionListViewData.Loading() else this
    }
}
