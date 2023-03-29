package com.kyrillosg.rijksstudio.core.domain.collection.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

interface CollectionItem {
    val itemId: Id
    val title: String
    val author: String
    val originalImage: CollectionItemImage?
    val imageUrl: String?
    val thumbnailUrl: String?

    @Serializable
    @Parcelize
    @JvmInline
    value class Id(val value: String) : Parcelable
}

interface CollectionItemImage {
    val url: String
    val width: Int
    val height: Int
}
