package us.pinguo.messer.local

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.act_local_file_browser.*
import org.jetbrains.anko.find
import us.pinguo.messer.DebugMesser
import us.pinguo.messer.R
import us.pinguo.messer.db.DbActivity
import us.pinguo.messer.image.ImageBrowserActivity
import us.pinguo.messer.util.AppUtils
import java.io.File

/**
 * 内置文件浏览
 * Created by tsmile on 2017/6/27.
 */
class LocalFileBrowserActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    companion object {
        val ROOT_FILE_TAG = "ROOT_FILE_TAG"
    }

    private val mRootFileList: ArrayList<File> = arrayListOf()
    private val mAdapter: PathListAdapter by lazy {
        PathListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_local_file_browser)
        initTitleBar()
        last_level_layout.setOnClickListener {
            if (path_list.tag is File) {
                if (isRootDir(path_list.tag as File)) {
                    updateRootList()
                } else {
                    var parentDir = path_list.tag as File
                    parentDir = parentDir.parentFile
                    updatePathList(parentDir)
                }
            }
        }
        path_list.adapter = mAdapter
        path_list.onItemClickListener = this

        mRootFileList.add(this.filesDir.parentFile)
        // 有给我们设置sd卡根路径，且授权了sd卡权限的情况下显示
        if (!TextUtils.isEmpty(DebugMesser.appSdRoot)
                && AppUtils.checkDangerousPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mRootFileList.add(File(DebugMesser.appSdRoot))
        }
        updateRootList()
    }

    fun initTitleBar() {
        toolbar.setTitle(R.string.file_browser)
        toolbar.setNavigationIcon(R.drawable.back)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun isRootDir(file: File?): Boolean {
        return file != null && file in mRootFileList
    }

    fun updateRootList() {
        current_path.setText(R.string.root_path)
        val data = ArrayList<PathData>()
        mRootFileList.mapTo(data) { PathData(it.name, it) }
        mAdapter.setList(data)
        path_list.tag = ROOT_FILE_TAG
        last_level_layout.visibility = View.GONE
    }

    fun updatePathList(dir: File) {
        current_path.text = dir.absolutePath
        val files = dir.listFiles()
        val data = ArrayList<PathData>()
        last_level_layout.visibility = View.VISIBLE

        files.sortBy {
            if (it.isDirectory) 0
            else if (isDBFile(it)) 1
            else if (isImageFile(it)) 2
            else 3
        }

        files.mapTo(data) { PathData(it.name, it) }
        mAdapter.setList(data)
        path_list.tag = dir
    }

    override fun onBackPressed() {
        if (path_list.tag is File) {
            if (isRootDir(path_list.tag as File)) {
                updateRootList()
            } else {
                var parentDir = path_list.tag as File
                parentDir = parentDir.parentFile
                updatePathList(parentDir)
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val path = mAdapter.getItem(position)!!.path
        if (path.isDirectory) {
            updatePathList(mAdapter.getItem(position)!!.path)
        } else {
            if (isImageFile(path)) {
                ImageBrowserActivity.launch(this, path.absolutePath)
            } else if (isDBFile(path)) {
                DbActivity.launch(this, path.absolutePath)
            } else {
                LocalFileReadActivity.launch(this, path.absolutePath)
            }
        }
    }

    private fun isDBFile(file: File) = file.name.endsWith("db")

    private fun isImageFile(file: File) = file.name.endsWith("png") || file.name.endsWith("jpg")


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
            } else if (isDBFile(pathData.path)) {
                holder.icon!!.setImageResource(R.drawable.ic_db)
            } else if (isImageFile(pathData.path)) {
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