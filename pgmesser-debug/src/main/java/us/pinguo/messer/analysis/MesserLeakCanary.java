package us.pinguo.messer.analysis;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.internal.HeapAnalyzerService;

import static com.squareup.leakcanary.internal.LeakCanaryInternals.isInServiceProcess;

/**
 * Created by hedongjin on 2017/8/18.
 */

public class MesserLeakCanary {
    public static void setWatchEnable(Boolean enable) {
        MesserActivityRefWatcher.sWatchEnable.compareAndSet(!enable, enable);
    }


    /**
     * Creates a {@link RefWatcher} that works out of the box, and starts watching activity
     * references (on ICS+).
     */
    public static RefWatcher install(Application application) {
        return refWatcher(application).listenerServiceClass(DisplayLeakService.class)
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();
    }

    /** Builder to create a customized {@link RefWatcher} with appropriate Android defaults. */
    public static MesserAndroidRefWatcherBuilder refWatcher(Context context) {
        return new MesserAndroidRefWatcherBuilder(context);
    }

    /**
     * Whether the current process is the process running the {@link HeapAnalyzerService}, which is
     * a different process than the normal app process.
     */
    public static boolean isInAnalyzerProcess(Context context) {
        return isInServiceProcess(context, HeapAnalyzerService.class);
    }
}
