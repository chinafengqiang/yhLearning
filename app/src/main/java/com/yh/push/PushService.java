package com.yh.push;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.igexin.sdk.GTServiceManager;

/**
 * Created by FQ.CHINA on 2016/12/5.
 */

public class PushService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
