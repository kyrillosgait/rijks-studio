package com.kyrillosg.rijksstudio.core.network

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.paging.PaginatedData
import com.kyrillosg.rijksstudio.core.data.RijksGateway
import com.kyrillosg.rijksstudio.core.data.model.CollectionItem
import com.kyrillosg.rijksstudio.core.data.model.DetailedCollectionItem
import com.kyrillosg.rijksstudio.core.data.model.GroupBy
import com.kyrillosg.rijksstudio.core.network.di.NetworkConfiguration
import com.kyrillosg.rijksstudio.core.network.model.CollectionDetailsResponse
import com.kyrillosg.rijksstudio.core.network.model.CollectionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal class DefaultRijksGateway(
    private val client: HttpClient,
    private val config: NetworkConfiguration,
) : RijksGateway {

    override suspend fun getCollection(filter: CollectionFilter): PaginatedData<List<CollectionItem>> {
        val response = client.get("${config.baseUrl}/${filter.language}/collection") {
            url {
                parameters.append("key", config.apiKey)
                parameters.append("p", filter.page.toString())
                parameters.append("ps", filter.pageSize.toString())
                parameters.append("imgonly", "True")
                parameters.append("toppieces", "True")

                when (filter.groupBy) {
                    GroupBy.NONE -> {
                        // Nothing to do
                    }
                    GroupBy.ARTIST_ASCENDING -> {
                        parameters.append("s", "artist")
                    }
                    GroupBy.ARTIST_DESCENDING -> {
                        parameters.append("s", "artistdesc")
                    }
                }
            }
        }

        val body = response.body<CollectionResponse>()

        return PaginatedData(
            items = body.artObjects,
            total = body.count,
        )
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter): DetailedCollectionItem? {
        val response = client.get("${config.baseUrl}/${filter.language}/collection/${filter.id}") {
            url {
                parameters.append("key", config.apiKey)
            }
        }

        return response.body<CollectionDetailsResponse>().artObject
    }
}