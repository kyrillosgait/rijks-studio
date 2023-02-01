package com.kyrillosg.rijksstudio.core.data.network

import com.kyrillosg.rijksstudio.core.data.RijksService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class DefaultRijksService(
    private val client: HttpClient,
) : RijksService {

    override suspend fun getCollection(
        filter: RijksService.CollectionFilter
    ): CollectionResponse {
        val response = client.get("https://www.rijksmuseum.nl/api/${filter.language}/collection") {
            url {
                parameters.append("key", "placeholder")
                parameters.append("p", filter.page.toString())
                parameters.append("ps", filter.pageSize.toString())
                parameters.append("s", "artist")
                parameters.append("imgonly", "True")
                parameters.append("toppieces", "True")
            }
        }

        return response.body()
    }

    override suspend fun getCollectionDetails(
        objectNumber: String,
    ): CollectionDetailsResponse {
        val response = client.get("https://www.rijksmuseum.nl/api/en/collection/$objectNumber") {
            url {
                parameters.append("key", "")
            }
        }

        return response.body()
    }
}