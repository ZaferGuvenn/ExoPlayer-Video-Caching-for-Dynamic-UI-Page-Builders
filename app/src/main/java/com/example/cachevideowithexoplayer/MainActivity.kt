package com.example.cachevideowithexoplayer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.util.UnstableApi
import com.example.cachevideowithexoplayer.adapter.HomeAdapter
import com.example.cachevideowithexoplayer.databinding.ActivityMainBinding
import com.example.cachevideowithexoplayer.model.BaseModel
import com.example.cachevideowithexoplayer.utils.Enums
import com.example.cachevideowithexoplayer.utils.RecyclerViewItemDecorator
import com.siro.android.base.BaseActivity
import com.siro.android.ui.adapter.clicklistener.ListActionClickListener
import com.siro.android.ui.adapter.clicklistener.ListActionClickType

@OptIn(UnstableApi::class)
class MainActivity : BaseActivity(),  ListActionClickListener {
    lateinit var mBinding: ActivityMainBinding
    private val homeAdapter = HomeAdapter()
    var isFirstItemVisible = true

    override fun getActivityLayout(): Int = R.layout.activity_main


    override fun getViewBinding() {
        mBinding = binding as ActivityMainBinding

        val arraylist = arrayListOf<BaseModel>()
        arraylist.apply {
            add(
                BaseModel(viewType = Enums.HomeItemViewType.typeHomeTopBanner).apply {
                })

            add( BaseModel(viewType = Enums.HomeItemViewType.typeDefault))
            add(
                BaseModel(viewType = Enums.HomeItemViewType.typeCustomHeadLargeSquareImgMidHeadDesc).apply {
                    title = "DYNAMIC PAGE BUILDER"
                    subHeading = "How to use cache in exoplayer video"
                    desc = "In this Example you will see how we can use the exoplayer video player cache in dynamic UI page Builder scenario."
                })
            add( BaseModel(viewType = Enums.HomeItemViewType.typeDefault))
            add( BaseModel(viewType = Enums.HomeItemViewType.typeCustomHeadLargeImgMidHeadDesc).apply {
                title = "DYNAMIC PAGE BUILDER"
                subHeading = "How to use cache in exoplayer video"
                desc = "In this Example you will see how we can use the exoplayer video player cache in dynamic UI page Builder scenario."
            })

            add(
                BaseModel(viewType = Enums.HomeItemViewType.typeHomeTopBanner).apply {
                })

            add( BaseModel(viewType = Enums.HomeItemViewType.typeDefault))
            add(
                BaseModel(viewType = Enums.HomeItemViewType.typeCustomHeadLargeSquareImgMidHeadDesc).apply {
                    title = "Example number 2"
                    subHeading = "How to use cache in exoplayer video"
                    desc = "In this Example you will see how we can use the exoplayer video player cache in dynamic UI page Builder scenario."
                })
            add( BaseModel(viewType = Enums.HomeItemViewType.typeDefault))
            add( BaseModel(viewType = Enums.HomeItemViewType.typeCustomHeadLargeImgMidHeadDesc).apply {
                title = "Example Number 2"
                subHeading = "How to use cache in exoplayer video"
                desc = "In this Example you will see how we can use the exoplayer video player cache in dynamic UI page Builder scenario."
            })
        }
        homeAdapter.listItems = arraylist
        setHomeList()
    }

    override fun setClickListeners() {

    }

    override fun onItemClick(item: ListActionClickType) {

    }


    private fun setHomeList() {
        mBinding.apply {
            val spacing = resources.getDimensionPixelSize(R.dimen.dp16)
            rvHome.adapter = homeAdapter
            rvHome.addItemDecoration(
                RecyclerViewItemDecorator(
                    spacing,
                    RecyclerViewItemDecorator.VERTICAL
                )
            )
            rvHome.setHasFixedSize(true)
            //     homeListItemAdapter.listItems = DataCenter.getHomeList()

            homeAdapter.listActionClickListener = this@MainActivity

            rvHome.addCustomScrollListener(
                onFirstItemVisible = {
                    isFirstItemVisible = true
                },
                onFirstItemNotVisible = {
                    isFirstItemVisible = false
                },
                onSecondItemNotVisible = {
                    isFirstItemVisible = false
                }
            )

        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        ExoPlayerManager.releaseAllPlayers()
        super.onDestroy()
    }
}