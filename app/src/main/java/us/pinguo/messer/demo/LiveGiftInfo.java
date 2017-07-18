package us.pinguo.messer.demo;

import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import us.pinguo.common.db.annotation.DbColumn;
import us.pinguo.common.db.annotation.DbPrimaryKey;
import us.pinguo.common.db.annotation.DbTable;

@DbTable(tableName = "live_gift")
public class LiveGiftInfo {

    public static final int NORMAL_GIFT_TYPE = 1;
    public static final int COMBO_GIFT_TYPE = 2;
    private String DOWNLOAD_DIR = Environment.getExternalStorageDirectory().toString() + File.separator + "livegift" + File.separator;

    @DbColumn
    @DbPrimaryKey(autoincrement = false)
    public String guid;// 礼物自身数据id

    @DbColumn
    public String data_id;// 礼物归类数据id

    @DbColumn
    public int dataversion;

    @DbColumn
    public String uniq;

    @DbColumn
    public String name;

    @DbColumn
    public int type;

    @DbColumn
    public boolean cumulative;

    @DbColumn
    public String desc;
    @DbColumn
    public String icon;
    @DbColumn
    public String animation;
    @DbColumn
    public int priceType;
    @DbColumn
    public Float price;
    @DbColumn
    public int coldDown;

    @DbColumn
    public long startTime;

    @DbColumn
    public long endTime;

    @DbColumn
    public int limitAmount;

    @DbColumn
    public int limitLevel;

    @DbColumn
    public String randomRule;

    @DbColumn
    public int discount;

    @DbColumn
    public String discountStartTime;
    @DbColumn
    public String discountEndTime;
    @DbColumn
    public String activityId;
    @DbColumn
    public String activityTag;
    @DbColumn
    public String functions;

    public LiveGiftInfo() {
    }

    public LiveGiftInfo(String _id, String data_id, Integer dataversion,
                        String uniq, String name, Integer type, boolean cumulative,
                        String desc, String icon, String animation, int priceType,
                        Float price, int coldDown, long startTime, long endTime,
                        int limitAmount, int limitLevel, String randomRule,
                        int discount, String discountStartTime, String discountEndTime,
                        String activityId, String activityTag, String functions) {
        this.guid = _id;
        this.data_id = data_id;
        this.dataversion = dataversion;
        this.uniq = uniq;
        this.name = name;
        this.type = type;
        this.cumulative = cumulative;
        this.desc = desc;
        this.icon = icon;
        this.animation = animation;
        this.priceType = priceType;
        this.price = price;
        this.coldDown = coldDown;
        this.startTime = startTime;
        this.endTime = endTime;
        this.limitAmount = limitAmount;
        this.limitLevel = limitLevel;
        this.randomRule = randomRule;
        this.discount = discount;
        this.discountStartTime = discountStartTime;
        this.discountEndTime = discountEndTime;
        this.activityId = activityId;
        this.activityTag = activityTag;
        this.functions = functions;
    }

    public String getDownloadGiftPath() {
        return DOWNLOAD_DIR + guid + ".webp";
    }

    public String getParseGiftDesc() {
        if (!TextUtils.isEmpty(desc)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(desc);
                String lan = Locale.getDefault().getLanguage();
                String des = jsonObject.getString(lan);
                return des;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getParseGiftName() {
        if (!TextUtils.isEmpty(name)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(name);
                String lan = Locale.getDefault().getLanguage();
                String name = jsonObject.getString(lan);
                return name;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
