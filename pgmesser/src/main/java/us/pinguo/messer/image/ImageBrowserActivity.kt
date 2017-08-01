package us.pinguo.messer.image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nostra13.universalimageloader.core.download.ImageDownloader
import kotlinx.android.synthetic.main.act_image_browser.*
import us.pinguo.messer.R

/**
 * Created by mr on 2017/6/27.
 */
class ImageBrowserActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, readPath: String) {
            val intent = Intent(context, ImageBrowserActivity::class.java)
            intent.putExtra("readPath", readPath)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_image_browser)
        val readPath = intent.getStringExtra("readPath")
        image_loader_view.setImageUrl(ImageDownloader.Scheme.FILE.wrap(readPath))
    }


}