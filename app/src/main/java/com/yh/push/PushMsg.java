package com.yh.push;

import android.content.Context;

import com.yh.dao.MsgService;
import com.yh.learning.R;
import com.yh.notify.NotifyTemplate;
import com.yh.utils.AppConstants;
import com.yh.utils.CommonUtil;
import com.yh.utils.ObjUtils;
import com.yh.utils.StringUtils;
import com.yh.vo.LessonVO;
import com.yh.vo.MsgVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/12/5.
 */

public class PushMsg {
    private Context context;
    private MsgService service;

    public PushMsg(Context context){
        this.context = context;
    }

    public void processMsg(String data){

        String[] msgArr = data.split("#@@#");
        int cmd = 0;
        if(msgArr != null){
            String cmdStr = msgArr[0];
            if(cmdStr != null && cmdStr.length() > 0){
                cmd = Integer.parseInt(cmdStr);
            }
        }

        switch (cmd){
            case AppConstants.PUSH_CMD_MESSAGE:
                sendMsg(msgArr);
               break;
            case AppConstants.PUSH_CMD_LESSON:
                sendLesson(msgArr);
                break;
            case AppConstants.PUSH_CMD_PLAN:

                break;
        }



    }


    private void sendMsg(String[] msgArr){
        MsgVO msg = new MsgVO();
        String msgContent = "";
        String msgTitle = "";
        if(msgArr != null && msgArr.length == 3){
            msgTitle = msgArr[1];
            msgContent = msgArr[2];
        }
        msg.setMsg(msgContent);
        msg.setTitle(msgTitle);
        msg.setRtime(CommonUtil.dateTime2String(new Date()));
        msg.setStatus(0);
        if(service == null){
            service = new MsgService(context);
        }
        if(StringUtils.isBlank(msgContent))
            return;
        boolean ret = service.saveMsg(msg);
        if(ret){
            //通知展示
            NotifyTemplate ntp = new NotifyTemplate(context,msgTitle,msgContent);
            ntp.showIntentApkNotify();
        }
    }

    private void sendLesson(String[] lessonArr){
        try{
            if(lessonArr != null && lessonArr.length == 3){
                int lessonId = Integer.parseInt(lessonArr[1]);
                String lessons = lessonArr[2];
                if(StringUtils.isNotBlank(lessons)){
                    String[] lArrs = lessons.split(";");
                    if(lArrs != null){
                        if(service == null){
                            service = new MsgService(context);
                        }
                        //删除
                        //service.deleteLesson(lessonId);
                        service.deleteAllLesson();

                        LessonVO lessonVO = null;
                        for(String lr : lArrs){
                            if(StringUtils.isNotBlank(lr)){
                                String[] linfoArr = lr.split(",");
                                if(linfoArr != null&&linfoArr.length == 7){
                                    lessonVO = new LessonVO();
                                    lessonVO.setLid(lessonId);
                                    lessonVO.setLnum(Integer.parseInt(linfoArr[0]));
                                    lessonVO.setLtime(linfoArr[1]);
                                    lessonVO.setLone(linfoArr[2]);
                                    lessonVO.setLtwo(linfoArr[3]);
                                    lessonVO.setLthree(linfoArr[4]);
                                    lessonVO.setLfour(linfoArr[5]);
                                    lessonVO.setLfive(linfoArr[6]);
                                    service.saveLesson(lessonVO);
                                }
                            }
                        }
                    }

                    NotifyTemplate ntp = new NotifyTemplate(context,context.getString(R.string.lesson_msg_title),context.getString(R.string.lesson_msg_content));
                    ntp.showIntentApkNotify();
                }
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
    }
}
