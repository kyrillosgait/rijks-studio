package com.kyrillosg.rijksstudio.core.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds an on scroll listener to [RecyclerView], which triggers based on the [prefetchDistance].
 * Requires a [LinearLayoutManager] to have been set.
 *
 * @param prefetchDistance how far from the edge of loaded content an access must be to trigger the listener.
 *  Defaults to the number of visible items on screen.
 * @param predicate additional condition(s) to trigger the listener.
 * @param onTrigger what happens when the listener is triggered.
 */
fun RecyclerView.addOnEndlessScrollListener(
    prefetchDistance: (visibleItemCount: Int) -> Int = { visibleItemCount -> visibleItemCount },
    predicate: () -> Boolean = { true },
    onTrigger: () -> Unit,
) {
    val layoutManager = layoutManager

    require(layoutManager != null) { "RecyclerView has no layoutManager set" }

    require(layoutManager is LinearLayoutManager) {
        "RecyclerView.addOnEndlessScrollListener only supports LinearLayoutManager"
    }

    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val isScrollingDown = dy > 0

            if (isScrollingDown && predicate()) {
                val totalItemCount = layoutManager.itemCount
                val scrolledItemCount = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val accessedItemCount = scrolledItemCount + visibleItemCount

                val shouldTrigger = totalItemCount <= accessedItemCount + prefetchDistance(visibleItemCount)

                if (shouldTrigger) {
                    onTrigger()
                }
            }
        }
    })
}
