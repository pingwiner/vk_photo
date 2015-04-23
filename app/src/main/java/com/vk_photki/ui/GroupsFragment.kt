package com.vk_photki.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.vk.sdk.api.model.VKApiCommunity
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R
import com.vk_photki.api.VkFriendsLoader
import com.vk_photki.api.VkGroupsLoader

/**
 * Created by nightrain on 4/21/15.
 */

public class GroupsFragment() : BaseFragment<VKApiCommunity>() {
    override val TAG: String = "GroupsFragment"
    override val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums

    override fun startLoaders() {
        VkGroupsLoader(ownerId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiCommunity>): BaseAdapter<VKApiCommunity> {
        return GroupAdapter(context, data)
    }

    override fun onItemClick(view: View, position: Int) {
        val group = (mList?.getAdapter() as GroupAdapter).getItem(position)
        Log.d(TAG, "click: " + group.id);
        (getActivity() as LoginActivity).showGroupsFragment(group.id)
    }


}

fun getGroupsFragment(userId: Int) : GroupsFragment {
    val args = Bundle()
    args.putInt(BaseFragment.ARG_USER_ID, userId)
    var result = GroupsFragment()
    result.setArguments(args)
    return result
}
