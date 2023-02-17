package com.kyrillosg.rijksstudio.core.network

import com.kyrillosg.rijksstudio.core.data.CollectionDetailsFilter
import com.kyrillosg.rijksstudio.core.data.CollectionFilter
import com.kyrillosg.rijksstudio.core.network.di.NetworkConfiguration
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DefaultRijksGatewayTest {

    private val responseHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    private val collectionItemsSuccessJson = readFile("get_collection_items.json")
    private val collectionItemDetailSuccessJson = readFile("get_collection_item_details.json")

    private val gateway = DefaultRijksGateway(
        client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    when {
                        request.url.encodedPath.contains("/collection/") -> {
                            respond(
                                collectionItemDetailSuccessJson,
                                HttpStatusCode.OK,
                                responseHeaders,
                            )
                        }
                        request.url.encodedPath.contains("/collection") -> {
                            respond(
                                collectionItemsSuccessJson,
                                HttpStatusCode.OK,
                                responseHeaders,
                            )
                        }
                        else -> error("unsupported request")
                    }
                }
            }
            expectSuccess = true
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        coerceInputValues = true
                    },
                )
            }
        },
        config = NetworkConfiguration(
            baseUrl = "https://test.com/api",
            apiKey = "myKey",
        ),
    )

    @Nested
    @DisplayName("Given successful response")
    inner class SuccessfulResponse {

        @Test
        @DisplayName("Items are deserialized successfully")
        fun collectionItems_deserializationSuccess() {
            runTest {
                val filter = CollectionFilter(page = 0, pageSize = 20)
                val data = gateway.getCollection(filter)
                val collectionItems = data.items

                assertEquals(collectionItems.size, filter.pageSize)
            }
        }

        @Test
        @DisplayName("Detail item is deserialized successfully")
        fun collectionItemDetails_deserializationSuccess() {
            runTest {
                val filter = CollectionDetailsFilter(id = "NG-MC-807")
                val collectionDetailsItem = gateway.getCollectionDetails(filter)

                assertEquals(collectionDetailsItem?.itemId?.value, filter.id)
            }
        }
    }

    private fun readFile(name: String): String {
        return object {}.javaClass.getResourceAsStream("/$name")
            ?.bufferedReader()?.use { it.readText() }.orEmpty()
    }
}
