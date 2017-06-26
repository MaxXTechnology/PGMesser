package us.pinguo.messer.home

import android.content.Context
import android.view.View
import android.view.WindowManager

/**
 * Created by hedongjin on 2017/6/26.
 */
open abstract class AbstractWindow(val context: Context) {

    private lateinit var mRootView: View

    fun getView(): View = mRootView
    fun dispatchCreate() {
        mRootView = onCreate()
    }

    fun dispatchResume() {
        onResume()
    }
    fun dispatchPause() {
        onPause()
    }
    fun dispatchDestory() {
        onDestory()
    }

    open abstract fun getLayoutParams(): WindowManager.LayoutParams

    open abstract fun onCreate(): View

    open fun onResume() {}

    open fun onPause() {}

    open fun onDestory() {}


}