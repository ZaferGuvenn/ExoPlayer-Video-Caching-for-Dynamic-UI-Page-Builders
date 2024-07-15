package com.example.cachevideowithexoplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.cachevideowithexoplayer.ExoPlayerManager
import com.example.cachevideowithexoplayer.databinding.CustomHeadLargeImgMidHeadDescViewBinding
import com.example.cachevideowithexoplayer.databinding.CustomHeadLargeSquareImgMidHeadDescViewBinding
import com.example.cachevideowithexoplayer.databinding.DividerViewBinding
import com.example.cachevideowithexoplayer.databinding.LayoutHomeTopBannerBinding
import com.example.cachevideowithexoplayer.getSafe
import com.example.cachevideowithexoplayer.model.BaseModel
import com.example.cachevideowithexoplayer.utils.Enums
import com.siro.android.base.BaseRecyclerViewAdapter
import com.siro.android.base.BaseViewHolder
import com.siro.android.ui.adapter.clicklistener.ListActionClickListener
import com.siro.android.ui.adapter.viewholder.CustomHeadLargeImgMidHeadDescViewHolder
import com.siro.android.ui.adapter.viewholder.CustomHeadLargeSquareImgMidHeadDescViewHolder
import com.siro.android.ui.adapter.viewholder.HomeTopBannerViewHolder
import com.siro.android.ui.adapter.viewholder.ItemDividerViewHolder

class HomeAdapter : BaseRecyclerViewAdapter<BaseViewHolder<BaseModel>, BaseModel>(
) {
    var listActionClickListener: ListActionClickListener? = null
    override var layout: (viewType: Int) -> Int
        get() = { viewType ->
            Enums.HomeItemViewType.values().find { it.id == viewType }?.layoutResId.getSafe()
        }
        set(value) {}

    override fun getItemViewType(position: Int): Int {
        return listItems[position].viewType.id
    }

    @OptIn(UnstableApi::class)
    override fun viewHolder(view: View, viewType: Int): BaseViewHolder<BaseModel> {

        return when (viewType) {
            Enums.HomeItemViewType.typeHomeTopBanner.id -> {
                HomeTopBannerViewHolder(
                    LayoutHomeTopBannerBinding.inflate(
                        LayoutInflater.from(view.context),
                        view as ViewGroup,
                        false
                    ),
                    listActionClickListener
                )
            }

            Enums.HomeItemViewType.typeCustomHeadLargeSquareImgMidHeadDesc.id -> {
                CustomHeadLargeSquareImgMidHeadDescViewHolder(
                    CustomHeadLargeSquareImgMidHeadDescViewBinding.inflate(
                        LayoutInflater.from(view.context),
                        view as ViewGroup,
                        false
                    )
                )
            }

            Enums.HomeItemViewType.typeCustomHeadLargeImgMidHeadDesc.id -> {
                CustomHeadLargeImgMidHeadDescViewHolder(
                    CustomHeadLargeImgMidHeadDescViewBinding.inflate(
                        LayoutInflater.from(view.context),
                        view as ViewGroup,
                        false
                    )
                )
            }

            else -> {
                ItemDividerViewHolder(
                    DividerViewBinding.inflate(
                        LayoutInflater.from(view.context),
                        view as ViewGroup,
                        false
                    ),
                )
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onViewRecycled(holder: BaseViewHolder<BaseModel>) {
        if (holder.itemViewType == Enums.HomeItemViewType.typeHomeTopBanner.id) {
            if (holder.bindingAdapterPosition != -1) {
                if ((holder as? HomeTopBannerViewHolder)?.binding?.videoView?.visibility == View.VISIBLE) {
                    ExoPlayerManager.releasePlayer(
                        holder,
                        listItems[holder.layoutPosition]
                    )
                }
            }
        }
        super.onViewRecycled(holder)
    }
}