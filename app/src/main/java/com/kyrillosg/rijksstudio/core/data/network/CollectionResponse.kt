package com.kyrillosg.rijksstudio.core.data.network

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import kotlinx.serialization.Serializable

@Serializable
data class CollectionResponse(
    val artObjects: List<NetworkCollectionItem>,
)

@Serializable
data class NetworkCollectionItem(
    val objectNumber: String,
    val principalOrFirstMaker: String,
    val webImage: NetworkCollectionImage? = null,
    override val title: String,
) : CollectionItem {

    override val itemId: CollectionItem.Id = CollectionItem.Id(objectNumber)
    override val author: String = principalOrFirstMaker
    override val imageUrl: String? = webImage?.url
}

@Serializable
data class NetworkCollectionImage(
    val url: String,
)
