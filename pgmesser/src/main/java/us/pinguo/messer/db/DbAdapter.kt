package com.wisdom.cy.mykotlin

import android.support.v7.widget.RecyclerView
import android.view.View.inflate
import android.view.ViewGroup
import kotlinx.android.synthetic.main.db_layout.view.*
import kotlinx.android.synthetic.main.db_table_layout.view.*
import org.jetbrains.anko.layoutInflater
import us.pinguo.messer.R

/**
 * Created by pinguo on 2017/6/20.
 */
class DbAdapter(val items: List<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = parent.ctx.layoutInflater.inflate(R.layout.item_list_main, parent, false)
        var view = parent.context.layoutInflater.inflate(R.layout.db_table_layout, parent, false)

        return object:RecyclerView.ViewHolder(view) {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.textView.text = "" + items[position].main.temp_min;
            holder.itemView.content.text = items[position]
    }

    override fun getItemCount() = items.size


}