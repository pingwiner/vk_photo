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


public class AlbumAdapter(var mContext: Context, var mAlbums: List<VKApiPhotoAlbum>)
    : RecyclerView.Adapter<Holder>() {

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val item = mAlbums.get(position);
        if (holder == null) return;
        if (holder.title != null) {
            holder.title?.setText(item.title)
        }
        if (holder.description != null) {
            holder.description?.setText(item.description)
        }
        if (holder.cover != null) {
            if (item.thumb_src != null) {
                holder.setCover(mContext, item.thumb_src);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder? {
        if (parent == null) return null;
        val v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_layout, parent, false);
        return Holder(v);
    }

    override fun getItemCount(): Int {
        return mAlbums.size()
    }

    public fun getItem(position: Int): VKApiPhotoAlbum {
        return mAlbums.get(position)
    }
}