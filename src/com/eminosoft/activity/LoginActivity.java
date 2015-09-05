package com.eminosoft.activity;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eminosoft.divid.R;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;


public class LoginActivity extends GmailActivity implements OnClickListener {
	EditText mUsername,mPassword;
	Button mSignin,mSignup;
	TextView mForgotPassword;
	AlertDialog.Builder builder;
	SharedPreferences pref;

	private LoginButton loginBtn;
	private UiLifecycleHelper uiHelper;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static String message = "Sample status posted from android app";


	@SuppressLint({ "NewApi", "NewApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		builder = new AlertDialog.Builder(this);  
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);

		unitUi();

		mSignin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "need to update", 1).show();				
			}
		});

		mSignup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent signUp=new Intent(getApplicationContext(), RegistrationActivity.class);
				startActivity(signUp);
			}
		});
		mForgotPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mForgotPassword=(TextView) findViewById(R.id.forgotPassword);
				mForgotPassword.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showAlert();
					}

					private void showAlert() {
						LinearLayout layout= new LinearLayout(getApplicationContext());
						TextView tvMessage        = new TextView(getApplicationContext());
						TextView tvMessage1       = new TextView(getApplicationContext());
						final EditText etInput    = new EditText(getApplicationContext());

						tvMessage.setText("FORGOT YOUR PASSWORD?");
						tvMessage.setTextSize(20.0f);
						tvMessage1.setText("Send your Email ID To Reset password");
						tvMessage1.setTextSize(15.0f);
						etInput.setSingleLine();
						etInput.setHint("Email ID");
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.addView(tvMessage);
						layout.addView(tvMessage1);
						layout.addView(etInput);
						builder.setView(layout);
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String name = etInput.getText().toString();
								Toast.makeText(getApplicationContext(), ""+name, 1).show();
							}
						});
						builder.show();					
					}
				});
			}
		});

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {

			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {

					StringBuilder userInfo = new StringBuilder("");              
					String Name =  user.getName();
					String Id =   user.getId();
					String lastname =    user.getLastName();
					String firstname =  user.getFirstName();
					String getUsername =  user.getUsername(); 
					String get_gender = (String) user.getProperty("gender");
					String imageURL = "http://graph.facebook.com/"+user.getId()+"/picture?type=square";        

					Editor editor = pref.edit();
					editor.putString("Name", Name);
					editor.putString("Id",Id);
					editor.commit();

					Intent i =new Intent(getApplicationContext(), FacebookActivity.class);
					startActivity(i);
					return;

				} else {
					//Toast.makeText(getApplicationContext(), "user was " +user, 1).show();
				}
			}
		});
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if (state.isClosed()) {
				Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};

	public boolean checkPermissions() {
		Session s = Session.getActiveSession();
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}
	public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
		
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}
	private void unitUi() {
		mUsername=(EditText) findViewById(R.id.userName);
		mPassword=(EditText) findViewById(R.id.passWord);
		mSignin=(Button) findViewById(R.id.signIn);
		mSignup=(Button) findViewById(R.id.signUp);
		mForgotPassword=(TextView) findViewById(R.id.forgotPassword);
	}
	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
		uiHelper.onResume();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}