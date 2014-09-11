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
		
		/*
		 * How to update (updated for new version [10th Sept 2014] due to heavy obfuscation):
		 * 
		 * Search the root of the decompiled source for "ArrayAdapter"
		 * You want the file containing a getView that sets both a TextView and an ImageView
		 * Find the class the List references. Delete the old one from this app and copy the new class in
		 * Update the add an item method 
		 * To get the IDs of the icons: 
		 * Decompile the APK using apktool, open res/values/public.xml
		 * Find the hex IDs for "ic_emoji_dark" and "ic_edit_gray", and convert them to decimal. They are the compiled IDs
		 * 
		 * Search the root of the decompiled source for "DialogInterface.OnClickListener"
		 * You want the file containing multiple startActivityForResult calls
		 * Change the click hook to the new obfuscated names, including the methods to call startActivityForResult
		 * 
		 * You are done :D
		 * 
		 */
		
		//Add an item
		findAndHookMethod("bxf",
				lpparam.classLoader, "b", boolean.class, boolean.class,
				boolean.class, new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {
						mButtonPosition = (Integer) XposedHelpers.callMethod(
								param.thisObject, "getCount");
						XposedHelpers.callMethod(param.thisObject, "a",
								"Add sticker", 2130838691, mButtonPosition);
						XposedHelpers.callMethod(param.thisObject, "a",
								"Add Drawing", 2130838687, mButtonPosition + 1);
					}
				});
		//Hook the click from the item
		findAndHookMethod("caw",
				lpparam.classLoader, "onClick", DialogInterface.class,
				int.class, new XC_MethodHook() {
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
		/*
		 * WIP
		 * 
		 * bikeshed easteregg 
		 * findAndHookMethod(
		 * "com.google.android.apps.babel.views.ConversationListItemView",
		 * lpparam.classLoader, "k", CharSequence.class, new XC_MethodHook() {
		 * 
		 * @Override protected void afterHookedMethod(MethodHookParam param)
		 * throws Throwable { if(param.args[0] != null){
		 * XposedBridge.log(param.args[0].toString());
		 * if(param.args[0].toString().trim().equals("/bikeshed")){
		 * XposedBridge.log("BIKESHED!"); } } Class<?> conversationFragment =
		 * findClass(
		 * "com.google.android.apps.babel.fragments.ConversationFragment",
		 * lpparam.classLoader); Activity a = (Activity) callMethod(
		 * conversationFragment, "getActivity");
		 * a.getWindow().getDecorView().setBackgroundColor(Color.RED);
		 * //ListView messageListView = (ListView)
		 * conservationFragment.getDeclaredField("Kz").;//
		 * getObjectField(conservationFragment, "Kz");
		 * //messageListView.setBackgroundColor(Color.BLUE); } });
		 */
	}

}
