package com.vk_photki.ui

import android.content.Intent
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

class AlbumsFragment() : Fragment(), VkAlbumsLoader.OnAlbumsLoadedListener,
        VkPhotoLoader.OnAlbumLoadListener, AdapterView.OnItemClickListener,
        VkFriendsLoader.OnFriendsLoadListener, VkGroupsLoader.OnGroupsLoadedListener {

    private var userId: Int = 0;
    private val TAG = "AlbumsFragment"
    private var mListView: ListView? = null;
    private var mAlbums: List<VKApiPhotoAlbum> = ArrayList<VKApiPhotoAlbum>();

    companion object Args {
        public val ARG_USER_ID: String = "ARG_USER_ID"
    }

    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        userId = Integer.parseInt(getArguments().getString(ARG_USER_ID));
        VkAlbumsLoader(userId, this)
        VkGroupsLoader(userId, this)
        //VkFriendsLoader(userId, this)
        setProgressVisibility(true)
        val view = inflater.inflate(R.layout.fragment_albums, container, false);
        mListView = view.findViewById(R.id.list) as ListView;
        mListView?.setOnItemClickListener(this)
        return view;
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val album = mListView?.getAdapter()?.getItem(position) as VKApiPhotoAlbum
        Log.d(TAG, "click: " + album.id);
        VkPhotoLoader(userId, album.id, this)
    }

    override public fun onAlbumsLoaded(albums: List<VKApiPhotoAlbum>) {
        Log.d(TAG, albums.size().toString() + " albums loaded");
        setProgressVisibility(false)
        var adapter = AlbumAdapter(getActivity(), albums)
        mListView?.setAdapter(adapter)
        mAlbums = albums
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

    private fun findAlbumById(id: Int): VKApiPhotoAlbum? {
        for (album in mAlbums) {
            if (album.id == id) return album;
        }
        return null;
    }

    private fun getMaxPhotoPath(photo: VKApiPhoto): String? {
        if (photo.photo_2560 != null) return photo.photo_2560;
        if (photo.photo_1280 != null) return photo.photo_1280;
        if (photo.photo_807 != null) return photo.photo_807;
        if (photo.photo_604 != null) return photo.photo_604;
        if (photo.photo_130 != null) return photo.photo_130;
        if (photo.photo_75 != null) return photo.photo_75;
        return null;
    }


    override fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>) {
        Log.d(TAG, "onPhotosReady: " + ownerId + " " + albumId + " " + photos.size())
        val album = findAlbumById(albumId);
        if (album == null) return;
        for (photo in photos) {
            val url = getMaxPhotoPath(photo);
            if (url == null) continue;
            Starter.startService(getActivity(), url, album.title);
        }
    }

    override fun onPhotosLoadingFailed(error: VKError) {
        Log.d(TAG, "onPhotosLoadingFailed: " + error.errorMessage);
    }

    override fun onFriendsReady(friends: List<VKApiUser>) {
        Log.d(TAG, "onFriendsReady: " + friends.size);
    }

    override fun onFriendsLoadingFailed(error: VKError) {
        Log.d(TAG, "onFriendsLoadingFailed");
    }

    override fun onGroupsReady(groups: List<VKApiCommunity>) {
        Log.d(TAG, "grops: " + groups.size());
    }

    override fun onGroupsLoadingFailed(error: VKError) {
        Log.d(TAG, "onGroupsLoadingFailed");
    }

}

fun getAlbumsFragment(userId: String) : AlbumsFragment {
    val args = Bundle()
    args.putString(AlbumsFragment.ARG_USER_ID, userId)
    var result = AlbumsFragment()
    result.setArguments(args)
    return result
}
