package com.vk_photki.ui

import android.content.Context
import android.content.Intent
import com.vk.sdk.api.model.VKApiCommunity
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R

/**
 * Created by nightrain on 4/22/15.
 */

public class GroupAdapter(context: Context, groups: List<VKApiCommunity>)
: BaseAdapter<VKApiCommunity>(context, groups) {

    public val ACTION_GROUP_CLICKED = "com.vk_photki.ui.ACTION_GROUP_CLICKED";
    public val ARG_GROUP_ID = "ARG_GROUP_ID";

    override protected fun getTitle(item: VKApiCommunity) : String {
        return item.name
    }

    override protected fun getThumbSrc(item: VKApiCommunity) : String {
        return item.photo_100
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

    override fun click(item : VKApiCommunity) {
        val intent = Intent(ACTION_GROUP_CLICKED)
        intent.putExtra(ARG_GROUP_ID, item.id);
        context.sendBroadcast(intent)
    }
}