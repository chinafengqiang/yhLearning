package com.yh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.yh.db.DB;
import com.yh.db.DBHelper;
import com.yh.utils.AppConstants;
import com.yh.utils.SpUtil;
import com.yh.vo.LessonVO;
import com.yh.vo.LoginInfo;
import com.yh.vo.MsgVO;
import com.yh.vo.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/11/17.
 */
public class MsgService {
    private DBHelper helper = null;
    private int userId;
    private SharedPreferences sp;
    public MsgService(Context context) {
        helper = new DBHelper(context);
        if(sp == null)
            sp = SpUtil.getSharePerference(context);
        userId = sp.getInt(LoginInfo.USER_ID,0);
    }

    public boolean saveMsg(MsgVO msg){
        try{
            ContentValues content = new ContentValues();
            content.put(DB.TABLES.MESSAGE.FIELDS.TITLE,msg.getTitle());
            content.put(DB.TABLES.MESSAGE.FIELDS.MSG,msg.getMsg());
            content.put(DB.TABLES.MESSAGE.FIELDS.STATUS,msg.getStatus());
            content.put(DB.TABLES.MESSAGE.FIELDS.R_TIME,msg.getRtime());
            content.put(DB.TABLES.MESSAGE.FIELDS.EXT1,msg.getExt1());
            content.put(DB.TABLES.MESSAGE.FIELDS.EXT2,msg.getExt2());
            content.put(DB.TABLES.MESSAGE.FIELDS.USER_ID,userId);
            helper.insert(DB.TABLES.MESSAGE.TABLENAME,content);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int getMsgCount(int status){
        String condition = DB.TABLES.MESSAGE.FIELDS.USER_ID+" = "+userId;
        if(status >= 0){
            condition += " and "+DB.TABLES.MESSAGE.FIELDS.STATUS+" = "+status;
        }
        String sql = String.format(DB.TABLES.MESSAGE.SQL.SELECT_COUNT,condition);
       try{
           Cursor cursor = helper.SELECT(sql);
           if(cursor.moveToNext()){
               return cursor.getInt(0);
           }
       }catch(Exception e ){
       }
        return 0;
    }

    public List<MsgVO> getMsgListPaged(PageInfo pageInfo){
        List<MsgVO> resList = new ArrayList<MsgVO>();
        String condition = DB.TABLES.MESSAGE.FIELDS.USER_ID+" = "+userId;
        String order = " order by "+DB.TABLES.MESSAGE.FIELDS.R_TIME+" desc";
        String limit = " limit "+pageInfo.getOffset()+","+ pageInfo.getPagesize();
        //获取总条数
        String countSql = String.format(DB.TABLES.MESSAGE.SQL.SELECT_COUNT,condition);
        pageInfo.setTotals(this.getCount(countSql));

        String sql = String.format(DB.TABLES.MESSAGE.SQL.SELECT,condition+order+limit);
        getList(sql,resList);
        return resList;
    }

    public List<MsgVO> getMsgList(int status){
        List<MsgVO> resList = new ArrayList<MsgVO>();
        String condition = DB.TABLES.MESSAGE.FIELDS.USER_ID+" = "+userId;
        if(status >= 0){
            condition += " and "+DB.TABLES.MESSAGE.FIELDS.STATUS+" = "+status;
        }
        String sql = String.format(DB.TABLES.MESSAGE.SQL.SELECT,condition);
        getList(sql,resList);
        return resList;
    }

    private void getList(String sql,List<MsgVO> msgList){
        try{
            Cursor cursor = helper.SELECT(sql);
            MsgVO msg = null;
            while (cursor.moveToNext()) {
                msg = new MsgVO();
                msg.setId(cursor.getInt(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.ID)));
                msg.setTitle(cursor.getString(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.TITLE)));
                msg.setMsg(cursor.getString(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.MSG)));
                msg.setRtime(cursor.getString(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.R_TIME)));
                msg.setExt1(cursor.getString(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.EXT1)));
                msg.setExt2(cursor.getString(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.EXT2)));
                msg.setStatus(cursor.getInt(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.STATUS)));
                msg.setUserId(cursor.getInt(cursor.getColumnIndex(DB.TABLES.MESSAGE.FIELDS.USER_ID)));
                msgList.add(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getCount(String sql){
        try{
            Cursor cursor = helper.SELECT(sql);
            if(cursor.moveToNext()){
                return cursor.getInt(0);
            }
        }catch(Exception e ){
        }
        return 0;
    }


    public boolean setMsgStatus(int status){
        try{
            ContentValues content = new ContentValues();
            content.put(DB.TABLES.MESSAGE.FIELDS.STATUS,status);
            String where = DB.TABLES.MESSAGE.FIELDS.USER_ID+" = ?";
            helper.update(DB.TABLES.MESSAGE.TABLENAME,content,where,new String[]{userId+""});
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public boolean saveLesson(LessonVO lesson){
        try{
            ContentValues content = new ContentValues();
            content.put(DB.TABLES.LESSON.FIELDS.LESSON_ID,lesson.getLid());
            content.put(DB.TABLES.LESSON.FIELDS.LESSON_NUM,lesson.getLnum());
            content.put(DB.TABLES.LESSON.FIELDS.LESSON_TIME,lesson.getLtime());
            content.put(DB.TABLES.LESSON.FIELDS.WEEK_ONE_LESSON,lesson.getLone());
            content.put(DB.TABLES.LESSON.FIELDS.WEEK_TWO_LESSON,lesson.getLtwo());
            content.put(DB.TABLES.LESSON.FIELDS.WEEK_THREE_LESSON,lesson.getLthree());
            content.put(DB.TABLES.LESSON.FIELDS.WEEK_FOUR_LESSON,lesson.getLfour());
            content.put(DB.TABLES.LESSON.FIELDS.WEEK_FIVE_LESSON,lesson.getLfive());
            helper.insert(DB.TABLES.LESSON.TABLENAME,content);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void deleteLesson(int lessonId){
        String condition = DB.TABLES.LESSON.FIELDS.LESSON_ID +" = "+lessonId;
        String sql = String.format(DB.TABLES.LESSON.SQL.DELETE,condition);
        helper.ExecuteSQL(sql);
    }

    public void deleteAllLesson(){
        helper.ExecuteSQL(DB.TABLES.LESSON.SQL.DELETE_ALL);
    }

    public List<LessonVO> getLessonList(){
        List<LessonVO> retList = new ArrayList<LessonVO>();
        try{
            Cursor cursor = helper.SELECT(DB.TABLES.LESSON.SQL.SELECT_ALL);
            LessonVO lessonVO = null;
            while (cursor.moveToNext()) {
                lessonVO = new LessonVO();
                lessonVO.setLid(cursor.getInt(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.LESSON_ID)));
                lessonVO.setLnum(cursor.getInt(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.LESSON_NUM)));
                lessonVO.setLtime(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.LESSON_TIME)));
                lessonVO.setLone(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.WEEK_ONE_LESSON)));
                lessonVO.setLtwo(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.WEEK_TWO_LESSON)));
                lessonVO.setLthree(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.WEEK_THREE_LESSON)));
                lessonVO.setLfour(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.WEEK_FOUR_LESSON)));
                lessonVO.setLfive(cursor.getString(cursor.getColumnIndex(DB.TABLES.LESSON.FIELDS.WEEK_FIVE_LESSON)));
                retList.add(lessonVO);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }
        return retList;
    }
}
