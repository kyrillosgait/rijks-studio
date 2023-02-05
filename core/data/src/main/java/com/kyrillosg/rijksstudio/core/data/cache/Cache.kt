package com.kyrillosg.rijksstudio.core.data.cache

interface Cache<Key, Value> {

    fun put(key: Key, value: Value)

    fun get(key: Key): Value?
}