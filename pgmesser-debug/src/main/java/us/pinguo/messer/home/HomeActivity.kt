package us.pinguo.messer.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import us.pinguo.messer.ActivityLauncher
import us.pinguo.messer.R
import us.pinguo.messer.analysis.MesserLeakCanary

/**
 * Created by hedongjin on 2017/8/24.
 */
class HomeActivity : Activity() {

    companion object {
        val OVERLAY_PERMISSION_REG_CODE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            gotoShortcut()
        } else {
            gotoSystemPermission()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == OVERLAY_PERMISSION_REG_CODE) {
            if (Settings.canDrawOverlays(this)) {
                gotoShortcut()
            } else {
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun gotoSystemPermission() {
        AlertDialog.Builder(this)
                .setTitle(R.string.permission_title)
                .setMessage(R.string.permission_desc)
                .setPositiveButton(R.string.permission_positive) { dialog, which ->

                    startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + packageName)), OVERLAY_PERMISSION_REG_CODE)
                    dialog.dismiss()

                }
                .setNegativeButton(R.string.permission_negative) { dialog, which ->

                    dialog.dismiss()
                    finish()

                }
                .create()
                .show()

    }

    fun gotoShortcut() {
        MesserWindowManager.getInstance().init(application, object : HomeMvpContract.IHomeNavigation {
            override fun gotoFolderPage() {
                ActivityLauncher.launchLocalFileBrowser(this@HomeActivity)
            }

            override fun watchMemory(isStart: Boolean) {
                MesserLeakCanary.setWatchEnable(isStart)
            }
        })
        MesserWindowManager.getInstance().gotoShortcut()

        finish()
    }


}