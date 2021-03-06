/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2014 Zhenghong Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eminosoft.activity;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.UNIT;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eminosoft.divid.R;

public class WeatherActivity extends Activity implements YahooWeatherInfoListener,
YahooWeatherExceptionListener {

	private ImageView mIvWeather0;
	private TextView mTvWeather0;
	private TextView mTvErrorMessage;
	private TextView mTvTitle;
	private EditText mEtAreaOfCity;
	private Button mBtSearch;
	private Button mBtGPS;
	AlertDialog.Builder builder;
	private LinearLayout mWeatherInfosLayout;

	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);

	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_report);
		builder = new AlertDialog.Builder(this);

		mYahooWeather.setExceptionListener(this);

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Please wait..");
		mProgressDialog.show();

		mTvTitle = (TextView) findViewById(R.id.textview_title);
		mTvWeather0 = (TextView) findViewById(R.id.textview_weather_info_0);
		mTvErrorMessage = (TextView) findViewById(R.id.textview_error_message);
		mIvWeather0 = (ImageView) findViewById(R.id.imageview_weather_info_0);

		mEtAreaOfCity = (EditText) findViewById(R.id.edittext_area);

		mBtSearch = (Button) findViewById(R.id.search_button);
		mBtSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String _location = mEtAreaOfCity.getText().toString();
				if (!TextUtils.isEmpty(_location)) {
					InputMethodManager imm = (InputMethodManager)getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);
					searchByPlaceName(_location);	
					showProgressDialog();
				} else {
					Toast.makeText(getApplicationContext(), "location is not inputted", 1).show();
				}
			}
		});

		mBtGPS = (Button) findViewById(R.id.gps_button);
		mBtGPS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchByGPS();
				showProgressDialog();
			}
		});

		mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);

		searchByGPS();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		hideProgressDialog();
		mProgressDialog = null;
		super.onDestroy();
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		hideProgressDialog();
		if (weatherInfo != null) {
			setNormalLayout();
			if (mYahooWeather.getSearchMode() == SEARCH_MODE.GPS) {
				mEtAreaOfCity.setText("Your Current Location");
			}
			mWeatherInfosLayout.removeAllViews();
			mTvTitle.setText(
					weatherInfo.getTitle() + "\n"
							+ weatherInfo.getWOEIDneighborhood() + ", "
							+ weatherInfo.getWOEIDCounty() + ", "
							+ weatherInfo.getWOEIDState() + ", " 
							+ weatherInfo.getWOEIDCountry());
			mTvWeather0.setText("====== CURRENT ======" + "\n" +
					"date: " + weatherInfo.getCurrentConditionDate() + "\n" +
					"weather: " + weatherInfo.getCurrentText() + "\n" +
					"temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
					"wind chill: " + weatherInfo.getWindChill() + "\n" +
					"wind direction: " + weatherInfo.getWindDirection() + "\n" +
					"wind speed: " + weatherInfo.getWindSpeed() + "\n" +
					"Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
					"Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
					"Visibility: " + weatherInfo.getAtmosphereVisibility()
					);
			if (weatherInfo.getCurrentConditionIcon() != null) {
				mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
			}
			for (int i = 0; i < YahooWeather.FORECAST_INFO_MAX_SIZE; i++) {
				final LinearLayout forecastInfoLayout = (LinearLayout) 
						getLayoutInflater().inflate(R.layout.forecastinfo, null);
				final TextView tvWeather = (TextView) forecastInfoLayout.findViewById(R.id.textview_forecast_info);
				final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);
				tvWeather.setText("====== FORECAST " + (i+1) + " ======" + "\n" +
						"date: " + forecastInfo.getForecastDate() + "\n" +
						"weather: " + forecastInfo.getForecastText() + "\n" +
						"low  temperature in ºC: " + forecastInfo.getForecastTempLow() + "\n" +
						"high temperature in ºC: " + forecastInfo.getForecastTempHigh() + "\n"
						//						           "low  temperature in ºF: " + forecastInfo.getForecastTempLowF() + "\n" +
						//				                   "high temperature in ºF: " + forecastInfo.getForecastTempHighF() + "\n"
						);
				final ImageView ivForecast = (ImageView) forecastInfoLayout.findViewById(R.id.imageview_forecast_info);
				if (forecastInfo.getForecastConditionIcon() != null) {
					ivForecast.setImageBitmap(forecastInfo.getForecastConditionIcon());
				}
				mWeatherInfosLayout.addView(forecastInfoLayout);
			}
		} else {
			setNoResultLayout();
		}
	}

	@Override
	public void onFailConnection(final Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailParsing(final Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailFindLocation(final Exception e) {
		// TODO Auto-generated method stub

	}

	private void setNormalLayout() {
		mWeatherInfosLayout.setVisibility(View.VISIBLE);
		mTvTitle.setVisibility(View.VISIBLE);
		mTvErrorMessage.setVisibility(View.INVISIBLE);
	}

	private void setNoResultLayout() {
		mTvTitle.setVisibility(View.INVISIBLE);
		mWeatherInfosLayout.setVisibility(View.INVISIBLE);
		mTvErrorMessage.setVisibility(View.VISIBLE);
		mTvErrorMessage.setText("Sorry, no result returned");
		mProgressDialog.cancel();
	}

	private void searchByGPS() {
		
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			mYahooWeather.setNeedDownloadIcons(true);
			mYahooWeather.setUnit(UNIT.CELSIUS);
			mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
			mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);
		} else {
			showGPSDisabledAlertToUser();
		}
	}

	private void showGPSDisabledAlertToUser() {

		builder.setMessage(
				"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent callGPSSettingIntent = new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(callGPSSettingIntent);
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void searchByPlaceName(String location) {
		mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setUnit(UNIT.CELSIUS);
		mYahooWeather.setSearchMode(SEARCH_MODE.PLACE_NAME);
		mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, WeatherActivity.this);
	}

	private void showProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
		mProgressDialog = new ProgressDialog(WeatherActivity.this);
		mProgressDialog.setMessage("Please wait..");
		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
	}

	
}
