package com.kyrillosg.rijksstudio.core.domain.collection.model

interface DetailedCollectionItem : CollectionItem {
    val description: String
    val colors: List<CollectionItemColor>
    val normalizedColors: List<CollectionItemColor>
}

interface CollectionItemColor {
    val percentage: Int
    val hex: String
}
