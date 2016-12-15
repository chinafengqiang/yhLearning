package com.yh.learning;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yh.utils.ApiConstants;
import com.yh.utils.AppConstants;
import com.yh.utils.SpUtil;
import com.yh.utils.StringUtils;
import com.yh.utils.ToastUtil;
import com.yh.view.TitleBarView;
import com.yh.vo.IntegerVO;
import com.yh.vo.LoginInfo;
import com.yh.vo.UserInfo;
import com.yh.volley.FRestClient;
import com.yh.volley.FastJsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by FQ.CHINA on 2016/12/6.
 */

public class UserInfoActivity extends BaseFragmentActivity{
    private int userId;
    private SharedPreferences sp;
    private TitleBarView mTitleBarView;
    private TextView user_info_username;
    private EditText user_info_truename, user_info_mphone;
    private Button button_submit;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        user_info_username = (TextView) findViewById(R.id.user_info_username);
        user_info_truename = (EditText) findViewById(R.id.user_info_truename);
        user_info_mphone = (EditText) findViewById(R.id.user_info_mphone);
        button_submit = (Button) findViewById(R.id.button_submit);
    }

    @Override
    protected void initTitle() {
        mTitleBarView.setTitleText(R.string.user_info_setting_title);
        mTitleBarView.setTitleComeBackVisibility(View.VISIBLE);
        mTitleBarView.setTitleComeBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        userId = sp.getInt(LoginInfo.USER_ID, 0);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void processLogic() {
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_USER_INFO + "?userId=" + userId;

        FastJsonRequest<UserInfo> fastRequest = new FastJsonRequest<UserInfo>(Request.Method.GET, url, UserInfo.class, null, new Response.Listener<UserInfo>() {

            @Override
            public void onResponse(UserInfo resVO) {
                if (resVO != null) {
                    String username = resVO.getUsernane();
                    user_info_username.setText(username);
                    user_info_truename.setText(resVO.getTruename());
                    user_info_mphone.setText(resVO.getMphone());
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    @Override
    protected void setListener() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String truename = user_info_truename.getText().toString().trim();
                final String utruename = truename;
                final String mphone = user_info_mphone.getText().toString().trim();
                if (StringUtils.isBlank(truename)) {
                    ToastUtil.showToast(mContext, R.string.userinfo_truename_null);
                    return;
                }

                if (StringUtils.isBlank(mphone)) {
                    ToastUtil.showToast(mContext, R.string.userinfo_mphone_null);
                    return;
                }

                try {
                    truename = URLEncoder.encode(truename,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                showProgressDialog();

                String tag_json_obj = "json_obj_req";
                String url = AppConstants.API_EDIT_USER_INFO + "?userId=" + userId;
                url += "&utruename="+truename+"&umphone="+mphone;

                FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.POST, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

                    @Override
                    public void onResponse(IntegerVO resVO) {
                        closeProgressDialog();
                        if (resVO != null && resVO.getRet() == ApiConstants.Request_Success) {
                            SpUtil.removeSharedPerference(sp,LoginInfo.USER_MPHONE);
                            SpUtil.removeSharedPerference(sp,LoginInfo.USER_TRUENAME);
                            SpUtil.setStringSharedPerference(sp,LoginInfo.USER_MPHONE,mphone);
                            SpUtil.setStringSharedPerference(sp,LoginInfo.USER_TRUENAME,utruename);
                            ToastUtil.showToast(mContext, R.string.setting_userinfo_ok);
                        }else{
                            ToastUtil.showToast(mContext, R.string.setting_userinfo_error);
                        }
                    }
                },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtil.showToast(mContext, R.string.get_data_server_exception);
                                closeProgressDialog();
                            }
                        }
                );

                FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
