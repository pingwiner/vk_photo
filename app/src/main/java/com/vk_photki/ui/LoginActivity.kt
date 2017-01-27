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
import com.vk.sdk.*
import com.vk.sdk.api.VKError
import com.vk.sdk.dialogs.VKCaptchaDialog
import com.vk_photki.R
import com.vk_photki.service.DownloadService
import java.lang.Integer

public class LoginActivity() : ActionBarActivity() {
    private val TAG = "LoginActivity";
    private var userId = 0;
    private var mProgress: ProgressBar? = null;
    private val mReceiver = Receiver();

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.getAction();
            Log.d(TAG, "onReceive " + action);
            if (action.equals(DownloadService.ACTION_COMPLETE)) {
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
    }

    override fun onResume() {
        super.onResume();

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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

    public fun showPhotosFragment(ownerId: Int, albumId: Int) {
        Log.d(TAG, "showPhotosFragment");
        showFragment(getPhotosFragment(ownerId, albumId))
    }

    private inner class MySdkListener(val context : Context) : VKCallback<VKAccessToken> {


        override public fun onResult(res: VKAccessToken) {
            userId = Integer.parseInt(res.userId);
            showAlbumsFragment(userId);
        }

        override public fun onError(authorizationError: VKError) {
            AlertDialog.Builder(context)
                    .setMessage(authorizationError.toString())
                    .show();
        }

    }

    public fun getUserId(): Int {
        return userId;
    }
}