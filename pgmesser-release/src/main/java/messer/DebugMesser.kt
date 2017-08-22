package us.pinguo.messer

import android.app.Application
import android.os.Environment

/**
 * Created by hedongjin on 2017/8/1.
 */
object DebugMesser {

    fun handleMessage(time: Long, level: Int, tag: String, msg: String) {
    }

    fun install(context: Application) {

    }

    fun install(context: Application, sdcardRootDir: String = Environment.getExternalStorageDirectory().absolutePath) {

    }
}