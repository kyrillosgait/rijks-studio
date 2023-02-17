package com.kyrillosg.rijksstudio.feature.collection.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CollectionDetailViewModel(
    private val getDetailedCollectionItemUseCase: GetDetailedCollectionItemUseCase,
) : ViewModel() {

    private val _detailedCollectionItem = MutableStateFlow<DetailedCollectionItem?>(null)

    val detailedCollectionItem: Flow<UiState<DetailedCollectionItem>>
        get() = _detailedCollectionItem
            .filterNotNull()
            .map<DetailedCollectionItem, UiState<DetailedCollectionItem>> {
                UiState.Success(it)
            }
            .catch { throwable ->
                throwable.message?.let { emit(UiState.Error(it)) }
            }
            .onStart {
                emit(UiState.Loading)
            }

    fun getDetails(id: CollectionItem.Id) {
        viewModelScope.launch {
            val detailedItem = getDetailedCollectionItemUseCase(id)
            _detailedCollectionItem.value = detailedItem
        }
    }
}
