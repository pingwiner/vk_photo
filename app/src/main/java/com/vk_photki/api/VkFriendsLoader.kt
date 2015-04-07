package com.vk_photki.api

import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/6/15.
 */

public class VkFriendsLoader(var userId: Int, var listener: VkFriendsLoader.OnFriendsLoadListener) : VkLoader<VKApiUser>() {

    trait OnFriendsLoadListener {
        abstract fun onFriendsReady(friends: List<VKApiUser>);
        abstract fun onFriendsLoadingFailed(error: VKError)
    }

    init {
        val request = VKRequest("friends.get", VKParameters.from(
                VKApiConst.USER_ID, userId,
                VKApiConst.FIELDS, "nickname,photo_50"));
        request.executeWithListener(this);
    }

    override fun parseJson(json: JSONObject): VKApiUser {
        return VKApiUser().parse(json)
    }

    override fun onDataReady(data: ArrayList<VKApiUser>) {
        listener.onFriendsReady(data)
    }

    override public fun onError(error: VKError) {
        listener.onFriendsLoadingFailed(error)
        // Ошибка. Сообщаем пользователю об error.
    }

}