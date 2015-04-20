package com.vk_photki.ui

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

public class Holder(var rootView: View)  : RecyclerView.ViewHolder(rootView), ImgCacheTask.OnTaskCompleteListener {
    public var cover: ImageView? = null;
    public var title : TextView? = null;
    public var description : TextView? = null;
    private var mImgUrl: String = "";
    private val TAG = "Holder";

    init {
        cover = rootView.findViewById(R.id.cover) as ImageView
        title = rootView.findViewById(R.id.title) as TextView
        description = rootView.findViewById(R.id.description) as TextView
    }

    public fun setCover(context: Context, url: String?) {
        if (url == null) return;
        mImgUrl = url;
        if (isCached(context, url)) {
            cover?.setImageURI(Uri.fromFile(getCacheFileFor(context, url)))
            return;
        }
        putInCache(context, url, this);
    }

    override fun onTaskComplete(result: Boolean, url: Uri?) {
        Log.d(TAG, "onTaskComplete: " + url + " " + result);
        if (url == null) return;
        if (url.equals(mImgUrl)) {
            cover?.setImageURI(url)
        }
    }

}