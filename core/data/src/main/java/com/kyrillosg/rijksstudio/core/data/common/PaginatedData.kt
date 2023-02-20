package com.kyrillosg.rijksstudio.core.data.common

data class PaginatedData<T>(
    val items: T,
    val total: Int,
)
