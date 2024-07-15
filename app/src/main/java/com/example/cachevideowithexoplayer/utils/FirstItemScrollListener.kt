package com.example.cachevideowithexoplayer.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class FirstItemScrollListener : RecyclerView.OnScrollListener() {

    private var isFirstItemVisible = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        onScrolledItem(recyclerView,dx, dy)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null && layoutManager.childCount > 0) {
            val firstVisibleItemPosition =
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstVisibleItemPosition == 0 && !isFirstItemVisible) {
                isFirstItemVisible = true
                onFirstItemVisible()
            } else if (firstVisibleItemPosition == 1 && !isFirstItemVisible) {
                isFirstItemVisible = false
                onSecondItemNotVisible()
            }else if (firstVisibleItemPosition > 0 && isFirstItemVisible) {
                isFirstItemVisible = false
                onFirstItemNotVisible()
            }
        }
    }

    abstract fun onFirstItemVisible()

    abstract fun onFirstItemNotVisible()
    abstract fun onSecondItemNotVisible()

    open fun onScrolledItem(recyclerView: RecyclerView, dx: Int, dy: Int){

    }
}




