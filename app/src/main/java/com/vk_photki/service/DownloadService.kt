package com.vk_photki.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import com.vk_photki.R
import java.util.LinkedList

/**
 * Created by nightrain on 4/7/15.
 */

public class DownloadService : Service(), WorkerTask.OnTaskCompleteListener {
    private val mNM: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;;
    private val mTasks = LinkedList<ServiceTask>();
    private val mBinder = ServiceBinder(this);
    private var mWorker: AsyncTask<String, Int, Boolean>? = null;
    private val NOTIFICATION = R.string.operation_finished;

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override public fun onCreate() {
        super<Service>.onCreate()
    }

    override public fun onDestroy() {
        super<Service>.onDestroy()
        mNM.cancel(NOTIFICATION);
    }

    override public fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent == null) return Service.START_STICKY;
        val url = intent.getStringExtra("url")
        val albumName = intent.getStringExtra("album")
        synchronized(mTasks) {
            mTasks.add(ServiceTask(url, albumName))
        }
        checkTasks();
        return Service.START_STICKY;
    }

    private fun checkTasks() {
        if ((mWorker == null) || (mWorker?.getStatus() == AsyncTask.Status.FINISHED)) {
            if (mTasks.size() > 0) {
                synchronized(mTasks) {
                    val task = mTasks.get(0);
                    mTasks.remove(0);
                    mWorker = WorkerTask(this, this);
                    mWorker?.execute(task.url, task.albumName)
                }
            }
        }
    }

    override fun onTaskComplete(result: Boolean) {
        checkTasks();
    }

    private data class ServiceTask(var url: String, var albumName: String) {}
}


