package com.siro.android.ui.adapter.clicklistener

import com.example.cachevideowithexoplayer.model.BaseModel

sealed class ListActionClickType {
    class OnHeaderButton(val item: BaseModel? = null) : ListActionClickType()

    data class OnItemClick(val item: BaseModel, val pos: Int) : ListActionClickType()
    data class OnButtonOneClick(val item: BaseModel, val pos: Int) : ListActionClickType()
    data class OnButtonTwoClick(val item: BaseModel, val pos: Int) : ListActionClickType()
    data class OnButtonThreeClick(val item: BaseModel, val pos: Int) : ListActionClickType()
}