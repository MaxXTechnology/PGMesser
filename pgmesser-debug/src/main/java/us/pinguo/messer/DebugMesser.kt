package us.pinguo.messer

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import us.pinguo.common.imageloader.ImageLoaderExecutorFactory
import us.pinguo.messer.analysis.MesserLeakCanary
import us.pinguo.messer.home.LogReceiver

/**
 * Created by hedongjin on 2017/8/1.
 */
object DebugMesser {
    var appSdRoot: String? = null
    var receiver: LogReceiver? = null

    internal fun registerLogReceiver(receiver: LogReceiver) {
        this.receiver = receiver
    }

    internal fun unRegisterLogReceiver() {
        this.receiver = null
    }

    fun handleMessage(time: Long, level: Int, tag: String, msg: String) {
        receiver?.onReceiveLog(time, level, tag, msg)
    }

    fun install(context: Application) {
        install(context, Environment.getExternalStorageDirectory().absolutePath)
    }

    fun install(context: Application, sdcardRootDir: String) {

        appSdRoot = sdcardRootDir

        val config = ImageLoaderConfiguration.Builder(context)
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


        if (!MesserLeakCanary.isInAnalyzerProcess(context)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            MesserLeakCanary.install(context)
        }

        // 延迟2秒初始化，保证覆盖在主Activity之上
        Handler(Looper.getMainLooper()).postDelayed({
            ActivityLauncher.launchHome(context)
        }, 2000)

    }



}