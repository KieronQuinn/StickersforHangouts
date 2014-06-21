package com.quinny898.app.stickersforhangouts;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ExtraFragment extends Fragment {
    View v;
    String[] gifs = new String[]{"af.gif", "bm.gif", "cc.gif", "cs.gif",
            "dg.gif", "dl.gif", "ib.gif", "jl.gif", "km.gif", "kr.gif",
            "lw.gif", "mk.gif", "mt.gif", "mu.gif", "ni.gif", "nk.gif",
            "rr.gif", "rs.gif", "tj.gif", "tl.gif", "zf.gif"};
    String[] pngs = new String[]{"af.png", "bm.png", "cc.png", "cs.png",
            "dg.png", "dl.png", "ib.png", "jl.png", "km.png", "kr.png",
            "lw.png", "mk.png", "mt.png", "mu.png", "ni.png", "nk.png",
            "rr.png", "rs.png", "tj.png", "tl.png", "zf.png"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_sticker_picker, null);
        ProgressBar loading = (ProgressBar) v.findViewById(R.id.loading);
        GridView toplayout = (GridView) v.findViewById(R.id.grid_view);

        toplayout.setAdapter(new ImageAdapter(getActivity()));
        loading.setVisibility(View.GONE);
        toplayout.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Uri uri = Uri.parse("file://"
                        + Environment.getExternalStorageDirectory().toString()
                        + "/Android/data/" + getActivity().getPackageName()
                        + "/stickers/" + "/" + gifs[arg2]);
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
                            .getAssets(), gifs[arg2]);
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

                            }
                    );
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
                                                    + "/stickers/" + "/"
                                                    + gifs[arg2]);
                                    getActivity().setResult(
                                            StickerPickerActivity.RESULT_OK,
                                            new Intent().setData(uri));
                                    getActivity().finish();

                                }
                            }
                    );
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
            return pngs.length;
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
                    + pngs[position];

            Picasso.with(context) //
                    .load(url) //
                    .fit().placeholder(R.drawable.ic_action_loading) //
                    .error(R.drawable.ic_action_failed) //
                    .into(view);

            return convertView;
        }

    }
}
