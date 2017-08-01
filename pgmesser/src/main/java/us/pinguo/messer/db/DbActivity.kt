package us.pinguo.messer.db

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import us.pinguo.messer.R


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





