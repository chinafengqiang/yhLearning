package com.yh.utils;

/**
 * Created by FQ.CHINA on 2015/8/28.
 */
public interface AppConstants {

    //服务地址
    String SERVER_HOST = "http://171.8.63.71:8886/yh";

    //API基础地址
    String API_HOST = SERVER_HOST+"/api";

    //服务器存放文件的地址
    String FILE_HOST = SERVER_HOST+"/upload/";

    //登录
    String API_USER_LOGIN = API_HOST+"/login";
    String API_BIND_PUSH_USER = API_HOST+"/pushBind";
    //编辑用户信息
    String API_EDIT_USER_INFO = API_HOST+"/editUser";
    //获取用户信息
    String API_GET_USER_INFO = API_HOST+"/getUserInfo";
    //修改密码
    String API_EDIT_USER_PASS = API_HOST+"/editUserPass";
    //课程进度
    String API_GET_LESSON_PLAN = API_HOST+"/getLessonPlan";

    //获取备课表
    String API_GET_PRELESSON = API_HOST+"/getPreLesson";

    //app调用地址相关
    String APP_SCHEME = "yhloader";
    String APP_HOST = "learning";
    String APP_LOADER_URL = APP_SCHEME+"://"+APP_HOST;

    //消息状态
    int MSG_STATUS_ALL = -1;
    int MSG_STATUS_LOOKING = 0;
    int MSG_STATUS_LOOKED = 1;

    int PAGED_SIZE = 10;


    /**
     * 消息推送部分分隔标示
     */
    String PUSH_SPLIT = "#@@#";

    /**
     * 推送命令号
     */
    int PUSH_CMD_MESSAGE = 1;//消息

    int PUSH_CMD_LESSON = 2;//课程表

    int PUSH_CMD_PLAN = 3;//教学计划
}
