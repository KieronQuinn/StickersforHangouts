package com.quinny898.app.stickersforhangouts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class StickerPickerActivity extends ActionBarActivity {
	SharedPreferences prefs;
	ViewPager mViewPager;
	String[] mViewPagerTabsTitles;
	Fragment[] mViewPagerFragments;
	boolean isUserFragment;
	static ActionBarActivity activity;
	private StartAppAd startAppAd = new StartAppAd(this);
	static StartAppAd saa;
	static String[] gifs = new String[] { "angry.gif", "cake.gif",
			"cathappy.gif", "catheart.gif", "celebration.gif", "cheers.gif",
			"confused.gif", "crying.gif", "doublehighfive.gif",
			"fist_bump.gif", "flexedarm.gif", "frustrated.gif", "ghost.gif",
			"haloguy.gif", "happy.gif", "happyblushing.gif", "happycrying.gif",
			"hearteyed.gif", "joy.gif", "kiss.gif", "lol.gif", "meh.gif",
			"mumstheword.gif", "music.gif", "ohno.gif", "okay.gif",
			"omgterrified.gif", "poop.gif", "praying.gif", "princess.gif",
			"sad.gif", "scared.gif", "sick.gif", "silly.gif", "sleepy.gif",
			"smirk.gif", "sparklingheart.gif", "sunglasses.gif",
			"thumbsdown.gif", "thumbsup.gif", "thumpingheart.gif",
			"waving.gif", "wink.gif", "winter.gif", "worried.gif",
			"spring.gif", "valentine.gif", "newyear.gif", "irish.gif" };
	static String[] pngs = new String[] { "angry.png", "cake.png",
			"cathappy.png", "catheart.png", "celebration.png", "cheers.png",
			"confused.png", "crying.png", "doublehighfive.png",
			"fist_bump.png", "flexedarm.png", "frustrated.png", "ghost.png",
			"haloguy.png", "happy.png", "happyblushing.png", "happycrying.png",
			"hearteyed.png", "joy.png", "kiss.png", "lol.png", "meh.png",
			"mumstheword.png", "music.png", "ohno.png", "okay.png",
			"omgterrified.png", "poop.png", "praying.png", "princess.png",
			"sad.png", "scared.png", "sick.png", "silly.png", "sleepy.png",
			"smirk.png", "sparklingheart.png", "sunglasses.png",
			"thumbsdown.png", "thumbsup.png", "thumpingheart.png",
			"waving.png", "wink.png", "winter.png", "worried.png",
			"spring.png", "valentine.png", "newyear.png", "irish.png" };
	static Fragment userFragment;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		saa = startAppAd;
		StartAppSDK.init(this, "102378373", "205305173");
		prefs = this.getSharedPreferences(getPackageName() + "_preferences",
				Context.MODE_PRIVATE);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(
				getResources().getDrawable(R.drawable.ic_hangouts_ab));
		copyAssets();
		start();

	}

	static void refreshUserFragment() {
		Fragment currentFragment = userFragment;
		FragmentTransaction fragTransaction = activity
				.getSupportFragmentManager().beginTransaction();
		fragTransaction.detach(currentFragment);
		fragTransaction.attach(currentFragment);
		fragTransaction.commit();
	}

	public void refreshUserFragment2() {
		UserFragment.refresh(this);
	}

	public void about() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(getString(R.string.about));

		alertDialogBuilder.setMessage(
				Html.fromHtml(getString(R.string.about_text)))

		.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	@Override
	public void onResume() {
		super.onResume();
		startAppAd.onResume();
	}

	public void start() {
		StartAppAd.showSlider(this);
		userFragment = new UserFragment();
		if (prefs.getBoolean("ponies", true)) {
			mViewPagerTabsTitles = new String[] { getString(R.string.stickers),
					getString(R.string.ponies),
					getString(R.string.your_stickers) };
			mViewPagerFragments = new Fragment[] { new StickerFragment(),
					new ExtraFragment(), userFragment };
		} else {
			mViewPagerTabsTitles = new String[] { getString(R.string.stickers),
					getString(R.string.your_stickers) };
			mViewPagerFragments = new Fragment[] { new StickerFragment(),
					userFragment };
		}
		setContentView(R.layout.activity_fragments);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.removeAllTabs();
		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager
				.setAdapter(new StatePagerAdapter(getSupportFragmentManager()));
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.stickers))
				.setTabListener(new TabListener() {

					@Override
					public void onTabReselected(Tab arg0,
							FragmentTransaction arg1) {

					}

					@Override
					public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
						mViewPager.setCurrentItem(arg0.getPosition());
						isUserFragment = false;
						supportInvalidateOptionsMenu();
					}

					@Override
					public void onTabUnselected(Tab arg0,
							FragmentTransaction arg1) {

					}

				}));
		if (prefs.getBoolean("ponies", true)) {
			actionBar.addTab(actionBar.newTab()
					.setText(getString(R.string.ponies))
					.setTabListener(new TabListener() {

						@Override
						public void onTabReselected(Tab arg0,
								FragmentTransaction arg1) {

						}

						@Override
						public void onTabSelected(Tab arg0,
								FragmentTransaction arg1) {
							mViewPager.setCurrentItem(arg0.getPosition());
							isUserFragment = false;
							supportInvalidateOptionsMenu();

						}

						@Override
						public void onTabUnselected(Tab arg0,
								FragmentTransaction arg1) {

						}

					}));
		}
		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.your_stickers))
				.setTabListener(new TabListener() {

					@Override
					public void onTabReselected(Tab arg0,
							FragmentTransaction arg1) {

					}

					@Override
					public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
						try {
							mViewPager.setCurrentItem(arg0.getPosition());
							isUserFragment = true;
							supportInvalidateOptionsMenu();
						} catch (Exception e) {
							// App has closed, don't want to force close
						}
					}

					@Override
					public void onTabUnselected(Tab arg0,
							FragmentTransaction arg1) {

					}

				}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/temp/");
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				new File(dir, children[i]).delete();
			}
		} catch (Exception e) {
			// Prevent crashes on exit
		}
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		File f = new File(Environment.getExternalStorageDirectory().toString()
				+ "/Android/data/" + getPackageName() + "/stickers/");

		f.mkdirs();

		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("StickersForHangouts", e.getMessage());
		}

		for (String filename : files) {
			System.out.println("File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/Android/data/"
						+ getPackageName()
						+ "/stickers/"
						+ filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				Log.e("StickersForHangouts", e.getMessage());
			}
		}

	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isUserFragment) {
			getMenuInflater().inflate(R.menu.sticker_picker_user, menu);
		} else {
			getMenuInflater().inflate(R.menu.sticker_picker, menu);
		}
		/*
		 * May be re-implemented later
		 * 
		 * try{ animateItem = menu.findItem(R.id.animate);
		 * animateItem.setChecked(prefs.getBoolean("animate", true));
		 * }catch(Exception e){}
		 */

		try {
			MenuItem showApp = menu.findItem(R.id.action_show);
			int state = getPackageManager().getComponentEnabledSetting(
					new ComponentName(this, MainActivity.class));
			if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
				showApp.setVisible(true);
			}
		} catch (Exception e) {
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == UserFragment.IMAGE_PICKER_SELECT
				&& resultCode == Activity.RESULT_OK) {
			Uri path = data.getData();
			String uri = getRealPathFromURI(path);
			if(uri==null){
				Toast.makeText(this, getString(R.string.error_download), Toast.LENGTH_LONG).show();
				return;
			}
			History h = new History(uri);
			h.save();
			refreshUserFragment2();
		}
		if (requestCode == 1) {
			start();
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		try{
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) {
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		}catch(Exception e){
			return null;
		}
		return result;
	}

	public class StatePagerAdapter extends FragmentStatePagerAdapter {
		public StatePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return mViewPagerFragments[i];
		}

		@Override
		public int getCount() {
			return mViewPagerFragments.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mViewPagerTabsTitles[position];
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			about();
			break;
		case android.R.id.home:
			finish();
			break;
		case R.id.action_add:
			UserFragment.addSticker();
			break;
		case R.id.action_settings:
			startActivityForResult(new Intent(this, SettingsActivity.class), 1);
			break;
		case R.id.action_clear_all:
			UserFragment.clearAll();
			break;
		case R.id.action_show:
			PackageManager pm = getPackageManager();
			pm.setComponentEnabledSetting(new ComponentName(this,
					MainActivity.class),
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
			Toast.makeText(this, getString(R.string.reboot), Toast.LENGTH_LONG)
					.show();
			break;

		}
		return false;
	}

	public void addSticker(View v) {
		UserFragment.addSticker();
	}

	

}