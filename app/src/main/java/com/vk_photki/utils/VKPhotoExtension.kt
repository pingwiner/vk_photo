package com.vk_photki.utils

import com.vk.sdk.api.model.VKApiPhoto

/**
 * Created by nightrain on 29/01/2017.
 */

fun VKApiPhoto.getBiggestPhotoUrl() : String {
    val sizes = charArrayOf('s', 'm', 'x', 'y', 'z', 'w')

    var maxSize = -1;
    var result = "";
    for (sizeItem in this.src) {
        val idx = sizes.indexOf(sizeItem.type)
        if (idx > maxSize) {
            maxSize = idx
            result = sizeItem.src
        }
    }
    return result;
}