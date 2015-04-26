package com.vk_photki.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R
import com.vk_photki.api.VkFriendsLoader
import com.vk_photki.api.VkPhotoLoader

/**
 * Created by nightrain on 4/22/15.
 */

public class FriendsFragment() : BaseFragment<VKApiUser>() {
    override val TAG: String = "FriendsFragment"
    override val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums

    override public fun onResume() {
        super<BaseFragment>.onResume()
        getActivity().setTitle(R.string.action_friends);
    }

    override fun startLoaders() {
        VkFriendsLoader(ownerId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiUser>): BaseAdapter<VKApiUser> {
        return FriendAdapter(context, data)
    }

    override fun onItemClick(view: View, position: Int) {
        val friend = (mList?.getAdapter() as FriendAdapter).getItem(position)
        Log.d(TAG, "click: " + friend.id);
        (getActivity() as LoginActivity).showAlbumsFragment(friend.id)
    }

}

fun getFriendsFragment(userId: Int) : FriendsFragment {
    val args = Bundle()
    args.putInt(BaseFragment.ARG_USER_ID, userId)
    var result = FriendsFragment()
    result.setArguments(args)
    return result
}