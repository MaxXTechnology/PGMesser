package us.pinguo.messer.db

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wisdom.cy.mykotlin.DbAdapter
import com.wisdom.cy.mykotlin.DbTableDetailAdapter
import kotlinx.android.synthetic.main.db_table_detail_layout.view.*
import kotlinx.android.synthetic.main.db_table_layout.view.*
import us.pinguo.messer.R
import java.util.*

/**
 * Created by pinguo on 2017/6/27.
 */

@SuppressLint("ValidFragment")
class DbTableDetailFragment(val tableName : String, val nameList : ArrayList<String>, val resList : List<Map<String, Any?>>) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.db_table_detail_layout, container, false)

        initTitleBar(view)

        initDetailList(view)

        return view;
    }

    private fun initTitleBar(view: View) {
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle(tableName)
        toolbar.setNavigationIcon(R.drawable.back)
        toolbar.setNavigationOnClickListener {
            activity.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    private fun initDetailList(view: View) {

        val recyclerview = view.findViewById(R.id.recyclerview) as RecyclerView
        var layoutParams : ViewGroup.LayoutParams = recyclerview.layoutParams
        layoutParams.width = nameList.size * resources.getDimensionPixelSize(R.dimen.detail_item_width)
        recyclerview.layoutParams = layoutParams


        if (nameList.size != 0) {
            recyclerview.layoutManager = GridLayoutManager(context, nameList.size)
            recyclerview.adapter = DbTableDetailAdapter(context, nameList, resList)
        }
    }
}