package com.kyrillosg.rijksstudio.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.data.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.GetCollectionItemsUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CollectionListViewModel(
    private val getCollectionItemsUseCase: GetCollectionItemsUseCase,
) : ViewModel() {

    private val _collectionItems: MutableStateFlow<CollectionListUiState> =
        MutableStateFlow(CollectionListUiState.Loading)

    val collectionItems: StateFlow<CollectionListUiState>
        get() = _collectionItems

    init {
        viewModelScope.launch {

            val collectionItems = getCollectionItemsUseCase(Unit).also {
                it.forEach {
                    Napier.v { "CollectionItem - $it" }
                }
            }
            _collectionItems.value = CollectionListUiState.Success(collectionItems)
        }

    }
}

sealed interface CollectionListUiState {
    object Loading : CollectionListUiState
    object Error : CollectionListUiState
    data class Success(val collectionItems: List<CollectionItem>) : CollectionListUiState
}