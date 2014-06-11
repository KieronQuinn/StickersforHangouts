package com.quinny898.app.stickersforhangouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.squareup.picasso.Picasso;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;

public class UserFragment extends Fragment {
	static View v;
	private static GridView toplayout;
	private static List<History> images;
	static int numberNotExisting = 0;
	public static ArrayList<String> namesNotExisting;
	static ActionBarActivity c;
	static int IMAGE_PICKER_SELECT = 0;
	static ActionBarActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (ActionBarActivity) getActivity();
		c = (ActionBarActivity) getActivity();
		images = History.listAll(History.class);
		final SharedPreferences prefs = getActivity().getSharedPreferences(
				getActivity().getPackageName() + "_prefs",
				Context.MODE_PRIVATE);
		final SharedPreferences prefs2 = getActivity().getSharedPreferences(
				getActivity().getPackageName() + "_preferences",
				Context.MODE_PRIVATE);
		StartAppAd startAppAd = StickerPickerActivity.saa;
		StartAppSDK.init(getActivity(), "102378373", "205305173");
		new CopyFiles().execute();
		int Min = 0;
		int Max = 3;
		int random = Min + (int) (Math.random() * ((Max - Min) + 1));
		if (random == 1) {
			startAppAd.showAd(); // show the ad
			startAppAd.loadAd();
			if(!prefs2.getBoolean("disabletoast", false)){
			Toast.makeText(getActivity(), getString(R.string.toast_ad),
					Toast.LENGTH_LONG).show();
			}
		}
		if (random == 3) {
			StartAppAd.showSplash(getActivity(), savedInstanceState);
			if(!prefs2.getBoolean("disabletoast", false)){
			Toast.makeText(getActivity(), getString(R.string.toast_splash),
					Toast.LENGTH_LONG).show();
			}
		}
		v = inflater.inflate(R.layout.activity_sticker_picker, null);

		ProgressBar loading = (ProgressBar) v.findViewById(R.id.loading);
		toplayout = (GridView) v.findViewById(R.id.grid_view);

