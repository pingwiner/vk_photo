package com.vk_photki.api

import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.model.VKApiCommunity
import com.vk_photki.ui.OnDataReadyListener
import com.vk_photki.ui.api.VkAlbumsLoader
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/7/15.
 */

public class VkGroupsLoader(var userId: Int,
                            var listener: OnDataReadyListener<VKApiCommunity>) : VkLoader<VKApiCommunity>() {

    init {
        val request = VKRequest("groups.get", VKParameters.from(
                VKApiConst.USER_ID, userId,
                VKApiConst.EXTENDED, 1,
                VKApiConst.FIELDS, "description"));
        request.executeWithListener(this);

    }

    override fun parseJson(json: JSONObject): VKApiCommunity {
        return VKApiCommunity().parse(json)
    }

    override fun onDataReady(data: ArrayList<VKApiCommunity>) {
        listener.onDataReady(data)
    }

    override fun onError(error: VKError) {
        listener.onDataLoadingFailed(error)
    }

}
