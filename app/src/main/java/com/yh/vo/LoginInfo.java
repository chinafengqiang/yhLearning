package com.yh.vo;

/**
 * Created by FQ.CHINA on 2015/9/23.
 */
public class LoginInfo {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String USER_TRUENAME = "truename";
    public static final String USER_MPHONE = "user_mphone";
    public static final String USER_TYPE = "user_type";
    public static final String USER_ORG = "user_org";
    public static final String USER_DEPT = "user_dept";
    public static final String USER_ROLE = "user_role";

    private int userId;
    private int userType;
    private int userOrg;
    private int userDept;
    private int userRole;
    private String username;
    private String userpass;
    private String truename;
    private String mphone;
    private int ret;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserOrg() {
        return userOrg;
    }

    public void setUserOrg(int userOrg) {
        this.userOrg = userOrg;
    }

    public int getUserDept() {
        return userDept;
    }

    public void setUserDept(int userDept) {
        this.userDept = userDept;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
