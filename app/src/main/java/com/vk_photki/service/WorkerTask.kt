package com.vk_photki.service

import android.os.AsyncTask

/**
 * Created by nightrain on 4/7/15.
 */

public class WorkerTask(var listener: WorkerTask.OnTaskCompleteListener) : AsyncTask<String, Int, Boolean>() {

    override fun doInBackground(vararg params: String?): Boolean? {
        var url = params[0];
        var pathToSave = params[1];
        return true;
    }

    override fun onPostExecute(result: Boolean) {
        listener.onTaskComplete(result)
    }

    public trait OnTaskCompleteListener {
        public fun onTaskComplete(result: Boolean)
    }
}