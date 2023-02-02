package com.kyrillosg.rijksstudio.core.cache

interface Cache<Key, Value> {

    fun put(key: Key, value: Value)

    fun get(key: Key): Value?
}