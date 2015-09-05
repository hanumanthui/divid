package com.eminosoft.fragments;

import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.eminosoft.adapter.GridAdapter;
import com.eminosoft.divid.R;

public class GridFragment extends Fragment {
	PackageManager myPackageManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		myPackageManager = getActivity().getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> appIntentList = getActivity().getPackageManager().queryIntentActivities(intent, 0);

		GridView gridview = new GridView(getActivity());

		gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridview.setNumColumns(GridView.AUTO_FIT);
		gridview.setHorizontalSpacing(30);
		gridview.setVerticalSpacing(30);
		gridview.setNumColumns(3);
		gridview.setGravity(Gravity.CENTER);
		gridview.setColumnWidth(100);
		gridview.setPadding(10, 10, 10, 10);
		gridview.setBackgroundResource(R.drawable.d);

		gridview.setAdapter(new GridAdapter(getActivity(), appIntentList));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ResolveInfo cleckedResolveInfo = (ResolveInfo) parent.getItemAtPosition(position);
				ActivityInfo clickedActivityInfo = cleckedResolveInfo.activityInfo;

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClassName(clickedActivityInfo.applicationInfo.packageName,clickedActivityInfo.name);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(intent);
			}
		});

		View view = gridview;
		return view;
	}
}
