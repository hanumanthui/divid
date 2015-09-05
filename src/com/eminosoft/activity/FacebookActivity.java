package com.eminosoft.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.eminosoft.divid.R;
import com.squareup.picasso.Picasso;

public class FacebookActivity extends Activity {

	TextView mName,mId,mEmail;
	SharedPreferences pref;
	ImageView	userpicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		

		unitUi();

		String Name = pref.getString("Name", null);
		String Id = pref.getString("Id", null);

		mName.setText(Name);
		mId.setText(Id);
		mEmail.setText(Id+"@dummyfbemail.com");
		Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + Id+ "/picture?type=large").into(userpicture);
	}

	private void unitUi() {
		mName=(TextView) findViewById(R.id.name);	
		mEmail=(TextView) findViewById(R.id.Email);
		mId=(TextView) findViewById(R.id.Id);

		userpicture=(ImageView)findViewById(R.id.pp);

	}
}
