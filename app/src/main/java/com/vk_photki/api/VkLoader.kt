package com.vk_photki.api

import android.util.Log
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiModel
import com.vk.sdk.api.model.VKApiUser
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by nightrain on 4/6/15.
 */

public abstract class VkLoader<T : VKApiModel> : VKRequest.VKRequestListener() {
    private val TAG = "VkLoader";

    protected abstract fun parseJson(json: JSONObject): T
    protected abstract fun onDataReady(data: ArrayList<T>)

    override public fun onComplete(vkResponse: VKResponse) {
        Log.d(TAG, "vkResponse: " + vkResponse.responseString);
        val result = ArrayList<T>();
        val json = vkResponse.json;
        if (json != null) {
            val response = json.getJSONObject("response");
            if (response != null) {
                val count = response.getInt("count");
                if (count != 0) {
                    val items = response.getJSONArray("items");
                    for (i in 0..count - 1) {
                        val itemJson = items.getJSONObject(i)
                        val item = parseJson(itemJson)
                        result.add(item)
                    }
                }
            }
        }
        onDataReady(result)
    }

    override public abstract fun onError(error: VKError);

    override public fun attemptFailed(request: VKRequest, attemptNumber: Int, totalAttempts: Int) {
        // Неудачная попытка. В аргументах имеется номер попытки и общее их количество.
    }


}

