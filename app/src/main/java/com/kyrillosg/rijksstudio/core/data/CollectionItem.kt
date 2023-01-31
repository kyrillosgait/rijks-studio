package com.kyrillosg.rijksstudio.core.data

import kotlinx.serialization.Serializable

interface CollectionItem {
    val itemId: Id
    val title: String
    val author: String
    val imageUrl: String?

    @Serializable
    @JvmInline
    value class Id(val value: String)
}