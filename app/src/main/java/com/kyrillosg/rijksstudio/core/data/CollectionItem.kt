package com.kyrillosg.rijksstudio.core.data

interface CollectionItem {
    val id: Id
    val title: String
    val author: String
    val image: String?

    @JvmInline
    value class Id(val value: String)
}