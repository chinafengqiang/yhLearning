package com.yh.learning;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yh.adapter.LessonItemAdapter;
import com.yh.adapter.PreLessonItemAdapter;
import com.yh.dao.MsgService;
import com.yh.utils.AppConstants;
import com.yh.utils.DateUtils;
import com.yh.utils.SpUtil;
import com.yh.utils.ToastUtil;
import com.yh.view.TitleBarView;
import com.yh.vo.LessonVO;
import com.yh.vo.LoginInfo;
import com.yh.vo.PreLessonList;
import com.yh.vo.PreLessonVO;
import com.yh.vo.UserInfo;
import com.yh.volley.FRestClient;
import com.yh.volley.FastJsonRequest;

import java.util.List;

/**
 * Created by FQ.CHINA on 2016/12/12.
 */

public class PreLessonActivity extends BaseFragmentActivity{
    private Context mContext;
    public ListView list[] = new ListView[7];
    private TabHost tabs   = null;
    //定义手势检测器实例
    private GestureDetector detector = null;
    //定义手势动作两点之间的最小距离
    private final int FLIP_DISTANCE = 200;
    //
    private List<PreLessonVO> lessonList = null;

    private TitleBarView title_bar;
    private SharedPreferences sp;
    private int userId;
    private String name;

    @Override
    protected void findViewById() {
        title_bar = (TitleBarView)findViewById(R.id.title_bar);

        list[0] = (ListView)findViewById(R.id.list0);
        list[1] = (ListView)findViewById(R.id.list1);
        list[2] = (ListView)findViewById(R.id.list2);
        list[3] = (ListView)findViewById(R.id.list3);
        list[4] = (ListView)findViewById(R.id.list4);
        list[5] = (ListView)findViewById(R.id.list5);
        list[6] = (ListView)findViewById(R.id.list6);
        tabs  = (TabHost)findViewById(R.id.tabhost);

        //创建手势检测器
        detector = new GestureDetector(this, new PreLessonActivity.DetectorGestureListener());

        //在配置任何的TabSpec之前，必须在TabHost上调用该方法
        tabs.setup();

        //为主界面注册七个选项卡
        TabHost.TabSpec  spec = null;
        addCard(spec,"tag1",R.id.list0,"日");
        addCard(spec,"tag2",R.id.list1,"一");
        addCard(spec,"tag3",R.id.list2,"二");
        addCard(spec,"tag4",R.id.list3,"三");
        addCard(spec,"tag5",R.id.list4,"四");
        addCard(spec,"tag6",R.id.list5,"五");
        addCard(spec,"tag7",R.id.list6,"六");

        //修改tabHost选项卡中的字体的颜色
        TabWidget tabWidget = tabs.getTabWidget();
        for(int i=0;i<tabWidget.getChildCount();i++){
            TextView tv = (TextView)tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.highblue));
            tv.setTextSize(20);
        }

        //设置打开时默认的选项卡是当天的选项卡
        tabs.setCurrentTab(DateUtils.getWeekDay());

        //用适配器为各选项卡添加所要显示的内容
        loadLesson();
        //setListView();

        setListViewTouchListener();
    }

    @Override
    protected void initTitle() {
        title_bar.setTitleComeBackVisibility(View.VISIBLE);
        title_bar.setTitleText(R.string.prel_title);
        title_bar.setTitleComeBackListener(new View.OnClickListener() {
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

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_prelesson);
        mContext = this;
    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {

    }


    private void loadLesson(){
        sp = SpUtil.getSharePerference(mContext);
        userId = sp.getInt(LoginInfo.USER_ID,0);

        String tag_json_obj = "json_obj_req";
        String url = AppConstants.API_GET_PRELESSON + "?userId=" + userId;

        FastJsonRequest<PreLessonList> fastRequest = new FastJsonRequest<PreLessonList>(Request.Method.GET, url, PreLessonList.class, null, new Response.Listener<PreLessonList>() {

            @Override
            public void onResponse(PreLessonList resVO) {
                if (resVO != null) {
                    lessonList = resVO.getPldList();
                    name = resVO.getName();
                    setListView();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showToast(mContext,R.string.get_data_fail);
                        setListView();
                    }
                }
        );

        FRestClient.getInstance(mContext).addToRequestQueue(fastRequest, tag_json_obj);
    }

    private void setListView(){
        //用适配器为各选项卡添加所要显示的内容
        for(int i=0;i<7;i++){
            list[i].setAdapter(new PreLessonItemAdapter(mContext,i,lessonList));
        }
    }

    //子 方法:为主界面添加选项卡
    public void addCard(TabHost.TabSpec spec,String tag,int id,String name){
        spec = tabs.newTabSpec(tag);
        spec.setContent(id);
        spec.setIndicator(name);
        tabs.addTab(spec);
    }

    private void setListViewTouchListener(){
        for( int day=0;day<7;day++){
            //为七个ListView绑定触碰监听器，将其上的触碰事件交给GestureDetector处理
            //此监听器是必须的，不然滑动手势只在ListView下的空白区域有效，而在ListView上无效
            list[day].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event)   {
                    return detector.onTouchEvent(event);
                }
            });
            //为每个ListView的每个item绑定监听器，点击则弹出由AlertDialog创建的列表对话框进行选择
            list[day].setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        final int id, long arg3) {
                    final int currentDay=tabs.getCurrentTab();
                    final int n=id;

                }
            });
        }
    }


    //内部类，实现GestureDetector.OnGestureListener接口
    class DetectorGestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        //当用户在触屏上“滑过”时触发此方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            int i = tabs.getCurrentTab();
            if(e1 == null || e2 == null)
                return false;
            //第一个触点事件的X坐标值减去第二个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从右向左滑动
            if(e1.getX() - e2.getX() > FLIP_DISTANCE){
                if(i<6)
                    tabs.setCurrentTab(i+1);
                //	float currentX = e2.getX();
                //	list[i].setRight((int) (inialX - currentX));
                return true;
            }

            //第二个触点事件的X坐标值减去第一个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从左向右滑动
            else if(e2.getX() - e1.getX() > FLIP_DISTANCE){
                if(i>0)
                    tabs.setCurrentTab(i-1);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

    }

    //覆写Activity中的onTouchEvent方法，将该Activity上的触碰事件交给GestureDetector处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
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
