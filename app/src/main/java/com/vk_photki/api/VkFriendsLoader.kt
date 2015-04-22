package com.vk_photki.api

import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiPhoto
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.ui.OnDataReadyListener
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/6/15.
 */

public class VkFriendsLoader(var userId: Int, var listener: OnDataReadyListener<VKApiUser>) : VkLoader<VKApiUser>() {

    trait OnFriendsLoadListener {
        fun onFriendsReady(friends: List<VKApiUser>);
        fun onFriendsLoadingFailed(error: VKError)
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
        listener.onDataReady(data)
    }

    override public fun onError(error: VKError) {
        listener.onDataLoadingFailed(error)
        // Ошибка. Сообщаем пользователю об error.
    }

}