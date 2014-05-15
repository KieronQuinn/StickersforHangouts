package com.quinny898.app.stickersforhangouts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.startapp.android.publish.StartAppSDK;

import com.nostra13.universalimageloader.core.assist.FailReason;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StickerPickerActivity extends ActionBarActivity {
	public final static int FINGER_RELEASED = 0;
	public final static int FINGER_TOUCHED = 1;
	public final static int FINGER_DRAGGING = 2;
	public final static int FINGER_UNDEFINED = 3;
	SharedPreferences prefs;
	static DisplayImageOptions options;
	static String[] gifs = new String[] { "angry.gif", "cake.gif", "cathappy.gif",
			"catheart.gif", "celebration.gif", "cheers.gif", "confused.gif",
			"crying.gif", "doublehighfive.gif", "fist_bump.gif",
			"flexedarm.gif", "frustrated.gif", "ghost.gif", "haloguy.gif",
			"happy.gif", "happyblushing.gif", "happycrying.gif",
			"hearteyed.gif", "joy.gif", "kiss.gif", "lol.gif", "meh.gif",
			"mumstheword.gif", "music.gif", "ohno.gif", "okay.gif",
			"omgterrified.gif", "poop.gif", "praying.gif", "princess.gif",
			"sad.gif", "scared.gif", "sick.gif", "silly.gif", "sleepy.gif",
			"smirk.gif", "sparklingheart.gif", "sunglasses.gif",
			"thumbsdown.gif", "thumbsup.gif", "thumpingheart.gif",
			"waving.gif", "wink.gif", "winter.gif", "worried.gif",
			"spring.gif", "valentine.gif", "newyear.gif", "irish.gif" };
	static String[] pngs = new String[] { "angry.png", "cake.png", "cathappy.png",
			"catheart.png", "celebration.png", "cheers.png", "confused.png",
			"crying.png", "doublehighfive.png", "fist_bump.png",
			"flexedarm.png", "frustrated.png", "ghost.png", "haloguy.png",
			"happy.png", "happyblushing.png", "happycrying.png",
			"hearteyed.png", "joy.png", "kiss.png", "lol.png", "meh.png",
			"mumstheword.png", "music.png", "ohno.png", "okay.png",
			"omgterrified.png", "poop.png", "praying.png", "princess.png",
			"sad.png", "scared.png", "sick.png", "silly.png", "sleepy.png",
			"smirk.png", "sparklingheart.png", "sunglasses.png",
			"thumbsdown.png", "thumbsup.png", "thumpingheart.png",
			"waving.png", "wink.png", "winter.png", "worried.png",
			"spring.png", "valentine.png", "newyear.png", "irish.png" };
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "102378373", "205305173", true);
		prefs = this.getSharedPreferences(
			      getPackageName()+"_preferences", Context.MODE_PRIVATE);
		copyAssets();
		
		
		
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_hangouts_ab));
		
		start();
		
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
	public void start(){
		setContentView(R.layout.activity_fragments);
		 final ActionBar actionBar = getSupportActionBar();
		    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		    new StickerFragment();
		    new StickerFragment();

		    actionBar.addTab(actionBar.newTab().setText(getString(R.string.stickers)).setTabListener(new TabListener(){

				@Override
				public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
					
					
				}

				@Override
				public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
					getSupportFragmentManager().beginTransaction().replace(R.id.container, new StickerFragment()).commit();
					
				}

				@Override
				public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
					
					
				}
		    	
		    }));
		    actionBar.addTab(actionBar.newTab().setText(getString(R.string.extras)).setTabListener(new TabListener(){

				@Override
				public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
					
					
				}

				@Override
				public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
					getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExtraFragment()).commit();
					
				}

				@Override
				public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
					
					
				}
		    	
		    }));

	}
	
	public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }

        return bitmap;
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
							+ "/stickers/" + filename);
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
		getMenuInflater().inflate(R.menu.sticker_picker, menu);
		/*
		 * May be re-implemented later
		 * 
		 * try{ 
		animateItem = menu.findItem(R.id.animate);
		animateItem.setChecked(prefs.getBoolean("animate", true));
		}catch(Exception e){}*/
		try{
			MenuItem showApp = menu.findItem(R.id.action_show);
			int state = getPackageManager().getComponentEnabledSetting(new ComponentName(this, MainActivity.class));
			if(state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED){
			showApp.setVisible(true);
			}
			}catch(Exception e){}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			about();
			break;
		case android.R.id.home:
			finish();
			break;
		case R.id.action_show:
		    PackageManager pm = getPackageManager(); 
			pm.setComponentEnabledSetting(new ComponentName(this, MainActivity.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
		    Toast.makeText(this, getString(R.string.reboot), Toast.LENGTH_LONG).show();
			break;
		
		}
		return false;
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
	}
	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return pngs.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage("file:///"+Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()+"/stickers/"+pngs[position], holder.imageView, options, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {
											 holder.progressBar.setProgress(0);
											 holder.progressBar.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,
												 FailReason failReason) {
											 holder.progressBar.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											 holder.progressBar.setVisibility(View.GONE);
										 }
									 }, new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.progressBar.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}

}
