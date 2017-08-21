package us.pinguo.messer.util

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat

/**
 * Created by hedongjin on 2017/7/11.
 */
object AppUtils {

    fun clearAppData(context: Context) = try {
        Runtime.getRuntime().exec("pm clear " + context.packageName) != null
    } catch (e: Exception) {
        false
    }

    fun checkDangerousPermission(context: Context, permission: String): Boolean {
        val isGranted: Boolean
        if (Build.VERSION.SDK_INT >= 23) {
            isGranted = true
        } else {
            isGranted = ContextCompat.checkSelfPermission(context, permission) == 0
        }

        return isGranted
    }
}