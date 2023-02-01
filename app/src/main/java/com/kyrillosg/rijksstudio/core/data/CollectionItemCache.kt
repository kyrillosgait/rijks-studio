package com.kyrillosg.rijksstudio.core.data

interface CollectionItemCache {

    fun insert(key: RijksService.CollectionFilter, value: List<CollectionItem>)

    fun get(key: RijksService.CollectionFilter): List<CollectionItem>?
}