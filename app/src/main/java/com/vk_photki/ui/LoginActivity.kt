package com.vk_photki.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBarActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vk.sdk.*
import com.vk.sdk.api.VKError
import com.vk.sdk.dialogs.VKCaptchaDialog
import com.vk_photki.R
import java.lang.Integer

public class LoginActivity() : ActionBarActivity() {
    private val TAG = "LoginActivity";
    private var userId = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        VKUIHelper.onCreate(this)
        VKSdk.initialize(MySdkListener(), CredentialStore().getAppId());
        if (VKSdk.wakeUpSession()) {
            Log.d(TAG, "wakeUpSession");
        }

    }

    override fun onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        if (VKSdk.isLoggedIn()) {
            Log.d(TAG, "Logged In");
            showAlbumsFragment(userId);
        } else {
            Log.d(TAG, " not logged In");
            VKSdk.authorize(
                    VKScope.FRIENDS,
                    VKScope.PHOTOS,
                    VKScope.GROUPS);
        }
    }

    override fun onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
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

    private inner class MySdkListener : VKSdkListener() {
        override fun onCaptchaError(captchaError: VKError?) {
            Log.d(TAG, "onCaptchaError");
            VKCaptchaDialog(captchaError).show();
        }

        override fun onTokenExpired(expiredToken: VKAccessToken?) {
            Log.d(TAG, "onTokenExpired");
            VKSdk.authorize(
                    VKScope.FRIENDS,
                    VKScope.PHOTOS,
                    VKScope.GROUPS);
        }

        override fun onAccessDenied(authorizationError: VKError?) {
            Log.d(TAG, "onAccessDenied");
            AlertDialog.Builder(VKUIHelper.getTopActivity())
                    .setMessage(authorizationError.toString())
                    .show();
        }

        override fun onReceiveNewToken(newToken: VKAccessToken) {
            Log.d(TAG, "onReceiveNewToken: " + newToken.accessToken);
            userId = Integer.parseInt(VKSdk.getAccessToken().userId);
            showAlbumsFragment(userId);
        }

        override fun onAcceptUserToken(token: VKAccessToken) {
            Log.d(TAG, "onAcceptUserToken: " + token.accessToken);
            userId = Integer.parseInt(VKSdk.getAccessToken().userId);
            showAlbumsFragment(userId);
        }

        override fun onRenewAccessToken(token: VKAccessToken) {
            Log.d(TAG, "onRenewAccessToken: " + token.accessToken);
            userId = Integer.parseInt(VKSdk.getAccessToken().userId);
            showAlbumsFragment(userId);
        }
    }
}