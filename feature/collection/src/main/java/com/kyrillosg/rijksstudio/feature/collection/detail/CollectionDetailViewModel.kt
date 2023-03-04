package com.kyrillosg.rijksstudio.feature.collection.detail

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItemColor
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItemImage
import com.kyrillosg.rijksstudio.core.domain.collection.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.usecases.GetDetailedCollectionItemUseCase
import com.kyrillosg.rijksstudio.core.ui.UiState
import com.kyrillosg.rijksstudio.core.ui.views.ColorPaletteView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class CollectionDetailViewModel(
    private val getDetailedCollectionItemUseCase: GetDetailedCollectionItemUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<UiState<DetailScreenModel>>(UiState.Loading)

    val screenState: Flow<UiState<DetailScreenModel>>
        get() = _screenState

    fun getDetails(id: CollectionItem.Id) {
        viewModelScope.launch(Dispatchers.Default) {
            _screenState.value = UiState.Loading

            runCatching {
                getDetailedCollectionItemUseCase(id)
            }.onSuccess {
                _screenState.value = UiState.Success(DetailScreenModel.from(it))
            }.onFailure {
                _screenState.value = UiState.Error(message = it.message.toString())
            }
        }
    }
}

internal data class DetailScreenModel(
    val imageUrl: String?,
    val imageRatio: String?,
    val description: String,
    val colors: ColorPaletteView.Model,
    val normalizedColors: ColorPaletteView.Model,
) {

    companion object {
        fun from(item: DetailedCollectionItem): DetailScreenModel {
            return DetailScreenModel(
                imageUrl = item.image?.url,
                imageRatio = item.image?.toImageRatio(),
                description = item.plaqueDescription ?: item.description,
                colors = item.normalizedColors.toViewModel(label = "Colors"),
                normalizedColors = item.normalizedColors.toViewModel(label = "Normalized colors"),
            )
        }

        private fun CollectionItemImage.toImageRatio(): String {
            return "$width:$height"
        }

        private fun List<CollectionItemColor>.toViewModel(label: String): ColorPaletteView.Model {
            return ColorPaletteView.Model(
                label = label,
                colors = this
                    .sortedByDescending { it.percentage }
                    .map { collectionItemColor ->
                        ColorPaletteView.Model.Color(
                            colorInt = Color.parseColor(collectionItemColor.hex.trim()),
                            percentage = "${collectionItemColor.percentage}%",
                        )
                    },
            )
        }
    }
}
