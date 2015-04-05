package com.vk_photki.ui

import android.os.Bundle
import android.support.v4.app.Fragment
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

/**
 * Created by nightrain on 4/4/15.
 */

fun getAlbumsFragment(userId: String) : AlbumsFragment {
    val args = Bundle()
    args.putString(AlbumsFragment.ARG_USER_ID, userId)
    var result = AlbumsFragment()
    result.setArguments(args)
    return result
}

class AlbumsFragment() : Fragment(), VkAlbumsLoader.OnAlbumsLoadedListener,
        VkPhotoLoader.OnAlbumLoadListener, AdapterView.OnItemClickListener {

    private var userId: Int = 0;
    private val TAG = "AlbumsFragment"
    private var mListView: ListView? = null;

    companion object Args {
        public val ARG_USER_ID: String = "ARG_USER_ID"
    }

    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        userId = Integer.parseInt(getArguments().getString(ARG_USER_ID));
        VkAlbumsLoader(userId, this)
        setProgressVisibility(true)
        val view = inflater.inflate(R.layout.fragment_albums, container, false);
        mListView = view.findViewById(R.id.list) as ListView;
        mListView?.setOnItemClickListener(this)
        return view;
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        Log.d(TAG, "click: " + position);
        val album = mListView?.getAdapter()?.getItem(position) as VKApiPhotoAlbum
        VkPhotoLoader(userId, album.id, this)
    }

    override public fun onAlbumsLoaded(albums: List<VKApiPhotoAlbum>) {
        Log.d(TAG, albums.size().toString() + " albums loaded");
        setProgressVisibility(false)
        var adapter = AlbumAdapter(getActivity(), albums)
        mListView?.setAdapter(adapter)
    }

    override public fun onAlbumsLoadFailed(error: VKError) {
        Toast.makeText(getActivity(), error.errorMessage, Toast.LENGTH_SHORT).show()
        setProgressVisibility(false)
    }

    override public fun onActivityCreated(savedInstanceState: Bundle?) {
        super<Fragment>.onActivityCreated(savedInstanceState)
    }

    private fun setProgressVisibility(visible: Boolean) {

    }

    override fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>) {
        Log.d(TAG, "onPhotosReady: " + ownerId + " " + albumId + " " + photos.size())
    }

    override fun onPhotosLoadingFailed(error: VKError) {
        Log.d(TAG, "onPhotosLoadingFailed: " + error.errorMessage);
    }

}