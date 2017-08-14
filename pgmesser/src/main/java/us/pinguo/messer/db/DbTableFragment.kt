package us.pinguo.messer.db

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wisdom.cy.mykotlin.DbAdapter
import kotlinx.android.synthetic.main.db_table_layout.view.*
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import us.pinguo.messer.R
import java.util.*


/**
 * Created by pinguo on 2017/6/27.
 */
class DbTableFragment(var dbName : String) : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.db_table_layout, container, false)


        var sqliteDatabase =  activity.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);

        doAsync {

            var list = sqliteDatabase.select("sqlite_master").parseList(object : MapRowParser<String> {
                override fun parseRow(columns: Map<String, Any?>): String {

                    var name = columns["name"] as String
                    return name
                }
            })

            uiThread {
                if (!activity.isFinishing) {
                    if (list == null) {
                        list = ArrayList();
                    }

                    view.recyclerview.layoutManager = LinearLayoutManager(context)
                    view.recyclerview.adapter = DbAdapter(list, View.OnClickListener {

                        startDetail(it.getTag() as String)
                    })
                }


            }
        }




        view.back.setOnClickListener{
            activity.finish();
        }

        return view;
    }

    fun startDetail(name : String) {

        doAsync {

            var sqliteDatabase =  activity.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
            var list = sqliteDatabase.select(name).parseList(object : MapRowParser<Map<String, Any?>> {
                override fun parseRow(columns: Map<String, Any?>): Map<String, Any?> {

                    return columns
                }
            })

            uiThread {
                var nameList : ArrayList<String> = ArrayList()

                if (list == null) {
                    list = ArrayList();
                }

                if (list.size > 0) {
                    for (name in list.get(0).keys) {
                        nameList.add(name)
                    }
                }

                var fm: FragmentManager = activity.supportFragmentManager
                var tableDetailFragment : DbTableDetailFragment = DbTableDetailFragment(name, nameList, list)
                fm.beginTransaction().add(R.id.content, tableDetailFragment).addToBackStack("table_detail").commit()

            }

        }

    }
}