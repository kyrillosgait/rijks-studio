package com.kyrillosg.rijksstudio.core.data.network

import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDetailsResponse(
    val artObject: NetworkCollectionDetailsItem,
)

@Serializable
data class NetworkCollectionDetailsItem(
    val objectNumber: String,
    val principalOrFirstMaker: String,
    val webImage: NetworkCollectionImage? = null,
    override val title: String,
    override val description: String = "",
) : DetailedCollectionItem {

    override val itemId: CollectionItem.Id = CollectionItem.Id(objectNumber)
    override val author: String = principalOrFirstMaker
    override val imageUrl: String? = webImage?.url
}