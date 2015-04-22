package com.vk_photki.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk_photki.R


public class AlbumAdapter(context: Context, albums: List<VKApiPhotoAlbum>)
    : BaseAdapter<VKApiPhotoAlbum>(context, albums) {

    override protected fun getTitle(item: VKApiPhotoAlbum) : String {
        return item.title
    }

    override protected fun getDescription(item: VKApiPhotoAlbum) : String {
        return item.description
    }

    override protected fun getThumbSrc(item: VKApiPhotoAlbum) : String {
        return item.thumb_src
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

}