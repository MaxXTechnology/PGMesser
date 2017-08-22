package us.pinguo.messer;

import android.app.Application;

/**
 * Created by hedongjin on 2017/8/22.
 */

public class DebugMesser {

    public static final DebugMesser INSTANCE = new DebugMesser();

    private DebugMesser() {}

    public void handleMessage(Long time, int level, String tag, String msg) {
    }

    public void install(Application context) {
    }

    public void install(Application context, String sdcardRootDir) {

    }
}
