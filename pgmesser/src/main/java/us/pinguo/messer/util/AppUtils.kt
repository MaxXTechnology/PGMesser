package us.pinguo.messer.util

import android.content.Context

/**
 * Created by hedongjin on 2017/7/11.
 */
object AppUtils {

    fun clearAppData(context: Context) = try {
        Runtime.getRuntime().exec("pm clear " + context.packageName) != null
    } catch (e : Exception) {
        false
    }

}