package com.vk_photki.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by nightrain on 4/22/15.
 */
abstract public class BaseAdapter<T>(var context: Context, var data: List<T>)
    : RecyclerView.Adapter<Holder>() {

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        val item = data.get(position);
        if (holder == null) return;
        if (holder.title != null) {
            holder.title?.setText(getTitle(item))
        }
        if (holder.cover != null) {
            val thumbSrc = getThumbSrc(item);
            if (thumbSrc != null) {
                holder.setCover(context, thumbSrc);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder? {
        if (parent == null) return null;
        val v = LayoutInflater.from(parent.getContext())
                .inflate(getLayoutId(), parent, false);
        return Holder(v);
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    public fun getItem(position: Int): T {
        return data.get(position)
    }

    protected abstract fun getTitle(item: T) : String;
    protected abstract fun getThumbSrc(item: T) : String;
    protected abstract fun getLayoutId() : Int;

}

