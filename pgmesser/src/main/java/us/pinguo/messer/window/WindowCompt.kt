package us.pinguo.messer.home

import android.content.Context
import android.view.View
import android.view.WindowManager

/**
 * Created by hedongjin on 2017/6/26.
 */
class WindowCompt {

    // 伴生对象
    companion object {

        fun startWindow(context: Context, window: AbstractWindow) {

            window.dispatchCreate()

            window.getView()?.let {
                val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.addView(it, window.getLayoutParams())
            }

            window.dispatchResume()

        }

        fun stopWindow(context: Context, window: AbstractWindow) {
            window.dispatchPause()

            window.getView()?.let {
                val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                windowManager.removeView(it)
            }

            window.dispatchDestory()
        }

        fun updateViewLayout(context: Context, view: View, layoutParams: WindowManager.LayoutParams) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.updateViewLayout(view, layoutParams)
        }
    }

}