		toplayout.setAdapter(new ImageAdapter(getActivity()));
		loading.setVisibility(View.GONE);
		toplayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Uri uri = Uri.parse("file:///"
						+ Environment.getExternalStorageDirectory() + "/temp/"
						+ new File(images.get(arg2).filepath).getName());
				Log.d("SFH", uri.toString());
				getActivity().setResult(StickerPickerActivity.RESULT_OK,
						new Intent().setData(uri));
				getActivity().finish();
			}

		});
		toplayout.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// Stop showing "Long click for preview" crouton
				
				prefs.edit().putBoolean("show_crouton", false).commit();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				try {
					GifDrawable gifFromAssets = new GifDrawable(new File(images
							.get(arg2).filepath));
					GifImageView giv = new GifImageView(getActivity());
					giv.setImageDrawable(gifFromAssets);
					builder.setView(giv);
					builder.setPositiveButton(getString(R.string.close),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}

							});
					builder.setNeutralButton(getString(R.string.delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									images.get(arg2).delete();
									StickerPickerActivity.refreshUserFragment();
								}
							});
					builder.setNegativeButton(getString(R.string.use),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

									Uri uri = Uri.parse("file:///"
											+ Environment
													.getExternalStorageDirectory()
											+ "/temp/"
											+ new File(
													images.get(arg2).filepath)
													.getName());

									getActivity().setResult(
											StickerPickerActivity.RESULT_OK,
											new Intent().setData(uri));
									getActivity().finish();

								}
							});
					builder.show();

				} catch (IOException e) {
					Drawable gifFromAssets = Drawable.createFromPath(images
							.get(arg2).filepath);
					ImageView giv = new ImageView(getActivity());
					giv.setImageDrawable(gifFromAssets);
					builder.setView(giv);
					builder.setPositiveButton(getString(R.string.close),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}

							});
					builder.setNeutralButton(getString(R.string.delete),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									images.get(arg2).delete();
									StickerPickerActivity.refreshUserFragment();
								}
							});
					builder.setNegativeButton(getString(R.string.use),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

									Uri uri = Uri.parse("file:///"
											+ Environment
													.getExternalStorageDirectory()
											+ "/temp/"
											+ new File(
													images.get(arg2).filepath)
													.getName());

									getActivity().setResult(
											StickerPickerActivity.RESULT_OK,
											new Intent().setData(uri));
									getActivity().finish();

								}
							});
					builder.show();
					e.printStackTrace();
				}

				return false;
			}

		});
		if (images.size() == 0) {
			RelativeLayout no_stickers = (RelativeLayout) v
					.findViewById(R.id.no_stickers);
			RelativeLayout main = (RelativeLayout) v.findViewById(R.id.main);
			no_stickers.setVisibility(View.VISIBLE);
			main.setVisibility(View.GONE);
			return v;
		}
		return v;

	}

	private static class DeleteFiles extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		protected void onPreExecute() {
			pd = new ProgressDialog(c);
			pd.setMessage(c.getString(R.string.deleting));
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			int x = 0;
			while (x < images.size()) {
				File f = new File(images.get(x).filepath);
				if (!f.exists()) {
					images.get(x).delete();
				}
				x++;
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			StickerPickerActivity.refreshUserFragment();
		}
	}

	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	private static class CopyFiles extends AsyncTask<String, Void, String> {
		ProgressDialog pd;

		protected void onPreExecute() {
			numberNotExisting = 0;
			pd = new ProgressDialog(c);
			pd.setMessage(c.getString(R.string.loading));
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}

		@SuppressLint("SdCardPath")
		@Override
		protected String doInBackground(String... params) {

			namesNotExisting = new ArrayList<String>();
			int x = 0;
			while (x < images.size()) {
				File f = new File(images.get(x).filepath);
				if (!f.exists()) {
					numberNotExisting++;
					namesNotExisting.add(f.getName());
				} else {
					File out = new File(
							Environment.getExternalStorageDirectory()
									+ "/temp/" + f.getName());
					out.getParentFile().mkdirs();
					try {
						copy(f, out);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				x++;
			}
			return "Executed";
		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			if (numberNotExisting != 0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						c);

				alertDialogBuilder.setTitle(c
						.getString(R.string.missing_images));
				alertDialogBuilder
						.setMessage(c.getString(R.string.missing_images_info))
						.setPositiveButton(c.getString(android.R.string.yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										new DeleteFiles().execute();
									}
								})
						.setNegativeButton(c.getString(android.R.string.no),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								})
						.setNeutralButton(c.getString(R.string.info_dialog),
								null);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										c);
								String missingImages = c
										.getString(R.string.missing_images_info_title);
								int y = 0;
								while (y < namesNotExisting.size()) {
									missingImages = missingImages + "\n"
											+ namesNotExisting.get(y);
									y++;
								}
								alertDialogBuilder
										.setMessage(missingImages)
										.setPositiveButton(
												c.getString(android.R.string.ok),
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.dismiss();
													}
												});

								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.show();

							}

						});

			}
		}
	}

	public static class ImageAdapter extends BaseAdapter {
		private Context context;

		@Override
		public int getCount() {
			return images.size();
		}

		public ImageAdapter(Context context) {
			this.context = context;
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = c.getLayoutInflater().inflate(
					R.layout.item_grid_image, parent, false);

			ImageView view = (ImageView) convertView.findViewById(R.id.image);

			String url = "file:///" + images.get(position).filepath;

			Picasso.with(context) //
					.load(url) //
					.fit().placeholder(R.drawable.ic_action_loading) //
					.error(R.drawable.ic_action_failed) //
					.into(view);

			return convertView;
		}

	}

	public static void addSticker() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(i, IMAGE_PICKER_SELECT);
	}

	public static void refresh(ActionBarActivity c) {
		UserFragment.c = c;
		new CopyFiles().execute();
		RelativeLayout no_stickers = (RelativeLayout) v
				.findViewById(R.id.no_stickers);
		RelativeLayout main = (RelativeLayout) v.findViewById(R.id.main);
		no_stickers.setVisibility(View.GONE);
		main.setVisibility(View.VISIBLE);
		images = History.listAll(History.class);
		toplayout.setAdapter(new ImageAdapter(c));
	}

	public static void clearAll() {
		History.deleteAll(History.class);
		StickerPickerActivity.refreshUserFragment();

	}
}
