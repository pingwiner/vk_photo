package com.vk_photki.utils

import android.content.Context
import android.os.Environment
import com.vk_photki.service.ImgCacheTask
import java.io.File
import java.io.IOException
import java.security.NoSuchAlgorithmException

/**
 * Created by nightrain on 4/12/15.
 */

public fun isCached(context: Context, url: String): Boolean {
    if (url.isEmpty()) return false;
    val file = getCacheFileFor(context, url);
    if (file == null) return false;
    return file.exists();
}

public fun getCacheUrlFor(context: Context, url: String): String? {
    if (url.isEmpty()) return null;
    val file = getCacheFileFor(context, url);
    try {
        return "file://" + file?.getCanonicalPath();
    } catch (e: IOException) {
        e.printStackTrace();
    }
    return null;
}

public fun getCacheFileFor(context: Context, urlString: String): File? {
    if (urlString.isEmpty()) return null;
    val first = urlString.lastIndexOf('.');
    var last = urlString.indexOf('?');
    if (last == -1) last = urlString.length();
    var extension = "";

    if (last > first) {
        extension = urlString.substring(first, last).toLowerCase();
    }
    val filename = MD5(urlString).toString() + extension;
    return File(getCacheFolder(context), filename);
}

private fun getCacheFolder(context: Context): File {
    return  context.getCacheDir();
}

public fun putInCache(context: Context, url: String, listener: ImgCacheTask.OnTaskCompleteListener) {
    ImgCacheTask(context, listener).execute(url);
}