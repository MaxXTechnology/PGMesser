package us.pinguo.messer.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.support.v4.view.MarginLayoutParamsCompat
import android.util.DisplayMetrics
import android.util.LayoutDirection
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout

/**
 * Created by hedongjin on 2017/6/26.
 */
class UIUtils {
    companion object{
        fun setMarginStart(params: ViewGroup.MarginLayoutParams, value: Int) {
            MarginLayoutParamsCompat.setMarginStart(params, value)
        }

        fun setMarginEnd(params: ViewGroup.MarginLayoutParams, value: Int) {
            MarginLayoutParamsCompat.setMarginEnd(params, value)
        }

        fun hideNavigationBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }

        fun checkDeviceHasNavigationBar(context: Context): Boolean {
            var hasNavigationBar = false
            val rs = context.resources
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (e: Exception) {
            }

            return hasNavigationBar
        }

        fun isNavigationBarTranslucent(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val flag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                val attrs = activity.window.attributes
                if (attrs.flags == attrs.flags and flag.inv() or (flag and flag)) {
                    return true
                }
            }
            return false
        }

        /**
         * 有NavigationBar且半透明时得到它的高度：
         */
        fun getTranslucentNavigationBarHeight(activity: Activity): Int {
            val navigationBarHeight: Int
            if (UIUtils.isNavigationBarTranslucent(activity)) {
                navigationBarHeight = UIUtils.getNavigationBarHeight(activity)
            } else {
                navigationBarHeight = 0
            }
            return navigationBarHeight
        }

        /**
         * 获取NavigationBar的高度：
         */
        fun getNavigationBarHeight(context: Context): Int {
            var navigationBarHeight = 0
            val rs = context.resources
            val id = rs.getIdentifier("navigation_bar_height", "dimen", "android")
            if (id > 0 && checkDeviceHasNavigationBar(context)) {
                navigationBarHeight = rs.getDimensionPixelSize(id)
            }
            return navigationBarHeight
        }

        fun getScreenRatio(): Float {
            return 1f * getScreenHeight() / getScreenWidth()
        }

        fun getScreenWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun getScreenHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun getScreenDiagonal(): Int {
            return Math.sqrt(Math.pow(getScreenWidth().toDouble(), 2.0) + Math.pow(getScreenHeight().toDouble(), 2.0)).toInt()
        }

        fun getDisplayMetrics(): DisplayMetrics {
            return Resources.getSystem().displayMetrics
        }

        fun dp2px(value: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (value * scale + 0.5f).toInt()
        }

        fun sp2px(value: Float): Int {
            val scale = Resources.getSystem().displayMetrics.scaledDensity
            return (value * scale + 0.5f).toInt()
        }

        fun removeRule(layoutParams: RelativeLayout.LayoutParams, rule: Int) {
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.removeRule(rule)
            } else {
                layoutParams.addRule(rule, 0)
            }
        }

        fun isLaoutDirectionRtl(): Boolean {
            return Build.VERSION.SDK_INT >= 17 && Resources.getSystem().configuration.layoutDirection == LayoutDirection.RTL
        }
    }
}