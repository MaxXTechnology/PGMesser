package us.pinguo.messer.demo;

import us.pinguo.common.db.annotation.DbColumn;
import us.pinguo.common.db.annotation.DbPrimaryKey;
import us.pinguo.common.db.annotation.DbTable;

@DbTable(tableName = "live_gift_data")
public class LiveGiftDataInfo {

    @DbColumn
    @DbPrimaryKey
    public int _id = -1;

    @DbColumn
    public String id;

    @DbColumn
    public Long ctime;

    @DbColumn
    public String crc32;

    @DbColumn
    public int dataVersion;

    @DbColumn
    public int gift_count;

    public String getId() {
        return id;
    }

    public Long getCtime() {
        return ctime;
    }

    public String getCrc32() {
        return crc32;
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public Integer getGift_count() {
        return gift_count;
    }


    public LiveGiftDataInfo() {
    }

    public LiveGiftDataInfo(String id, Long ctime, String crc32, Integer dataVersion, Integer count) {
        this.id = id;
        this.ctime = ctime;
        this.crc32 = crc32;
        this.dataVersion = dataVersion;
        this.gift_count = gift_count;
    }


}
