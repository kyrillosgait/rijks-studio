package com.kyrillosg.rijksstudio.core.network.model

import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItem
import com.kyrillosg.rijksstudio.core.domain.collection.model.CollectionItemImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CollectionResponse(
    val artObjects: List<NetworkCollectionItem>,
    val count: Int,
)

@Serializable
internal data class NetworkCollectionItem(
    val objectNumber: String,
    val principalOrFirstMaker: String,
    @SerialName("webImage") override val image: NetworkCollectionImage? = null,
    override val title: String,
) : CollectionItem {

    override val itemId: CollectionItem.Id = CollectionItem.Id(objectNumber)
    override val author: String = principalOrFirstMaker
}

@Serializable
internal data class NetworkCollectionImage(
    override val url: String,
    override val width: Int,
    override val height: Int,
) : CollectionItemImage
