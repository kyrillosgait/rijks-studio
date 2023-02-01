package com.kyrillosg.rijksstudio.core.data

class DefaultCollectionItemCache : CollectionItemCache {

    private val cache = hashMapOf<Int, Entry>()

    override fun insert(key: RijksService.CollectionFilter, value: List<CollectionItem>) {
        val entry = Entry(value)
        cache[key.hashCode()] = entry
    }

    override fun get(key: RijksService.CollectionFilter): List<CollectionItem>? {
        return cache[key.hashCode()]?.value
    }

    data class Entry(val value: List<CollectionItem>)
}