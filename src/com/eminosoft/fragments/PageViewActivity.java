package com.eminosoft.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.eminosoft.activity.GmailActivity;
import com.eminosoft.activity.WeatherActivity;
import com.eminosoft.divid.R;
import com.eminosoft.slidingmenu.NavDrawerItem;
import com.eminosoft.slidingmenu.NavDrawerListAdapter;

public class PageViewActivity extends FragmentActivity implements ActionBar.TabListener {
	public class SlideMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			displayView(position);
		}

		private void displayView(int position) {
			switch (position) {
			case 0:
				/*Intent settings = new Intent(getApplicationContext(),com.eminosoft.activity.LoginActivity.class);
				startActivity(settings);*/
				break;
			case 1:
				Intent weather = new Intent(getApplicationContext(),com.eminosoft.activity.LoginActivity.class);
				startActivity(weather);
				break;
			case 2:
				Intent setings = new Intent(getApplicationContext(),WeatherActivity.class);
				startActivity(setings);
				break;
			case 3:
				/*Intent timer = new Intent(getApplicationContext(),com.eminosoft.activity.RegistrationActivity.class);
				startActivity(timer);*/
				break;
			case 4:
				/*Intent gmail = new Intent(getApplicationContext(),com.eminosoft.activity.GmailActivity.class);
				startActivity(gmail);*/
				break;
			case 5:
				/*Intent gmail = new Intent(getApplicationContext(),com.eminosoft.activity.GmailActivity.class);
				startActivity(gmail);*/
				break;


			default:
				break;
			}
		}
	}

	MyPageAdapter pageAdapter;
	Intent	mShareIntent;

	ViewPager pager;

	private ActionBar actionBar;
	Typeface typeface;

	//For drawer layout
	private DrawerLayout mDrawerLayout;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ArrayList<String> navDrawerItems;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	AlertDialog.Builder alertDialogBuilder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_view);

		alertDialogBuilder = new AlertDialog.Builder(this);

		mShareIntent = new Intent();
		mShareIntent.setAction(Intent.ACTION_SEND);
		mShareIntent.setType("text/plain");
		mShareIntent.putExtra(Intent.EXTRA_TEXT, "Please enter message");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.navigation));
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		ArrayList<NavDrawerItem> ad = new ArrayList<NavDrawerItem>();
		ad.add(new NavDrawerItem("Settings"));
		ad.add(new NavDrawerItem("Profile"));
		ad.add(new NavDrawerItem("Weather Report"));
		ad.add(new NavDrawerItem("Carousel"));
		ad.add(new NavDrawerItem("Contact Divid"));
		ad.add(new NavDrawerItem("Terms & Conditions"));
		NavDrawerListAdapter adp = new NavDrawerListAdapter(getApplicationContext(), ad); 

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerList.setAdapter(adp);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
				// accessibility
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		pager = (ViewPager)findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_share);
		ShareActionProvider  mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(mShareIntent);
		}
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private List<Fragment> getFragments(){
		List<Fragment> fList = new ArrayList<Fragment>();
		fList.add(new SplashScreen());
		fList.add(new PhoneAndContacts());
		fList.add(new SocialNetWorks());
		fList.add(new News());
		fList.add(new GridFragment());
		fList.add(new MyFragmentThree());
		fList.add(new Acount());
		return fList;
	}

	private class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}
		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}


	@Override
	public void onBackPressed() {
		if (pager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the system to handle the
			// Back button. This calls finish() on this activity and pops the back stack.
			//    super.onBackPressed();

			alertDialogBuilder.setMessage("Do you want to close this application ?")  
			.setCancelable(false)  
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int id) {  
					finish();  
				}  
			})  
			.setNegativeButton("No", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int id) {  
					dialog.cancel();  
				}  
			});  

			AlertDialog alert = alertDialogBuilder.create();  
			alert.show();  



		} else {
			// Otherwise, select the previous step.
			pager.setCurrentItem(pager.getCurrentItem() - 1);
		}
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
}
