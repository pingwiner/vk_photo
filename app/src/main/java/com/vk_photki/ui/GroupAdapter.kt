package com.vk_photki.ui

import android.content.Context
import com.vk.sdk.api.model.VKApiCommunity
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk_photki.R

/**
 * Created by nightrain on 4/22/15.
 */

public class GroupAdapter(context: Context, groups: List<VKApiCommunity>)
: BaseAdapter<VKApiCommunity>(context, groups) {

    override protected fun getTitle(item: VKApiCommunity) : String {
        return item.name
    }

    override protected fun getThumbSrc(item: VKApiCommunity) : String {
        return item.photo_100
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

}