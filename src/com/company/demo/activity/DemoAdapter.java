package com.company.demo.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.company.demo.R;
import com.company.demo.activity.DemoEntity.DemoListEntity;
public class DemoAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private DemoEntity entity;

	public DemoAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setData(DemoEntity mEntity) {
		this.entity = mEntity;
		notifyDataSetChanged();
	}

	public DemoEntity getData() {
		return entity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (entity!=null&&entity.mList != null && entity.mList.size() > 0) {
			return entity.mList.size();
		}
		return 0;
	}

	@Override
	public DemoListEntity getItem(int position) {
		// TODO Auto-generated method stub
		if (entity.mList != null && entity.mList.size() > 0) {
			return entity.mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DemoListEntity entity = getItem(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_demo, null);
			viewHolder.txt_top = (TextView) convertView
					.findViewById(R.id.txt_top);
			viewHolder.txt_bottom = (TextView) convertView
					.findViewById(R.id.txt_bottom);
			convertView.setBackgroundColor(Color.GRAY);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txt_top.setText(entity.name);
		viewHolder.txt_bottom.setText(entity.address);
		return convertView;
	}

	private class ViewHolder {
		public TextView txt_top;
		public TextView txt_bottom;
	}

}
