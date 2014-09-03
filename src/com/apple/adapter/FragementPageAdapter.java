package com.apple.adapter;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apple.activity.BaseFragment;

public class FragementPageAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> list;

	public FragementPageAdapter(FragmentManager fm, List<BaseFragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BaseFragment getItem(int position) {
		return list.get(position);
	}

}
