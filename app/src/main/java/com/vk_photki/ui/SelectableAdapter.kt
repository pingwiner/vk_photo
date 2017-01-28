package com.vk_photki.ui

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import java.util.*

/**
 * Created by nightrain on 4/25/15.
 */
abstract class SelectableAdapter<T>(context: Context, data: List<T>) :
        BaseAdapter<T>(context, data) {

    private var selectedItems : BooleanArray

    init {
         selectedItems = BooleanArray(data.size)
        for (i in 0..data.size - 1) {
            selectedItems[i] = false
        }
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        super.onBindViewHolder(holder, position)
        holder?.checkbox!!.setOnCheckedChangeListener(
                { compoundButton, b -> selectedItems[position] = b}
        )
        holder?.checkbox?.setVisibility(View.VISIBLE)
        val checked : Boolean = selectedItems[position]
        holder!!.checkbox!!.setChecked(checked)
    }



    fun getSelectedItems() : List<T> {
        var i = 0;
        var result: MutableList<T> = arrayListOf<T>()
        for (item in data) {
            if (selectedItems[i]) {
                result.add(item)
            }
            i++
        }
        return result
    }

    fun selectAll() {
        for (i in 0..data.size - 1) {
            selectedItems[i] = true;
        }
        notifyDataSetChanged()
    }

    fun unselectAll() {
        for (i in 0..data.size - 1) {
            selectedItems[i] = false;
        }
        notifyDataSetChanged()
    }

    fun toggleSelection(i : Int) {
        selectedItems[i] = !selectedItems[i]
        notifyItemChanged(i)
    }
}