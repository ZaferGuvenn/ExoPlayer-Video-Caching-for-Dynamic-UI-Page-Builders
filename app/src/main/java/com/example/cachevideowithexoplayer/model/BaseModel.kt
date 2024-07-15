package com.example.cachevideowithexoplayer.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.example.cachevideowithexoplayer.R
import com.example.cachevideowithexoplayer.utils.Enums
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.Serializable
import java.time.LocalDate

@Parcelize
open class BaseModel(
    var viewType: Enums.HomeItemViewType = Enums.HomeItemViewType.typeDefault,
    open var id: Int = 0,
) : Serializable, Parcelable {
    var selectedTabPos: MutableStateFlow<Int> = MutableStateFlow(0)

    var imageRes: Int = 0
    var imageRes2: Int = 0
    var imageUri: Uri? = null
    var imageBitmap: Bitmap? = null

    //  Uri.parse("android.resource://" + getAppContext().packageName + "/drawable/profile_pic")
    open var desc: String = ""
    var categoryCode: String = ""
    var showBottomPanel: Boolean = false
    var showBottomPanel1: Boolean = true
    var showBottomPanel2: Boolean = true
    var buttonInsideBannerText = ""

    var showBannerInsideBannerText = true
    var showHeadingArrow = true
    var subHeading = ""
    open var title: String = ""
    var addOnLimit: Int = 0
    var imageUrl: String = ""
    var label1Text = ""
    var label2Text = ""
    var label3Text = ""
    var label4Text = ""
    var label5Text = ""
    var label6Text = ""
    var label7Text = ""
    var label8Text = ""
    var tagText = ""
    var label1Icon = 0
    var label2Icon = 0
    var label3Icon = 0

    var dividerHeight = R.dimen.dp1
    var dividerColor = R.color.zen_grey

    var playbackPosition = 0L

}