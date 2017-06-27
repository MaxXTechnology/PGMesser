package us.pinguo.messer.home

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View

/**
 * Created by hedongjin on 2017/6/26.
 */
class MesserWindowManager private constructor() {

    private lateinit var mContext: Application
    private lateinit var mHomeWindow: HomeWindow
    private lateinit var mShortcutWindow: ShortcutWindow
    private var mCurrentWindow: AbstractWindow? = null

    private val mLifecycleCallback = object : Application.ActivityLifecycleCallbacks{
        override fun onActivityPaused(activity: Activity?) {
            mCurrentWindow?.let {
                it.getView().visibility = View.GONE
            }
        }

        override fun onActivityResumed(activity: Activity?) {
            mCurrentWindow?.let {
                it.getView().visibility = View.VISIBLE
            }
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

    fun gotoHome() {
        if (mHomeWindow.isAttachToWindow()) {
            mHomeWindow.getView().visibility = View.VISIBLE
        } else {
            WindowCompat.startWindow(mContext, mHomeWindow)
        }

        if (mShortcutWindow.isAttachToWindow()) {
            mShortcutWindow.getView().visibility = View.GONE
        }

        mCurrentWindow = mHomeWindow
    }

    fun gotoShortcut() {
        if (mShortcutWindow.isAttachToWindow()) {
            mShortcutWindow.getView().visibility = View.VISIBLE
        } else {
            WindowCompat.startWindow(mContext, mShortcutWindow)
        }

        if (mHomeWindow.isAttachToWindow()) {
            mHomeWindow.getView().visibility = View.GONE
        }

        mCurrentWindow = mShortcutWindow

    }

    companion object {
        fun getInstance() = Inner.INSTANCE
    }

    private object Inner {
        val INSTANCE = MesserWindowManager()
    }
}