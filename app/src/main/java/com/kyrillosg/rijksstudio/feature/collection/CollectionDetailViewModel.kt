package com.kyrillosg.rijksstudio.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.GetDetailedCollectionItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class CollectionDetailViewModel(
    private val getDetailedCollectionItemUseCase: GetDetailedCollectionItemUseCase,
) : ViewModel() {

    private val _detailedCollectionItem = MutableStateFlow<DetailedCollectionItem?>(null)

    val detailedCollectionItem: Flow<DetailedCollectionItem>
        get() = _detailedCollectionItem
            .filterNotNull()

    fun getDetails(id: CollectionItem.Id) {
        viewModelScope.launch {
            val detailedItem = getDetailedCollectionItemUseCase(id)
            _detailedCollectionItem.value = detailedItem
        }
    }
}
