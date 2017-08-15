package us.pinguo.messer.local

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.act_local_file_browser.*
import org.jetbrains.anko.find
import us.pinguo.messer.R
import us.pinguo.messer.db.DbActivity
import us.pinguo.messer.image.ImageBrowserActivity
import java.io.File

/**
 * 内置文件浏览
 * Created by tsmile on 2017/6/27.
 */
class LocalFileBrowserActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var mSelectFile: File? = null
    private var mRootFile: File? = null
    private val mAdapter: PathListAdapter by lazy {
        PathListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_local_file_browser)
        initTitleBar()
        last_level_layout.setOnClickListener {
            if (path_list.tag is File) {
                var parentDir = path_list.tag as File
                parentDir = parentDir.parentFile
                updatePathList(parentDir)
            }
        }
        path_list.adapter = mAdapter
        path_list.onItemClickListener = this
        mRootFile = this.filesDir.parentFile
        mSelectFile = mRootFile
        updatePathList(mSelectFile!!)
    }

    fun initTitleBar() {
        toolbar.setTitle(R.string.file_browser)
        toolbar.setNavigationIcon(R.drawable.back)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun isRootDir(dir: File?): Boolean {
        return dir != null && dir.absolutePath.equals(mRootFile!!.absolutePath, ignoreCase = true)
    }

    fun updatePathList(dir: File) {
        current_path.text = dir.absolutePath
        val files = dir.listFiles()
        val data = ArrayList<PathData>()
        last_level_layout.visibility = if (isRootDir(dir)) View.GONE else View.VISIBLE
        files.sortWith(Comparator { o1, o2 ->
            if (o1.isDirectory && o2.isFile) {
                return@Comparator -1
            } else if (o1.isFile && o2.isDirectory) {
                return@Comparator 1
            } else {
                return@Comparator o1.name.compareTo(o2.name)
            }
        })
        files.mapTo(data) { PathData(it.name, it) }
        mAdapter.setList(data)
        path_list.tag = dir
    }

    override fun onBackPressed() {
        if (path_list.tag is File && mRootFile!!.absolutePath != (path_list.tag as File).absolutePath) {
            var parentDir = path_list.tag as File
            parentDir = parentDir.parentFile
            updatePathList(parentDir)
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val path = mAdapter.getItem(position)!!.path
        if (path.isDirectory) {
            updatePathList(mAdapter.getItem(position)!!.path)
        } else {
            val fileName = path.name

            if (fileName.endsWith("png") || fileName.endsWith("jpg")) {
                ImageBrowserActivity.launch(this, path.absolutePath)
            } else if (fileName.endsWith("db")) {
                DbActivity.launch(this, path.absolutePath)
            } else {
                LocalFileReadActivity.launch(this, path.absolutePath)
            }
        }
    }

    private data class PathData(val name: String, val path: File)

    private inner class PathListAdapter : BaseAdapter() {

        val mData: ArrayList<PathData> by lazy {
            ArrayList<PathData>()
        }

        fun setList(data: ArrayList<PathData>?) {
            if (data == null) {
                return
            }

            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val itemView: View
            if (convertView == null) {
                itemView = layoutInflater.inflate(R.layout.adapter_path_item, parent, false)
                holder = ViewHolder()
                holder.title = itemView.find(R.id.tv_options_save_path)
                holder.icon = itemView.find(R.id.icon)
                itemView.tag = holder
            } else {
                itemView = convertView
                holder = itemView.tag as ViewHolder
            }
            val pathData = getItem(position)!!
            holder.title!!.text = pathData.name
            if (pathData.path.isDirectory) {
                holder.icon!!.setImageResource(R.drawable.ic_file)
            } else if (pathData.path.name.endsWith("db")) {
                holder.icon!!.setImageResource(R.drawable.ic_db)
            } else if (pathData.path.name.endsWith("png") || pathData.path.endsWith("jpg")) {
                holder.icon!!.setImageResource(R.drawable.ic_image)
            } else {
                holder.icon!!.setImageResource(R.drawable.ic_txt)
            }
            return itemView
        }

        override fun getItem(position: Int): PathData? {
            if (position < mData.size) {
                return mData[position]
            } else {
                return null
            }
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return mData.size
        }

    }

    private inner class ViewHolder {
        var title: TextView? = null
        var icon: ImageView? = null
    }
}