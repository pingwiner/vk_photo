package com.vk_photki.service;

import android.os.Binder;

/**
 * Created by nightrain on 4/7/15.
 */
public class ServiceBinder extends Binder {
    private DownloadService mService;

    public ServiceBinder(DownloadService service) {
        super();
        mService = service;
    }

    DownloadService getService() {
        return mService;
    }

}
