package com.vk_photki.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBarActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.vk.sdk.*
import com.vk.sdk.api.VKError
import com.vk.sdk.dialogs.VKCaptchaDialog
import com.vk_photki.CustomApplication
import com.vk_photki.R
import com.vk_photki.service.DownloadService
import com.nostra13.universalimageloader.utils.StorageUtils



class LoginActivity() : ActionBarActivity() {
    private val TAG = "LoginActivity";
    private var userId = 0;
    private var mProgress: ProgressBar? = null;
    private val mReceiver = Receiver();
    private val credentialStore = CredentialStore();
    private val activity = this;

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getAction();
            Log.d(TAG, "onReceive " + action);

            if (action == DownloadService.ACTION_COMPLETE) {
                val args = intent.getExtras();
                if (args != null) {
                    val percents = args.getInt(DownloadService.ARG_PERCENTS)
                    Log.d(TAG, "percents " + percents);
                    mProgress?.setProgress(percents)
                    if (percents == 100) {
                        mProgress?.setVisibility(View.GONE);
                    } else {
                        mProgress?.setVisibility(View.VISIBLE);
                    }
                }
            } else if (action == CustomApplication.ACTION_RELOGIN) {
                userId = 0;
                credentialStore.setUserId(context, 0);
                VKSdk.login(activity,
                        VKScope.FRIENDS,
                        VKScope.PHOTOS,
                        VKScope.GROUPS);
            }
        }

    }

    override public fun onPause() {
        super.onPause();
        unregisterReceiver(mReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgress = findViewById(R.id.download_progress) as ProgressBar

        val config : ImageLoaderConfiguration  = ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(320, 320) // default = device screen dimensions
                .diskCacheExtraOptions(320, 320, null)
                .memoryCache(LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(500)
                .build();
        ImageLoader.getInstance().init(config);
    }

    override fun onResume() {
        super.onResume();
        userId = credentialStore.getUserId(this);
        if (VKSdk.isLoggedIn()) {
            Log.d(TAG, "Logged In");
            showAlbumsFragment(userId);
        } else {
            Log.d(TAG, " not logged In");
            VKSdk.login(this,
                    VKScope.FRIENDS,
                    VKScope.PHOTOS,
                    VKScope.GROUPS);
        }
        val filter = IntentFilter();
        filter.addAction(DownloadService.ACTION_COMPLETE)
        registerReceiver(mReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, MySdkListener(this))) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    override public fun onCreateOptionsMenu(menu: Menu) : Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    override public fun onOptionsItemSelected(item: MenuItem) : Boolean {
        Log.d(TAG, "onOptionsItemSelected " + item.getItemId())
        userId = Integer.parseInt(VKSdk.getAccessToken().userId);
        Log.d(TAG, "userId " + userId)
        if (userId != 0) {
            when (item.getItemId()) {
                R.id.action_albums -> {
                    showAlbumsFragment(userId);
                }
                R.id.action_friends -> {
                    showFriendsFragment(userId);
                }
                R.id.action_groups -> {
                    showGroupsFragment(userId);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    fun showFragment(fragment: Fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();

    }

    public fun showAlbumsFragment(userId: Int) {
        Log.d(TAG, "showMyAlbumsFragment");
        showFragment(getAlbumsFragment(userId))
    }

    public fun showFriendsFragment(userId: Int) {
        Log.d(TAG, "showMyFriendsFragment");
        showFragment(getFriendsFragment(userId))
    }

    public fun showGroupsFragment(userId: Int) {
        Log.d(TAG, "showMyGroupsFragment");
        showFragment(getGroupsFragment(userId))
    }

    public fun showPhotosFragment(ownerId: Int, albumId: Int, title: String) {
        Log.d(TAG, "showPhotosFragment");
        showFragment(getPhotosFragment(ownerId, albumId, title))
    }

    private inner class MySdkListener(val context : Context) : VKCallback<VKAccessToken> {

        override fun onResult(res: VKAccessToken) {
            userId = Integer.parseInt(res.userId);
            credentialStore.setUserId(context, userId)
            showAlbumsFragment(userId);
        }

        override fun onError(authorizationError: VKError) {
            AlertDialog.Builder(context)
                    .setMessage(authorizationError.toString())
                    .show();
        }

    }

    fun getUserId(): Int {
        return credentialStore.getUserId(this)
    }


}