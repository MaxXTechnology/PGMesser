package us.pinguo.messer.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import us.pinguo.common.log.L;

/**
 * Created by hedongjin on 2017/8/2.
 */

public class MainActivity extends AppCompatActivity {

    public boolean mIsRunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIsRunning = true;
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 60 && mIsRunning) {
                    i++;
                    L.i("i = " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsRunning = false;
    }
}
