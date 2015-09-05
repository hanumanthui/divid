package com.eminosoft.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eminosoft.divid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class News extends Fragment {

	private AdView adView;
	private final String unitid = "a14f840b733d0dc";
	private final String unitid1 = "ca-app-pub-3940256099942544/6300978111";
	ImageView img;
	Typeface typeface;
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.myfragment_layout_five, container,false);

		LinearLayout rootLayout = (LinearLayout) v.findViewById(R.id.rootViewGroup);
		adView = new AdView(getActivity());
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(unitid);
		rootLayout.addView(adView, 0);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
		LinearLayout rootLayout1 = (LinearLayout) v.findViewById(R.id.rootViewGroup1);
		adView = new AdView(getActivity());
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(unitid1);
		rootLayout.addView(adView, 0);
		AdRequest adRequest1 = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		

		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BastardusSans.ttf");
		img=(ImageView) v.findViewById(R.id.imageView1);
		TextView text = (TextView) v.findViewById(R.id.textView1);
		text.setTypeface(typeface); 
		Button button = (Button) v.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "com.cnn.mobile.android.phone");

			}
		});
		Button button2 = (Button) v.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "in.AajTak.headlines");

			}
		});
		Button button3 = (Button) v.findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "com.eterno");

			}
		});

		return v;
	}

	private void startNewActivity(FragmentActivity activity, String packageName) {
		Intent intent = getActivity().getPackageManager()
				.getLaunchIntentForPackage(packageName);
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
