package com.vk_photki.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.vk.sdk.api.VKError
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk_photki.R
import com.vk_photki.api.VkPhotoLoader
import com.vk_photki.utils.Starter
import com.vk_photki.utils.getBiggestPhotoUrl

/**
 * Created by nightrain on 4/25/15.
 */

class PhotosFragment() : SelectableFragment<VKApiPhoto>(), VkPhotoLoader.OnAlbumLoadListener {
    override val TAG: String = "PhotosFragment";
    override val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums
    public val ARG_ALBUM_ID: String = "KEY_ALBUM"
    public val ARG_ALBUM_TITLE: String = "KEY_ALBUM_TITLE"
    private var albumId: Int = 0;
    private var title: String = "";

    override fun onCreate(state: Bundle?) {
        super<SelectableFragment>.onCreate(state)
        val args = getArguments()
        albumId = args.getInt(ARG_ALBUM_ID)
        title = args.getString(ARG_ALBUM_TITLE)
    }

    override fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>) {
        onDataReady(photos)
    }

    override fun onPhotosLoadingFailed(error: VKError) {
        onDataLoadingFailed(error)
    }


    override fun onItemClick(view: View, position: Int) {
        //toggleSelection(position)
    }

    override fun startLoaders() {
        VkPhotoLoader(ownerId, albumId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiPhoto>): BaseAdapter<VKApiPhoto> {
        return PhotoAdapter(context, data)
    }

    override public fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val userName = arguments.getString(ARG_USER_NAME)
        when(item.getItemId()) {
            R.id.action_download -> {
                val selectedItems = getSelectedItems()
                for (photo in selectedItems) {
                    Starter.startService(activity, userName, photo.getBiggestPhotoUrl(), title)
                }
            }
            R.id.action_select_all -> {
                selectAll();
            }

        }
        return super<SelectableFragment>.onOptionsItemSelected(item);
    }
}

public fun getPhotosFragment(ownerId: Int, ownerName : String?, albumId: Int, title: String): PhotosFragment {
    val frag = PhotosFragment()
    val args = Bundle();
    args.putInt(BaseFragment.ARG_USER_ID, ownerId)
    args.putInt(frag.ARG_ALBUM_ID, albumId)
    args.putString(frag.ARG_ALBUM_TITLE, title)
    args.putString(BaseFragment.ARG_USER_NAME, ownerName)
    frag.setArguments(args)
    return frag
}