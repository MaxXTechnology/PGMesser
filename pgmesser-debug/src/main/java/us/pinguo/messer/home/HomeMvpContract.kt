package us.pinguo.messer.home

import android.app.Activity
import android.content.Context

/**
 * Created by hedongjin on 2017/6/26.
 */
class HomeMvpContract {
    open interface IHomeView {
        open fun setPresenter(presenter: IHomePresenter)
        open fun writeContent(content: String, append: Boolean = true)
    }

    open interface IHomePresenter {
        open fun gotoFolderPage(context: Context)
        open fun watchMemory(isStart: Boolean)
    }

    open interface IHomeNavigation {
        fun gotoFolderPage(context: Context)
        fun watchMemory(isStart: Boolean)
    }

    open interface IInnerNavigation {
        fun closeHome()
    }
}