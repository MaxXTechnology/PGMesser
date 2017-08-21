package us.pinguo.messer.analysis;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.AndroidDebuggerControl;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.AndroidHeapDumper;
import com.squareup.leakcanary.AndroidWatchExecutor;
import com.squareup.leakcanary.DebuggerControl;
import com.squareup.leakcanary.DefaultLeakDirectoryProvider;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.HeapDumper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.LeakDirectoryProvider;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.leakcanary.RefWatcherBuilder;
import com.squareup.leakcanary.ServiceHeapDumpListener;
import com.squareup.leakcanary.WatchExecutor;

import java.util.concurrent.TimeUnit;

import static com.squareup.leakcanary.RefWatcher.DISABLED;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by hedongjin on 2017/8/18.
 */

public final class MesserAndroidRefWatcherBuilder extends RefWatcherBuilder<MesserAndroidRefWatcherBuilder> {

    private static final long DEFAULT_WATCH_DELAY_MILLIS = SECONDS.toMillis(5);

    private final Context context;

    MesserAndroidRefWatcherBuilder(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Sets a custom {@link AbstractAnalysisResultService} to listen to analysis results. This
     * overrides any call to {@link #heapDumpListener(HeapDump.Listener)}.
     */
    public MesserAndroidRefWatcherBuilder listenerServiceClass(
            Class<? extends AbstractAnalysisResultService> listenerServiceClass) {
        return heapDumpListener(new ServiceHeapDumpListener(context, listenerServiceClass));
    }

    /**
     * Sets a custom delay for how long the {@link RefWatcher} should wait until it checks if a
     * tracked object has been garbage collected. This overrides any call to {@link
     * #watchExecutor(WatchExecutor)}.
     */
    public MesserAndroidRefWatcherBuilder watchDelay(long delay, TimeUnit unit) {
        return watchExecutor(new AndroidWatchExecutor(unit.toMillis(delay)));
    }

    /**
     * Sets the maximum number of heap dumps stored. This overrides any call to {@link
     * #heapDumper(HeapDumper)} as well as any call to
     * {@link LeakCanary#setDisplayLeakActivityDirectoryProvider(LeakDirectoryProvider)})}
     *
     * @throws IllegalArgumentException if maxStoredHeapDumps < 1.
     */
    public MesserAndroidRefWatcherBuilder maxStoredHeapDumps(int maxStoredHeapDumps) {
        LeakDirectoryProvider leakDirectoryProvider =
                new DefaultLeakDirectoryProvider(context, maxStoredHeapDumps);
        LeakCanary.setDisplayLeakActivityDirectoryProvider(leakDirectoryProvider);
        return heapDumper(new AndroidHeapDumper(context, leakDirectoryProvider));
    }

    /**
     * Creates a {@link RefWatcher} instance and starts watching activity references (on ICS+).
     */
    public RefWatcher buildAndInstall() {
        RefWatcher refWatcher = build();
        if (refWatcher != DISABLED) {
            LeakCanary.enableDisplayLeakActivity(context);
            ActivityRefWatcher.install((Application) context, refWatcher);
        }
        return refWatcher;
    }

    @Override protected boolean isDisabled() {
        return LeakCanary.isInAnalyzerProcess(context);
    }

    @Override protected HeapDumper defaultHeapDumper() {
        LeakDirectoryProvider leakDirectoryProvider = new DefaultLeakDirectoryProvider(context);
        return new AndroidHeapDumper(context, leakDirectoryProvider);
    }

    @Override protected DebuggerControl defaultDebuggerControl() {
        return new AndroidDebuggerControl();
    }

    @Override protected HeapDump.Listener defaultHeapDumpListener() {
        return new ServiceHeapDumpListener(context, DisplayLeakService.class);
    }

    @Override protected ExcludedRefs defaultExcludedRefs() {
        return AndroidExcludedRefs.createAppDefaults().build();
    }

    @Override protected WatchExecutor defaultWatchExecutor() {
        return new AndroidWatchExecutor(DEFAULT_WATCH_DELAY_MILLIS);
    }
}
