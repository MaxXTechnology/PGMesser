package us.pinguo.messer.home

/**
 * Created by hedongjin on 2017/6/26.
 */
open class HomePresenter(val navigation: HomeMvpContract.IHomeNavigation) : HomeMvpContract.IHomePresenter {

    override fun gotoFolderPage() {
        navigation.gotoFolderPage()
    }

    override fun watchCpu(isStart: Boolean) {
        navigation.watchCpu(isStart)
    }

    override fun watchMemory(isStart: Boolean) {
        navigation.watchMemory(isStart)
    }
}
