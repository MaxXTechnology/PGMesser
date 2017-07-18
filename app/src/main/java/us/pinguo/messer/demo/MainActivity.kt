package us.pinguo.messer.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import us.pinguo.common.tinypref.TinyPref
import us.pinguo.live.commontop.livedb.BroadcastDataDbHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TinyPref.getInstance().putString("test1", "addasda")
        TinyPref.getInstance().putInt("testInt", 23213)

        val giftData = LiveGiftDataInfo()
        giftData.id = "ad"
        giftData.ctime = System.currentTimeMillis()
        val giftInfoList = arrayListOf<LiveGiftInfo>()
        val giftInfo = LiveGiftInfo()
        giftInfo.guid = System.currentTimeMillis().toString()
        giftInfoList.add(giftInfo)
        BroadcastDataDbHelper.getInstance(applicationContext).insertGiftData(giftData, giftInfoList)
    }
}
