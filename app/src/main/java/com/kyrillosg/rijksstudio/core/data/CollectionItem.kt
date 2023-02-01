package com.kyrillosg.rijksstudio.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

interface CollectionItem {
    val itemId: Id
    val title: String
    val author: String
    val imageUrl: String?

    @Serializable
    @Parcelize
    @JvmInline
    value class Id(val value: String) : Parcelable
}