package us.pinguo.messer.local

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_local_file_read.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import us.pinguo.messer.R
import java.io.File

/**
 * Created by hedongjin on 2017/7/11.
 */
class LocalFileReadActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, readPath: String) {
            val intent = Intent(context, LocalFileReadActivity::class.java)
            intent.putExtra("readPath", readPath)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_local_file_read)

        initTitleBar()
        showFileContent()
    }

    fun initTitleBar() {
        val readPath = intent.getStringExtra("readPath")
        toolbar.title = File(readPath).name
        toolbar.setNavigationIcon(R.drawable.back)

        setSupportActionBar(toolbar)
        supportActionBar?.let { it.setDisplayHomeAsUpEnabled(true) }

        toolbar.setNavigationOnClickListener { finish() }
    }

    fun showFileContent() {
        doAsync {
            val readPath = intent.getStringExtra("readPath")
            val readText = File(readPath).bufferedReader().readText()

            uiThread {
                read_text.text = readText
            }
        }
    }

}