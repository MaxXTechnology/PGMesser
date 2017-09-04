package us.pinguo.messer.home

import android.content.Context

/**
 * Created by hedongjin on 2017/6/26.
 */
open class HomePresenter(val navigation: HomeMvpContract.IHomeNavigation) : HomeMvpContract.IHomePresenter {

    override fun gotoFolderPage(context: Context) {
        navigation.gotoFolderPage(context)
    }

    override fun watchMemory(isStart: Boolean) {
        navigation.watchMemory(isStart)
    }
}
