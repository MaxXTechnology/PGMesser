package us.pinguo.messer.db

import android.content.ContentValues
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.db_table_detail_layout.*
import kotlinx.android.synthetic.main.db_table_detail_layout.view.*
import org.jetbrains.anko.doAsync
import us.pinguo.messer.R
import us.pinguo.messer.util.UIUtils
import java.util.*


/**
 * Created by pinguo on 2017/6/27.
 */
class DbTableDetailFragment(val tableName: String, val nameList: ArrayList<String>, val resList: List<Map<String, Any?>>, var dbName: String, var name: String) : Fragment() {

    var mNamePosition: Int = -1
    var mListPosition: Int = -1
    var oldText: String = ""
    var mContentView: View? = null

    var mKeyboardHeight: Int = -1
    var mPreviousDisplayHeight: Int = -1

    var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val displayHeight: Int = getDisplayFrameHeight(mContentView)
        val keyboardHeight = getSystemKeyboardHeight(mContentView)


        if (mPreviousDisplayHeight !== displayHeight && (keyboardHeight != 0 || mKeyboardHeight !== -1)) {
            mPreviousDisplayHeight = displayHeight
            // 更新软件盘的大小
            if (mKeyboardHeight < keyboardHeight) {
                mKeyboardHeight = keyboardHeight
            }

            var key_height: Int = resources.getDimensionPixelSize(R.dimen.keyboard_height_limit)
            if (keyboardHeight > key_height) {

            } else {

                edit_text_layout.visibility = View.GONE
            }
        }
    }


    var mItemClickListener: View.OnClickListener = View.OnClickListener {

        if (it.tag != null) {
            mNamePosition = it.getTag(R.id.name_position) as Int
            mListPosition = it.getTag(R.id.list_position) as Int

            edit_text_layout.visibility = View.VISIBLE
            oldText = it.tag.toString()
            edit_text.setText(oldText)
            edit_text.setFocusable(true);
            edit_text.setFocusableInTouchMode(true);
            edit_text.requestFocus();

            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(edit_text, InputMethodManager.SHOW_FORCED)
        }
    }

    private var mDoneClickListener: View.OnClickListener = View.OnClickListener {
        (resList[mListPosition] as HashMap).put(nameList[mNamePosition], edit_text.text)
        recyclerview.adapter.notifyDataSetChanged()
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(edit_text.windowToken, 0)
        edit_text_layout.visibility = View.GONE
        doAsync {
            activity?.let {
                val sqliteDatabase = it.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null)
                val values = ContentValues()
                values.put(nameList[mNamePosition], edit_text.text.toString())
                val arrayList: ArrayList<String> = ArrayList()
                var where = ""
                for ((k, v) in resList[mListPosition]) {
                    where += "$k = ? and "
                    if (k == nameList[mNamePosition]) {
                        arrayList.add(oldText)
                    } else {
                        if (v != null) {
                            arrayList.add(v.toString())
                        } else {
                            arrayList.add("null")
                        }

                    }
                }
                where += "1 = 1"
                println("where:" + where)
                sqliteDatabase.update(name, values, where, arrayList.toArray(arrayOfNulls<String>(nameList.size)))
            }
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.db_table_detail_layout, container, false)
        view.title.text = tableName

        view.button.setOnClickListener(mDoneClickListener)

        var layoutParams: ViewGroup.LayoutParams = view.recyclerview.layoutParams
        layoutParams.width = nameList.size * resources.getDimensionPixelSize(R.dimen.detail_item_width)
        view.recyclerview.layoutParams = layoutParams


        if (nameList.size != 0) {
            view.recyclerview.layoutManager = GridLayoutManager(context, nameList.size)
            view.recyclerview.adapter = DbTableDetailAdapter(context, nameList, resList, mItemClickListener)
        }

        view.back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


        return view;
    }

    override fun onPause() {
        super.onPause()

        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView!!.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mContentView = activity?.window?.decorView
        mContentView?.viewTreeObserver?.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    fun getDisplayFrameHeight(view: View?): Int {
        // 显示区域的大小，不包括键盘区域
        val displayRect = Rect()
        view!!.getWindowVisibleDisplayFrame(displayRect)
        return displayRect.height()
    }

    fun getSystemKeyboardHeight(view: View?): Int {

        // 显示区域的大小，不包括键盘区域
        val displayRect = Rect()
        view!!.getWindowVisibleDisplayFrame(displayRect)

        // 状态栏的高度
        val stateHeight = displayRect.top

        val screenHeight = UIUtils.getScreenHeight()

        val systemKeyboardHeight = screenHeight - stateHeight - displayRect.height()


        return systemKeyboardHeight
    }
}