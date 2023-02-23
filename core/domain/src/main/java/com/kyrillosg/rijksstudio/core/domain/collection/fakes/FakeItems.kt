package com.kyrillosg.rijksstudio.core.domain.collection.fakes

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem

val fakeItems = (0..2250).map { index ->
    FakeDetailedCollectionItem.create(
        id = CollectionItem.Id(index.toString()),
        title = index.toString(),
        author = when (index) {
            in 0 until 105 -> "Leonardo da Vinci"
            in 105 until 237 -> "Pablo Picasso"
            in 237 until 843 -> "Salvador Dalí"
            in 843 until 2057 -> "Unknown"
            else -> "Vincent van Gogh"
        },
        imageWidth = setOf(144, 192, 256).random(),
        imageHeight = setOf(144, 192, 256).random(),
    )
}
