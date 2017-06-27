package us.pinguo.messer.demo

import android.app.Activity
import android.app.Application
import android.util.Log
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import us.pinguo.common.imageloader.ImageLoaderExecutorFactory
import us.pinguo.messer.ActivityLauncher
import us.pinguo.messer.home.HomeMvpContract
import us.pinguo.messer.home.MesserWindowManager


/**
 * Created by hedongjin on 2017/6/26.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

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
            override fun gotoFolderPage(act: Activity?) {
                Log.i("MainApplication", "gotoFolderPage")
                MesserWindowManager.getInstance().getForegroundActivity()?.let {
                    ActivityLauncher.launchLocalFileBrowser(it)
                }
            }

            override fun watchCpu(isStart: Boolean) {
                Log.i("MainApplication", "watchCpu isStart = $isStart")
                MesserWindowManager.getInstance().getForegroundActivity()?.let {
                    ActivityLauncher.launchDbBrowser(it)
                }
            }

            override fun watchMemory(isStart: Boolean) {
                Log.i("MainApplication", "watchMemory isStart = $isStart")
                MesserWindowManager.getInstance().getForegroundActivity()?.let {
                    ActivityLauncher.launchImageBrowser(it)
                }

            }
        })

        MesserWindowManager.getInstance().gotoHome()
    }

    override fun onTerminate() {
        super.onTerminate()
        MesserWindowManager.getInstance().destory()
    }
}