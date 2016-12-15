package com.yh.learning;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.yh.push.PushUtils;
import com.yh.utils.ApiConstants;
import com.yh.utils.AppConstants;
import com.yh.utils.SpUtil;
import com.yh.utils.StringUtils;
import com.yh.utils.ToastUtil;
import com.yh.vo.IntegerVO;
import com.yh.vo.LoginInfo;
import com.yh.volley.FRestClient;
import com.yh.volley.FastJsonRequest;


/**
 * Created by FQ.CHINA on 2015/9/23.
 */
public class LoginActivity extends BaseFragmentActivity {
    private ImageView title_come_back;
    private EditText username, password;
    private Button button_login;
    private SharedPreferences sp;
    private int type;
    private Context mContext;

    @Override
    protected void findViewById() {
        mContext = this;
        title_come_back = (ImageView) findViewById(R.id.title_come_back);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button_login = (Button) findViewById(R.id.button_login);
    }

    @Override
    protected void initTitle() {
        title_come_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    protected void initSp() {
        sp = SpUtil.getSharePerference(mContext);
        String loginname = sp.getString(LoginInfo.USER_NAME, "");
        if (StringUtils.isNotBlank(loginname)) {
            username.setText(loginname);
        }
        type = getIntent().getIntExtra("type", 0);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String uname = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (StringUtils.isBlank(uname)) {
            ToastUtil.showToast(mContext, R.string.login_username_null);
            return;
        }
        if (StringUtils.isBlank(pass)) {
            ToastUtil.showToast(mContext, R.string.login_password_null);
            return;
        }
        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_USER_LOGIN + "?username=" + uname + "&userpass=" + pass;

        FastJsonRequest<LoginInfo> fastRequest = new FastJsonRequest<LoginInfo>(Request.Method.GET, url, LoginInfo.class, null, new Response.Listener<LoginInfo>() {
            @Override
            public void onResponse(LoginInfo resVO) {
                if (resVO != null) {
                    int ret = resVO.getRet();
                    if (ret == ApiConstants.Request_Success) {
                        SharedPreferences.Editor ed = sp.edit();
                        int userId = resVO.getUserId();
                        int orgId = resVO.getUserOrg();
                        int deptId = resVO.getUserDept();
                        ed.putInt(LoginInfo.USER_ID, userId);
                        ed.putString(LoginInfo.USER_NAME, resVO.getUsername());
                        ed.putString(LoginInfo.USER_PASS, resVO.getUserpass());
                        ed.putString(LoginInfo.USER_TRUENAME, resVO.getTruename());
                        ed.putString(LoginInfo.USER_MPHONE, resVO.getMphone());
                        ed.putInt(LoginInfo.USER_ROLE, resVO.getUserRole());
                        ed.putInt(LoginInfo.USER_TYPE, resVO.getUserType());
                        ed.putInt(LoginInfo.USER_ORG, resVO.getUserOrg());
                        ed.putInt(LoginInfo.USER_DEPT, resVO.getUserDept());
                        ed.commit();

                        gotoMain();

                        bindPushUser(userId,orgId,deptId);

                        finish();
                    } else if (ret == ApiConstants.OBJECT_NOT_EXIST) {
                        ToastUtil.showToast(mContext, R.string.login_user_notexist);
                    } else if (ret == ApiConstants.ERROR_CODE_A) {
                        ToastUtil.showToast(mContext, R.string.login_password_error);
                    } else if (ret == ApiConstants.ERROR_CODE_B) {
                        ToastUtil.showToast(mContext, R.string.login_user_novalid);
                    } else {
                        ToastUtil.showToast(mContext, R.string.login_fail);
                    }
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext, R.string.login_fail);
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);

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

    private void gotoMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        finish();
    }

    private void bindPushUser(int userId,int orgId,int deptId) {

        String clientId = PushUtils.getClientId(mContext);

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_BIND_PUSH_USER + "?userId=" + userId + "&clientId=" + clientId+"&orgId="+orgId+"&deptId="+deptId;

        FastJsonRequest<IntegerVO> fastRequest = new FastJsonRequest<IntegerVO>(Request.Method.POST, url, IntegerVO.class, null, new Response.Listener<IntegerVO>() {
            @Override
            public void onResponse(IntegerVO resVO) {
                if (resVO != null) {

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


}
