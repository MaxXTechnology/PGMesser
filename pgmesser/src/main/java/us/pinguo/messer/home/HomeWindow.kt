package us.pinguo.messer.home

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.window_home.view.*
import us.pinguo.messer.ActivityLauncher
import us.pinguo.messer.R
import us.pinguo.messer.util.UIUtils


/**
 * Created by hedongjin on 2017/6/26.
 */
open class HomeWindow(context: Context, val navigation: HomeMvpContract.IInnerNavigation) : AbstractWindow(context), HomeMvpContract.IHomeView {
    private lateinit var mRootView: View
    private lateinit var mPresenter: HomeMvpContract.IHomePresenter

    override fun onCreate(): View {
        mRootView = LayoutInflater.from(context).inflate(R.layout.window_home, null, false)

        mRootView.home_close.setOnClickListener {
            navigation.closeHome()
        }

        mRootView.home_folder.setOnClickListener {
            mPresenter.gotoFolderPage()
            ActivityLauncher.launchLocalFileBrowser(context)
            navigation.closeHome()
        }

        mRootView.home_cpu.setOnClickListener {
            mRootView.home_cpu.isSelected = !mRootView.home_cpu.isSelected
            mPresenter.watchCpu(mRootView.home_cpu.isSelected)
            //TODO-remove test code
            ActivityLauncher.launchDbBrowser(context)
            navigation.closeHome()
        }

        mRootView.home_memory.setOnClickListener {
            mRootView.home_memory.isSelected = !mRootView.home_memory.isSelected
            mPresenter.watchMemory(mRootView.home_memory.isSelected)
            //TODO-remove test code
            ActivityLauncher.launchImageBrowser(context)
            navigation.closeHome()
        }

        return mRootView
    }

    override fun getLayoutParams(): WindowManager.LayoutParams {
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.width = UIUtils.dp2px(204f)
        layoutParams.height = UIUtils.dp2px(222f)
        return layoutParams
    }

    override fun setPresenter(presenter: HomeMvpContract.IHomePresenter) {
        mPresenter = presenter
    }

    override fun writeContent(content: String, append: Boolean) {
        mRootView.home_content.setText(content, append)
    }

    private fun TextView.setText(content: String, append: Boolean) {
        if (!append) {
            setText(content)
        } else {
            setText(StringBuffer().append(text).append(content).toString())
        }
    }


}
