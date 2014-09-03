package com.company.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageButton;

import com.apple.activity.BaseFragment;
import com.apple.adapter.FragementPageAdapter;
import com.apple.view.CustomViewPager;
import com.company.demo.R;



public class HomeActivity extends DemoBaseActivity {
	
	
	private CustomViewPager viewpager;
	private FragementPageAdapter adapter;
	private List<BaseFragment> viewList;
	private ImageButton btn_action,btn_group, btn_news,btn_games;
	
	@Override
	protected void initView(Bundle bundle) {
		// TODO Auto-generated method stub
		setContentView(R.layout.main);
		btn_action = (ImageButton) this.findViewById(R.id.btn_action);
		btn_group = (ImageButton) this.findViewById(R.id.btn_group);
		btn_news = (ImageButton) this.findViewById(R.id.btn_news);
		btn_games = (ImageButton) this.findViewById(R.id.btn_games);
		viewpager = (CustomViewPager) findViewById(R.id.viewpager);
	}

	
	
	@Override
	protected void initLisener() {
		// TODO Auto-generated method stub
		setOnClickListener(R.id.btn_action);
		setOnClickListener(R.id.btn_group);
		setOnClickListener(R.id.btn_news);
		setOnClickListener(R.id.btn_games);
	}

	@Override
	protected void initData(Bundle bundle) {
		// TODO Auto-generated method stub
		viewList = new ArrayList<BaseFragment>();
		viewList.add(new HomeFragment());
		adapter = new FragementPageAdapter(getSupportFragmentManager(), viewList);
		viewpager.setAdapter(adapter);
		viewpager.setScanScroll(false);
		viewpager.setCurrentItem(1, false);
		viewpager.setOffscreenPageLimit(viewList.size());
	}

	
	
	
}
