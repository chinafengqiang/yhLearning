package com.yh.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yh.learning.EditUserPassActivity;
import com.yh.learning.LoginActivity;
import com.yh.learning.MainActivity;
import com.yh.learning.R;
import com.yh.learning.SplashActivity;
import com.yh.learning.UserInfoActivity;
import com.yh.utils.SpUtil;
import com.yh.view.TitleBarView;
import com.yh.vo.LoginInfo;

/**
 * Created by FQ.CHINA on 2016/11/15.
 */
public class MyFragment extends Fragment{
    private Context mContext;
    private View mBaseView;
    private TextView truenameTv,usernameTv,version;
    private RelativeLayout exit_rl,user_info_rl,edit_pass;
    private SharedPreferences sp;
    private String truename,username;
    private int userId;
    public MyFragment(){

    }

    public static MyFragment newInstance(String param){
        MyFragment fragment = new MyFragment();
        Bundle args=new Bundle();
        args.putString("param",param);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_my, null);
        initData();
        findView();
        initTitle();
        processLogic();
        setListener();
        return mBaseView;
    }

    private void initData(){
        sp = SpUtil.getSharePerference(mContext);
        truename = sp.getString(LoginInfo.USER_TRUENAME, "");
        username = sp.getString(LoginInfo.USER_NAME,"");
        userId = sp.getInt(LoginInfo.USER_ID,0);
    }


    private void findView(){
        exit_rl = (RelativeLayout)mBaseView.findViewById(R.id.exit_rl);
        truenameTv = (TextView)mBaseView.findViewById(R.id.truename);
        usernameTv = (TextView)mBaseView.findViewById(R.id.username);
        version = (TextView)mBaseView.findViewById(R.id.version);
        user_info_rl = (RelativeLayout)mBaseView.findViewById(R.id.user_info_rl);
        edit_pass = (RelativeLayout)mBaseView.findViewById(R.id.edit_pass);
    }


    private void initTitle(){

    }

    private void processLogic(){
        truenameTv.setText(truename);
        usernameTv.setText(username);
        version.setText(getText(R.string.version)+getAppVersionName());
    }

    private void setListener(){
        exit_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.setting_exit).setMessage(getString(R.string.setting_exit_cfg)).setCancelable(false)
                        .setPositiveButton(R.string.setting_exit_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeLoginInfo();
                                gotoLogout();
                            }
                        }).setNegativeButton(R.string.setting_cance,null).create().show();




            }
        });

        user_info_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        edit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditUserPassActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });


    }


    private void removeLoginInfo(){
        SpUtil.removeSharedPerference(sp, LoginInfo.USER_ID);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_NAME);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_PASS);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_TRUENAME);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_MPHONE);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_DEPT);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_ORG);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_ROLE);
        SpUtil.removeSharedPerference(sp,LoginInfo.USER_TYPE);
    }

    private void gotoLogout(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
    }

    private String getAppVersionName() {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
