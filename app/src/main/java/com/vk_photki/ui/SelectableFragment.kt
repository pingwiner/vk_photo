package com.vk_photki.ui

import android.util.Log
import com.vk.sdk.api.model.VKApiModel
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by nightrain on 4/25/15.
 */

abstract class SelectableFragment<T : VKApiModel>() : BaseFragment<T>() {

    protected fun selectAll() {
        val adapter = mList?.getAdapter() as SelectableAdapter<T>
        adapter.selectAll()
    }

    protected fun unselectAll() {
        val adapter = mList?.getAdapter() as SelectableAdapter<T>
        adapter.unselectAll()
    }

    protected fun getSelectedItems(): List<T> {
        val adapter = mList?.getAdapter() as SelectableAdapter<T>
        return adapter.getSelectedItems()
    }

    protected fun toggleSelection(position: Int) {
        val adapter = mList?.getAdapter() as SelectableAdapter<T>
        adapter.toggleSelection(position)
    }
}