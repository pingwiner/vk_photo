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

/**
 * Created by nightrain on 4/25/15.
 */

class PhotosFragment() : SelectableFragment<VKApiPhoto>(), VkPhotoLoader.OnAlbumLoadListener {
    override val TAG: String = "PhotosFragment";
    override val LAYOUT_RESOURCE_ID: Int = R.layout.fragment_albums
    public val ARG_ALBUM_ID: String = "KEY_ALBUM"
    private var albumId: Int = 0;

    override fun onCreate(state: Bundle?) {
        super<SelectableFragment>.onCreate(state)
        val args = getArguments()
        albumId = args.getInt(ARG_ALBUM_ID)
    }

    override fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>) {
        onDataReady(photos)
    }

    override fun onPhotosLoadingFailed(error: VKError) {
        onDataLoadingFailed(error)
    }


    override fun onItemClick(view: View, position: Int) {
        toggleSelection(position)
    }

    override fun startLoaders() {
        VkPhotoLoader(ownerId, albumId, this)
    }

    override fun getAdapter(context: Context, data: List<VKApiPhoto>): BaseAdapter<VKApiPhoto> {
        return PhotoAdapter(context, data, mSelectedItems)
    }

    override public fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when(item.getItemId()) {
            R.id.action_download -> {
                val selectedItems = getSelectedItems()
                for (photo in selectedItems) {
                    Log.d(TAG, "id: " + photo.id.toString());
                }
            }
            R.id.action_select_all -> {
                selectAll();
            }

        }
        return super<SelectableFragment>.onOptionsItemSelected(item);
    }
}

public fun getPhotosFragment(ownerId: Int, albumId: Int): PhotosFragment {
    val frag = PhotosFragment()
    val args = Bundle();
    args.putInt(BaseFragment.ARG_USER_ID, ownerId)
    args.putInt(frag.ARG_ALBUM_ID, albumId)
    frag.setArguments(args)
    return frag
}