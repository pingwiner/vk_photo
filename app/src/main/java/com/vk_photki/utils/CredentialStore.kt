package com.vk_photki.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE



/**
 * Created by nightrain on 4/4/15.
 */

class CredentialStore {
    val PREF_FILE_NAME = "userdetails";
    val PREF_USER_ID = "userId";

    fun setUserId(context: Context, userId: Int) {
        val userDetails = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        val edit = userDetails.edit()
        edit.putInt(PREF_USER_ID, userId)
        edit.apply()
    }

    fun getUserId(context: Context): Int {
        val userDetails = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE)
        return userDetails.getInt(PREF_USER_ID, 0)
    }

}