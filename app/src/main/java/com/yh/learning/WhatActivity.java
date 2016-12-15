package com.yh.learning;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;

import com.yh.utils.SpUtil;

public class WhatActivity extends Activity{
	private ViewPager mViewPager;	
	
	private SharedPreferences sp;
	
	private int currIndex = 0;
	
	private boolean isWhats = false;//是否已经点击过轮播
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        sp = SpUtil.getSharePerference(this);
		isWhats = sp.getBoolean("isWhats",false);

		//只给用户播放一次轮播
		if(isWhats){
			gotoMain();
		}
		
        setContentView(R.layout.activity_whats);
        mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);        
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
       
      //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.activity_whats1, null);
        View view2 = mLi.inflate(R.layout.activity_whats2, null);
        View view3 = mLi.inflate(R.layout.activity_whats3, null);
        View view4=  mLi.inflate(R.layout.activity_whats4, null);

        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);

        
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
    }    
    

    public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			if(arg0<4)
				currIndex = arg0;
			else{
				startbutton(null);
			}
			//animation.setFillAfter(true);// True:图片停在动画结束位置
			//animation.setDuration(300);
			//mPageImg.startAnimation(animation);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
    public void startbutton(View v) {  
      	Intent intent = new Intent();
		intent.setClass(this,MainActivity.class);
		startActivity(intent);
		
		Editor ed = sp.edit();
		ed.putBoolean("isWhats",true);
		ed.commit();
		
		this.finish();
      }  
    
	private void gotoMain(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		finish();
	}

	private void gotoLogin(){
		Intent intent = new Intent(this,LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
		finish();
	}
}
