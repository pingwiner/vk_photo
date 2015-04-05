package com.vk_photki.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vk.sdk.api.model.VKApiPhotoAlbum


public class AlbumAdapter(var mContext: Context, var mAlbums: List<VKApiPhotoAlbum>) : ArrayAdapter<VKApiPhotoAlbum>(mContext, android.R.layout.simple_list_item_1, mAlbums) {

    override public fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView: View;
        if (convertView == null) {
            var inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            rowView = convertView;
        }
        val textView = rowView.findViewById(android.R.id.text1) as TextView;
        textView.setText(mAlbums.get(position).title);
        return rowView;
    }

    override public fun getItem(position: Int): VKApiPhotoAlbum {
        return mAlbums.get(position)
    }
}