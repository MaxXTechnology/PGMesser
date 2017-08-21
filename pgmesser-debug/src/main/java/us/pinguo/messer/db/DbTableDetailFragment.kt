package us.pinguo.messer.db

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.wisdom.cy.mykotlin.DbAdapter
import com.wisdom.cy.mykotlin.DbTableDetailAdapter
import kotlinx.android.synthetic.main.db_table_detail_layout.*
import kotlinx.android.synthetic.main.db_table_detail_layout.view.*
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select
import us.pinguo.messer.R
import java.util.*
import android.content.ContentValues
import android.os.AsyncTask
import android.support.annotation.UiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.collections.ArrayList


/**
 * Created by pinguo on 2017/6/27.
 */
class DbTableDetailFragment(val tableName : String, val nameList : ArrayList<String>, val resList : List<Map<String, Any?>>, var dbName : String, var name : String) : Fragment() {

    var mNamePosition : Int = -1
    var mListPosition : Int = -1
    var oldText : String = ""


    var mItemClickListener : View.OnClickListener = View.OnClickListener {

        if (it.tag != null) {
            mNamePosition = it.getTag(R.id.name_position) as Int
            mListPosition = it.getTag(R.id.list_position) as Int

            edit_text_layout.visibility = View.VISIBLE
            oldText = it.tag.toString()
            edit_text.setText(oldText)
            edit_text.setFocusable(true);
            edit_text.setFocusableInTouchMode(true);
            edit_text.requestFocus();

            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit_text, InputMethodManager.SHOW_FORCED)
        }
    }

    var mDoneClickListener : View.OnClickListener = View.OnClickListener {

        (resList[mListPosition] as HashMap).put(nameList[mNamePosition], edit_text.text)
        recyclerview.adapter.notifyDataSetChanged();

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        edit_text_layout.visibility = View.GONE

        doAsync{

            var sqliteDatabase =  activity.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);

            val values = ContentValues()
            values.put(nameList[mNamePosition], edit_text.text.toString())


            var arrayList : ArrayList<String> = ArrayList()
            var where = ""
            for ((k,v) in resList[mListPosition]){
                where += "$k = ? and "
                if (k.equals(nameList[mNamePosition])) {

                    arrayList.add(oldText)
                } else {
                    if (v != null) {
                        arrayList.add(v.toString())
                    } else {
                        arrayList.add("null")
                    }

                }
//                println("value:" + whereArgs[index])
            }

            where += "1 = 1"
            println("where:" + where)

            sqliteDatabase.update(name, values, where, arrayList.toArray(arrayOfNulls<String>(nameList.size)))




        }





    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.db_table_detail_layout, container, false)
        view.title.text = tableName

        view.button.setOnClickListener(mDoneClickListener)

        var layoutParams : ViewGroup.LayoutParams = view.recyclerview.layoutParams
        layoutParams.width = nameList.size * resources.getDimensionPixelSize(R.dimen.detail_item_width)
        view.recyclerview.layoutParams = layoutParams


        if (nameList.size != 0) {
            view.recyclerview.layoutManager = GridLayoutManager(context, nameList.size)
            view.recyclerview.adapter = DbTableDetailAdapter(context, nameList, resList, mItemClickListener)
        }

        view.back.setOnClickListener{
            activity.supportFragmentManager.beginTransaction().remove(this).commit();
        }


        return view;
    }
}