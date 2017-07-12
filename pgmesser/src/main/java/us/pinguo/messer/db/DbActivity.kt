package us.pinguo.messer.db

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.wisdom.cy.mykotlin.DbAdapter
import kotlinx.android.synthetic.main.db_layout.*
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select
import us.pinguo.messer.R
import android.os.Environment.getExternalStorageDirectory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class DbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_layout);

        val fm: FragmentManager = supportFragmentManager

        var tableFragment : DbTableFragment = DbTableFragment("/data/data/us.pinguo.messer.demo/databases/puzzle.db");
        fm.beginTransaction().add(R.id.content, tableFragment).commit()


//
//        var outFile : File = getDatabasePath("puzzle.db")
//
//        val inFile : File = File(Environment.getExternalStorageDirectory().getPath() + "/puzzle.db")
//
//        try {
//            val fis = FileInputStream(inFile)
//            val fos = FileOutputStream(outFile)
//
//            var len = 0
//            var buffer = ByteArray(1024)
//
//            do {
//                len = fis.read(buffer)
//                if (len == -1) {
//                    break;
//                }
//                fos.write(buffer, 0, len)
//
//            } while (true)
//
//            fis.close()
//            fos.close()
//
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


//        database.use {
//            opendatab
//            val list = select("table").parseList { Table(java.util.HashMap(it)) }
//        } context.dat\


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





