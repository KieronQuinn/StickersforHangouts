package com.quinny898.app.stickersforhangouts;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.google.android.talk"))
			return;

		findAndHookMethod("com.google.android.apps.babel.util.bi",
				lpparam.classLoader, "b", boolean.class, boolean.class,
				boolean.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						XposedHelpers.callMethod(param.thisObject, "e",
								"Add sticker", 2130838672, 5);
					}
				});
		 findAndHookMethod("com.google.android.apps.babel.views.av", lpparam.classLoader, "onClick",
	                DialogInterface.class, int.class,
	                new XC_MethodHook() {
	                    @Override
	                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
	                        Log.d("SFH", param.args[1].toString());
	                    	if(Integer.parseInt(param.args[1].toString())==3){
	                    	Class<?> composeMessageViewClass = findClass("com.google.android.apps.babel.views.ComposeMessageView", lpparam.classLoader);
	                        Object aIx = getObjectField(param.thisObject, "aIX");
	                        Object asL = getObjectField(aIx, "asL");
	                        Object conversationFragment = callStaticMethod(composeMessageViewClass, "e", asL);
	                        final ComponentName cn = new ComponentName("com.quinny898.app.stickersforhangouts","com.quinny898.app.stickersforhangouts.StickerPickerActivity");
	                        Intent intent = new Intent().setComponent(cn);
	                        callMethod(conversationFragment, "startActivityForResult", intent, 1);
	                        }
	                        }
	                }
	        );
	}
	

}
