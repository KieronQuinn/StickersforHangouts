package com.quinny898.app.stickersforhangouts;

import java.util.Iterator;
import java.util.Set;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class StartAviaryActivity extends ActionBarActivity {

	/*
	 * This activity is to pass the different types of intent results from
	 * Aviary to Hangouts It's required because Aviary's TOS say you can't edit
	 * their stuff
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(Uri.parse("file://"
				+ Environment.getExternalStorageDirectory().toString()
				+ "/Android/data/com.quinny898.app.stickersforhangouts/"
				+ "/tmp.png"));
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET,
				"4fec5189ed6a43fd");
		newIntent.putExtra(Constants.EXTRA_OUTPUT, Uri.parse("file://"
				+ Environment.getExternalStorageDirectory().toString()
				+ "/Android/data/com.quinny898.app.stickersforhangouts/"
				+ "/tmp.png"));
		startActivityForResult(newIntent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				Uri mImageUri = data.getData();
				Bundle extra = data.getExtras();
				if (null != extra) {
					dumpIntent(data);
					setResult(RESULT_OK, new Intent().setData(mImageUri));
					finish();
				}
				break;
			}
		} else {
			finish();
		}
	}

	public static void dumpIntent(Intent i) {
		String LOG_TAG = "SFH";
		Bundle bundle = i.getExtras();
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			Log.e(LOG_TAG, "Dumping Intent start");
			while (it.hasNext()) {
				String key = it.next();
				Log.e(LOG_TAG, "[" + key + "=" + bundle.get(key) + "]");
			}
			Log.e(LOG_TAG, "Dumping Intent end");
		}
	}

}
