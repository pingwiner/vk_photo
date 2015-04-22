package com.vk_photki.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vk.sdk.*
import com.vk.sdk.api.VKError
import com.vk.sdk.dialogs.VKCaptchaDialog
import com.vk_photki.R

public class LoginActivity() : FragmentActivity() {
    private val TAG = "LoginActivity";

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
            showMyAlbumsFragment();
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
        var userId = VKSdk.getAccessToken().userId;
        if (userId != null) {
            when (item.getItemId()) {
                R.id.action_albums -> {
                    showMyAlbumsFragment(userId);
                }
                R.id.action_friends -> {
                    showMyFriendsFragment(userId);
                }
                R.id.action_groups -> {
                    showMyGroupsFragment(userId);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    fun showFragment(fragment: Fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    fun showMyAlbumsFragment(userId: String) {
        showFragment(getAlbumsFragment(userId))
    }

    fun showMyFriendsFragment(userId: String) {
        showFragment(getFriendsFragment(userId))
    }

    fun showMyGroupsFragment(userId: String) {
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
            showMyAlbumsFragment();
        }

        override fun onAcceptUserToken(token: VKAccessToken) {
            Log.d(TAG, "onAcceptUserToken: " + token.accessToken);
            showMyAlbumsFragment();
        }

        override fun onRenewAccessToken(token: VKAccessToken) {
            Log.d(TAG, "onRenewAccessToken: " + token.accessToken);
            showMyAlbumsFragment();
        }
    }
}