package us.pinguo.messer.home

import android.app.Activity
import android.app.Application
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View

/**
 * Created by hedongjin on 2017/6/26.
 */
class MesserWindowManager private constructor() {

    private lateinit var mContext: Application
    private lateinit var mHomeWindow: HomeWindow
    private lateinit var mShortcutWindow: ShortcutWindow
    private var mCurrentWindow: AbstractWindow? = null
    private var mForegroundActivity: Activity? = null

    private val mLifecycleCallback = object : Application.ActivityLifecycleCallbacks{
        override fun onActivityPaused(activity: Activity?) {
            mCurrentWindow.setVisibility(View.GONE)
            mForegroundActivity = null
        }

        override fun onActivityResumed(activity: Activity?) {
            activity?.let {
                when (activity.componentName.className) {
                    "us.pinguo.messer.db.DbActivity" ->
                        mCurrentWindow.setVisibility(View.GONE)
                    "us.pinguo.messer.local.LocalFileBrowserActivity" ->
                        mCurrentWindow.setVisibility(View.GONE)
                    "us.pinguo.messer.image.ImageBrowserActivity" ->
                        mCurrentWindow.setVisibility(View.GONE)
                    else ->
                        mCurrentWindow.setVisibility(View.VISIBLE)
                }
            }
            mForegroundActivity = activity
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }


    fun init(context: Application, navigation: HomeMvpContract.IHomeNavigation) {
        mContext = context
        mHomeWindow = HomeWindow(context, object : HomeMvpContract.IInnerNavigation{
            override fun closeHome() {
                gotoShortcut()
            }
        })
        mHomeWindow.setPresenter(HomePresenter(navigation))

        mShortcutWindow = ShortcutWindow(context, object : ShortcutWindow.IShortcutNavigation{
            override fun gotoHomeWindow() {
                gotoHome()
            }
        })

        context.registerActivityLifecycleCallbacks(mLifecycleCallback)
    }

    fun destory() {
        mContext.unregisterActivityLifecycleCallbacks(mLifecycleCallback)

        WindowCompat.stopWindow(mContext, mHomeWindow)
        WindowCompat.stopWindow(mContext, mShortcutWindow)
    }

    fun getForegroundActivity() = mForegroundActivity

    fun gotoHome() {
        if (mHomeWindow.isAttachToWindow()) {
            mHomeWindow.setVisibility(View.VISIBLE)
        } else {
            WindowCompat.startWindow(mContext, mHomeWindow)
        }

        if (mShortcutWindow.isAttachToWindow()) {
            mShortcutWindow.setVisibility(View.GONE)
        }

        mCurrentWindow = mHomeWindow
    }

    fun gotoShortcut() {
        if (mShortcutWindow.isAttachToWindow()) {
            mShortcutWindow.setVisibility(View.VISIBLE)
        } else {
            WindowCompat.startWindow(mContext, mShortcutWindow)
        }

        if (mHomeWindow.isAttachToWindow()) {
            mHomeWindow.setVisibility(View.GONE)
        }

        mCurrentWindow = mShortcutWindow

    }

    private fun AbstractWindow?.setVisibility(visibility: Int) {
        this?.let {
            it.getView().visibility = visibility
        }
    }

    companion object {
        fun getInstance() = Inner.INSTANCE
    }

    private object Inner {
        val INSTANCE = MesserWindowManager()
    }
}