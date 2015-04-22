package com.vk_photki.ui

import android.content.Context
import android.os.Bundle
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R
import com.vk_photki.api.VkFriendsLoader

/**
 * Created by nightrain on 4/22/15.
 */

public class FriendsFragment() : BaseFragment<VKApiUser>() {
    override val TAG: String = "FriendsFragment"
    override val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums

    override fun startLoaders() {
        VkFriendsLoader(ownerId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiUser>): BaseAdapter<VKApiUser> {
        return FriendAdapter(context, data)
    }

}

fun getFriendsFragment(userId: Int) : FriendsFragment {
    val args = Bundle()
    args.putInt(BaseFragment.ARG_USER_ID, userId)
    var result = FriendsFragment()
    result.setArguments(args)
    return result
}