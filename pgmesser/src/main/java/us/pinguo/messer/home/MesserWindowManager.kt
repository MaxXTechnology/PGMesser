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
    private lateinit var mCurrentWindow: AbstractWindow

    private val mLifecycleCallback = object : Application.ActivityLifecycleCallbacks{
        override fun onActivityPaused(activity: Activity?) {
            mCurrentWindow.getView().visibility = View.GONE
        }

        override fun onActivityResumed(activity: Activity?) {
            mCurrentWindow.getView().visibility = View.VISIBLE
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

    fun create(context: Application, navigation: HomeMvpContract.IHomeNavigation) {
        mContext = context
        mHomeWindow = HomeWindow(context, object : HomeMvpContract.IInnerNavigation{
            override fun closeHome() {
                mHomeWindow.getView().visibility = View.GONE
                mShortcutWindow.getView().visibility = View.VISIBLE
            }
        })
        mHomeWindow.setPresenter(HomePresenter(navigation))

        mShortcutWindow = ShortcutWindow(context, object : ShortcutWindow.IShortcutNavigation{
            override fun gotoHomeWindow() {
                mHomeWindow.getView().visibility = View.VISIBLE
                mShortcutWindow.getView().visibility = View.GONE
            }
        })

        WindowCompt.startWindow(context, mHomeWindow)
        WindowCompt.startWindow(context, mShortcutWindow)

        mHomeWindow.getView().visibility = View.VISIBLE
        mShortcutWindow.getView().visibility = View.GONE
        mCurrentWindow = mHomeWindow

        context.registerActivityLifecycleCallbacks(mLifecycleCallback)
    }

    fun destory() {
        mContext.unregisterActivityLifecycleCallbacks(mLifecycleCallback)
        WindowCompt.stopWindow(mContext, mHomeWindow)
    }

    companion object {
        fun getInstance() = Inner.INSTANCE
    }

    private object Inner {
        val INSTANCE = MesserWindowManager()
    }
}