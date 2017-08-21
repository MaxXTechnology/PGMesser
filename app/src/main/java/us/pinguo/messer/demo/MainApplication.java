package us.pinguo.messer.demo;

import android.app.Application;

import us.pinguo.common.log.L;
import us.pinguo.common.log.LogPrintListener;
import us.pinguo.messer.DebugMesser;

/**
 * Created by hedongjin on 2017/8/2.
 */

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        L.setLogEnable(true);
        DebugMesser.INSTANCE.install(this);
        L.setLogListener(new LogPrintListener() {
            @Override
            public void onLog(long time, String tag, int level, String msg) {
                DebugMesser.INSTANCE.handleMessage(time, level, tag, msg);
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        L.setLogListener(null);
    }
}
