package com.vk_photki.ui

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vk.sdk.api.VKError
import com.vk.sdk.api.model.VKApiModel
import com.vk.sdk.api.model.VKApiPhotoAlbum
import com.vk.sdk.api.model.VKApiUser
import com.vk_photki.R
import com.vk_photki.api.VkFriendsLoader
import com.vk_photki.ui.api.VkAlbumsLoader
import java.util.ArrayList

/**
 * Created by nightrain on 4/21/15.
 */
abstract class BaseFragment<T : VKApiModel>() : Fragment(), OnDataReadyListener<T> {
    protected abstract val TAG: String;
    protected abstract val LAYOUT_RESOURCE_ID: Int;
    protected var ownerId: Int = 0;
    private var mList: RecyclerView? = null;
    private var mDataset: List<T>? = null;
    private var mLayoutManager: RecyclerView.LayoutManager? = null;
    private var mProgress: View? = null;

    companion object Args {
        public val ARG_USER_ID: String = "ARG_USER_ID"
    }

    override public fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        ownerId = Integer.parseInt(getArguments().getString(BaseFragment.ARG_USER_ID));
        startLoaders();
        setProgressVisibility(true)
        val view = inflater.inflate(LAYOUT_RESOURCE_ID, container, false);
        mList = view.findViewById(R.id.list) as RecyclerView;
        mList?.setHasFixedSize(true);
        mProgress = view.findViewById(R.id.progress);
        // use a linear layout manager
        var display = getActivity().getWindowManager().getDefaultDisplay();
        var size = Point();
        display.getSize(size);
        val width = size.x;
        mLayoutManager = StaggeredGridLayoutManager(width / 200, StaggeredGridLayoutManager.VERTICAL);
        mList?.setLayoutManager(mLayoutManager);
        return view;
    }

    abstract protected fun startLoaders();
    abstract protected fun getAdapter(context: Context, data: List<T>): BaseAdapter<T>;

    override fun onDataReady(data: List<T>) {
        Log.d(TAG, "onDataReady: " + data.size);
        setProgressVisibility(false)
        if (getActivity() != null) {
            var adapter = getAdapter(getActivity(), data)
            mList?.setAdapter(adapter)
            mDataset = data
        }
    }

    override fun onDataLoadingFailed(error: VKError) {
        Log.d(TAG, "onFriendsLoadingFailed");
        setProgressVisibility(false)
    }

    private fun setProgressVisibility(visible: Boolean) {
        if (mProgress != null) {
            if (visible) {
                mProgress.setVisibility(View.VISIBLE);
            } else {
                mProgress.setVisibility(View.GONE);
            }
        }
    }

}

public trait OnDataReadyListener<T> {
    public fun onDataReady(data: List<T>);
    public fun onDataLoadingFailed(error: VKError);
}



