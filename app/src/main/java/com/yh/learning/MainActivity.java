package com.yh.learning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.igexin.sdk.PushManager;
import com.yh.dao.MsgService;
import com.yh.fragment.HomeFragment;
import com.yh.fragment.MessageFragment;
import com.yh.fragment.MyFragment;
import com.yh.utils.AppConstants;
import com.yh.utils.SpUtil;
import com.yh.utils.ToastUtil;
import com.yh.vo.LoginInfo;
import com.yh.vo.MsgVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/11/10.
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG="MainActivity";
    private BottomNavigationBar bottom_navigation_bar;
    private LinearLayout fl_content;
    private BadgeItem badge;
    private Context mContext;

    private List<Fragment> fragments;
    private SharedPreferences sp;
    private int userId;
    private MsgService msgService;
    private int msgCount = 0;
    //是否已经选择过消息菜单
    private boolean isMsgSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initSp();
        findView();
        initBottomNavigationBar();
        checkLogin();
    }

    private void initBottomNavigationBar() {

        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_DEFAULT);
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //设置默认颜色
        bottom_navigation_bar
                .setInActiveColor(R.color.bar_text_default)//设置未选中的Item的颜色，包括图片和文字
                .setActiveColor(R.color.white)
                .setBarBackgroundColor(R.color.bar_text_selected);//设置整个控件的背景色
        //消息数量提示
        msgCount = getMsgCount();
        if(msgCount > 0){
            badge = new BadgeItem();
            badge.setText(msgCount+"");
            badge.setHideOnSelect(true);
        }
        //添加选项
        bottom_navigation_bar.addItem(new BottomNavigationItem(R.mipmap.home_disselect, R.string.bar_home))
                .addItem(new BottomNavigationItem(R.mipmap.message_disselect, R.string.bar_message).setBadgeItem(badge))
                .addItem(new BottomNavigationItem(R.mipmap.my_disselect,R.string.bar_my))
                .initialise();//所有的设置需在调用该方法前完成
        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                if (fragments != null) {
                    if (position < fragments.size()) {
                        if(position == 1)
                            isMsgSelected = true;
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        ft.replace(R.id.fl_content, fragment);
                        ft.commitAllowingStateLoss();//选择性的提交，和commit有一定的区别，他不保证数据完整传输
                    }
                }
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中
                if(isMsgSelected && badge != null){
                    badge.hide();
                }
            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中
            }
        });


        fragments = getFragments();
        setDefaultFragment();

    }

    public void hide(View view){
        bottom_navigation_bar.hide();
    }
    public void show(View view){
        bottom_navigation_bar.show();
    }


    private void findView(){
        fl_content = (LinearLayout) findViewById(R.id.fl_content);
        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    private void initSp(){
        sp = SpUtil.getSharePerference(this);
        userId = sp.getInt(LoginInfo.USER_ID, 0);
    }


    /**
     * 设置默认显示的fragment
     */
    private void setDefaultFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_content, HomeFragment.newInstance("home"));
        transaction.commit();
    }

    /**
     * 获取fragment
     *
     * @return fragment列表
     */
    private List<Fragment> getFragments() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("home"));
        fragments.add(MessageFragment.newInstance("message",msgCount));
        fragments.add(MyFragment.newInstance("my"));
        return fragments;
    }

    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(this,R.string.exist_app_ts);
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return true;
    }

    private void checkLogin(){
        if(userId == 0){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            finish();
        }
    }

    private int getMsgCount(){
        if(msgService == null){
            msgService = new MsgService(mContext);
        }
       return msgService.getMsgCount(AppConstants.MSG_STATUS_LOOKING);
    }
}
