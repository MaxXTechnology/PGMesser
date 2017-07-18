package us.pinguo.messer.demo;

import us.pinguo.common.db.DbDefinition;
import us.pinguo.common.db.DbTableDefinition;
import us.pinguo.common.db.DbTableGenerator;

public class BroadcastDataDb {

    static final String DB_NAME = "broadcast.db";
    public static final int DB_VERSION = 1;

    // gift data Table
    public static final DbTableDefinition LIVE_GIFT_DATA_TABLE = DbTableGenerator.generate(LiveGiftDataInfo.class);

    // gift table
    public static final DbTableDefinition LIVE_GIFT_TABLE = DbTableGenerator.generate(LiveGiftInfo.class);
    public static final DbTableDefinition LIVE_GIFT_DOWNLOAD_TABLE = DbTableGenerator.generate(LiveGiftDownloadInfo.class);

    public static final DbDefinition PRODUCT_DB = new DbDefinition.Builder()
            .name(DB_NAME)
            .version(DB_VERSION)
            .table(LIVE_GIFT_DATA_TABLE)
            .table(LIVE_GIFT_TABLE)
            .table(LIVE_GIFT_DOWNLOAD_TABLE)
            .build();
}
