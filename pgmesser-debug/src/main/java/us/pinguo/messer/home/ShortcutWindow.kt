package us.pinguo.messer.home

import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import kotlinx.android.synthetic.main.window_shortcut.view.*
import us.pinguo.messer.R
import us.pinguo.messer.util.UIUtils
import us.pinguo.messer.util.WindowGestureDetector
import us.pinguo.messer.util.WindowGestureDetector.GestureDetectorListener

/**
 * Created by hedongjin on 2017/6/26.
 */
class ShortcutWindow(context: Context, val navigation: IShortcutNavigation) : AbstractWindow(context) {
    private lateinit var mRootView: View
    private val mLayoutParams by lazy {
        val params = LayoutParams()
        params.type = LayoutParams.TYPE_PHONE
        params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_FOCUSABLE
        params.format = PixelFormat.RGBA_8888
        params.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        params.width = UIUtils.dp2px(50f)
        params.height = UIUtils.dp2px(50f)
        params
    }

    override fun onCreate(): View {
        mRootView = LayoutInflater.from(context).inflate(R.layout.window_shortcut, null, false)

        val detector = WindowGestureDetector(object : GestureDetectorListener{

            override fun onSingleTap() {
                navigation.gotoHomeWindow()
            }

            override fun onScroll(dx: Int, dy: Int) {
                WindowCompat.updateWindowLayout(context, this@ShortcutWindow, dx, dy)
            }

        })
        mRootView.setOnTouchListener{v, e ->
            detector.onTouchEvent(e)
        }

        return mRootView
    }

    override fun getLayoutParams(): LayoutParams {
        return mLayoutParams
    }

    interface IShortcutNavigation {
        fun gotoHomeWindow()
    }

}