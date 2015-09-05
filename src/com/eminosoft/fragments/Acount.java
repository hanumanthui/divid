package com.eminosoft.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eminosoft.divid.R;

public class Acount extends Fragment {
	Typeface typeface;
	TextView text,text1,text2,text3;
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.myfragment_acount, container,false);
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BastardusSans.ttf");

		text = (TextView) v.findViewById(R.id.textView1);
		text.setTypeface(typeface);
		text1 = (TextView) v.findViewById(R.id.myaddress);
		text1.setTypeface(typeface);
		text2 = (TextView) v.findViewById(R.id.time);
		text2.setTypeface(typeface);
		text3 = (TextView) v.findViewById(R.id.pool);
		text3.setTypeface(typeface);

		return v;
	}

	private void startNewActivity(FragmentActivity activity, String packageName) {
		Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);

		if (intent != null) {
			/* We found the activity now start the activity */
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getActivity().startActivity(intent);
		} else {
			/* Bring user to the market or let them choose an app? */
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id=" + packageName));
			getActivity().startActivity(intent);
		}

	}
}
