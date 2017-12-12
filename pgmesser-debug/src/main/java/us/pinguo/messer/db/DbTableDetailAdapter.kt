package us.pinguo.messer.db

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import us.pinguo.messer.R
import java.util.*

/**
 * Created by pinguo on 2017/6/20.
 */
class DbTableDetailAdapter(val context: Context?, val nameList: ArrayList<String>, val resList: List<Map<String, Any?>>, val clickListener: View.OnClickListener) :
        RecyclerView.Adapter<DbTableDetailAdapter.DetailViewHolder>() {

    var grayColor: Int = Color.parseColor("#f9f9f9")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {


        var view: View = LayoutInflater.from(context).inflate(R.layout.db_table_detail_item_layout, parent, false);
        view.setOnClickListener(clickListener)
        return object : DetailViewHolder(view) {

        }
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        var textView: TextView = holder.name;


        if (position < nameList.size) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

            textView.setText(nameList[position])
            holder.itemView.tag = null;

        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

            var namePosition = position % nameList.size
            var listPosition = position / nameList.size - 1

            var linePosition = position / nameList.size
            if (linePosition % 2 == 1) {
                holder.itemView.setBackgroundColor(grayColor)
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }

            textView.text = "" + resList[listPosition][nameList[namePosition]]

            holder.itemView.tag = textView.text;
            holder.itemView.setTag(R.id.name_position, namePosition)
            holder.itemView.setTag(R.id.list_position, listPosition)
        }
    }

    override fun getItemCount() = resList.size * nameList.size

    open class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var name: TextView = view.findViewById(R.id.name)

    }
}