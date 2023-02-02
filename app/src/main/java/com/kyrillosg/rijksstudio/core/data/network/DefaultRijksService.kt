package com.kyrillosg.rijksstudio.core.data.network

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.data.RijksService
import com.kyrillosg.rijksstudio.core.data.di.NetworkConfiguration
import com.kyrillosg.rijksstudio.core.model.CollectionItem
import com.kyrillosg.rijksstudio.core.model.DetailedCollectionItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class DefaultRijksService(
    private val client: HttpClient,
    private val config: NetworkConfiguration,
) : RijksService {

    override suspend fun getCollection(filter: CollectionFilter): List<CollectionItem> {
        val response = client.get("${config.baseUrl}/${filter.language}/collection") {
            url {
                parameters.append("key", config.apiKey)
                parameters.append("p", filter.page.toString())
                parameters.append("ps", filter.pageSize.toString())
                parameters.append("s", "artist")
                parameters.append("imgonly", "True")
                parameters.append("toppieces", "True")
            }
        }

        return response.body<CollectionResponse>().artObjects
    }

    override suspend fun getCollectionDetails(filter: CollectionDetailsFilter, ): DetailedCollectionItem {
        val response = client.get("${config.baseUrl}/${filter.language}/collection/${filter.id}") {
            url {
                parameters.append("key", config.apiKey)
            }
        }

        return response.body<CollectionDetailsResponse>().artObject
    }
}