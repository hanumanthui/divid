package com.eminosoft.fragments;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.UNIT;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eminosoft.activity.WeatherActivity;
import com.eminosoft.divid.R;

public class WeatherFragment extends Fragment  implements YahooWeatherInfoListener,
YahooWeatherExceptionListener {
	
	
	private static final String LOCATION_SERVICE = null;
	private ImageView mIvWeather0;
	private TextView mTvWeather0;
	private TextView mTvErrorMessage;
	private TextView mTvTitle;
	private EditText mEtAreaOfCity;
	private Button mBtSearch;
	private Button mBtGPS;
	AlertDialog.Builder builder;
	private LinearLayout mWeatherInfosLayout;
	LocationManager locationManager;

	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);

	private ProgressDialog mProgressDialog;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.weather_report, container,false);
		
		mYahooWeather.setExceptionListener(this);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Please wait..");
		mProgressDialog.show();

		mTvTitle = (TextView) v.findViewById(R.id.textview_title);
		mTvWeather0 = (TextView) v.findViewById(R.id.textview_weather_info_0);
		mTvErrorMessage = (TextView) v.findViewById(R.id.textview_error_message);
		mIvWeather0 = (ImageView) v.findViewById(R.id.imageview_weather_info_0);

		mEtAreaOfCity = (EditText) v.findViewById(R.id.edittext_area);

		mBtSearch = (Button) v.findViewById(R.id.search_button);
		
		
		mBtSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String _location = mEtAreaOfCity.getText().toString();
				if (!TextUtils.isEmpty(_location)) {
					InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
							Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mEtAreaOfCity.getWindowToken(), 0);
					searchByPlaceName(_location);	
					showProgressDialog();
				} else {
					Toast.makeText(getActivity(), "location is not inputted", 1).show();
				}
			}
		});

		mBtGPS = (Button) v.findViewById(R.id.gps_button);
		mBtGPS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchByGPS();
				showProgressDialog();
			}
		});

		mWeatherInfosLayout = (LinearLayout) v.findViewById(R.id.weather_infos);

		searchByGPS();
		return v;
	}

	@Override
	public void onDestroy() {
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
						getActivity().getLayoutInflater().inflate(R.layout.forecastinfo, null);
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
		
		 locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			
			mYahooWeather.setNeedDownloadIcons(true);
			mYahooWeather.setUnit(UNIT.CELSIUS);
			mYahooWeather.setSearchMode(SEARCH_MODE.GPS);
			mYahooWeather.queryYahooWeatherByGPS(getActivity(), WeatherFragment.this);
		} else {
			showGPSDisabledAlertToUser();
		}
	}

	private LocationManager getSystemService(String locationService) {
		// TODO Auto-generated method stub
		return null;
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
		mYahooWeather.queryYahooWeatherByPlaceName(getActivity(), location, WeatherFragment.this);
	}

	private void showProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage("Please wait..");
		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
		}
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
