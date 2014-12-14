package com.quinny898.app.stickersforhangouts;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.squareup.picasso.Picasso;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class StickerFragment extends Fragment {
	View v;

	// Creates UI and setups up Tab Elements
	@SuppressLint("ResourceAsColor")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// try{
		v = inflater.inflate(R.layout.activity_sticker_picker, null);
		ProgressBar loading = (ProgressBar) v.findViewById(R.id.loading);
		GridView toplayout = (GridView) v.findViewById(R.id.grid_view);

		toplayout.setAdapter(new ImageAdapter(getActivity()));
		loading.setVisibility(View.GONE);
		SharedPreferences prefs = getActivity()
				.getSharedPreferences(
						getActivity().getPackageName() + "_prefs",
						Context.MODE_PRIVATE);
		if (prefs.getBoolean("show_crouton", true)) {
			Crouton.makeText(
					getActivity(),
					getString(R.string.long_press),
					new Style.Builder().setBackgroundColor(R.color.hangouts)
							.build()).show();
		}
		toplayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Uri uri = Uri
						.parse("file://"
								+ Environment.getExternalStorageDirectory()
										.toString() + "/Android/data/"
								+ getActivity().getPackageName() + "/stickers/"
								+ "/" + StickerPickerActivity.gifs[arg2]);
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
				SharedPreferences prefs = getActivity().getSharedPreferences(
						getActivity().getPackageName() + "_prefs",
						Context.MODE_PRIVATE);
				prefs.edit().putBoolean("show_crouton", false).commit();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				try {
					GifDrawable gifFromAssets = new GifDrawable(getActivity()
							.getAssets(), StickerPickerActivity.gifs[arg2]);
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
					builder.setNegativeButton(getString(R.string.use),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Uri uri = Uri
											.parse("file://"
													+ Environment
															.getExternalStorageDirectory()
															.toString()
													+ "/Android/data/"
													+ getActivity()
															.getPackageName()
													+ "/stickers/"
													+ "/"
													+ StickerPickerActivity.gifs[arg2]);
									getActivity().setResult(
											StickerPickerActivity.RESULT_OK,
											new Intent().setData(uri));
									getActivity().finish();

								}
							});
					builder.show();

				} catch (IOException e) {
					e.printStackTrace();
				}

				return false;
			}

		});

		return v;

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;

		@Override
		public int getCount() {
			return StickerPickerActivity.pngs.length;
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
			convertView = getActivity().getLayoutInflater().inflate(
					R.layout.item_grid_image, parent, false);

			ImageView view = (ImageView) convertView.findViewById(R.id.image);

			String url = "content://com.quinny898.app.stickersforhangouts.CustomContentProvider/"
					+ StickerPickerActivity.pngs[position];

			Picasso.with(context) //
					.load(url) //
					.fit().placeholder(R.drawable.ic_action_loading) //
					.error(R.drawable.ic_action_failed) //
					.into(view);

			return convertView;
		}

	}

}
