package com.apple.adapter;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;




public abstract class BaseListAdapter<T> extends BaseAdapter {

	public ArrayList<T> mListData;

	public LayoutInflater mInflater;

	public BaseListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<T> data) {
		mListData = data;
		notifyDataSetChanged();
	}

	
	@Override
	public T getItem(int position) {
		if (mListData == null)
			return null;
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getCount() {
		if (mListData == null)
			return 0;
		return mListData.size();
	}

	
}
