package com.vk_photki.ui

import android.util.Log
import com.vk.sdk.api.model.VKApiModel
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by nightrain on 4/25/15.
 */

abstract class SelectableFragment<T : VKApiModel>() : BaseFragment<T>() {
    protected val mSelectedItems: HashMap<Int, Boolean> = HashMap<Int, Boolean>();

    protected fun toggleSelection(position: Int) {
        Log.d(TAG, "toggleSelection: " + position);

        if (mSelectedItems.containsKey(position)) {
            val b = mSelectedItems.get(position)?.not()
            mSelectedItems.put(position, b)
        } else {
            mSelectedItems.put(position, true)
        }
        mList?.getAdapter()?.notifyDataSetChanged()
    }

    protected fun selectAll() {
        for (i in 0..mDataset!!.size() - 1) {
            mSelectedItems.put(i, true)
        }
        mList?.getAdapter()?.notifyDataSetChanged()
    }

    protected fun unselectAll() {
        for (i in 0..mDataset!!.size() - 1) {
            mSelectedItems.put(i, false)
        }
        mList?.getAdapter()?.notifyDataSetChanged()
    }

    protected fun getSelectedItems(): List<T> {
        val result = ArrayList<T>(mDataset!!.size())
        for (i in 0..mDataset!!.size() - 1) {
            if (mSelectedItems.containsKey(i) && mSelectedItems.get(i)) {
                result.add(mDataset!!.get(i))
            }
        }
        return result;
    }
}