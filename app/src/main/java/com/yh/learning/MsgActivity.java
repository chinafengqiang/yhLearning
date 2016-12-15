package com.yh.learning;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.yh.fragment.HomeFragment;
import com.yh.fragment.MessageFragment;

/**
 * Created by FQ.CHINA on 2016/11/24.
 */

public class MsgActivity extends BaseFragmentActivity{
    private LinearLayout fl_content;
    private Context mContext;

    @Override
    protected void findViewById() {
        fl_content = (LinearLayout)findViewById(R.id.fl_content);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initSp() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_msg);
    }

    @Override
    protected void processLogic() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_content, MessageFragment.newInstance("message",0,true));
        transaction.commit();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
