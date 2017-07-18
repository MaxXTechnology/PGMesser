package us.pinguo.messer.demo;

import us.pinguo.common.db.annotation.DbColumn;
import us.pinguo.common.db.annotation.DbPrimaryKey;
import us.pinguo.common.db.annotation.DbTable;

@DbTable(tableName = "live_gift_download")
public class LiveGiftDownloadInfo {
    @DbColumn
    @DbPrimaryKey(autoincrement = false)
    public String gift_id;// 礼物自身数据id

    @DbColumn
    public Boolean down_suc;// 是否下载成功

    public LiveGiftDownloadInfo() {
    }

    public LiveGiftDownloadInfo(String _id, Boolean down_suc) {
        this.gift_id = _id;
        this.down_suc = down_suc;
    }
}
