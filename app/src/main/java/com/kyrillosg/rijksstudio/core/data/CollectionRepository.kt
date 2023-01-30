package com.kyrillosg.rijksstudio.core.data

interface CollectionRepository {

    suspend fun getCollectionItems(): List<CollectionItem>
}