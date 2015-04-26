package com.vk_photki.ui

import android.content.Context
import android.view.View
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R
import java.util.HashMap

/**
 * Created by nightrain on 4/25/15.
 */

class PhotoAdapter(context: Context, photos: List<VKApiPhoto>, selectedItems: HashMap<Int, Boolean>) :
        SelectableAdapter<VKApiPhoto>(context, photos, selectedItems) {

    override fun getTitle(item: VKApiPhoto): String {
        return item.text
    }

    override fun getThumbSrc(item: VKApiPhoto): String {
        return item.src.getByType('m')
    }

    override fun getLayoutId(): Int {
        return R.layout.album_layout
    }

}