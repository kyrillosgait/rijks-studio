package com.kyrillosg.rijksstudio.core.data.network

import com.kyrillosg.rijksstudio.core.data.RijksService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class DefaultRijksService(
    private val client: HttpClient,
) : RijksService {

    override suspend fun getCollectionItems(
        page: Int,
        pageSize: Int,
    ): CollectionItemResponse {
        val response = client.get("https://www.rijksmuseum.nl/api/en/collection") {
            url {
                parameters.append("key", "placeholder")
                parameters.append("p", page.toString())
                parameters.append("ps", pageSize.toString())
                parameters.append("s", "artist")
                parameters.append("imgonly", "True")
                parameters.append("toppieces", "True")
            }
        }

        return response.body()
    }
}