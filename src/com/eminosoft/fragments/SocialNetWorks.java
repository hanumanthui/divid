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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.eminosoft.divid.R;

public class SocialNetWorks extends Fragment {

	Typeface typeface; 
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.myfragment_layout_three, container,false);
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BastardusSans.ttf");

		TextView text = (TextView) v.findViewById(R.id.textView1);
		text.setTypeface(typeface);
		Button button = (Button) v.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "com.facebook.katana");

			}

		});
		Button button2 = (Button) v.findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "com.whatsapp");

			}

		});
		Button button3 = (Button) v.findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startNewActivity(getActivity(), "com.twitter.android");

			}

		});
		WebView	webView = (WebView) v.findViewById(R.id.webView1);

		webView.getSettings().setJavaScriptEnabled(true);

		webView.loadUrl("file:///android_asset/filename.html");

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
