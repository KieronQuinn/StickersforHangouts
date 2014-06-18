package com.quinny898.app.stickersforhangouts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class FingerPaintView extends View {
	Path path;
	Bitmap bitmapBuff;
	Paint paint;
	Paint pen;
	Canvas canvas;
	Bitmap originBitmap;
	Bitmap pureBitmp;
	private float scaleWidth;
	private float scaleHeight;
	private float mX, mY;
	private static float TOUCH_TOLERANCE = 4;
	private Canvas printCanvas;
	Bitmap printBitmapBuff;
	private List<Path> pathList;
	int i = 0;
	private Bitmap printBitmap;

	public FingerPaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDrawingCacheEnabled(true);
		loadBitmap(context);
		Log.i("Context context, AttributeSet attrs", "11");
	}

	public FingerPaintView(Context context) {
		super(context);
		this.setDrawingCacheEnabled(true);
		loadBitmap(context);
		Log.i("Context context", "222");
	}

	public void setColor(int color) {
		paint.setColor(color);
	}

	public void setStrokeWidth(float width) {
		paint.setStrokeWidth(width);
	}

	public void setStrokeType(boolean circle) {
		if (circle) {
			paint.setStrokeCap(Paint.Cap.ROUND);
		} else {
			paint.setStrokeCap(Paint.Cap.SQUARE);
		}
	}

	public void setAlpha(int alpha) {
		int color = paint.getColor();
		int red=   (color >> 16) & 0xFF;
		int green= (color >> 8) & 0xFF;
		int blue=  (color >> 0) & 0xFF;
		paint.setColor(Color.argb(alpha, red, green, blue));
	}
	public Bitmap get(){
		   return this.getDrawingCache();
		}
	public void saveToSd() throws IOException{
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.quinny898.app.stickersforhangouts/";
	    OutputStream outStream = null;
	    if(!new File(extStorageDirectory).exists())new File(extStorageDirectory).mkdirs();
	    File file = new File(extStorageDirectory, "temp.png");
	    if(file.exists())file.delete();
	    Bitmap bbicon = get();
	    try {
	     outStream = new FileOutputStream(file);
	     bbicon.compress(Bitmap.CompressFormat.PNG, 100, outStream);
	     outStream.flush();
	     outStream.close();
	    }
	    catch(Exception e)
	    {}
	}
	public void loadBitmap(Context context) {

		pathList = new ArrayList<Path>();
		path = new Path();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(1.6f);
		int pixel = 200;
		final float scale = getResources().getDisplayMetrics().density;
		int dip = (int) (pixel* scale + 0.5f);

		originBitmap = Bitmap.createBitmap(dip, dip, Config.ARGB_8888);

		int width = originBitmap.getWidth();

		int height = originBitmap.getHeight();

		DisplayMetrics metrics = context.getApplicationContext().getResources()
				.getDisplayMetrics();

		int newWidth = metrics.widthPixels;

		int newHeight = metrics.heightPixels;

		scaleWidth = ((float) newWidth / width);

		scaleHeight = ((float) newHeight / height);

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		pureBitmp = Bitmap.createBitmap(originBitmap, 0, 0, width, height,
				matrix, true);

		bitmapBuff = Bitmap.createBitmap(pureBitmp.getWidth(),
				pureBitmp.getHeight(), Bitmap.Config.ARGB_8888);

		canvas = new Canvas(bitmapBuff);

		canvas.drawBitmap(pureBitmp, 0, 0, paint);

	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchStart(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touchUp(x, y);
			invalidate();
			break;
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawBitmap(bitmapBuff, 0, 0, paint);
		canvas.drawPath(path, paint);

	}

	private void touchStart(float x, float y) {
		path.reset();
		path.moveTo(x, y);
		mX = x;
		mY = y;

	}

	private void touchMove(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;

		}
	}

	private void touchUp(float x, float y) {

		pathList.add(new Path(path));

		canvas.drawPath(path, paint);

		path.reset();
	}

	public void createPrintCanvas() {

		printBitmap = Bitmap.createBitmap(originBitmap);

		printBitmapBuff = Bitmap.createBitmap(printBitmap.getWidth(),
				printBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Matrix matrix = new Matrix();

		matrix.postScale(1 / scaleWidth, 1 / scaleHeight);

		printCanvas = new Canvas(printBitmapBuff);

		printCanvas.drawBitmap(printBitmap, 0, 0, paint);

		for (int i = 0; i < pathList.size(); i++) {
			Path tempPath = new Path(pathList.get(i));

			tempPath.transform(matrix);

			printCanvas.drawPath(tempPath, paint);

			Log.d("path", tempPath.toString());

		}

		printCanvas.save();

	}

	public float getScaleWidth() {
		return scaleWidth;
	}

	public float getScaleHeight() {
		return scaleHeight;
	}

}