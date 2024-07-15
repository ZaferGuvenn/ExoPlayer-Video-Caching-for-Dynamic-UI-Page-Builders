package com.example.cachevideowithexoplayer.utils

import com.example.cachevideowithexoplayer.R


object Enums {

    enum class HomeItemViewType(
        val id: Int,
        val layoutResId: Int,
    ) {
        typeDefault(-1, R.layout.divider_view),
        typeHomeTopBanner(
            0, R.layout.layout_home_top_banner,
        ),
        typeCustomHeadLargeSquareImgMidHeadDesc(
            1, R.layout.custom_head_large_square_img_mid_head_desc_view
        ),
        typeCustomHeadLargeImgMidHeadDesc(
            2, R.layout.custom_head_large_img_mid_head_desc_view
        ),
    }
}

