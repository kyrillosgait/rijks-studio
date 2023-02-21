package com.kyrillosg.rijksstudio.feature.collection.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CollectionDetailViewModel(
    private val getDetailedCollectionItemUseCase: GetDetailedCollectionItemUseCase,
) : ViewModel() {

    private val _item = MutableStateFlow<UiState<DetailedCollectionItem>>(UiState.Loading)

    val detailedCollectionItem: Flow<UiState<DetailedCollectionItem>>
        get() = _item

    fun getDetails(id: CollectionItem.Id) {
        viewModelScope.launch {
            _item.value = UiState.Loading

            runCatching {
                getDetailedCollectionItemUseCase(id)
            }.onSuccess {
                _item.value = UiState.Success(it)
            }.onFailure {
                _item.value = UiState.Error(message = it.message.toString())
            }
        }
    }
}
