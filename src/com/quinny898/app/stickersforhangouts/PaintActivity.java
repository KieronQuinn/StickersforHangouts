package com.quinny898.app.stickersforhangouts;

import java.io.IOException;

import com.startapp.android.publish.StartAppSDK;

import it.gmariotti.android.example.colorpicker.Utils;
import it.gmariotti.android.example.colorpicker.calendarstock.ColorPickerDialog;
import it.gmariotti.android.example.colorpicker.calendarstock.ColorPickerSwatch;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class PaintActivity extends ActionBarActivity {

	private int mSelectedColorCal0 = Color.BLACK;
	private int[] mColor = {Color.parseColor("#ffffff"), Color.parseColor("#aaaaaa"), Color.parseColor("#7f7f7f"), Color.parseColor("#555555"), Color.parseColor("#383838"), Color.parseColor("#1a1a1a"), Color.parseColor("#000000"), Color.parseColor("#edc12d"), Color.parseColor("#df5b16"), Color.parseColor("#d82d18"), Color.parseColor("#55a436"), Color.parseColor("#198ebf"), Color.parseColor("#692282"), Color.parseColor("#71174c"), Color.parseColor("#441e14"), Color.parseColor("#f6eb3b"), Color.parseColor("#f38618"), Color.parseColor("#ef6b48"), Color.parseColor("#7bbb61"), Color.parseColor("#41b2d0"), Color.parseColor("#8b519f"), Color.parseColor("#d05da1"), Color.parseColor("#75554b"), Color.parseColor("#f6f697"), Color.parseColor("#f9c787"), Color.parseColor("#f4a38a"), Color.parseColor("#add59b"), Color.parseColor("#84d0e2"), Color.parseColor("#b18cbc"), Color.parseColor("#f4c6e0"), Color.parseColor("#99817a")};
	private ImageButton colorPicker;
	private FingerPaintView canvas;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		StartAppSDK.init(this, "102378373", "205305173");
		setContentView(R.layout.activity_paint);
		colorPicker = (ImageButton) findViewById(R.id.colorpicker);
		canvas = (FingerPaintView) findViewById(R.id.canvas);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(
				getResources().getDrawable(R.drawable.ic_hangouts_ab));
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return false;
	}
	public void colorpicker(View v){
		ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(
	              R.string.color_picker_default_title, 
	              mColor,
	              mSelectedColorCal0 ,
	              5,
	              Utils.isTablet(this)? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL);

	  colorcalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener(){

	                @Override
	                public void onColorSelected(int color) {
	                    mSelectedColorCal0=color;
	                    colorPicker.setBackgroundColor(color);
	                    canvas.setColor(color);
	                }

	    });

	  colorcalendar.show(getSupportFragmentManager(),"cal");
	}
	public void eraser(View v){
		canvas.setColor(Color.WHITE);
	}
	public void pencil(View v){
		canvas.setColor(mSelectedColorCal0);
	}
	public void penalpha100(View v){
		canvas.setAlpha(100);
	}
	public void penalpha75(View v){
		canvas.setAlpha(75);
	}
	public void penalpha50(View v){
		canvas.setAlpha(50);
	}
	public void penalpha25(View v){
		canvas.setAlpha(25);
	}
	public void pencircle(View v){
		canvas.setStrokeType(true);
	}
	public void pensquare(View v){
		canvas.setStrokeType(false);
	}
	public void pensize05(View v){
		canvas.setStrokeWidth(1f);
	}
	public void pensize25(View v){
		canvas.setStrokeWidth(3f);
	}
	public void pensize45(View v){
		canvas.setStrokeWidth(5f);
	}
	public void pensize75(View v){
		canvas.setStrokeWidth(8f);
	}
	public void cancel(View v){
		finish();
	}
	public void send(View v){
		try {
			canvas.saveToSd();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Uri uri = Uri
				.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.toString() + "/Android/data/" + getPackageName() + "/temp.png");
						
		setResult(StickerPickerActivity.RESULT_OK,
				new Intent().setData(uri));
		finish();
	}
}
