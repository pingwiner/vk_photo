package com.vk_photki.ui

import android.content.Context
import android.support.v7.internal.widget.AdapterViewCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by nightrain on 4/12/15.
 */
public class RecyclerItemClickListener(var context: Context, var clickListener: RecyclerItemClickListener.OnItemClickListener) : RecyclerView.OnItemTouchListener {
    private var mGestureDetector: GestureDetector = GestureDetector(context, MySimpleOnGestureListener());

    public trait OnItemClickListener {
        public fun onItemClick(view: View, position: Int);
    }

    override public fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        var childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            clickListener.onItemClick(childView, view.getChildPosition(childView));
        }
        return false;
    }

    override public fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) { }
}

class MySimpleOnGestureListener : GestureDetector.SimpleOnGestureListener() {
    override public fun onSingleTapUp(e: MotionEvent): Boolean {
        return true;
    }
}