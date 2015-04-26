package com.vk_photki.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.vk.sdk.api.VKError
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk_photki.R
import com.vk_photki.api.VkPhotoLoader
import com.vk_photki.ui.api.VkAlbumsLoader
import com.nostra13.universalimageloader.core.ImageLoader
import com.vk.sdk.api.model.VKApiCommunity
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.api.VkFriendsLoader
import com.vk_photki.api.VkGroupsLoader
import com.vk_photki.service.DownloadService
import com.vk_photki.utils.Starter
import java.util.ArrayList

/**
 * Created by nightrain on 4/4/15.
 */

class AlbumsFragment() : SelectableFragment<VKApiPhotoAlbum>() ,
        VkPhotoLoader.OnAlbumLoadListener {
    override protected val TAG: String = "AlbumsFragment";
    override protected val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums;

    override public fun onResume() {
        super<SelectableFragment>.onResume()
        val act:LoginActivity = getActivity() as LoginActivity;
        val id = act.getUserId();
        if (id == ownerId) {
            act.setTitle(R.string.action_albums);
        } else {
            if (ownerId > 0) {
                act.setTitle(act.getString(R.string.action_albums) + " " + act.getString(R.string.of_friend));
            } else {
                act.setTitle(act.getString(R.string.action_albums) + " " + act.getString(R.string.of_group));
            }

        }
    }

    override fun startLoaders() {
        VkAlbumsLoader(ownerId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiPhotoAlbum>): BaseAdapter<VKApiPhotoAlbum> {
        return AlbumAdapter(context, data, mSelectedItems)
    }

    override fun onItemClick(view: View, position: Int) {
        val album = (mList?.getAdapter() as AlbumAdapter).getItem(position)
        Log.d(TAG, "click: " + album.id);
        //setProgressVisibility(true)
        VkPhotoLoader(ownerId, album.id, this)
        //(getActivity() as LoginActivity).showPhotosFragment(ownerId, album.id)

    }

    override public fun onActivityCreated(savedInstanceState: Bundle?) {
        super<SelectableFragment>.onActivityCreated(savedInstanceState)
    }

    private fun findAlbumById(id: Int): VKApiPhotoAlbum? {
        if (mDataset == null) return null;
        for (i in 0..mDataset!!.size()) {
            val album = mDataset?.get(i)
            if (album!!.id == id) return album;
        }
        return null;
    }

    private fun getMaxPhotoPath(photo: VKApiPhoto): String? {
        val types = charArray('w' ,'z', 'y' , 'x', 'm', 's');

        for (type in types) {
            val photoSize = photo.src.getByType(type);
            if (photoSize != null) return photoSize;
        }

        return null;
    }


    override fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>) {
        Log.d(TAG, "onPhotosReady: " + ownerId + " " + albumId + " " + photos.size())
        val album = findAlbumById(albumId);
        if (album == null) return;
        for (photo in photos) {
            val url = getMaxPhotoPath(photo);
            if (url == null) continue;
            Log.d(TAG, "url: " + url + ", title: " + album.title);
            Starter.startService(getActivity(), url, album.title);
        }
    }

    override fun onPhotosLoadingFailed(error: VKError) {
        Log.d(TAG, "onPhotosLoadingFailed: " + error.errorMessage);
    }


}


fun getAlbumsFragment(userId: Int) : AlbumsFragment {
    Log.d("getAlbumsFragment", "userId: " + userId)
    val args = Bundle()
    args.putInt(BaseFragment.ARG_USER_ID, userId)
    var result = AlbumsFragment()
    result.setArguments(args)
    return result
}
