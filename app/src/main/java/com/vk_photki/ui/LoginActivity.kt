package com.vk_photki.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
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

    fun showMyAlbumsFragment() {
        var userId = VKSdk.getAccessToken().userId;
        Log.d(TAG, "userId: " + userId);
        if (userId == null) return;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, getAlbumsFragment(userId))
                .commit();
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