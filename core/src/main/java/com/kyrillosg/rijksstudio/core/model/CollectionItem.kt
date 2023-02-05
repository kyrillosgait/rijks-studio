package com.kyrillosg.rijksstudio.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

interface CollectionItem {
    val itemId: Id
    val title: String
    val author: String
    val image: CollectionItemImage?

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