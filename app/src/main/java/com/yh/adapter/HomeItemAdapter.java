package com.yh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yh.learning.LessonActivity;
import com.yh.learning.MsgActivity;
import com.yh.learning.MySettingActivity;
import com.yh.learning.PreLessonActivity;
import com.yh.learning.R;


/**
 * Created by FQ.CHINA on 2016/11/16.
 */
public class HomeItemAdapter extends BaseAdapter{
    private final int[] IMGs = {R.mipmap.course,R.mipmap.message,R.mipmap.course_classk12,R.mipmap.user};
    private final int[] NAMEs = {R.string.course,R.string.message,R.string.beike,R.string.my_info};
    private final Class[] activities = {LessonActivity.class,MsgActivity.class, PreLessonActivity.class, MySettingActivity.class};
    private Context mContext;
    public HomeItemAdapter(){

    }
    public HomeItemAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return IMGs.length;
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
        ItemHolder holder;
        final Class activity = activities[i];
        if(view == null){
            holder = new ItemHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.fragment_home_item, null);
            holder.item_img = (ImageView)view.findViewById(R.id.item_img);
            holder.item_name = (TextView)view.findViewById(R.id.item_name);
            holder.home_rl = (RelativeLayout)view.findViewById(R.id.home_rl);
            view.setTag(holder);
            view.setPadding(15, 15, 15, 15);
        }else {
            holder = (ItemHolder) view.getTag();
        }
        holder.item_img.setImageResource(IMGs[i]);
        holder.item_name.setText(NAMEs[i]);
        holder.home_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,activity);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ItemHolder{
        ImageView item_img;
        TextView item_name;
        RelativeLayout home_rl;
    }
}
