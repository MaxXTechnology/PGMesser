package us.pinguo.messer.analysis;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hedongjin on 2017/8/18.
 */

public final class MesserActivityRefWatcher {
    protected static AtomicBoolean sWatchEnable = new AtomicBoolean(false);

    /** @deprecated Use {@link #install(Application, RefWatcher)}. */
    @Deprecated
    public static void installOnIcsPlus(Application application, RefWatcher refWatcher) {
        install(application, refWatcher);
    }

    public static void install(Application application, RefWatcher refWatcher) {
        new ActivityRefWatcher(application, refWatcher).watchActivities();
    }

    private final Application.ActivityLifecycleCallbacks lifecycleCallbacks =
            new Application.ActivityLifecycleCallbacks() {
                @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                }

                @Override public void onActivityStarted(Activity activity) {
                }

                @Override public void onActivityResumed(Activity activity) {
                }

                @Override public void onActivityPaused(Activity activity) {
                }

                @Override public void onActivityStopped(Activity activity) {
                }

                @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                }

                @Override public void onActivityDestroyed(Activity activity) {
                    MesserActivityRefWatcher.this.onActivityDestroyed(activity);
                }
            };

    private final Application application;
    private final RefWatcher refWatcher;

    /**
     * Constructs an {@link ActivityRefWatcher} that will make sure the activities are not leaking
     * after they have been destroyed.
     */
    public MesserActivityRefWatcher(Application application, RefWatcher refWatcher) {
        this.application = application;
        this.refWatcher = refWatcher;
    }

    void onActivityDestroyed(Activity activity) {
        if (sWatchEnable.get()) {
            refWatcher.watch(activity);
        }
    }

    public void watchActivities() {
        // Make sure you don't get installed twice.
        stopWatchingActivities();
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }

    public void stopWatchingActivities() {
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
    }
}
