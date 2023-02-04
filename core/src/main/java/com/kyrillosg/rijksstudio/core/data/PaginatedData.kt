package com.kyrillosg.rijksstudio.core.data

data class PaginatedData<T>(
    val items: T,
    val total: Int,
)