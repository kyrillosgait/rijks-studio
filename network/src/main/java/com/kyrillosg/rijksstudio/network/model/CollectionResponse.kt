package com.kyrillosg.rijksstudio.network.model

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import kotlinx.serialization.Serializable

@Serializable
internal data class CollectionResponse(
    val artObjects: List<NetworkCollectionItem>,
)

@Serializable
internal data class NetworkCollectionItem(
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
internal data class NetworkCollectionImage(
    val url: String,
)
