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

    val groupBy: GroupBy
        get() = _groupBy.value

    val collectionItems: Flow<List<CollectionListViewData>>
        get() = _groupBy.flatMapLatest { groupBy ->
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
                .flowOn(Dispatchers.Default)
        }

    fun requestCollectionItems() {
        viewModelScope.launch(Dispatchers.IO) {
            requestMoreCollectionItemsUseCase(groupBy)
        }
    }

    fun setGroupBy(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }
}
