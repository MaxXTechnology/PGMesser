package com.wisdom.cy.mykotlin

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
class DbTableDetailAdapter() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = parent.ctx.layoutInflater.inflate(R.layout.item_list_main, parent, false)
        var view = parent.context.layoutInflater.inflate(R.layout.db_table_detail_item_layout, parent, false)

        return object:RecyclerView.ViewHolder(view) {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.textView.text = "" + items[position].main.temp_min;
        if (position == 0) {
            holder.itemView.name.text = "Id      Name      Age      Phone"
        } else {
            holder.itemView.name.text = "xx      xxx        xxx       xxxxx"
        }
    }

    override fun getItemCount() = 2


}