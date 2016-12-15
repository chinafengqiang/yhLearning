package com.yh.learning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yh.push.Push;
import com.yh.utils.CommonUtil;
import com.yh.utils.SpUtil;

import java.io.File;

/**
 * Created by FQ.CHINA on 2016/11/11.
 */
public class SplashActivity extends BaseFragmentActivity implements View.OnClickListener{
    private String versionText;
    private ProgressDialog pd;
    private SharedPreferences sp;
    private RelativeLayout ll_splash_main;
    private final static int YES_UPDATE = 1;
    private final static int YES_INTENTLOGIN = 2;
    boolean m_isNeedUpdate = false;
    int localVersion;
    @Override
    protected void findViewById() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // if(isTrue){
        ll_splash_main = (RelativeLayout) findViewById(R.id.ll_splash_main);

        sp = SpUtil.getSharePerference(mContext);

        initCopyright();

        Message mess = new Message();
        mess.what = YES_INTENTLOGIN;
        handler.sendMessage(mess);
    }



    private void initCopyright(){
        TextView copyright = (TextView)findViewById(R.id.copyright);
        String versionName = CommonUtil.getAppVersionName(mContext);
    }


    @Override
    protected void initTitle() {

    }

    @Override
    protected void initSp() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_splash);
        Push push = new Push(mContext);
        push.init();
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case YES_UPDATE:
                    //shwoUpdateDailog(updateInfo.getDes(), updateInfo.getUrl());
                    break;
                case YES_INTENTLOGIN:
                    IntentLogin();
                    break;
                default:
                    loadMainActivity();
                    break;
            }
        }
    };


    private void loadMainActivity() {
        //处理轮播图片
        Intent in = new Intent(this, WhatActivity.class);
        //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        startActivity(in);
        finish();
    }



    public void IntentLogin() {
        try {
            setUpView();
        } catch (Exception e) {
            loadMainActivity();
        }
    }


    private void setUpView() {
        pd = new ProgressDialog(this, R.style.ProgressDialogStyle);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage(getString(R.string.download_msg));
        pd.setCancelable(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                try {
                    sleep(1000);
                    localVersion = getAppVersion();
                    m_isNeedUpdate = isUpdate();
                    if (m_isNeedUpdate) {
                        Message mess = new Message();
                        mess.what = YES_UPDATE;
                        handler.sendMessage(mess);
                    }
                    else {
                        loadMainActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    loadMainActivity();
                }
                Looper.myLooper().loop();
            }
        }.start();
    }


    private boolean isUpdate() {
        try {
            /*updateInfo = UpdateUtil
                    .getUpdateInfo(getString(R.string.server_update));
            String version = updateInfo.getVersion().trim();
            int nversion = 0;
            if (version != null && !"".equals(version))
                nversion = Integer.parseInt(version);
            if (nversion > localVersion)
                return true;*/
        } catch (Exception e) {
            return false;
        }
        return false;


    }

    private int getAppVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


//    private void shwoUpdateDailog(String des, String url) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_imm));
//        // builder.setIcon(R.drawable.min_logo);
//        builder.setTitle(getString(R.string.download_title));
//        builder.setMessage(des);
//        builder.setCancelable(false);
//        builder.setPositiveButton(getString(R.string.save),
//                new View.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (Environment.getExternalStorageState().equals(
//                                Environment.MEDIA_MOUNTED)) {
//                            String url = updateInfo.getUrl();
//                            String name = url.substring(
//                                    url.lastIndexOf("/") + 1, url.length());
//                            DownLoadTaskApk dl = new DownLoadTaskApk(url,
//                                    "/sdcard/" + name);
//                            pd.show();
//                            new Thread(dl).start();
//
//                            SpUtil.removeSharedPerference(sp, "isWhats");
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    getString(R.string.update_error_msg), 1);
//                            loadMainActivity();
//                        }
//                    }
//                });
//        builder.setNegativeButton(getString(R.string.tv_cancel),
//                new View.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        loadMainActivity();
//                    }
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        setDialogFontSize(dialog, 16);
//    }
//
//    private void setDialogFontSize(Dialog dialog,int size)
//    {
//        Window window = dialog.getWindow();
//        View view = window.getDecorView();
//        setViewFontSize(view,size);
//    }
//    private void setViewFontSize(View view,int size)
//    {
//        if(view instanceof ViewGroup)
//        {
//            ViewGroup parent = (ViewGroup)view;
//            int count = parent.getChildCount();
//            for (int i = 0; i < count; i++)
//            {
//                setViewFontSize(parent.getChildAt(i),size);
//            }
//        }
//        else if(view instanceof TextView){
//            TextView textview = (TextView)view;
//            textview.setTextSize(size);
//        }
//    }
//
//    private class DownLoadTaskApk implements Runnable {
//        private String urlPath;
//        private String file;
//
//        public DownLoadTaskApk(String url, String string) {
//            urlPath = url;
//            file = string;
//        }
//
//        @Override
//        public void run() {
//            try {
//                File f = UpdateUtil.getDownFile(urlPath, file, pd);
//                pd.dismiss();
//                install(f);
//            } catch (Exception e) {
//                e.printStackTrace();
//                pd.dismiss();
//                loadMainActivity();
//            }
//        }
//    }
//
//    private void install(File f) {
//        Intent in = new Intent();
//        in.setAction(Intent.ACTION_VIEW);
//        in.setDataAndType(Uri.fromFile(f),
//                "application/vnd.android.package-archive");
//        finish();
//        startActivity(in);
//    }

}
