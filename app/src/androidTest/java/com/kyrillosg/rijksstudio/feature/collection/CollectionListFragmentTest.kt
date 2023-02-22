package com.kyrillosg.rijksstudio.feature.collection

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.LargeTest
import com.kyrillosg.rijksstudio.feature.collection.list.CollectionListFragment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@LargeTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CollectionListFragmentTest {

    @Test
    fun testFragmentRecreate() {
        val scenario = launchFragmentInContainer<CollectionListFragment>()

        scenario.recreate()
    }
}
