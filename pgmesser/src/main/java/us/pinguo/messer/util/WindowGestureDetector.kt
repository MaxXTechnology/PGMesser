package us.pinguo.messer.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.view.ViewConfiguration
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by hejiang on 2017/8/12.
 */
class WindowGestureDetector(val listener: GestureDetectorListener) {

    private val NO_INITIALIZE = -1
    private val MSG_SCROLL_EVENT = 1
    private val MSG_SINGLE_TAP_EVENT = 2
    private val MSG_DELAY_LONG_PRESS_EVENT = 3
    private val MSG_RESET_LONG_PRESS_EVENT = 4
    private val MSG_DELAY_SINGLE_TAP_EVENT = 5
    private val MSG_RESET_SINGLE_TAP_EVENT = 6

    private var mLastFocusX = NO_INITIALIZE
    private var mLastFocusY = NO_INITIALIZE

    private var mIsLongPress = AtomicBoolean(false)
    private val mIsSingleOutTime = AtomicBoolean(false)

    val mUIHandler = object : Handler(Looper.getMainLooper()) {


        override fun handleMessage(msg: Message?) {
            if (msg == null) return

            when (msg.what) {
                MSG_SINGLE_TAP_EVENT -> dispatchSingleTapEvent()
                MSG_SCROLL_EVENT -> dispatchScrollEvent(msg.arg1, msg.arg2)
                MSG_DELAY_SINGLE_TAP_EVENT -> setSingleTapOutTime(true)
                MSG_RESET_SINGLE_TAP_EVENT -> setSingleTapOutTime(false)
                MSG_DELAY_LONG_PRESS_EVENT -> setLongPressEvent(true)
                MSG_RESET_LONG_PRESS_EVENT -> setLongPressEvent(false)
            }
        }

        fun dispatchSingleTapEvent() {
            listener.onSingleTap()
        }

        fun dispatchScrollEvent(dx: Int, dy: Int) {
            listener.onScroll(dx, dy)
        }

        fun setLongPressEvent(isLongPress: Boolean) {
            mIsLongPress.set(isLongPress)
        }

        fun setSingleTapOutTime(isSingleTap: Boolean) {
            mIsSingleOutTime.set(isSingleTap)
        }
    }

    interface GestureDetectorListener {
        fun onSingleTap() {}
        fun onScroll(dx: Int, dy: Int)
    }

    fun onTouchEvent(ev: MotionEvent) : Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastFocusX = NO_INITIALIZE
                mLastFocusY = NO_INITIALIZE
                mUIHandler.sendEmptyMessage(MSG_RESET_LONG_PRESS_EVENT)
                mUIHandler.sendEmptyMessageDelayed(MSG_DELAY_LONG_PRESS_EVENT, ViewConfiguration.getLongPressTimeout().toLong())

                mUIHandler.sendEmptyMessage(MSG_RESET_SINGLE_TAP_EVENT)
                mUIHandler.sendEmptyMessageDelayed(MSG_DELAY_SINGLE_TAP_EVENT, ViewConfiguration.getTapTimeout().toLong())
            }
            MotionEvent.ACTION_CANCEL -> {
                mUIHandler.removeMessages(MSG_DELAY_LONG_PRESS_EVENT)
                mUIHandler.removeMessages(MSG_DELAY_SINGLE_TAP_EVENT)

                if (!mIsSingleOutTime.get()) {
                    mUIHandler.sendEmptyMessage(MSG_SINGLE_TAP_EVENT)
                }
            }
            MotionEvent.ACTION_UP -> {
                mUIHandler.removeMessages(MSG_DELAY_LONG_PRESS_EVENT)
                mUIHandler.removeMessages(MSG_DELAY_SINGLE_TAP_EVENT)

                if (!mIsSingleOutTime.get()) {
                    mUIHandler.sendEmptyMessage(MSG_SINGLE_TAP_EVENT)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mIsLongPress.get()) {

                    if (mLastFocusX == NO_INITIALIZE) {
                        mLastFocusX = ev.rawX.toInt()
                    }
                    if (mLastFocusY == NO_INITIALIZE) {
                        mLastFocusY = ev.rawY.toInt()
                    }

                    val dx = ev.rawX.toInt() - mLastFocusX
                    val dy = ev.rawY.toInt() - mLastFocusY
                    if (Math.abs(dx) >= 1 || Math.abs(dy) >= 1) {

                        val msg = Message()
                        msg.what = MSG_SCROLL_EVENT
                        msg.arg1 = dx
                        msg.arg2 = dy
                        mUIHandler.sendMessage(msg)


                        mLastFocusX = ev.rawX.toInt()
                        mLastFocusY = ev.rawY.toInt()
                    }
                    return true
                }
            }
        }

        return false
    }



}