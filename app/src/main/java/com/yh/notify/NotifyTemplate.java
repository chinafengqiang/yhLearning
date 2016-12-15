package com.yh.notify;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.yh.learning.R;
import com.yh.utils.AppConstants;

import java.io.File;

/**
 * Created by FQ.CHINA on 2016/11/23.
 */

public class NotifyTemplate extends BaseNotify{
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private String title;
    private String content;
    private String fShowContent;
    /** Notification的ID */
    int notifyId = 100;

    public NotifyTemplate(Context mContext,String title,String content){
        super(mContext);
        this.mContext = mContext;
        this.title = title;
        this.content = content;
        this.fShowContent = title;
        initNotify();
    }

    public NotifyTemplate(Context mContext,String title,String content,String fShowContent){
        super(mContext);
        this.mContext = mContext;
        this.title = title;
        this.content = content;
        this.fShowContent = fShowContent;
        initNotify();
    }

    /** 初始化通知栏 */
    private void initNotify(){
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))				//.setNumber(number)//显示数量
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
				//.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_yh);
    }

    public void showIntentApkNotify(){
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle(this.title)
                .setContentText(this.content)
                .setTicker(fShowContent);
        Intent apkIntent = new Intent();
        apkIntent.setData(Uri.parse(AppConstants.APP_LOADER_URL));
        PendingIntent contextIntent = PendingIntent.getActivity(mContext, 0,apkIntent, 0);
        mBuilder.setContentIntent(contextIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }



}
