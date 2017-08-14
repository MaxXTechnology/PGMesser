package us.pinguo.messer.home

import android.content.Context
import android.view.View
import android.view.WindowManager

/**
 * Created by hedongjin on 2017/6/26.
 */
object WindowCompat {

    fun startWindow(context: Context, window: AbstractWindow) {
        window.dispatchCreate()

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(window.getView(), window.getLayoutParams())

        window.dispatchResume()


    }

    fun stopWindow(context: Context, window: AbstractWindow) {
        window.dispatchPause()

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.removeView(window.getView())

        window.dispatchDestory()
    }

    fun updateWindowLayout(context: Context, window: AbstractWindow, dx: Int, dy: Int) {
        val layoutParams = window.getLayoutParams()
        window.getLayoutParams().x += dx
        window.getLayoutParams().y += dy

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.updateViewLayout(window.getView(), layoutParams)
    }
}