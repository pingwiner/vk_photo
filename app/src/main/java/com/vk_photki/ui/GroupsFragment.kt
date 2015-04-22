package com.vk_photki.ui

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by nightrain on 4/21/15.
 */

class GroupsFragment() : Fragment() {

}

fun getGroupsFragment(userId: String) : GroupsFragment {
    val args = Bundle()
    args.putString(AlbumsFragment.ARG_USER_ID, userId)
    var result = GroupsFragment()
    result.setArguments(args)
    return result
}
