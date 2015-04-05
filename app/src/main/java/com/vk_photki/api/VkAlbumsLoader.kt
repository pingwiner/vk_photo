package com.vk_photki.ui.api

import android.util.Log
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhotoAlbum
import java.util.ArrayList

/**
 * Created by nightrain on 4/4/15.
 */

public class VkAlbumsLoader(
        ownerId: Int,
        var listener: VkAlbumsLoader.OnAlbumsLoadedListener)
    : VKRequest.VKRequestListener() {

    private val TAG = "VkAlbumsLoader";
    trait OnAlbumsLoadedListener {
        public abstract fun onAlbumsLoaded(albums: List<VKApiPhotoAlbum>);
        public abstract fun onAlbumsLoadFailed(error: VKError);
    }

    init {
        getAlbums(ownerId)
    }

    private fun getAlbums(ownerId: Int) {
        Log.d(TAG, "request");
        val request = VKRequest("photos.getAlbums", VKParameters.from(VKApiConst.OWNER_ID, ownerId, VKApiConst.PHOTO_SIZES, 1));
        request.executeWithListener(this);
    }

    override public fun onComplete(vkResponse: VKResponse) {
        val result = ArrayList<VKApiPhotoAlbum>();
        val json = vkResponse.json;
        if (json != null) {
            val response = json.getJSONObject("response");
            if (response != null) {
                val count = response.getInt("count");
                if (count != 0) {
                    val items = response.getJSONArray("items");

                    for (i in 0..count - 1) {
                        val albumJson = items.getJSONObject(i)
                        val album = VKApiPhotoAlbum().parse(albumJson)
                        result.add(album)
                    }
                }
            }
        }
        listener.onAlbumsLoaded(result)
    }

    override public fun onError(error: VKError) {
        // Ошибка. Сообщаем пользователю об error.
    }

    override public fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {
        // Неудачная попытка. В аргументах имеется номер попытки и общее их количество.
    }

}