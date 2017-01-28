package com.vk_photki.ui

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.nostra13.universalimageloader.core.ImageLoader
import com.vk_photki.R
import com.vk_photki.service.ImgCacheTask
import com.vk_photki.utils.getCacheFileFor
import com.vk_photki.utils.getCacheUrlFor
import com.vk_photki.utils.isCached
import com.vk_photki.utils.putInCache
import java.net.URI

/**
 * Created by nightrain on 4/12/15.
 */

public class Holder(var rootView: View, val adapter : RecyclerView.Adapter<Holder>)  : RecyclerView.ViewHolder(rootView), ImgCacheTask.OnTaskCompleteListener {
    public var cover: ImageView? = null;
    public var title : TextView? = null;
    private var mImgUrl: String = "";
    public var checkbox: CheckBox? = null;
    private val TAG = "Holder";
    private var context: Context

    init {
        cover = rootView.findViewById(R.id.cover) as ImageView
        title = rootView.findViewById(R.id.title) as TextView
        checkbox = rootView.findViewById(R.id.select) as CheckBox
        context = rootView.context
    }

    public fun setCover(context: Context, url: String?) {
        if (url == null) return;
        mImgUrl = url;

        if (isCached(context, url)) {
            cover?.setImageURI(Uri.fromFile(getCacheFileFor(context, url)))
            return;
        } else {
            cover?.setImageBitmap(null)
        }
        putInCache(context, url, this);
    }

    override fun onTaskComplete(result: Boolean, url: Uri?) {
        Log.d(TAG, "onTaskComplete: " + url + " " + result);
        if (url == null) return;
        //Log.d(TAG, "mImgUrl "+ mImgUrl);
        if (url == Uri.fromFile(getCacheFileFor(context, mImgUrl))) {
            cover?.setImageURI(url)
            //adapter.notifyItemChanged(position)
        }
    }

}