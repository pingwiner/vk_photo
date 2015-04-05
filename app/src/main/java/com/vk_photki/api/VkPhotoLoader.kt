package com.vk_photki.api

import android.util.Log
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import java.util.ArrayList

/**
 * Created by nightrain on 4/5/15.
 */

public open class VkPhotoLoader(var ownerId: Int, var albumId: Int, var listener: VkPhotoLoader.OnAlbumLoadListener): VKRequest.VKRequestListener() {
    val TAG = "VkPhotoLoader";

    trait OnAlbumLoadListener {
        abstract fun onPhotosReady(ownerId: Int, albumId: Int, photos: List<VKApiPhoto>);
        abstract fun onPhotosLoadingFailed(error: VKError)
    }

    init {
        val request = VKRequest("photos.get", VKParameters.from(
                VKApiConst.OWNER_ID, ownerId,
                VKApiConst.ALBUM_ID, albumId,
                VKApiConst.PHOTO_SIZES, 1));
        request.executeWithListener(this);

    }

    override public fun onComplete(vkResponse: VKResponse) {
        val result = ArrayList<VKApiPhoto>();
        val json = vkResponse.json;
        if (json != null) {
            val response = json.getJSONObject("response")
            if (response != null) {
                val count = response.getInt("count")
                if (count != 0) {
                    val items = response.getJSONArray("items")
                    for (i in 0..count - 1) {
                        val photo = VKApiPhoto().parse(items.getJSONObject(i))
                        result.add(photo)
                    }
                }
            }
        }
        listener.onPhotosReady(ownerId, albumId, result)
    }

    override public fun onError(error: VKError) {
        listener.onPhotosLoadingFailed(error)
    }

    override public fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {
        // Неудачная попытка. В аргументах имеется номер попытки и общее их количество.
    }
}