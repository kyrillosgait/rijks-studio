package com.kyrillosg.rijksstudio.core.data.cache

class DefaultCache<Key, Value> : Cache<Key, Value> {

    private val cache = hashMapOf<Int, Entry<Value>>()

    override fun put(key: Key, value: Value) {
        val entry = Entry(value)
        cache[key.hashCode()] = entry
    }

    override fun get(key: Key): Value? {
        return cache[key.hashCode()]?.value
    }

    data class Entry<Value>(val value: Value)
}

fun <Key, Value> simpleCacheOf() = DefaultCache<Key, Value>()