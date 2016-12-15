package com.yh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import com.yh.learning.R;
import com.yh.utils.StringUtils;
import com.yh.vo.PreLessonVO;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by FQ.CHINA on 2016/12/12.
 */

public class PreLessonItemAdapter extends BaseAdapter{
    private final static int LESSON_TOTAL = 9;
    private Context context;
    private int week;
    private List<PreLessonVO> lessonList = new ArrayList<PreLessonVO>();

    public PreLessonItemAdapter(Context context, int week, List<PreLessonVO> lessonList){
        this.week = week;
        this.context = context;
        if(this.lessonList != null){
            this.lessonList.clear();
        }
        if(lessonList != null && lessonList.size() > 0) {
            this.lessonList.addAll(lessonList);
        }else{
            initEmptyList();
        }
    }

    @Override
    public int getCount() {
        return lessonList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ItemHolder holder;
        final PreLessonVO lesson = getLesson(i);
        if(view == null){
            holder = new ItemHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.activity_prelesson_detail, null);
            holder.lessonRow = (TableRow)view.findViewById(R.id.lessonRow);
            holder.lessonName = (TextView)view.findViewById(R.id.lessonName);
            holder.lessonTime = (TextView)view.findViewById(R.id.lessonTime);
            holder.lessonNumber = (TextView)view.findViewById(R.id.lessonNumber);
            view.setTag(holder);
        }else {
            holder = (ItemHolder) view.getTag();
        }
        holder.lessonNumber.setText(lesson.getPlnum()+"");
        holder.lessonName.setText(lesson.getName());
        holder.lessonTime.setText(lesson.getShowTime());
        return view;
    }

    class ItemHolder{
        TableRow lessonRow;
        TextView lessonNumber;
        TextView lessonName;
        TextView lessonTime;
    }


    private PreLessonVO getLesson(int i){
        PreLessonVO  lesson = lessonList.get(i);
        String name = "";
        String sTime = lesson.getPltime();
        switch (week){
            case 0:
                sTime = "";
                break;
            case 1:
                name = lesson.getPlone();
                break;
            case 2:
                name = lesson.getPltwo();
                break;
            case 3:
                name = lesson.getPlthree();
                break;
            case 4:
                name = lesson.getPlfour();
                break;
            case 5:
                name = lesson.getPlfive();
                break;
            case 6:
                sTime = "";
                break;
        }
        if(StringUtils.isBlank(name)){
            name = "";
            sTime = "";
        }
        lesson.setName(name);
        lesson.setShowTime(sTime);
        return lesson;
    }

    private void initEmptyList(){
        PreLessonVO pl = null;
        for(int i = 1;i <= LESSON_TOTAL;i++){
            pl = new PreLessonVO();
            pl.setName("");
            pl.setShowTime("");
            pl.setPlnum(i);
            lessonList.add(pl);
        }
    }
}
