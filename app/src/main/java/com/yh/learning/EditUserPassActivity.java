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
import com.yh.volley.FRestClient;
import com.yh.volley.FastJsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by FQ.CHINA on 2016/12/6.
 */

public class EditUserPassActivity extends BaseFragmentActivity{
    private int userId;
    private SharedPreferences sp;
    private TitleBarView mTitleBarView;
    private EditText edit_user_newpass, edit_user_newpass_agin;
    private Button button_submit;
    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        edit_user_newpass = (EditText) findViewById(R.id.edit_user_newpass);
        edit_user_newpass_agin = (EditText) findViewById(R.id.edit_user_newpass_agin);
        button_submit = (Button) findViewById(R.id.button_submit);
    }

    @Override
    protected void initTitle() {
        mTitleBarView.setTitleText(R.string.edit_user_pass_title);
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
        setContentView(R.layout.activity_edit_pass);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pass = edit_user_newpass.getText().toString().trim();
                String passagin = edit_user_newpass_agin.getText().toString().trim();
                if (StringUtils.isBlank(pass)) {
                    ToastUtil.showToast(mContext, R.string.edit_user_newpass_hint);
                    return;
                }
                if (!StringUtils.isValidPass(pass)) {
                    ToastUtil.showToast(mContext, R.string.edit_user_pass_novalid);
                    return;
                }
                if (!pass.equals(passagin)) {
                    ToastUtil.showToast(mContext, R.string.edit_user_pass_nosame);
                    return;
                }
                showProgressDialog();

                String tag_json_obj = "json_obj_req";
                String url = AppConstants.API_EDIT_USER_PASS + "?userId=" + userId;
                url += "&userpass="+pass;

                FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.POST, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {

                    @Override
                    public void onResponse(IntegerVO resVO) {
                        closeProgressDialog();
                        if (resVO != null && resVO.getRet() == ApiConstants.Request_Success) {
                            SpUtil.removeSharedPerference(sp,LoginInfo.USER_PASS);
                            SpUtil.setStringSharedPerference(sp,LoginInfo.USER_PASS,pass);
                            ToastUtil.showToast(mContext, R.string.edit_user_pass_ok);
                        }else{
                            ToastUtil.showToast(mContext, R.string.edit_user_pass_error);
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
