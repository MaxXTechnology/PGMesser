package us.pinguo.live.commontop.livedb

import android.content.Context
import us.pinguo.common.db.DbDataBase
import us.pinguo.common.db.DbTemplateTableModel
import us.pinguo.messer.demo.BroadcastDataDb
import us.pinguo.messer.demo.LiveGiftDataInfo
import us.pinguo.messer.demo.LiveGiftDownloadInfo
import us.pinguo.messer.demo.LiveGiftInfo
import java.util.*

class BroadcastDataDbHelper private constructor(context: Context) {

    private val mLiveGiftDataTable: DbTemplateTableModel<LiveGiftDataInfo>
    private val mLiveGiftTable: DbTemplateTableModel<LiveGiftInfo>
    private val mLiveGiftDownloadTable: DbTemplateTableModel<LiveGiftDownloadInfo>
    private var db: DbDataBase? = null

    init {
        db = DbDataBase(context, BroadcastDataDb.PRODUCT_DB)
        try {
            db!!.init()
        } catch (e: Exception) {
            //ignore
        }

        mLiveGiftDataTable = DbTemplateTableModel(BroadcastDataDb.LIVE_GIFT_DATA_TABLE,
                db, LiveGiftDataInfo::class.java)
        mLiveGiftTable = DbTemplateTableModel(BroadcastDataDb.LIVE_GIFT_TABLE,
                db, LiveGiftInfo::class.java)
        mLiveGiftDownloadTable = DbTemplateTableModel(BroadcastDataDb.LIVE_GIFT_DOWNLOAD_TABLE,
                db, LiveGiftDownloadInfo::class.java)
    }

    /**
     * insert gift data

     * @param dataInfo *
     * *
     * @param giftInfo *
     * *
     * @return
     */
    fun insertGiftData(dataInfo: LiveGiftDataInfo, giftInfo: List<LiveGiftInfo>): Boolean {
        try {
            db!!.beginTransactionLocked()
            if (isNull(dataInfo) || isNull(giftInfo)) return false
            val dataInfos = ArrayList<LiveGiftDataInfo>()
            dataInfos.add(dataInfo)
            var resultList = mLiveGiftDataTable.clearThenBulkInsert(dataInfos)
            resultList
                    .filter { it < 0 }
                    .forEach { return false }
            resultList = mLiveGiftTable.clearThenBulkInsert(giftInfo)
            resultList
                    .filter { it < 0 }
                    .forEach { return false }
            db!!.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db!!.endTransactionUnlocked()
        }
        return true
    }

    /**
     * 更新礼物下载信息
     */
    fun updateGiftDownloadInfo(giftId: String): Boolean {
        var downloadInfo: LiveGiftDownloadInfo = LiveGiftDownloadInfo(giftId, true)
        var result = mLiveGiftDownloadTable.insert(downloadInfo)
        return result > 0
    }

    /**
     * 获取礼物下载信息Id列表
     */
    fun getAllGiftDownloadInfo(): List<String> {
        val data = mLiveGiftDownloadTable.get(null, null, null)
        return data.map {
            it.gift_id
        }
    }

    /**
     * 获取所有礼物信息
     */
    fun getAllGiftInfo(): List<LiveGiftInfo> {
        val data = mLiveGiftTable.get(null, null, null)
        return data
    }

    /**
     * 获取当前礼物数据的版本号
     */
    fun getCurGiftDataVersion(): Int {
        val data = mLiveGiftDataTable.get(null, null, null)
        if (data.size > 0) {
            return data[0].dataVersion
        }
        return 0
    }

    private fun isNull(o: Any?): Boolean {
        return o == null
    }

    companion object {
        private var sInstance: BroadcastDataDbHelper? = null

        fun getInstance(appContext: Context): BroadcastDataDbHelper {
            if (sInstance == null) {
                synchronized(BroadcastDataDbHelper::class.java) {
                    if (sInstance == null) {
                        sInstance = BroadcastDataDbHelper(appContext)
                    }
                }
            }
            return sInstance!!
        }
    }
}
