package com.siro.android.ui.adapter.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.cachevideowithexoplayer.ExoPlayerManager
import com.example.cachevideowithexoplayer.R
import com.example.cachevideowithexoplayer.databinding.CustomHeadLargeImgMidHeadDescViewBinding
import com.example.cachevideowithexoplayer.databinding.CustomHeadLargeSquareImgMidHeadDescViewBinding
import com.example.cachevideowithexoplayer.databinding.DividerViewBinding
import com.example.cachevideowithexoplayer.databinding.LayoutHomeTopBannerBinding
import com.example.cachevideowithexoplayer.findActivity
import com.example.cachevideowithexoplayer.getRealScreenSize
import com.example.cachevideowithexoplayer.getSafe
import com.example.cachevideowithexoplayer.model.BaseModel
import com.example.cachevideowithexoplayer.setHeight
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.siro.android.base.BaseViewHolder
import com.siro.android.ui.adapter.clicklistener.ListActionClickListener
import com.siro.android.ui.adapter.clicklistener.ListActionClickType


const val className = "ViewHolder/"


@UnstableApi
class HomeTopBannerViewHolder(
    var binding: LayoutHomeTopBannerBinding, val clickListener: ListActionClickListener?
) : BaseViewHolder<BaseModel>(binding.root) {

    var exoPlayer: ExoPlayer? = null

    fun playbackStateListener(
        ivLoader: CircularProgressIndicator? = null
    ) = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    // Handle playback ended
                }

                Player.STATE_READY -> {
                    ivLoader?.visibility = View.GONE
                }

                Player.STATE_BUFFERING -> {
                    ivLoader?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onBind(item: BaseModel, position: Int) {
        exoPlayer = ExoPlayerManager.initializePlayer(binding.root.context, position)

        val screenHeight =
            binding.root.context.findActivity()?.getRealScreenSize()?.second.getSafe() //height
        binding.apply {
            itemView.apply {
                videoView.layoutParams.height = screenHeight * 3 / 5
                videoView.requestLayout()
            }


            itemView.setOnClickListener {
                clickListener?.onItemClick(ListActionClickType.OnItemClick(item, position))
            }

            videoView.player?.volume = 0f
                videoView.player = exoPlayer
                ExoPlayerManager.setMediaItem(
                    exoPlayer!!,
                    getVideoUrl(position),
                    item.playbackPosition,
                    playbackStateListener(ivLoader)
                )

        }
    }

    fun startPlayback() {
        exoPlayer?.playWhenReady = true
        exoPlayer?.play()
    }

    fun pausePlayback() {
        exoPlayer?.playWhenReady = false
        exoPlayer?.pause()
    }

    fun getVideoUrl(position: Int): String {
        return if (position == 0) {
            "https://static.videezy.com/system/resources/previews/000/005/456/original/Earth_Eclipse_Rotate_Medium.mp4"
        } /*else if (position == 3) {
            "https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
        }*/ else if (position == 5) {
            "https://samplelib.com/lib/preview/mp4/sample-20s.mp4"
        } else {
            ""
        }
    }
}

class ItemDividerViewHolder(var binding: DividerViewBinding) :
    BaseViewHolder<BaseModel>(binding.root) {
    override fun onBind(item: BaseModel, position: Int) {
        binding.apply {
            divider.setHeight(item.dividerHeight)
            divider.setBackgroundColor(binding.root.resources.getColor(item.dividerColor))
        }
    }
}



class CustomHeadLargeSquareImgMidHeadDescViewHolder(var binding: CustomHeadLargeSquareImgMidHeadDescViewBinding) :
    BaseViewHolder<BaseModel>(binding.root) {
    override fun onBind(item: BaseModel, position: Int) {
        binding.apply {
            itemView.apply {
                tvH2.text = item.title
                tvH4.text = item.subHeading
                tvDesc.text = item.desc
            }
        }
    }
}

class CustomHeadLargeImgMidHeadDescViewHolder(var binding: CustomHeadLargeImgMidHeadDescViewBinding) :
    BaseViewHolder<BaseModel>(binding.root) {
    override fun onBind(item: BaseModel, position: Int) {
        binding.apply {
            itemView.apply {
                tvH2.text = item.title
                tvH4.text = item.subHeading
                tvDesc.text = item.desc
                ivThumbnail.setImageResource(R.drawable.dest4)
            }
        }
    }
}
