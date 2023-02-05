package com.kyrillosg.rijksstudio.core.data.paging

data class PaginatedData<T>(
    val items: T,
    val total: Int,
)