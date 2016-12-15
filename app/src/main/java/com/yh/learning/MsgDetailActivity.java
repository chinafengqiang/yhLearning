package com.yh.learning;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.yh.fragment.MessageFragment;
import com.yh.view.TitleBarView;
import com.yh.vo.MsgVO;

/**
 * Created by FQ.CHINA on 2016/11/24.
 */

public class MsgDetailActivity extends BaseFragmentActivity{
    private TitleBarView title_bar;
    private TextView msg_detail;
    private TextView msg_time;
    private String title;
    private String msg;
    private String time;
    @Override
    protected void findViewById() {
        title_bar = (TitleBarView)findViewById(R.id.title_bar);
        msg_detail = (TextView)findViewById(R.id.msg_detail);
        msg_time = (TextView)findViewById(R.id.msg_time);
    }

    @Override
    protected void initTitle() {
        title_bar.setTitleText(title);
        title_bar.setTitleComeBackVisibility(View.VISIBLE);
        title_bar.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initSp() {
        Intent intent = getIntent();
        MsgVO msgVO = (MsgVO) intent.getSerializableExtra(MessageFragment.ITEM_MSG_INFO);
        if (msgVO != null) {
            title = msgVO.getTitle();
            msg = msgVO.getMsg();
            time = msgVO.getRtime();
        }

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_msg_detail);
    }

    @Override
    protected void processLogic() {
        msg_time.setText(time);
        msg_detail.setText(msg);
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
