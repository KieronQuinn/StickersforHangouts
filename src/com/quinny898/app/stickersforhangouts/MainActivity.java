package com.quinny898.app.stickersforhangouts;

import com.startapp.android.publish.StartAppSDK;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "102378373", "205305173", true);
		setContentView(R.layout.activity_main);
		TextView info = (TextView) findViewById(R.id.info);
		info.setText(Html.fromHtml(getString((R.string.info))));

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

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			about();
			break;

		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void youtube(View v) {
		watchYoutubeVideo("sGN7dgri4cQ");
	}
	public void hide(View v){
		PackageManager pm = getPackageManager(); 
	    pm.setComponentEnabledSetting(new ComponentName(this, MainActivity.class),
	                                  PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	    Toast.makeText(this, getString(R.string.how_to_show), Toast.LENGTH_LONG).show();
	    Toast.makeText(this, getString(R.string.reboot), Toast.LENGTH_LONG).show();

	}
	public void watchYoutubeVideo(String id) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("vnd.youtube:" + id));
			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.youtube.com/watch?v=" + id));
			startActivity(intent);
		}
	}
}
