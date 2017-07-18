package us.pinguo.messer.demo

import android.app.Application
import android.util.Log
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import us.pinguo.common.imageloader.ImageLoaderExecutorFactory
import us.pinguo.common.tinypref.TinyPref
import us.pinguo.messer.ActivityLauncher
import us.pinguo.messer.home.HomeMvpContract
import us.pinguo.messer.home.MesserWindowManager


/**
 * Created by hedongjin on 2017/6/26.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TinyPref.getInstance().init(this)

        val config = ImageLoaderConfiguration.Builder(applicationContext)
                .taskExecutor(ImageLoaderExecutorFactory.createIoExecutor())
                .taskExecutorForCachedImages(ImageLoaderExecutorFactory.createCacheExecutor())
                .diskCacheFileNameGenerator(Md5FileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.Builder()
                        .cacheInMemory(true).cacheOnDisk(true).build())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build()
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config)
        ImageLoader.getInstance().handleSlowNetwork(true)

        MesserWindowManager.getInstance().init(this, object : HomeMvpContract.IHomeNavigation{
            override fun gotoFolderPage() {
                Log.i("MainApplication", "gotoFolderPage")
                ActivityLauncher.launchLocalFileBrowser(this@MainApplication)
            }

            override fun watchCpu(isStart: Boolean) {
                Log.i("MainApplication", "watchCpu isStart = $isStart")
                ActivityLauncher.launchDbBrowser(this@MainApplication)
            }

            override fun watchMemory(isStart: Boolean) {
                Log.i("MainApplication", "watchMemory isStart = $isStart")
                ActivityLauncher.launchImageBrowser(this@MainApplication)

            }
        })

        MesserWindowManager.getInstance().gotoHome()
    }

    override fun onTerminate() {
        super.onTerminate()
        MesserWindowManager.getInstance().destory()
    }
}