package com.vk_photki.utils;

import android.content.Context;
import android.content.Intent;

import com.vk_photki.service.DownloadService;

/**
 * Created by nightrain on 4/8/15.
 */
public class Starter {
    public static void startService(Context context, String ownerName, String url, String albumName) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("url", url);
        intent.putExtra("owner", ownerName);
        intent.putExtra("album", albumName);
        context.startService(intent);
    }
}
