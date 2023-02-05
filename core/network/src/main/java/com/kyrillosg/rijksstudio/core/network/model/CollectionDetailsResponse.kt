package com.kyrillosg.rijksstudio.core.network.model

import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.CollectionItemColor
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CollectionDetailsResponse(
    val artObject: NetworkCollectionDetailsItem,
)

@Serializable
internal data class NetworkCollectionDetailsItem(
    val objectNumber: String,
    val principalOrFirstMaker: String,
    @SerialName("webImage") override val image: NetworkCollectionImage? = null,
    override val title: String,
    override val description: String = "",
    override val colors: List<NetworkCollectionColor>,
    override val normalizedColors: List<NetworkCollectionColor>,
) : DetailedCollectionItem {

    override val itemId: CollectionItem.Id
        get() = CollectionItem.Id(objectNumber)

    override val author: String
        get() = principalOrFirstMaker
}

@Serializable
internal data class NetworkCollectionColor(
    override val percentage: Int,
    override val hex: String,
) : CollectionItemColor