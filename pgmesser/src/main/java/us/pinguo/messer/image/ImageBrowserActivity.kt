package us.pinguo.messer.image

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_image_browser.*
import us.pinguo.messer.R

/**
 * Created by mr on 2017/6/27.
 */
class ImageBrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_image_browser)
        val path = "assets://home_folder.png"
        image_loader_view.setImageUrl(path)
    }


}