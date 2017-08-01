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
import us.pinguo.messer.DebugMesser
import us.pinguo.messer.home.HomeMvpContract
import us.pinguo.messer.home.MesserWindowManager


/**
 * Created by hedongjin on 2017/6/26.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMesser.install(this)
    }
}