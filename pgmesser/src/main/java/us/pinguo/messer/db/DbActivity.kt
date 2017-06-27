package us.pinguo.messer.db

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wisdom.cy.mykotlin.DbAdapter
import kotlinx.android.synthetic.main.db_layout.*
import us.pinguo.messer.R

class DbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_layout);

        val fm: FragmentManager = supportFragmentManager

        var tableFragment : DbTableFragment = DbTableFragment();
        fm.beginTransaction().add(R.id.content, tableFragment).commit()

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





