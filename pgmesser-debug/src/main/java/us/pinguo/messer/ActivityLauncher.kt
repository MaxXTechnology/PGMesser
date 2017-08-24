package us.pinguo.messer

import android.content.Context
import android.content.Intent
import us.pinguo.messer.db.DbActivity
import us.pinguo.messer.home.HomeActivity
import us.pinguo.messer.image.ImageBrowserActivity
import us.pinguo.messer.local.LocalFileBrowserActivity
import us.pinguo.messer.local.LocalFileReadActivity

/**
 * Created by mr on 2017/6/27.
 */
object ActivityLauncher {

    fun launchHome(context: Context) {
        val i = Intent(context, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun launchLocalFileBrowser(context: Context) {
        val i = Intent(context, LocalFileBrowserActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun launchDbBrowser(context: Context) {
        val i = Intent(context, DbActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun launchImageBrowser(context: Context) {
        val i = Intent(context, ImageBrowserActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    fun launchFileRead(context: Context, readPath: String) {
        val intent = Intent(context, LocalFileReadActivity::class.java)
        intent.putExtra("readPath", readPath)
        context.startActivity(intent)
    }
}