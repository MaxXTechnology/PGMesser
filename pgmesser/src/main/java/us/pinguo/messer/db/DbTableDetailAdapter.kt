package com.wisdom.cy.mykotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.db_layout.view.*
import kotlinx.android.synthetic.main.db_table_item_layout.view.*
import kotlinx.android.synthetic.main.db_table_layout.view.*
import org.jetbrains.anko.layoutInflater
import us.pinguo.messer.R

/**
 * Created by pinguo on 2017/6/20.
 */
class DbTableDetailAdapter(val context : Context, val nameList : ArrayList<String>, val resList : List<Map<String, Any?>>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return object:RecyclerView.ViewHolder(TextView(context)) {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.textView.text = "" + items[position].main.temp_min;
        var textView : TextView = holder.itemView as TextView;
        if (position < nameList.size) {

            textView.setText("  " + nameList[position])

        } else {
            var namePosition = position % nameList.size
            var listPosition = position / nameList.size - 1;

            textView.setText("  " + resList[listPosition][nameList[namePosition]])
        }
    }

    override fun getItemCount() = resList.size * nameList.size


}