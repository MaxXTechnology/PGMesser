package us.pinguo.messer.util;

/**
 * Created by hedongjin on 2017/8/7.
 */

import android.os.Looper;
import android.util.Log;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainThreadWatchDog implements Runnable {
    private static final String TAG = "MainThreadWatchDog";
    private static boolean sDebug = true;
    private static MainThreadWatchDog sMainThreadWatchDog = new MainThreadWatchDog();
    private HashMap<MainThreadWatchDog.WrappedStackTraceElement, MainThreadWatchDog.TimeCounter> mLegacyStackTrace = new LinkedHashMap();
    private final Map<MainThreadWatchDog.WrappedStackTraceElement, MainThreadWatchDog.TimeCounter> mAllCareStackTrace = new LinkedHashMap();
    private MainThreadWatchDog.WatchDogThread mWatchDogThread;
    private volatile boolean mStarted;
    private long mSleepInterval = 30L;
    private MainThreadWatchDog.TimeCounter mTotalTime = new MainThreadWatchDog.TimeCounter();
    private long mLastTimeDumpTrace;

    public MainThreadWatchDog() {
    }

    public static MainThreadWatchDog defaultInstance() {
        return sMainThreadWatchDog;
    }

    public synchronized void startWatch() {
        if(!this.mStarted) {
            this.mTotalTime.reset();
            this.mLegacyStackTrace.clear();
            this.mAllCareStackTrace.clear();
            this.mStarted = true;
            this.mLastTimeDumpTrace = 0L;
            this.mWatchDogThread = new MainThreadWatchDog.WatchDogThread(this, "MainThreadWatchDog");
            this.mWatchDogThread.start();
        }
    }

    public synchronized String stopWatch() {
        StringBuilder watchString = new StringBuilder();
        if(this.mStarted) {
            this.mStarted = false;
            List<MainThreadWatchDog.PriorityStackTraceProfile> priorityStackTraceProfileList = new ArrayList();
            Map var2 = this.mAllCareStackTrace;
            synchronized(this.mAllCareStackTrace) {
                Iterator var3 = this.mAllCareStackTrace.keySet().iterator();

                while(true) {
                    if(!var3.hasNext()) {
                        break;
                    }

                    MainThreadWatchDog.WrappedStackTraceElement wrappedStackTraceElement = (MainThreadWatchDog.WrappedStackTraceElement)var3.next();
                    MainThreadWatchDog.TimeCounter timeCounter = (MainThreadWatchDog.TimeCounter)this.mAllCareStackTrace.get(wrappedStackTraceElement);
                    MainThreadWatchDog.PriorityStackTraceProfile priorityStackTraceProfile = new MainThreadWatchDog.PriorityStackTraceProfile(wrappedStackTraceElement, timeCounter, 1.0D * (double)timeCounter.getTotalTime() / (double)this.mTotalTime.getTotalTime());
                    priorityStackTraceProfileList.add(priorityStackTraceProfile);
                }
            }

            MainThreadWatchDog.PriorityStackTraceProfile lastProfile = null;

            for(int i = priorityStackTraceProfileList.size() - 1; i >= 0; --i) {
                MainThreadWatchDog.PriorityStackTraceProfile profile = (MainThreadWatchDog.PriorityStackTraceProfile)priorityStackTraceProfileList.get(i);
                if(lastProfile != null && lastProfile.contains(profile)) {
                    profile.isDuplicate = true;
                } else {
                    profile.isDuplicate = false;
                }

                lastProfile = profile;
            }


            String totalString = "===============total:" + this.mTotalTime.getTotalCount() + " ||" + " >" + this.mTotalTime.getTotalTime() + "ms ===============\n";
            Log.i("MainThreadWatchDog", totalString);
            watchString.append(totalString);

            Iterator var13 = priorityStackTraceProfileList.iterator();

            while(var13.hasNext()) {
                MainThreadWatchDog.PriorityStackTraceProfile profile = (MainThreadWatchDog.PriorityStackTraceProfile)var13.next();
                if(!profile.isDuplicate) {
                    String everyTraceString = profile.timeCounter.getTotalCount() + " ||" + " >" + profile.timeCounter.getTotalTime() + "ms || " + profile.incPercent + " ||" + "\n" + profile.stackString;
                    Log.i("MainThreadWatchDog", everyTraceString);
                    watchString.append(everyTraceString);
                }
            }



        }

        return watchString.toString();
    }

    public void run() {
        while(this.mStarted) {
            long begin = System.nanoTime();
            Thread mainThread = Looper.getMainLooper().getThread();
            StackTraceElement[] mainStackTrace = mainThread.getStackTrace();
            long thisTimeDumpTrace = System.nanoTime() / 1000000L;
            long realSleepTime;
            if(this.mLastTimeDumpTrace == 0L) {
                realSleepTime = this.mSleepInterval;
            } else {
                realSleepTime = thisTimeDumpTrace - this.mLastTimeDumpTrace;
            }

            this.mLastTimeDumpTrace = thisTimeDumpTrace;
            if(mainStackTrace != null) {
                int startIndex = 0;
                int endIndex = mainStackTrace.length - 1;

                for(int i = mainStackTrace.length - 1; i >= 0; --i) {
                    StackTraceElement stackTraceElement = mainStackTrace[i];
                    if(stackTraceElement.getMethodName().equals("dispatchMessage") && stackTraceElement.getClassName().equals("android.os.Handler")) {
                        endIndex = i - 1;
                        break;
                    }
                }

                HashMap<MainThreadWatchDog.WrappedStackTraceElement, MainThreadWatchDog.TimeCounter> newLegacyStackTrace = new HashMap();
                if(endIndex != -1) {
                    boolean maybeMatch = true;
                    StringBuilder stackStringBuilder = new StringBuilder();

                    for(int j = endIndex; j >= startIndex; --j) {
                        StackTraceElement stackTraceElement = mainStackTrace[j];
                        MainThreadWatchDog.WrappedStackTraceElement wrappedStackTraceElement = new MainThreadWatchDog.WrappedStackTraceElement(stackTraceElement, stackStringBuilder.toString());
                        stackStringBuilder.insert(0, stackTraceElement.toString() + "\n");
                        MainThreadWatchDog.TimeCounter countThisTime = (MainThreadWatchDog.TimeCounter)this.mLegacyStackTrace.get(wrappedStackTraceElement);
                        if(countThisTime != null && maybeMatch) {
                            if(newLegacyStackTrace.get(wrappedStackTraceElement) == null) {
                                countThisTime.addTime(realSleepTime);
                                newLegacyStackTrace.put(wrappedStackTraceElement, countThisTime);
                                Map var18 = this.mAllCareStackTrace;
                                synchronized(this.mAllCareStackTrace) {
                                    MainThreadWatchDog.TimeCounter countTotal = (MainThreadWatchDog.TimeCounter)this.mAllCareStackTrace.get(wrappedStackTraceElement);
                                    if(countTotal == null) {
                                        countTotal = new MainThreadWatchDog.TimeCounter();
                                    }

                                    countTotal.addTime(realSleepTime);
                                    this.mAllCareStackTrace.put(wrappedStackTraceElement, countTotal);
                                }
                            }
                        } else {
                            maybeMatch = false;
                            newLegacyStackTrace.put(wrappedStackTraceElement, new MainThreadWatchDog.TimeCounter());
                        }
                    }
                }

                this.mLegacyStackTrace = newLegacyStackTrace;
                this.mTotalTime.addTime(realSleepTime);
            }

            long end = System.nanoTime();
            long durationInMs = (end - begin) / 1000000L;
            if(this.mSleepInterval > durationInMs && durationInMs >= 0L) {
                try {
                    Thread.sleep(this.mSleepInterval - durationInMs);
                } catch (InterruptedException var21) {
                    this.stopWatch();
                    return;
                }
            }
        }

    }

    private static class WatchDogThread extends Thread {
        WatchDogThread(Runnable runnable, String name) {
            super(runnable);
            this.setPriority(5);
            this.setName(name);
        }
    }

    private static class TimeCounter {
        private int count;
        private List<Long> timeList;

        private TimeCounter() {
            this.timeList = new ArrayList();
        }

        void addTime(long time) {
            this.timeList.add(Long.valueOf(time));
            ++this.count;
        }

        long getTotalTime() {
            long totalTime = 0L;

            long time;
            for(Iterator var3 = this.timeList.iterator(); var3.hasNext(); totalTime += time) {
                time = ((Long)var3.next()).longValue();
            }

            return totalTime;
        }

        long getTotalCount() {
            return (long)this.count;
        }

        void reset() {
            this.count = 0;
            this.timeList.clear();
        }
    }

    private static class WrappedStackTraceElement {
        public StackTraceElement stackTraceElement;
        public String callStackString;

        WrappedStackTraceElement(StackTraceElement stackTraceElement, String callStackString) {
            this.stackTraceElement = stackTraceElement;
            this.callStackString = callStackString;
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof MainThreadWatchDog.WrappedStackTraceElement)) {
                return false;
            } else {
                MainThreadWatchDog.WrappedStackTraceElement castObj = (MainThreadWatchDog.WrappedStackTraceElement)obj;
                if(this.stackTraceElement.getMethodName() != null && castObj.stackTraceElement.getMethodName() != null) {
                    if(!this.stackTraceElement.getMethodName().equals(castObj.stackTraceElement.getMethodName())) {
                        return false;
                    } else if(!this.stackTraceElement.getClassName().equals(castObj.stackTraceElement.getClassName())) {
                        return false;
                    } else {
                        String localFileName = this.stackTraceElement.getFileName();
                        if(localFileName == null) {
                            if(castObj.stackTraceElement.getFileName() != null) {
                                return false;
                            }
                        } else if(!localFileName.equals(castObj.stackTraceElement.getFileName())) {
                            return false;
                        }

                        return this.callStackString == null && castObj.callStackString != null?false:this.callStackString == null || this.callStackString.equals(castObj.callStackString);
                    }
                } else {
                    return false;
                }
            }
        }

        public int hashCode() {
            return this.stackTraceElement.hashCode();
        }
    }

    private static class PriorityStackTraceProfile implements Comparable<MainThreadWatchDog.PriorityStackTraceProfile> {
        private static NumberFormat percent = NumberFormat.getPercentInstance();
        public String stackString;
        public MainThreadWatchDog.TimeCounter timeCounter;
        public String incPercent;
        public boolean isDuplicate;

        PriorityStackTraceProfile(MainThreadWatchDog.WrappedStackTraceElement wrappedStackTraceElement, MainThreadWatchDog.TimeCounter timeCounter, double incPercent) {
            percent = new DecimalFormat("0.00#%");
            this.timeCounter = timeCounter;
            this.stackString = wrappedStackTraceElement.stackTraceElement.toString() + "\n" + wrappedStackTraceElement.callStackString;
            this.incPercent = percent.format(incPercent);
        }

        public boolean contains(MainThreadWatchDog.PriorityStackTraceProfile priorityStackTraceProfile) {
            return this.timeCounter.getTotalCount() == priorityStackTraceProfile.timeCounter.getTotalCount() && this.incPercent.equals(priorityStackTraceProfile.incPercent) && this.stackString.contains(priorityStackTraceProfile.stackString);
        }

        public int compareTo(MainThreadWatchDog.PriorityStackTraceProfile another) {
            int countDiff = (int)(another.timeCounter.getTotalCount() - this.timeCounter.getTotalCount());
            if(countDiff == 0) {
                int anotherStackLength = another.stackString.length();
                int thisStackLength = this.stackString.length();
                return anotherStackLength > thisStackLength?1:(anotherStackLength == thisStackLength?0:-1);
            } else {
                return countDiff;
            }
        }
    }
}

