package com.yh.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yh.learning.R;
import com.yh.vo.MsgVO;

import java.util.List;

/**
 * Created by FQ.CHINA on 2016/11/23.
 */

public class MsgItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<MsgVO> msgList;


    public MsgItemAdapter(Context mContext,List<MsgVO> msgList){
        this.mContext = mContext;
        this.msgList = msgList;
    }

    public void updateListView(List<MsgVO> msgList){
        this.msgList = msgList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (msgList != null)
            count = msgList.size();
        return count;
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
        MsgHolder holder;
        final MsgVO course = msgList.get(i);
        if(view == null) {
            holder = new MsgHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.fragment_message_item, null);
            holder.title = (TextView)view.findViewById(R.id.title);
            view.setTag(holder);
        }else{
            holder = (MsgHolder)view.getTag();
        }
        holder.title.setText(course.getTitle());
        return view;
    }

    class MsgHolder{
        TextView title;
    }
}
