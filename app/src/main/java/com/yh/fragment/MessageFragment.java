package com.yh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yh.adapter.MsgItemAdapter;
import com.yh.dao.MsgService;
import com.yh.learning.MsgDetailActivity;
import com.yh.learning.R;
import com.yh.utils.AppConstants;
import com.yh.utils.ToastUtil;
import com.yh.view.FProgrssDialog;
import com.yh.view.TitleBarView;
import com.yh.view.XListView;
import com.yh.vo.MsgVO;
import com.yh.vo.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FQ.CHINA on 2016/11/15.
 */
public class MessageFragment extends Fragment implements XListView.IXListViewListener {
    public static final String ITEM_MSG_INFO = "item_msg_info";
    public static final String NEW_MSG_COUNT = "new_msg_count";
    public static final String FROM_MSG_ACTIVITY = "isFromMsgActivity";
    private Context mContext;
    private View mBaseView;
    private TitleBarView titleBarView;
    private XListView mxListView;
    private List<MsgVO> msgList = new ArrayList<MsgVO>();
    private int newMsgCount = 0;
    private PageInfo pageInfo = null;
    private MsgItemAdapter adapter;
    private boolean isFromMsgActivity = false;

    public MessageFragment(){

    }

    public static MessageFragment newInstance(String param,int newMsgCount){
        MessageFragment fragment = new MessageFragment();
        Bundle args=new Bundle();
        args.putString("param",param);
        args.putInt(NEW_MSG_COUNT,newMsgCount);
        fragment.setArguments(args);
        return fragment;
    }
    public static MessageFragment newInstance(String param,int newMsgCount,boolean isFromMsgActivity){
        MessageFragment fragment = new MessageFragment();
        Bundle args=new Bundle();
        args.putString("param",param);
        args.putInt(NEW_MSG_COUNT,newMsgCount);
        args.putBoolean(FROM_MSG_ACTIVITY,isFromMsgActivity);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mBaseView = inflater.inflate(R.layout.fragment_message, null);

        getBundleData();

        findView();

        processLogic();

        setListener();

        return mBaseView;
    }

    private void getBundleData(){
        Bundle bundle = getArguments();
        if(bundle != null){
            newMsgCount = bundle.getInt(NEW_MSG_COUNT, 0);
            isFromMsgActivity = bundle.getBoolean(FROM_MSG_ACTIVITY,false);
        }
    }

    private void findView(){
        titleBarView = (TitleBarView)mBaseView.findViewById(R.id.title_bar);
        titleBarView.setTitleText(R.string.msg_title);
        mxListView = (XListView)mBaseView.findViewById(R.id.msgList);
        mxListView.setPullLoadEnable(true);//加载更多
        mxListView.setPullRefreshEnable(true);//下拉刷新
        mxListView.setXListViewListener(this);
        if (isFromMsgActivity) {
            titleBarView.setTitleComeBackVisibility(View.VISIBLE);
            titleBarView.setTitleComeBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    getActivity().finish();
                }
            });
        }
    }

    private void processLogic(){
        pageInfo = new PageInfo();
        msgList.clear();
        new LoadData().execute(0);
    }

    private void setListener(){
        mxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int postion = i - 1;
                if (msgList.size() > 0 && postion < msgList.size() && postion >= 0) {
                    MsgVO msg = msgList.get(postion);
                    Intent intent = new Intent(mContext, MsgDetailActivity.class);
                    intent.putExtra(ITEM_MSG_INFO, msg);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    startActivity(intent);
                }

            }
        });
    }

    private class LoadData extends AsyncTask<Integer,Void,Integer> {
        FProgrssDialog pDialog = null;
        int isLoadMore = 0;
        @Override
        protected void onPreExecute() {
            pDialog = new FProgrssDialog(mContext);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            isLoadMore = integers[0];
            MsgService service = new MsgService(mContext);
            //把未查看的消息设置为查看
            if(isLoadMore == 0 && newMsgCount > 0){
                service.setMsgStatus(AppConstants.MSG_STATUS_LOOKED);
            }
            List<MsgVO> retList = service.getMsgListPaged(pageInfo);
            if(retList != null && retList.size() > 0){
                msgList.addAll(retList);
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pDialog.dismiss();
            if(msgList != null){
                if(isLoadMore == 1){
                    if(adapter != null){
                        adapter.updateListView(msgList);
                    }
                }else{
                    adapter = new MsgItemAdapter(mContext,msgList);
                    mxListView.setAdapter(adapter);
                }

            }
            onLoad();
        }
    }

    @Override
    public void onRefresh() {
        pageInfo = new PageInfo();
        if(msgList != null)
            msgList.clear();
        new LoadData().execute(0);
    }

    @Override
    public void onLoadMore() {
        if(pageInfo.getOffset() >= pageInfo.getTotals()){
            ToastUtil.showToast(mContext,R.string.xlistview_load_complate);
            onLoad();
            return;
        }else{
            new LoadData().execute(1);
        }
    }

    private void onLoad() {
        mxListView.stopRefresh();
        mxListView.stopLoadMore();
        mxListView.setRefreshTime(getString(R.string.xlistview_refesh_time));
        //设置下一个加载起始
        pageInfo.nextOffset();
    }


}
