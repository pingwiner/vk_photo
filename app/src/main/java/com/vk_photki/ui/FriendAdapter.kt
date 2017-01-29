package com.vk_photki.ui

import android.content.Context
import android.content.Intent
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R

/**
 * Created by nightrain on 4/22/15.
 */
public class FriendAdapter(context: Context, friends: List<VKApiUser>)
: BaseAdapter<VKApiUser>(context, friends) {

    public val ACTION_USER_CLICKED = "com.vk_photki.ui.USER_CLICKED"
    public val ARG_USER_ID = "USER_ID"

    override protected fun getTitle(item: VKApiUser) : String {
        return item.first_name + " " + item.last_name
    }

    override protected fun getThumbSrc(item: VKApiUser) : String {
        return item.photo_200
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

    override fun click(item : VKApiUser) {
        val intent = Intent(ACTION_USER_CLICKED)
        intent.putExtra(ARG_USER_ID, item.id);
        context.sendBroadcast(intent)
    }
}