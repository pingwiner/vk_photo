package com.vk_photki.ui

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import java.util.HashMap

/**
 * Created by nightrain on 4/25/15.
 */
abstract class SelectableAdapter<T>(context: Context, data: List<T>, var selectedItems: HashMap<Int, Boolean>) :
        BaseAdapter<T>(context, data) {

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        super.onBindViewHolder(holder, position)
        holder?.checkbox?.setVisibility(View.VISIBLE)
        val checked = selectedItems.containsKey(position) && selectedItems.get(position)
        holder!!.checkbox!!.setChecked(checked)
        //holder!!.checkbox!!.setOnCheckedChangeListener { (compoundButton, b) -> selectedItems.put(position, b) }
    }



}