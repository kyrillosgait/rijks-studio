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
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class CollectionListViewModel(
    private val getGroupedCollectionStreamUseCase: GetGroupedCollectionStreamUseCase,
    private val requestMoreCollectionItemsUseCase: RequestMoreCollectionItemsUseCase,
) : ViewModel() {

    private val _groupBy = MutableStateFlow(GroupField.NONE)
    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _requestCollectionItemsState = MutableStateFlow<RequestState>(RequestState.None)

    val groupBy: GroupField
        get() = _groupBy.value

    val searchQuery: String?
        get() = _searchQuery.value

    val screenState: Flow<UiState<CollectionScreenModel>>
        get() = _groupBy
            .combine(_requestCollectionItemsState) { groupBy, requestState ->
                Pair(groupBy, requestState)
            }
            .flatMapLatest { pair ->
                val groupBy = pair.first

                getGroupedCollectionStreamUseCase(groupBy)
                    .map { collection -> collection.toUiState(requestState = pair.second) }
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)

    val canLoadMore: StateFlow<Boolean>
        get() = _requestCollectionItemsState
            .map { requestState ->
                when (requestState) {
                    RequestState.LoadingMore, RequestState.Refreshing, RequestState.AllItemsLoaded -> false
                    RequestState.None, RequestState.Success, is RequestState.Error -> true
                }
            }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = true,
            )

    init {
        requestCollectionItems(refreshData = true)
    }

    fun setGroupBy(groupBy: GroupField) {
        _groupBy.value = groupBy
    }

    private var job: Job? = null

    fun setSearchQuery(query: String?, debounceTimeMs: Long = 0L) {
        job?.cancel(CancellationException("Search query changed, pending job cancelled"))

        job = viewModelScope.launch {
            delay(debounceTimeMs)

            _searchQuery.value = query

            requestCollectionItems(refreshData = true)
        }
    }

    fun requestCollectionItems(refreshData: Boolean = false) {
        if (_requestCollectionItemsState.value == RequestState.LoadingMore ||
            _requestCollectionItemsState.value == RequestState.Refreshing
        ) {
            Napier.v { "Requested more items but another request is already in progress" }
            return
        }

        if (_requestCollectionItemsState.value == RequestState.AllItemsLoaded && !refreshData) {
            Napier.v { "Requested more items but all available items have already been loaded" }
            return
        }

        if (refreshData) {
            _requestCollectionItemsState.value = RequestState.Refreshing
        } else {
            _requestCollectionItemsState.value = RequestState.LoadingMore
        }

        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                val hasMoreItems = requestMoreCollectionItemsUseCase(
                    input = RequestMoreCollectionItemsUseCase.Params(
                        groupBy = groupBy,
                        refreshData = refreshData,
                        searchTerm = searchQuery,
                    ),
                )

                if (hasMoreItems) {
                    _requestCollectionItemsState.value = RequestState.Success
                } else {
                    _requestCollectionItemsState.value = RequestState.AllItemsLoaded
                }
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
                UiState.Success(
                    CollectionScreenModel.from(
                        collection = this,
                        footer = CollectionScreenModel.Companion.Footer.ProgressBar,
                    ),
                )
            }
            RequestState.AllItemsLoaded -> {
                UiState.Success(
                    CollectionScreenModel.from(
                        collection = this,
                        footer = CollectionScreenModel.Companion.Footer.EndOfList(searchQuery),
                    ),
                )
            }
            RequestState.Success,
            RequestState.Refreshing,
            RequestState.None,
            -> {
                UiState.Success(CollectionScreenModel.from(this))
            }
        }
    }

    private sealed interface RequestState {
        data class Error(val message: String) : RequestState
        object Success : RequestState
        object AllItemsLoaded : RequestState
        object LoadingMore : RequestState
        object Refreshing : RequestState
        object None : RequestState
    }
}

internal data class CollectionScreenModel(
    val items: List<CollectionListModel>,
) {

    companion object {
        fun from(
            collection: List<GroupedCollection>,
            footer: Footer = Footer.None,
        ): CollectionScreenModel {
            return CollectionScreenModel(
                items = collection
                    .flatMap { items -> items.toViewModel() }
                    .appendFooter(footer),
            )
        }

        private fun GroupedCollection.toViewModel(): List<CollectionListModel> {
            val items = items.map { CollectionListModel.ImageWithLabel.from(it) }

            val label = label?.let { label ->
                CollectionListModel.Label(
                    label = label,
                    uniqueId = label + items.map { it.uniqueId },
                )
            }

            return listOfNotNull(label) + items
        }

        private fun List<CollectionListModel>.appendFooter(footer: Footer): List<CollectionListModel> {
            return when (footer) {
                is Footer.EndOfList -> {
                    val label = buildString {
                        append("No ")

                        if (this@appendFooter.isNotEmpty()) {
                            append("more ")
                        }

                        append("results ")

                        footer.query?.let {
                            append("for \"$it\"")
                        }
                    }
                    this + CollectionListModel.Label(
                        label = label,
                        style = CollectionListModel.Label.Style.ITALIC,
                        uniqueId = label,
                    )
                }
                Footer.ProgressBar -> this + CollectionListModel.ProgressBar()
                Footer.None -> this
            }
        }

        sealed interface Footer {
            object None : Footer
            object ProgressBar : Footer
            data class EndOfList(val query: String?) : Footer
        }
    }
}
