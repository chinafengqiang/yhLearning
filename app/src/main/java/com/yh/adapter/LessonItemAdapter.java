package com.yh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yh.learning.MainActivity;
import com.yh.learning.R;
import com.yh.utils.AppConstants;
import com.yh.utils.StringUtils;
import com.yh.utils.ToastUtil;
import com.yh.view.tooltip.ToolTip;
import com.yh.view.tooltip.ToolTipRelativeLayout;
import com.yh.view.tooltip.ToolTipView;
import com.yh.vo.LessonVO;
import com.yh.vo.PlanVO;
import com.yh.vo.UserInfo;
import com.yh.volley.FRestClient;
import com.yh.volley.FastJsonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/12/7.
 */

public class LessonItemAdapter extends BaseAdapter implements ToolTipView.OnToolTipViewClickedListener{
    private final static int LESSON_TOTAL = 8;
    private Context context;
    private int week;
    private List<LessonVO> lessonList = new ArrayList<LessonVO>();
    private ToolTipView mToolTipView;
    //存放所有有效toolTipView
    private List<ToolTipView> toolTipViewList = new ArrayList<ToolTipView>();

    public LessonItemAdapter(Context context, int week, List<LessonVO> lessonList) {
        this.week = week;
        this.context = context;
        if(this.lessonList != null){
            this.lessonList.clear();
        }
        this.lessonList.addAll(lessonList);
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
        final LessonVO lesson = getLesson(i);
        if(view == null){
            holder = new ItemHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.activity_lesson_detail, null);
            holder.lessonRow = (TableRow)view.findViewById(R.id.lessonRow);
            holder.lessonName = (TextView)view.findViewById(R.id.lessonName);
            holder.lessonTime = (TextView)view.findViewById(R.id.lessonTime);
            holder.lessonNumber = (TextView)view.findViewById(R.id.lessonNumber);
            holder.tooltipframelayout = (ToolTipRelativeLayout)view.findViewById(R.id.tooltipframelayout);
            view.setTag(holder);
        }else {
            holder = (ItemHolder) view.getTag();
        }
        holder.lessonNumber.setText(lesson.getLnum()+"");
        holder.lessonName.setText(lesson.getName());
        holder.lessonTime.setText(lesson.getShowTime());
        holder.lessonRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isBlank(lesson.getName()))
                    return;
                String tag_json_obj = "json_obj_req";
                String url = AppConstants.API_GET_LESSON_PLAN + "?lessonId=" + lesson.getLid();
                        url += "&lessonWeek="+week+"&lessonNum="+lesson.getLnum();

                FastJsonRequest<PlanVO> fastRequest = new FastJsonRequest<PlanVO>(Request.Method.GET, url, PlanVO.class, null, new Response.Listener<PlanVO>() {
                    @Override
                    public void onResponse(PlanVO resVO) {
                        String content = "";
                        if (resVO != null) {
                            content = resVO.getLessonContent();
                        }
                        if(StringUtils.isBlank(content))
                            content = context.getText(R.string.lesson_no_plan).toString();
                        String tipUID = lesson.getLid()+"_"+week+"_"+lesson.getLnum();
                        //关闭所有toolTip
                        closeToolTipView();
                        mToolTipView = holder.tooltipframelayout.showToolTipForView(
                                new ToolTip()
                                        .withText(content)
                                        .withColor(context.getResources().getColor(R.color.deepcolor))
                                        .withShadow(true).withTipUID(tipUID),
                                holder.lessonName);
                        mToolTipView.setOnToolTipViewClickedListener(LessonItemAdapter.this);
                        toolTipViewList.add(mToolTipView);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(context,R.string.get_data_fail);
                    }
                }
                );

                FRestClient.getInstance(context).addToRequestQueue(fastRequest, tag_json_obj);
            }
        });
        return view;
    }


    private LessonVO getLesson(int i){
        LessonVO  lesson = lessonList.get(i);
        String name = "";
        String sTime = lesson.getLtime();
        switch (week){
            case 0:
                sTime = "";
                break;
            case 1:
                name = lesson.getLone();
                break;
            case 2:
                name = lesson.getLtwo();
                break;
            case 3:
                name = lesson.getLthree();
                break;
            case 4:
                name = lesson.getLfour();
                break;
            case 5:
                name = lesson.getLfive();
                break;
            case 6:
                sTime = "";
                break;
        }
        if(StringUtils.isBlank(name) || " ".equals(name)){
            name = "";
            sTime = "";
        }
        lesson.setName(name);
        lesson.setShowTime(sTime);
        return lesson;
    }

    class ItemHolder{
        TableRow lessonRow;
        TextView lessonNumber;
        TextView lessonName;
        TextView lessonTime;
        ToolTipRelativeLayout tooltipframelayout;
    }

    @Override
    public void onToolTipViewClicked(ToolTipView toolTipView) {
        if (mToolTipView == toolTipView) {
            mToolTipView = null;
        }
    }

    public void closeToolTipView(){
        for (ToolTipView toolTipView : toolTipViewList) {
            if(toolTipView != null){
                toolTipView.remove();
                toolTipView = null;
            }
        }
    }

    public void clearToolTipViewList(){
        if(toolTipViewList != null)
            toolTipViewList.clear();
        toolTipViewList = null;
    }


}
