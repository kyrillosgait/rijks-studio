package com.kyrillosg.rijksstudio.feature.collection.di

import com.kyrillosg.rijksstudio.feature.collection.CollectionDetailViewModel
import com.kyrillosg.rijksstudio.feature.collection.CollectionListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val collectionFeatureModule = module {

    viewModelOf(::CollectionListViewModel)
    viewModelOf(::CollectionDetailViewModel)
}