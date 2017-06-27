package us.pinguo.messer.db

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wisdom.cy.mykotlin.DbAdapter
import com.wisdom.cy.mykotlin.DbTableDetailAdapter
import kotlinx.android.synthetic.main.db_table_layout.view.*
import us.pinguo.messer.R


/**
 * Created by pinguo on 2017/6/27.
 */
class DbTableDetailFragment(val tableName : String) : Fragment() {





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.db_table_layout, container, false)
        view.title.text = tableName

//        var list = listOf<String>("table1", "table2")
//
        view.recyclerview.layoutManager = LinearLayoutManager(context)
        view.recyclerview.adapter = DbTableDetailAdapter()
//
        view.back.setOnClickListener{
            activity.supportFragmentManager.beginTransaction().remove(this).commit();
        }

        return view;
    }
}