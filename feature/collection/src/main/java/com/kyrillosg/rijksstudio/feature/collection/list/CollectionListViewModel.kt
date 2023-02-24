@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kyrillosg.rijksstudio.feature.collection.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.GroupedCollection
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetGroupedCollectionStreamUseCase
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GroupField
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.RequestMoreCollectionItemsUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.feature.collection.adapter.CollectionListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getGroupedCollectionStreamUseCase: GetGroupedCollectionStreamUseCase,
    private val requestMoreCollectionItemsUseCase: RequestMoreCollectionItemsUseCase,
) : ViewModel() {

    private val _groupBy = MutableStateFlow(GroupField.NONE)
    private val _requestCollectionItemsState = MutableStateFlow<RequestState>(RequestState.None)

    val groupBy: GroupField
        get() = _groupBy.value

    val screenState: Flow<UiState<CollectionScreenModel>>
        get() = _groupBy
            .combine(_requestCollectionItemsState) { groupBy, requestState ->
                Pair(groupBy, requestState)
            }
            .flatMapLatest { pair ->
                val groupBy = pair.first

                getGroupedCollectionStreamUseCase(groupBy)
                    .distinctUntilChanged()
                    .map { collection -> collection.toUiState(requestState = pair.second) }
                    .flowOn(Dispatchers.Default)
            }

    init {
        requestCollectionItems(refreshData = true)
    }

    fun setGroupBy(groupBy: GroupField) {
        _groupBy.value = groupBy
    }

    fun requestCollectionItems(refreshData: Boolean = false) {
        if (_requestCollectionItemsState.value == RequestState.LoadingMore ||
            _requestCollectionItemsState.value == RequestState.Refreshing
        ) {
            return
        }

        viewModelScope.launch {
            if (refreshData) {
                _requestCollectionItemsState.value = RequestState.Refreshing
            } else {
                _requestCollectionItemsState.value = RequestState.LoadingMore
            }

            runCatching {
                requestMoreCollectionItemsUseCase(
                    input = RequestMoreCollectionItemsUseCase.Params(groupBy, refreshData),
                )
            }.onSuccess {
                _requestCollectionItemsState.value = RequestState.Success
            }.onFailure {
                _requestCollectionItemsState.value = RequestState.Error(it.message.toString())
            }
        }
    }

    private fun List<GroupedCollection>.toUiState(requestState: RequestState): UiState<CollectionScreenModel> {
        return when (requestState) {
            is RequestState.Error -> {
                UiState.Error(message = requestState.message)
            }
            RequestState.LoadingMore -> {
                UiState.Success(CollectionScreenModel.from(this, isLoadingMore = true))
            }
            RequestState.Success,
            RequestState.Refreshing,
            RequestState.None,
            -> {
                UiState.Success(CollectionScreenModel.from(this, isLoadingMore = false))
            }
        }
    }

    private sealed interface RequestState {
        data class Error(val message: String) : RequestState
        object Success : RequestState
        object LoadingMore : RequestState
        object Refreshing : RequestState
        object None : RequestState
    }
}

data class CollectionScreenModel(
    val items: List<CollectionListModel>,
) {

    companion object {
        fun from(collection: List<GroupedCollection>, isLoadingMore: Boolean): CollectionScreenModel {
            return CollectionScreenModel(
                items = collection
                    .flatMap { items -> items.toViewModel() }
                    .appendLoadingItem { isLoadingMore && collection.isNotEmpty() },
            )
        }

        private fun GroupedCollection.toViewModel(): List<CollectionListModel> {
            val items = items.map { CollectionListModel.ImageWithLabel.from(it) }

            val header = label?.let { label ->
                CollectionListModel.Header(
                    label = label,
                    uniqueId = label + items.map { it.uniqueId },
                )
            }

            return listOfNotNull(header) + items
        }

        private fun List<CollectionListModel>.appendLoadingItem(predicate: () -> Boolean): List<CollectionListModel> {
            return if (predicate()) this + CollectionListModel.Loading() else this
        }
    }
}
