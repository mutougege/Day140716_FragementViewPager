package com.zzw.day140716_fragementviewpager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AFragment extends Fragment{
	
	private final String TAG = "AFragment";
	private Activity activity;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		if (inflater == null) {
			inflater = LayoutInflater.from(mContext);
		}
		return inflater.inflate(R.layout.a, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.i(TAG, "onViewCreated");
		initView(view);
		initData();
	}

	private void initData() {
		
	}

	private void initView(View view) {
		
	}
}