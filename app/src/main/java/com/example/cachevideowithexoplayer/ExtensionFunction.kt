package com.example.cachevideowithexoplayer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cachevideowithexoplayer.utils.FirstItemScrollListener
import com.siro.android.ui.adapter.viewholder.HomeTopBannerViewHolder
import java.util.ArrayList

fun getAppContext(): Context {
    return MyApplication.getContext()
}

fun getString(@StringRes resId: Int): String {
    return MyApplication.getContext().getString(resId)
}

fun View.setVisible(show: Boolean = true, invisible: Boolean = false) {
    if (show) this.visibility = View.VISIBLE
    else this.visibility = if (invisible) View.INVISIBLE else View.GONE
}
fun Int?.getSafe(defaultVal: Int = 0): Int {
    return this ?: defaultVal
}

fun Long?.getSafe(): Long {
    return this ?: 0L
}

fun Double?.round(upto: Int): String {
    if (this == null) return "0.0"
    return String.format("%.${upto}f", this)
}

fun Float?.getSafe(): Float {
    return this ?: 0f
}

fun Double?.getSafe(defaultValue: Double = 0.0): Double {
    return this ?: defaultValue
}

fun Int?.getBoolean(): Boolean {
    if (this != null && this == 1) return true
    return false
}

fun String?.getBoolean(): Boolean {
    if (this != null && (this == "1" || this == "true")) return true
    return false
}

fun Boolean?.getSafe(defaultValue: Boolean = false): Boolean {
    return this ?: defaultValue
}


//fun LocalDate?.getSafe(defaultValue: LocalDate = LocalDate.now()): LocalDate {
//    return this ?: defaultValue
//}

fun Boolean?.getInt(): Int {
    if (this != null && this) return 1
    return 0
}

fun <T> ArrayList<T>?.getSafe(): ArrayList<T> {
    return this ?: arrayListOf()
}

fun <T> List<T>?.getSafe(): ArrayList<T> {
    return (this ?: arrayListOf()) as ArrayList<T>
}
fun View.setHeight(spaceHeight: Int) {
    val layoutParams = this.layoutParams
    layoutParams.height = this.context.resources.getDimensionPixelSize(spaceHeight)
    this.layoutParams = layoutParams
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
fun Activity.getRealScreenSize(): Pair<Int, Int> { //<width, height> result.first(), result.second()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val size = Point()
        val mode = display?.mode
        Pair(mode?.physicalWidth.getSafe(), mode?.physicalHeight.getSafe())
    } else {
        val size = Point()
        windowManager.defaultDisplay.getRealSize(size)
        Pair(size.x, size.y)
    }
}

@UnstableApi
fun RecyclerView.addCustomScrollListener(
    onFirstItemVisible: () -> Unit,
    onFirstItemNotVisible: () -> Unit,
    onSecondItemNotVisible: () -> Unit
) {
    var firstVisibleItem: Int = 0
    var lastVisibleItem: Int = 0
    var visibleCount: Int = 0
    var y: Int = 0
    val layoutManager = layoutManager as? LinearLayoutManager ?: return

    val scrollListener = object : FirstItemScrollListener() {

        override fun onFirstItemVisible() {
            onFirstItemVisible.invoke()
        }

        override fun onFirstItemNotVisible() {
            onFirstItemNotVisible.invoke()
        }

        override fun onSecondItemNotVisible() {
            onSecondItemNotVisible.invoke()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {

                    for (i in 0 until visibleCount) {
                        val view =
                            recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
                                ?: continue

                        if (view is HomeTopBannerViewHolder) {
                            if (view.binding.videoView.visibility == View.VISIBLE) {
                                handlePlayAndPauseCondition(view)
                            }
                            break
                        }
                    }
                }
            }
        }

        private fun handlePlayAndPauseCondition(view: HomeTopBannerViewHolder) {
            if (y <= 0) {
                //scroll up
                if (visibleAreaOffset(
                        view.binding.videoView,
                        view.itemView
                    ) >= 0.30F
                ) {
                    ExoPlayerManager.pauseAllPlayer(view.exoPlayer)
                    view.startPlayback()
                } else {
                    ExoPlayerManager.playNextOrPreviousVideo(
                        view.exoPlayer,
                        view.bindingAdapterPosition,
                        true
                    )
                    view.pausePlayback()
                }
                return
            } else {

                //scroll down
                y = 0;
                if (visibleAreaOffset(
                        view.binding.videoView,
                        view.itemView
                    ) <= 0.30F
                ) {
                    ExoPlayerManager.playNextOrPreviousVideo(
                        view.exoPlayer,
                        view.bindingAdapterPosition,
                        false
                    )
                    view.pausePlayback()
                } else {
                    ExoPlayerManager.pauseAllPlayer(view.exoPlayer)
                    view.startPlayback()
                }
                return
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            firstVisibleItem = layoutManager?.findFirstVisibleItemPosition() ?: 0;
            lastVisibleItem = layoutManager?.findLastVisibleItemPosition() ?: 0;
            visibleCount = (lastVisibleItem - firstVisibleItem) + 1;

            if (dx == 0 && dy == 0 && recyclerView.childCount > 0) {
                var viewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(0))
                if (viewHolder is HomeTopBannerViewHolder) {
                    if (viewHolder.binding.videoView.visibility == View.VISIBLE) {
                        viewHolder.startPlayback()
                    }
                }
            }
            y = dy
        }
    }

    addOnScrollListener(scrollListener)
}

fun getViewRect(view: View): Rect {
    val rect = Rect()
    val offset = Point()
    view.getGlobalVisibleRect(rect, offset)
    return rect
}

fun visibleAreaOffset(player: PlayerView, parent: View): Float {
    val videoRect = getViewRect(player)
    val parentRect = getViewRect(parent)

    return if ((parentRect.contains(videoRect) || parentRect.intersect(videoRect))) {
        val visibleArea = (videoRect.height() * videoRect.width()).toFloat()
        val viewArea = player.width * player.height
        if (viewArea <= 0f) 1f else visibleArea / viewArea
    } else {
        0f
    }
}

fun visibleAreaOffset(parent: View): Float {
    val videoRect = getViewRect(parent)
    val parentRect = getViewRect(parent)

    return if ((parentRect.contains(videoRect) || parentRect.intersect(videoRect))) {
        val visibleArea = (videoRect.height() * videoRect.width()).toFloat()
        val viewArea = parent.getWidth() * parent.getHeight()
        if (viewArea <= 0f) 1f else visibleArea / viewArea
    } else {
        0f
    }
}
