package com.kyrillosg.rijksstudio.core.ui

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
}
