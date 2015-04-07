package com.vk_photki.ui.api

import android.util.Log
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.api.VkLoader
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/4/15.
 */

public class VkAlbumsLoader(
        var ownerId: Int,
        var listener: VkAlbumsLoader.OnAlbumsLoadedListener)
    : VkLoader<VKApiPhotoAlbum>() {

    init {
        val request = VKRequest("photos.getAlbums", VKParameters.from(
                VKApiConst.USER_ID, ownerId,
                VKApiConst.PHOTO_SIZES, 1));
        request.executeWithListener(this);
    }

    override fun parseJson(json: JSONObject): VKApiPhotoAlbum {
        return VKApiPhotoAlbum().parse(json)
    }

    override fun onDataReady(data: ArrayList<VKApiPhotoAlbum>) {
        listener.onAlbumsLoaded(data)
    }

    private val TAG = "VkAlbumsLoader";

    trait OnAlbumsLoadedListener {
        public abstract fun onAlbumsLoaded(albums: List<VKApiPhotoAlbum>);
        public abstract fun onAlbumsLoadFailed(error: VKError);
    }

    override public fun onError(error: VKError) {
        listener.onAlbumsLoadFailed(error)
        Log.d(TAG, error.toString())
        // Ошибка. Сообщаем пользователю об error.
    }

}