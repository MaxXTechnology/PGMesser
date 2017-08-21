package us.pinguo.messer.db

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import us.pinguo.messer.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class DbActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, readPath: String) {
            val intent = Intent(context, DbActivity::class.java)
            intent.putExtra("readPath", readPath)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_layout);

        val fm: FragmentManager = supportFragmentManager

        val readPath = intent.getStringExtra("readPath")

        var tableFragment: DbTableFragment = DbTableFragment(readPath)
        fm.beginTransaction().add(R.id.content, tableFragment).commit()




    }


}

data class Table(val map: MutableMap<String, Any?>) {

    var name: String by map

    constructor() : this(HashMap()) {
    }

    constructor(name: String) : this(HashMap()) {
        this.name = name
    }

}





