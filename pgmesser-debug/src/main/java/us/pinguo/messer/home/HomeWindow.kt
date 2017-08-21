package us.pinguo.messer.home

import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.window_home.view.*
import org.jetbrains.anko.onClick
import us.pinguo.messer.R
import us.pinguo.messer.util.AppUtils
import us.pinguo.messer.analysis.MainThreadWatchDog
import us.pinguo.messer.util.UIUtils


/**
 * Created by hedongjin on 2017/6/26.
 */
open class HomeWindow(context: Context, val navigation: HomeMvpContract.IInnerNavigation) : AbstractWindow(context), HomeMvpContract.IHomeView {

    companion object {
        val LOG_LEVELS = listOf("Verbose", "Debug", "Info", "Warn", "Error")
    }

    private lateinit var mRootView: View
    private lateinit var mPresenter: HomeMvpContract.IHomePresenter
    private val mLayoutParams by lazy {
        val params: WindowManager.LayoutParams = WindowManager.LayoutParams()
        params.type = WindowManager.LayoutParams.TYPE_PHONE
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.format = PixelFormat.RGBA_8888
        params.gravity = Gravity.LEFT or Gravity.TOP
        params.width = UIUtils.dp2px(204f)
        params.height = UIUtils.dp2px(222f)
        params
    }

    override fun onCreate(): View {
        mRootView = LayoutInflater.from(context).inflate(R.layout.window_home, null, false)

        mRootView.home_close.setOnClickListener {
            navigation.closeHome()
        }

        mRootView.home_folder.setOnClickListener {
            mPresenter.gotoFolderPage()
        }

        mRootView.home_cpu.setOnClickListener {
            mRootView.home_cpu.isSelected = !mRootView.home_cpu.isSelected
            if (mRootView.home_cpu.isSelected) {
                MainThreadWatchDog.defaultInstance().startWatch()
                writeContent(context.resources.getString(R.string.home_cpu_start))
            } else {
                writeContent(MainThreadWatchDog.defaultInstance().stopWatch())
                writeContent(context.resources.getString(R.string.home_cpu_end))
            }
        }

        mRootView.home_memory.setOnClickListener {
            mRootView.home_memory.isSelected = !mRootView.home_memory.isSelected
            mPresenter.watchMemory(mRootView.home_memory.isSelected)

            if (mRootView.home_memory.isSelected) {
                writeContent(context.resources.getString(R.string.home_memory_start))
            } else {
                writeContent(context.resources.getString(R.string.home_memory_end))
            }
        }

        mRootView.home_more.setOnClickListener {

            writeContent(context.resources.getString(R.string.home_clear_start))

            if (AppUtils.clearAppData(context))
                writeContent(context.resources.getString(R.string.home_clear_success))
            else
                writeContent(context.resources.getString(R.string.home_clear_fail))
        }

        val detector = WindowGestureDetector(object : WindowGestureDetector.GestureDetectorListener {
            override fun onScroll(dx: Int, dy: Int) {
                WindowCompat.updateWindowLayout(context, this@HomeWindow, dx, dy)
            }

        })
        mRootView.setOnTouchListener { v, e ->
            detector.onTouchEvent(e)
        }

        val logLevelAdapter = LogLevelAdapter()
        mRootView.log_level_spinner.adapter = logLevelAdapter
        mRootView.log_level_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }
        for (i in 0..100) {
            mRootView.home_content.append("Logs will print here\n")
        }

        mRootView.clear_log.onClick { mRootView.home_content.text = "" }
        return mRootView
    }

    private class LogLevelAdapter : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val textView: TextView = View.inflate(parent?.context, R.layout.view_log_level,
                    null) as TextView
            textView.text = getItem(position)
            return textView
        }

        override fun getItem(position: Int): String {
            return LOG_LEVELS[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return LOG_LEVELS.count()
        }

    }

    override fun getLayoutParams() = mLayoutParams

    override fun setPresenter(presenter: HomeMvpContract.IHomePresenter) {
        mPresenter = presenter
    }

    override fun writeContent(content: String, append: Boolean) {
        mRootView.home_content.setText(content, append)
    }

    private fun TextView.setText(content: String, append: Boolean) {
        if (!append) {
            text = content + "\n"
        } else {
            text = StringBuffer().append(text).append(content).append("\n").toString()
        }
    }


}
