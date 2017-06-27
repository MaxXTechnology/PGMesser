package us.pinguo.messer.db

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wisdom.cy.mykotlin.DbAdapter
import kotlinx.android.synthetic.main.db_layout.*
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.uiThread
import us.pinguo.messer.R
import java.net.URL
import java.util.*

class DbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_layout);

        var list = listOf<String>("table1", "table2")
//        recyclerView {
//            layoutManager = LinearLayoutManager(context)
//            adapter = DbAdapter(list);
//        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = DbAdapter(list)

        back.setOnClickListener{
            finish();
        }

//        database.use {
//            opendatab
//            val list = select("table").parseList { Table(java.util.HashMap(it)) }
//        }


    }

//    data class Table(val map: MutableMap<String, Any?>) {
//        var _id: Long by map
//        var name: String by map
//        var address: String by map
//
//        constructor() : this(HashMap()) {
//        }
//
//        constructor(id:Long,name: String,address:String) : this(HashMap()) {
//            this._id = id
//            this.name = name
//            this.address = address
//        }
//
//    }
}





