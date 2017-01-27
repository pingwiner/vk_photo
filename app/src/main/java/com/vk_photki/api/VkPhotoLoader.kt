package com.vk_photki.api

import android.util.Log
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/5/15.
 */

public open class VkPhotoLoader(var ownerId: Int, var albumId: Int, var listener: VkPhotoLoader.OnAlbumLoadListener): VkLoader<VKApiPhoto>() {
    val TAG = "VkPhotoLoader";

    interface OnAlbumLoadListener {
        fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>);
        fun onPhotosLoadingFailed(error: VKError)
    }

    init {
        val request = VKRequest("photos.get", VKParameters.from(
                VKApiConst.OWNER_ID, ownerId,
                VKApiConst.ALBUM_ID, albumId,
                VKApiConst.PHOTO_SIZES, 1));
        request.executeWithListener(this);
    }

    override fun parseJson(json: JSONObject): VKApiPhoto {
        return VKApiPhoto().parse(json)
    }

    override fun onDataReady(data: ArrayList<VKApiPhoto>) {
        listener.onPhotosReady(ownerId, albumId, data)
    }

    override public fun onError(error: VKError) {
        listener.onPhotosLoadingFailed(error)
        Log.d(TAG, error.toString())
    }

}