package us.pinguo.messer

import android.content.Context
import android.content.Intent
import us.pinguo.messer.db.DbActivity
import us.pinguo.messer.image.ImageBrowserActivity
import us.pinguo.messer.local.LocalFileBrowserActivity

/**
 * Created by mr on 2017/6/27.
 */
object ActivityLauncher {

    fun launchLocalFileBrowser(context: Context) {
        val i = Intent(context, LocalFileBrowserActivity::class.java)
        context.startActivity(i)
    }

    fun launchDbBrowser(context: Context) {
        val i = Intent(context, DbActivity::class.java)
        context.startActivity(i)
    }

    fun launchImageBrowser(context: Context) {
        val i = Intent(context, ImageBrowserActivity::class.java)
        context.startActivity(i)
    }
}