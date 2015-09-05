package com.eminosoft.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eminosoft.Utils.DividUtils;
import com.eminosoft.divid.R;

public class RegistrationActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {

	Button mSignup;
	TextView mGender, mSignIn;
	EditText mFirstName, mLastName, mEmail, mMobile, mPassWord,
			mConfirmPassWord, mDateOfBirth;
	DatePicker mDatePicker;
	private int year, month, day;
	JSONObject signUpParams;
	JSONObject emailObj;
	JSONObject mobileObj;
	static final int DATE_DIALOG_ID = 999;

	String fName, lName, pWord, confirmPword, email, mobile, dob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		unitUi();
		setCurrentDateOnView();
		addListenerOnButton();
	}

	// for date picker
	private void addListenerOnButton() {
		mDatePicker = (DatePicker) findViewById(R.id.datePicker1);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		// set current date into datepicker
		mDatePicker.init(year, month, day, null);

	}

	private void setCurrentDateOnView() {
		mDateOfBirth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			// set selected date into textview
			mDateOfBirth.setText(new StringBuilder().append(month + 1)
					.append(" ").append("-").append(day).append("-")
					.append(year));
			// set selected date into datepicker also
			mDatePicker.init(year, month, day, null);
		}
	};

	private void unitUi() {

		mFirstName = (EditText) findViewById(R.id.firstName);
		mLastName = (EditText) findViewById(R.id.lastName);
		
		mEmail = (EditText) findViewById(R.id.email);
		mEmail.setOnFocusChangeListener((OnFocusChangeListener) this);
 
		mMobile = (EditText) findViewById(R.id.mobile);
		mMobile.setOnFocusChangeListener((OnFocusChangeListener) this);
		
		mPassWord = (EditText) findViewById(R.id.passWord);
		
		mConfirmPassWord = (EditText) findViewById(R.id.confirmPassword);
		mConfirmPassWord.setOnFocusChangeListener((OnFocusChangeListener) this);
		mDateOfBirth = (EditText) findViewById(R.id.DOB);
		mDateOfBirth.setOnFocusChangeListener((OnFocusChangeListener) this);

		mSignup = (Button) findViewById(R.id.signUp);
		mSignup.setOnClickListener(this);

		mSignIn = (TextView) findViewById(R.id.signIn);
		mSignIn.setOnClickListener(this);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.email:
			Toast.makeText(getApplicationContext(), "EMAIL", 1).show();
			if (hasFocus) {
			} else {
				final String userEmail = mEmail.getText().toString();
				emailObj = new JSONObject();
				try {

					emailObj.put("userEmail", userEmail);
					// signUpParams.put("Password", sPassword);
					new EmailCheck().execute();

				} catch (Exception e) {
					Log.d("Error ", "- " + e);
				}

			}
		case R.id.mobile:
			if (hasFocus) {

			} else {
				Toast.makeText(getApplicationContext(), "MOBILE", 1).show();
				final String userMobile = mMobile.getText().toString();
				mobileObj = new JSONObject();
				try {
					mobileObj.put("userMobileNumber", userMobile);
					new MobileCheck().execute();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;
		case R.id.confirmPassword :
			
			if(!mPassWord.getText().toString().equals(mConfirmPassWord.getText().toString()))
			{
				mConfirmPassWord.setError("Should Match with password");
			}
			
			break ;
		case R.id.passWord:
			isValidPassword();
			break;
		case R.id.firstName:
			isValidFName();
			break;
		case R.id.lastName:
			isValidLName();
			break;

		default:
			break;
		}
	}

	private boolean isValidMail() {
		email = mEmail.getText().toString().trim();
		boolean isValideMail = true;
		if (email.length() == 0) {
			mEmail.setError("Enter eMail");
			isValideMail = false;
		}
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		boolean isValidMail = matcher.matches();
		if (isValidMail == false) {
			mEmail.setError("Enter Valid Mail");
			isValideMail = false;
		}
		checkEmailAvailability(email);
		return isValideMail;
	}

	// ////Need to update
	private void checkEmailAvailability(String eMail) {
		final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

	}

	private boolean isValidPhone() {
		boolean isValidPhone = true;
		mobile = mMobile.getText().toString();
		if (mobile.length() <= 9) {
			mMobile.setError("Enter 10 digit telephone number");
			isValidPhone = false;
		}
		return isValidPhone;
	}

	private void checkMobileAvailability(String mobileNumber) {

		final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

	}

	private boolean isValidPassword() {
		boolean isValidPassword = true;
		pWord = mPassWord.getText().toString();
		if (pWord.length() <= 7) {
			mPassWord.setError("Enter atleast 8 characters!!");
			isValidPassword = false;
		}
		return isValidPassword;
	}

	private boolean isValidConfirmPassword() {
		boolean isValidConfirmPassword = true;
		pWord = mPassWord.getText().toString();
		confirmPword = mConfirmPassWord.getText().toString();
		if (pWord.equals(confirmPword)) {

		} else {
			mConfirmPassWord.setError("Passwords do not match");
			isValidConfirmPassword = false;
		}

		return isValidConfirmPassword;

	}

	private boolean isValidFName() {
		boolean isValidName = true;
		fName = mFirstName.getText().toString();
		if (fName.equals("") || fName.length() == 0 || fName == null) {
			mFirstName.setError("Enter First Name");
			isValidName = false;
		} else if (fName.length() <= 4) {
			mFirstName.setError("Enter atleast 5 characters");
			isValidName = false;
		}
		return isValidName;
	}

	private boolean isValidLName() {
		boolean isValidLName = true;
		lName = mLastName.getText().toString();
		if (lName.equals("") || lName.length() == 0 || lName == null) {
			mLastName.setError("Enter Last Name");
			isValidLName = false;
		} else if (lName.length() <= 4) {
			mLastName.setError("Enter atleast 5 characters");
			isValidLName = false;
		}

		return isValidLName;
	}

	private boolean isValidDateOfBirth() {
		boolean isValidDateOfBirth = true;
		dob = mDateOfBirth.getText().toString();
		if (dob.length() <= 0) {
			mDateOfBirth.setError("Enter DateOfBirth");
			isValidDateOfBirth = false;
		}
		return isValidDateOfBirth;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUp:
			signUp();
			break;
		case R.id.signIn:
			Intent signin = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(signin);

			break;

		default:
			break;
		}
	}

	private void signUp() {
		boolean isValidConfirmPassword, isValidMail, isValidPassword, isValidFName, isValidLName, isValidPhone, isValidDateOfBirth;

		isValidMail = isValidMail();
		isValidPassword = isValidPassword();
		isValidConfirmPassword = isValidConfirmPassword();
		isValidFName = isValidFName();
		isValidLName = isValidLName();
		isValidPhone = isValidPhone();
		isValidDateOfBirth = isValidDateOfBirth();

		if (isValidMail == true && isValidPassword == true
				&& isValidConfirmPassword == true && isValidFName == true
				&& isValidLName == true && isValidPhone == true
				&& isValidDateOfBirth == true) {

			Toast.makeText(getApplicationContext(),
					"need to implement registration ", 1).show();

		}
	}

	public class EmailCheck extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String responseCode = "";
			HttpClient clien = new DefaultHttpClient();
			HttpPost ppost = new HttpPost(DividUtils.emailAvailability);
			StringEntity se;
			try {
				se = new StringEntity(emailObj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				ppost.setEntity(se);
				HttpResponse response = clien.execute(ppost);
				if (response != null) {
					// InputStream in = response.getEntity().getContent(); //Get
					// the data in the entity
					String temp = EntityUtils.toString(response.getEntity());
					JSONObject js = new JSONObject(temp);
					final String status = js.getString("status");
					responseCode = js.getString("responseCode");

					Log.i("tag", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return responseCode;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("1")) {
				mEmail.setError("Email Exists");
			}

		}

	}

	public class MobileCheck extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String responseCode = "";
			HttpClient clien = new DefaultHttpClient();
			HttpPost ppost = new HttpPost(DividUtils.mobileAvailability);
			StringEntity se;
			try {
				se = new StringEntity(mobileObj.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				ppost.setEntity(se);
				HttpResponse response = clien.execute(ppost);
				if (response != null) {
					// InputStream in = response.getEntity().getContent(); //Get
					// the data in the entity
					String temp = EntityUtils.toString(response.getEntity());
					JSONObject js = new JSONObject(temp);
					final String status = js.getString("status");
					responseCode = js.getString("responseCode");

					Log.i("tag", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return responseCode;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals("1")) {
				mMobile.setError("Mobile Exists");
			}

		}

	}

}
