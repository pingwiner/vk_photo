package com.vk_photki.ui

import android.content.Context
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R

/**
 * Created by nightrain on 4/22/15.
 */
public class FriendAdapter(context: Context, friends: List<VKApiUser>)
: BaseAdapter<VKApiUser>(context, friends) {

    override protected fun getTitle(item: VKApiUser) : String {
        return item.first_name + " " + item.last_name
    }

    override protected fun getThumbSrc(item: VKApiUser) : String {
        return item.photo_100
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

}