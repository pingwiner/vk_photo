package com.vk_photki.utils

import java.security.NoSuchAlgorithmException

/**
 * Created by nightrain on 4/12/15.
 */
public class MD5(var s: String) {
    val MD5 = "MD5";
    private var mResult: String = "";

    init {
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.toByteArray(Charsets.UTF_8));
            val messageDigest = digest.digest();

            // Create Hex String
            val hexString = StringBuilder();
            for (aMessageDigest in  messageDigest) {
                var h = Integer.toHexString(aMessageDigest.toInt());
                while (h.length < 2) h = "0" + h;
                hexString.append(h);
            }
            mResult = hexString.toString();

        } catch (e: NoSuchAlgorithmException) {
            //e.printStackTrace();
        }
    }

    public override fun toString(): String {
        return mResult;
    }
}
