package us.pinguo.messer.demo

import android.app.Application
import android.util.Log
import us.pinguo.messer.home.HomeMvpContract
import us.pinguo.messer.home.MesserWindowManager

/**
 * Created by hedongjin on 2017/6/26.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MesserWindowManager.getInstance().init(this, object : HomeMvpContract.IHomeNavigation{
            override fun gotoFolderPage() {
                Log.i("MainApplication", "gotoFolderPage")
            }

            override fun watchCpu(isStart: Boolean) {
                Log.i("MainApplication", "watchCpu isStart = $isStart")
            }

            override fun watchMemory(isStart: Boolean) {
                Log.i("MainApplication", "watchMemory isStart = $isStart")
            }
        })

        MesserWindowManager.getInstance().gotoHome()
    }

}