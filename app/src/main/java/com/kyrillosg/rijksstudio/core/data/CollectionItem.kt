package com.kyrillosg.rijksstudio.core.data

interface CollectionItem {
    val itemId: Id
    val title: String
    val author: String
    val imageUrl: String?

    @JvmInline
    value class Id(val value: String)
}