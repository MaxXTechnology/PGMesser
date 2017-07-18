package us.pinguo.messer.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import us.pinguo.common.tinypref.TinyPref

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TinyPref.getInstance().putString("test1", "addasda")
        TinyPref.getInstance().putInt("testInt", 23213)
    }
}
