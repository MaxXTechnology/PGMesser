package us.pinguo.messer.demo;

import android.app.Application;
import android.os.Environment;

import us.pinguo.messer.DebugMesser;

/**
 * Created by hedongjin on 2017/8/2.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DebugMesser.INSTANCE.install(this, Environment.getExternalStorageDirectory().getAbsolutePath());
    }
}
