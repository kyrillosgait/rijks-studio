package com.kyrillosg.rijksstudio.feature.collection.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionDetailViewModel(
    private val getDetailedCollectionItemUseCase: GetDetailedCollectionItemUseCase,
) : ViewModel() {

    private val _detailedCollectionItem = MutableStateFlow<DetailedCollectionItem?>(null)

    val detailedCollectionItem: Flow<com.kyrillosg.rijksstudio.core.ui.UiState<DetailedCollectionItem>>
        get() = _detailedCollectionItem
            .filterNotNull()
            .map<DetailedCollectionItem, com.kyrillosg.rijksstudio.core.ui.UiState<DetailedCollectionItem>> {
                com.kyrillosg.rijksstudio.core.ui.UiState.Success(it)
            }
            .catch { throwable ->
                throwable.message?.let { emit(com.kyrillosg.rijksstudio.core.ui.UiState.Error(it)) }
            }
            .onStart {
                emit(com.kyrillosg.rijksstudio.core.ui.UiState.Loading)
            }

    fun getDetails(id: CollectionItem.Id) {
        viewModelScope.launch {
            val detailedItem = getDetailedCollectionItemUseCase(id)
            _detailedCollectionItem.value = detailedItem
        }
    }
}
