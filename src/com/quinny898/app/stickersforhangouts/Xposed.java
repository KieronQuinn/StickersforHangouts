package com.quinny898.app.stickersforhangouts;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;

public class Xposed implements IXposedHookLoadPackage {
	private int mButtonPosition;

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.google.android.talk"))
			return;

		findAndHookMethod("com.google.android.apps.babel.util.bh",
				lpparam.classLoader, "b", boolean.class, boolean.class,
				boolean.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						mButtonPosition = (Integer) XposedHelpers.callMethod(
								param.thisObject, "getCount");
						XposedHelpers.callMethod(param.thisObject, "e",
								"Add sticker", 2130838676, mButtonPosition);
						XposedHelpers.callMethod(param.thisObject, "e",
								"Add Drawing", 2130838672, mButtonPosition+1);
					}
				});
		findAndHookMethod("com.google.android.apps.babel.views.at",
				lpparam.classLoader, "onClick", DialogInterface.class,
				int.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param)
							throws Throwable {
						if (Integer.parseInt(param.args[1].toString()) == mButtonPosition) {
							Class<?> composeMessageViewClass = findClass(
									"com.google.android.apps.babel.views.ComposeMessageView",
									lpparam.classLoader);
							Object aIx = getObjectField(param.thisObject, "biG");
							Object asL = getObjectField(aIx, "aPk");
							Object conversationFragment = callStaticMethod(
									composeMessageViewClass, "e", asL);
							final ComponentName cn = new ComponentName(
									"com.quinny898.app.stickersforhangouts",
									"com.quinny898.app.stickersforhangouts.StickerPickerActivity");
							Intent intent = new Intent().setComponent(cn);
							callMethod(conversationFragment,
									"startActivityForResult", intent, 1);
							param.setResult(null);
						}
						if (Integer.parseInt(param.args[1].toString()) == mButtonPosition+1) {
							Class<?> composeMessageViewClass = findClass(
									"com.google.android.apps.babel.views.ComposeMessageView",
									lpparam.classLoader);
							Object aIx = getObjectField(param.thisObject, "biG");
							Object asL = getObjectField(aIx, "aPk");
							Object conversationFragment = callStaticMethod(
									composeMessageViewClass, "e", asL);
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
	}

}
