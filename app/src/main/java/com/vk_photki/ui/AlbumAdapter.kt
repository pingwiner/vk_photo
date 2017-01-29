package com.vk_photki.ui

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vk.sdk.api.model.VKApiCommunity
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk_photki.R
import java.util.HashMap


public class AlbumAdapter(context: Context, albums: List<VKApiPhotoAlbum>)
    : SelectableAdapter<VKApiPhotoAlbum>(context, albums) {

    companion object Static {
        public val ACTION_ALBUM_CLICKED = "com.vk_photki.ui.ACTION_ALBUM_CLICKED";
        public val ARG_ALBUM_ID = "ARG_ALBUM_ID";
        public val ARG_ALBUM_TITLE = "ARG_ALBUM_TITLE";
    }

    override protected fun getTitle(item: VKApiPhotoAlbum) : String {
        return item.title
    }

    override protected fun getThumbSrc(item: VKApiPhotoAlbum) : String {
        return item.thumb_src
    }

    override protected fun getLayoutId() : Int {
        return R.layout.album_layout
    }

    override fun click(item : VKApiPhotoAlbum) {
        val intent = Intent(ACTION_ALBUM_CLICKED)
        intent.putExtra(ARG_ALBUM_ID, item.id);
        intent.putExtra(ARG_ALBUM_TITLE, item.title);
        context.sendBroadcast(intent)
    }

}