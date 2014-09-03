package com.company.demo.activity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apple.activity.BaseFragment;
import com.company.demo.R;


public class HomeFragment extends BaseFragment {
	
	private Context mcontext;
	private ListView xList;
	RelativeLayout focusLayout;
    TextView voice_title;
    DemoAdapter adapter;
	public HomeFragment() {

	}

	@Override
	public void treatClickEvent(int id) {
		// TODO Auto-generated method stub
		super.treatClickEvent(id);

	}

	@Override
	protected void initLisener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.layout_home, null);
		xList = (ListView) mView.findViewById(R.id.homeListview);
		return mView;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mcontext=this.getActivity();
		adapter=new DemoAdapter(mcontext);
		xList.setAdapter(adapter);
		onInitList(1);
	}

	
	/**
	 * 初始化新闻列表数据
	 */
	public void onInitList(int page){
		initParam();
		sendRequest(DemoHttpRes.REQ_METHOD_GET_DEMO_HOME, DemoHttpRes.getInstance().getUrl(DemoHttpRes.REQ_METHOD_GET_DEMO_HOME),
				mParams,mAct.getAsyncClient(), false);
	}

	@Override
	public void onSuccess(String content, Object object, int reqType) {
		// TODO Auto-generated method stub
		if(reqType==DemoHttpRes.REQ_METHOD_GET_DEMO_HOME){
			DemoEntity entity=(DemoEntity)object;
			Log.i("HU", "onSuccess===>");
			
			adapter.setData(entity);
		}
		
	}

	@Override
	public void onFailure(Throwable error, String content, int reqType) {
		// TODO Auto-generated method stub
		
	}	
	
}
