@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupBy
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetCollectionItemStreamUseCase
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.RequestMoreCollectionItemsUseCase
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getCollectionItemStreamUseCase: GetCollectionItemStreamUseCase,
    private val requestMoreCollectionItemsUseCase: RequestMoreCollectionItemsUseCase,
) : ViewModel() {

    private val _groupBy = MutableStateFlow(GroupBy.ARTIST_ASCENDING)
    private val _isLoading = MutableStateFlow(false)

    val groupBy: GroupBy
        get() = _groupBy.value

    val collectionItems: Flow<List<CollectionListViewData>>
        get() = _groupBy
            .combine(_isLoading) { groupBy, isLoading -> Pair(groupBy, isLoading) }
            .flatMapLatest { pair ->
                val groupBy = pair.first
                val isLoading = pair.second

                getCollectionItemStreamUseCase(groupBy)
                    .distinctUntilChanged()
                    .map { groupedCollectionItemList ->

                        groupedCollectionItemList
                            .flatMap { groupedCollectionItems ->
                                val items = groupedCollectionItems.items.map {
                                    CollectionListViewData.ImageWithLabel.from(it)
                                }
                                val header = groupedCollectionItems.label?.let { label ->
                                    CollectionListViewData.Header(
                                        label = label,
                                        uniqueId = groupedCollectionItems.label + items.map { it.uniqueId },
                                    )
                                }

                                listOfNotNull(header) + items
                            }
                    }
                    .map { items -> if (isLoading) items + CollectionListViewData.Loading() else items }
                    .flowOn(Dispatchers.Default)
            }

    fun requestCollectionItems() {
        viewModelScope.launch {
            _isLoading.value = true

            requestMoreCollectionItemsUseCase(groupBy)

            _isLoading.value = false
        }
    }

    fun setGroupBy(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }
}
