package us.pinguo.messer.util

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

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
        if (Build.VERSION.SDK_INT < 23) {
            isGranted = true
        } else {
            isGranted = ContextCompat.checkSelfPermission(context, permission) == 0
        }

        return isGranted
    }

    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }

    fun isMainApplication(context: Context): Boolean {
        val pid = android.os.Process.myPid()
        val processName = getProcessName(pid)
        val packageName = context.packageName

        return packageName == processName

    }
}