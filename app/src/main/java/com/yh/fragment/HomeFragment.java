package com.yh.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yh.adapter.HomeItemAdapter;
import com.yh.adapter.ViewPagerAdapter;
import com.yh.learning.R;
import com.yh.utils.BitmapUtils;
import com.yh.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/11/15.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private Context mContext;
    private View mBaseView;

    private int width;
    private int height;
    private List<View> views;
    private ViewPager vp;
    private RelativeLayout relativeLayout;
    private ViewPagerAdapter vpAdapter;
    private ClearEditText filter_edit;
    private GridView  gridView;
    // 引导图片资源
    private static final int[] pics = { R.mipmap.main_show1, R.mipmap.main_show2,
            R.mipmap.main_show3, R.mipmap.side_banner };

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    public HomeFragment(){

    }

    public static HomeFragment newInstance(String param){
        HomeFragment fragment = new HomeFragment();
        Bundle args=new Bundle();
        args.putString("param",param);
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_home, null);

        findView();

        init();

        processLogic();

        return mBaseView;
    }


    private void findView(){
        vp = (ViewPager) mBaseView.findViewById(R.id.viewpager);
        relativeLayout = (RelativeLayout)mBaseView.findViewById(R.id.relative);
        filter_edit = (ClearEditText)mBaseView.findViewById(R.id.filter_edit);
        filter_edit.clearFocus();
        gridView = (GridView)mBaseView.findViewById(R.id.homeGrid);
    }

    private void init(){

        //等到屏幕的大小

        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //以670*240的图片为例,正常中不要这样用
        height	=width*240/670;

        LinearLayout.LayoutParams layoutParams	= (LinearLayout.LayoutParams)relativeLayout.getLayoutParams();
        layoutParams.width	= width;
        layoutParams.height	= height;
        relativeLayout.setLayoutParams(layoutParams);

        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(mParams);
            //改变大小
            //iv.setImageResource(pics[i]);
            iv.setImageBitmap(BitmapUtils.zoomImage(BitmapFactory.decodeResource(getResources(), pics[i]), width, height));
            views.add(iv);
        }

        RelativeLayout.LayoutParams params		= new RelativeLayout.LayoutParams(width, height);
//        params.leftMargin=6;
//        params.topMargin=2;
//        params.rightMargin=6;
        vp.setLayoutParams(params);

        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
        initDots();
    }


    private void initDots() {
        LinearLayout ll = (LinearLayout)mBaseView.findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        vp.requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        setCurView(position);
        setCurDot(position);
    }

    private void processLogic(){
        HomeItemAdapter adapter = new HomeItemAdapter(mContext);
        gridView.setAdapter(adapter);
    }
}
