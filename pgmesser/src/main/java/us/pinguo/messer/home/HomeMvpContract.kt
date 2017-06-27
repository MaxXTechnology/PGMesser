package us.pinguo.messer.home

import android.app.Activity

/**
 * Created by hedongjin on 2017/6/26.
 */
class HomeMvpContract {
    open interface IHomeView {
        open fun setPresenter(presenter: IHomePresenter)
        open fun writeContent(content: String, append: Boolean = false)
    }

    open interface IHomePresenter {
        open fun gotoFolderPage()
        open fun watchCpu(isStart: Boolean)
        open fun watchMemory(isStart: Boolean)
    }

    open interface IHomeNavigation {
        fun gotoFolderPage()
        fun watchCpu(isStart: Boolean)
        fun watchMemory(isStart: Boolean)
    }

    open interface IInnerNavigation {
        fun closeHome()
    }
}