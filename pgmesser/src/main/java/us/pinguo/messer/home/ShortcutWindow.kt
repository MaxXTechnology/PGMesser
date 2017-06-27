package us.pinguo.messer.home

import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import kotlinx.android.synthetic.main.window_shortcut.view.*
import us.pinguo.messer.R
import us.pinguo.messer.util.UIUtils

/**
 * Created by hedongjin on 2017/6/26.
 */
class ShortcutWindow(context: Context, val navigation: IShortcutNavigation) : AbstractWindow(context) {
    private lateinit var mRootView: View
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    override fun onCreate(): View {
        mRootView = LayoutInflater.from(context).inflate(R.layout.window_shortcut, null, false)

        mRootView.shortcut_home.setOnClickListener {
            navigation.gotoHomeWindow()
        }

        return mRootView
    }

    override fun getLayoutParams(): WindowManager.LayoutParams {
        mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams.format = PixelFormat.RGBA_8888
        mLayoutParams.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        mLayoutParams.width = UIUtils.dp2px(50f)
        mLayoutParams.height = UIUtils.dp2px(50f)
        return mLayoutParams
    }


    interface IShortcutNavigation {
        fun gotoHomeWindow()
    }

}