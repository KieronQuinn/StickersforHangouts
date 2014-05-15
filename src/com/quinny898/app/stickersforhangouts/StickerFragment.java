package com.quinny898.app.stickersforhangouts;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

    //Creates UI and setups up Tab Elements
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StickerPickerActivity.options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_action_loading)
                .showImageForEmptyUri(R.drawable.ic_action_failed)
                .showImageOnFail(R.drawable.ic_action_failed)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        StickerPickerActivity.initImageLoader(getActivity());
        //try{
        v = inflater.inflate(R.layout.activity_sticker_picker, null);
        ProgressBar loading = (ProgressBar) v.findViewById(R.id.loading);
        GridView toplayout = (GridView) v.findViewById(R.id.grid_view);


        toplayout.setAdapter(new ImageAdapter());
        loading.setVisibility(View.GONE);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                getActivity().getPackageName() + "_prefs", Context.MODE_PRIVATE);
        if (prefs.getBoolean("show_crouton", true)) {
            Crouton.makeText(getActivity(), getString(R.string.long_press), new Style.Builder().setBackgroundColor(R.color.hangouts).build()).show();
        }
        toplayout.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Uri uri = Uri.parse("file://"
                        + Environment.getExternalStorageDirectory()
                        .toString() + "/Android/data/"
                        + getActivity().getPackageName() + "/stickers/" + "/"
                        + StickerPickerActivity.gifs[arg2]);
                getActivity().setResult(StickerPickerActivity.RESULT_OK, new Intent().setData(uri));
                getActivity().finish();
            }

        });
        toplayout.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                //Stop showing "Long click for preview" crouton
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        getActivity().getPackageName() + "_prefs", Context.MODE_PRIVATE);
                prefs.edit().putBoolean("show_crouton", false).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                try {
                    GifDrawable gifFromAssets = new GifDrawable(getActivity().getAssets(), StickerPickerActivity.gifs[arg2]);
                    GifImageView giv = new GifImageView(getActivity());
                    giv.setImageDrawable(gifFromAssets);
                    builder.setView(giv);
                    builder.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }

                    });
                    builder.setNegativeButton(getString(R.string.use), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Uri uri = Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory()
                                    .toString() + "/Android/data/"
                                    + getActivity().getPackageName() + "/stickers/" + "/"
                                    + StickerPickerActivity.gifs[arg2]);
                            getActivity().setResult(StickerPickerActivity.RESULT_OK, new Intent().setData(uri));
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
        @Override
        public int getCount() {
            return StickerPickerActivity.pngs.length;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage("assets://" + StickerPickerActivity.pngs[position],
                    holder.imageView, StickerPickerActivity.options, new SimpleImageLoadingListener() {
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
