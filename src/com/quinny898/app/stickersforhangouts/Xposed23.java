package com.quinny898.app.stickersforhangouts;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class Xposed23 {
	private static int mButtonPosition;
	Fragment qjFragment;
	private static int[] bikeshedColors = new int[] { Color.parseColor("#F8F8F8"),
			Color.parseColor("#F0F8FF"), Color.parseColor("#FAEBD7"),
			Color.parseColor("#00FFFF"), Color.parseColor("#7FFFD4"),
			Color.parseColor("#F0FFFF"), Color.parseColor("#F5F5DC"),
			Color.parseColor("#FFE4C4"), Color.parseColor("#000000"),
			Color.parseColor("#FFEBCD"), Color.parseColor("#0000FF"),
			Color.parseColor("#8A2BE2"), Color.parseColor("#A52A2A"),
			Color.parseColor("#DEB887"), Color.parseColor("#5F9EA0"),
			Color.parseColor("#7FFF00"), Color.parseColor("#D2691E"),
			Color.parseColor("#FF7F50"), Color.parseColor("#6495ED"),
			Color.parseColor("#FFF8DC"), Color.parseColor("#DC143C"),
			Color.parseColor("#00FFFF"), Color.parseColor("#00008B"),
			Color.parseColor("#008B8B"), Color.parseColor("#B8860B"),
			Color.parseColor("#A9A9A9"), Color.parseColor("#006400"),
			Color.parseColor("#BDB76B"), Color.parseColor("#8B008B"),
			Color.parseColor("#556B2F"), Color.parseColor("#FF8C00"),
			Color.parseColor("#9932CC"), Color.parseColor("#8B0000"),
			Color.parseColor("#E9967A"), Color.parseColor("#8FBC8F"),
			Color.parseColor("#483D8B"), Color.parseColor("#2F4F4F"),
			Color.parseColor("#00CED1"), Color.parseColor("#9400D3"),
			Color.parseColor("#FF1493"), Color.parseColor("#00BFFF"),
			Color.parseColor("#696969"), Color.parseColor("#1E90FF"),
			Color.parseColor("#B22222"), Color.parseColor("#FFFAF0"),
			Color.parseColor("#228B22"), Color.parseColor("#FF00FF"),
			Color.parseColor("#DCDCDC"), Color.parseColor("#F8F8FF"),
			Color.parseColor("#FFD700"), Color.parseColor("#DAA520"),
			Color.parseColor("#808080"), Color.parseColor("#008000"),
			Color.parseColor("#ADFF2F"), Color.parseColor("#F0FFF0"),
			Color.parseColor("#FF69B4"), Color.parseColor("#CD5C5C"),
			Color.parseColor("#4B0082"), Color.parseColor("#FFFFF0"),
			Color.parseColor("#F0E68C"), Color.parseColor("#E6E6FA"),
			Color.parseColor("#FFF0F5"), Color.parseColor("#7CFC00"),
			Color.parseColor("#FFFACD"), Color.parseColor("#ADD8E6"),
			Color.parseColor("#F08080"), Color.parseColor("#E0FFFF"),
			Color.parseColor("#FAFAD2"), Color.parseColor("#D3D3D3"),
			Color.parseColor("#90EE90"), Color.parseColor("#FFB6C1"),
			Color.parseColor("#FFA07A"), Color.parseColor("#20B2AA"),
			Color.parseColor("#87CEFA"), Color.parseColor("#778899"),
			Color.parseColor("#B0C4DE"), Color.parseColor("#FFFFE0"),
			Color.parseColor("#00FF00"), Color.parseColor("#32CD32"),
			Color.parseColor("#FAF0E6"), Color.parseColor("#FF00FF"),
			Color.parseColor("#800000"), Color.parseColor("#66CDAA"),
			Color.parseColor("#0000CD"), Color.parseColor("#BA55D3"),
			Color.parseColor("#9370DB"), Color.parseColor("#3CB371"),
			Color.parseColor("#7B68EE"), Color.parseColor("#00FA9A"),
			Color.parseColor("#48D1CC"), Color.parseColor("#C71585"),
			Color.parseColor("#191970"), Color.parseColor("#F5FFFA"),
			Color.parseColor("#FFE4E1"), Color.parseColor("#FFE4B5"),
			Color.parseColor("#FFDEAD"), Color.parseColor("#000080"),
			Color.parseColor("#FDF5E6"), Color.parseColor("#808000"),
			Color.parseColor("#6B8E23"), Color.parseColor("#FFA500"),
			Color.parseColor("#FF4500"), Color.parseColor("#DA70D6"),
			Color.parseColor("#EEE8AA"), Color.parseColor("#98FB98"),
			Color.parseColor("#AFEEEE"), Color.parseColor("#DB7093"),
			Color.parseColor("#FFEFD5"), Color.parseColor("#FFDAB9"),
			Color.parseColor("#CD853F"), Color.parseColor("#FFC0CB"),
			Color.parseColor("#DDA0DD"), Color.parseColor("#B0E0E6"),
			Color.parseColor("#800080"), Color.parseColor("#FF0000"),
			Color.parseColor("#BC8F8F"), Color.parseColor("#4169E1"),
			Color.parseColor("#8B4513"), Color.parseColor("#FA8072"),
			Color.parseColor("#F4A460"), Color.parseColor("#2E8B57"),
			Color.parseColor("#FFF5EE"), Color.parseColor("#A0522D"),
			Color.parseColor("#C0C0C0"), Color.parseColor("#87CEEB"),
			Color.parseColor("#6A5ACD"), Color.parseColor("#708090"),
			Color.parseColor("#FFFAFA"), Color.parseColor("#00FF7F"),
			Color.parseColor("#4682B4"), Color.parseColor("#D2B48C"),
			Color.parseColor("#008080"), Color.parseColor("#D8BFD8"),
			Color.parseColor("#FF6347"), Color.parseColor("#40E0D0"),
			Color.parseColor("#EE82EE"), Color.parseColor("#F5DEB3"),
			Color.parseColor("#FFFFFF"), Color.parseColor("#F5F5F5"),
			Color.parseColor("#FFFF00"), Color.parseColor("#9ACD32") };
	protected static Activity conversationActivity;
	protected static Intent intent;
	protected static Fab f;
	protected static boolean isFabShown;
	protected static RelativeLayout root;

	public static void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.google.android.talk"))
			return;

		/*
		 * How to update (updated for new version [10th Sept 2014] due to heavy
		 * obfuscation):
		 * 
		 * Search the root of the decompiled source for "ArrayAdapter" You want
		 * the file containing a getView that sets both a TextView and an
		 * ImageView Find the class the List references. Delete the old one from
		 * this app and copy the new class in Update the add an item method To
		 * get the IDs of the icons: Decompile the APK using apktool, open
		 * res/values/public.xml Find the hex IDs for "ic_emoji_dark" and
		 * "ic_edit_gray", and convert them to decimal. They are the compiled
		 * IDs
		 * 
		 * Search the root of the decompiled source for
		 * "DialogInterface.OnClickListener" You want the file containing
		 * multiple startActivityForResult calls Change the click hook to the
		 * new obfuscated names, including the methods to call
		 * startActivityForResult
		 * 
		 * Updating the easter eggs: They're all handled from one file,
		 * MessageListItemView The method you need to hook is the one where the
		 * textview's text is set and then the Linkify links are added Modify
		 * the hook's name and the textview's object name Find what
		 * "ConversationFragment"'s object name is called and update that Open
		 * ConversationFragment and update the object name for the listview that
		 * is set from the class with casts ["(ListView)"]
		 * 
		 * You are done :D
		 */

		// Add an item
		findAndHookMethod("bxn", lpparam.classLoader, "b", boolean.class,
				boolean.class, boolean.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						mButtonPosition = (Integer) XposedHelpers.callMethod(
								param.thisObject, "getCount");
						XposedHelpers.callMethod(param.thisObject, "a",
								"Add sticker", 2130838697, mButtonPosition);
						XposedHelpers.callMethod(param.thisObject, "a",
								"Add Drawing", 2130838693, mButtonPosition + 1);
					}
				});
		// Easter eggs
		findAndHookMethod(
				"com.google.android.apps.hangouts.views.MessageListItemView",
				lpparam.classLoader, "z", new XC_MethodHook() {
					private LinearLayout footerView;

					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						TextView tv = (TextView) getObjectField(
								param.thisObject, "i");
						String message = tv.getText().toString();
						// Change the background to a random web-save colour
						// (/bikeshed)
						if (message.trim().equals("/bikeshed")) {
							Object conversationFragment = getObjectField(
									param.thisObject, "w");
							ListView list = (ListView) getObjectField(
									conversationFragment, "h");
							Random random = new Random();
							int n = random.nextInt(bikeshedColors.length);
							int color = bikeshedColors[n];
							list.setBackgroundColor(color);
						}
						// Change the background to an image (Konami Code)
						if (message.trim().equals("/uuddlrlrba")) {
							Object conversationFragment = getObjectField(
									param.thisObject, "w");
							ListView list = (ListView) getObjectField(
									conversationFragment, "h");
							list.setBackgroundResource(Xposed.mFakeId);
						}
						// Show a little dinosaur and a house at the bottom of
						// the screen
						if (message.trim().equals("/shydino")) {
							XposedBridge.log("ShyDino!");
							Context c = (Context) callMethod(param.thisObject,
									"getContext");
							Object conversationFragment = getObjectField(
									param.thisObject, "w");
							ListView list = (ListView) getObjectField(
									conversationFragment, "h");
							ImageView iv = new ImageView(c);
							int height = (int) c.getResources().getDimension(
									Xposed.mFakeIdH);
							iv.setLayoutParams(new ViewGroup.LayoutParams(
									height, height));
							iv.setAdjustViewBounds(true);
							iv.setImageResource(Xposed.mFakeIdS);
							iv.setScaleType(ScaleType.FIT_XY);
							LinearLayout layout = new LinearLayout(c);
							layout.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
							layout.setGravity(Gravity.CENTER_HORIZONTAL);
							layout.addView(iv);
							try {
								list.removeFooterView(footerView);
							} catch (Exception e) {
							}
							footerView = layout;
							TranslateAnimation anim = new TranslateAnimation(0,
									0, 200, 0);
							anim.setFillAfter(true);
							anim.setDuration(1200);
							iv.startAnimation(anim);
							list.addFooterView(layout);
						}

					}
				});

		// Hook the click from the item
		findAndHookMethod("cbe", lpparam.classLoader, "onClick",
				DialogInterface.class, int.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						if (Integer.parseInt(param.args[1].toString()) == mButtonPosition) {
							Class<?> composeMessageViewClass = findClass(
									"com.google.android.apps.hangouts.views.ComposeMessageView",
									lpparam.classLoader);
							Object aIx = getObjectField(param.thisObject, "a");
							Object asL = getObjectField(aIx, "a");
							Object conversationFragment = callStaticMethod(
									composeMessageViewClass, "d", asL);
							final ComponentName cn = new ComponentName(
									"com.quinny898.app.stickersforhangouts",
									"com.quinny898.app.stickersforhangouts.StickerPickerActivity");
							Intent intent = new Intent().setComponent(cn);
							callMethod(conversationFragment,
									"startActivityForResult", intent, 1);
							param.setResult(null);
						}
						if (Integer.parseInt(param.args[1].toString()) == mButtonPosition + 1) {
							Class<?> composeMessageViewClass = findClass(
									"com.google.android.apps.hangouts.views.ComposeMessageView",
									lpparam.classLoader);
							Object aIx = getObjectField(param.thisObject, "a");
							Object asL = getObjectField(aIx, "a");
							Object conversationFragment = callStaticMethod(
									composeMessageViewClass, "d", asL);
							final ComponentName cn = new ComponentName(
									"com.quinny898.app.stickersforhangouts",
									"com.quinny898.app.stickersforhangouts.PaintActivity");
							Intent intent = new Intent().setComponent(cn);
							callMethod(conversationFragment,
									"startActivityForResult", intent, 1);
							param.setResult(null);
						}
					}
				});
		findAndHookMethod("qj", lpparam.classLoader, "r", new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param)
					throws Throwable {
				Drawable input = (Drawable) param.getResult();
				Bitmap bitmap = ((BitmapDrawable) input).getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] b = stream.toByteArray();
				String fileName = Environment.getExternalStorageDirectory()
						.toString()
						+ "/Android/data/com.quinny898.app.stickersforhangouts/"
						+ "/tmp.png";
				try {
					FileOutputStream fos = new FileOutputStream(fileName);
					fos.write(b);
					fos.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				param.setResult(null);
			}
		});

		findAndHookMethod("qj", lpparam.classLoader, "onCreateView",
				LayoutInflater.class, ViewGroup.class, Bundle.class,
				new XC_MethodHook() {

					@SuppressLint("NewApi")
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						root = (RelativeLayout) param.getResult();
						final Activity a = (Activity) callMethod(
								param.thisObject, "getActivity");
						f = new Fab(a);
						final float scale = a.getResources()
								.getDisplayMetrics().density;
						int dip = (int) (72 * scale);
						int margin = (int) (20 * scale);
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								dip, dip);
						params.setMargins(0, 0, dip/4, Xposed.getNavBarHeight(a,margin));
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						f.setLayoutParams(params);
						f.setFabColor(Color.WHITE);
						f.setFabDrawable(a.getResources().getDrawable(Xposed.mFakeIdE));
						f.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Object conversationFragment = getObjectField(
										conversationActivity, "s");
								final ComponentName cn = new ComponentName(
										"com.quinny898.app.stickersforhangouts",
										"com.quinny898.app.stickersforhangouts.StartAviaryActivity");
								Intent newIntent = new Intent()
										.setComponent(cn);

								callMethod(conversationFragment,
										"startActivityForResult", newIntent, 1);
								a.finish();
							}

						});
						root.addView(f);

						isFabShown = false;
					}
				});

		// Save ConversationFragment's Activity for future use
		findAndHookMethod(
				"com.google.android.apps.hangouts.fragments.ConversationFragment",
				lpparam.classLoader, "onCreateView", LayoutInflater.class,
				ViewGroup.class, Bundle.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						conversationActivity = (Activity) callMethod(
								param.thisObject, "getActivity");
					}
				});
	}

}
