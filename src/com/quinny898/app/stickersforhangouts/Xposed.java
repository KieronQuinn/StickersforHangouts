package com.quinny898.app.stickersforhangouts;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.os.Build;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Xposed implements IXposedHookLoadPackage, IXposedHookZygoteInit,
		IXposedHookInitPackageResources {

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.google.android.talk"))
			return;
		//Material Hangouts is now 4.0+, but maintaining 2.3 & 3.0 compatibility is nice
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			Xposed40.handleLoadPackage(lpparam);
		}else{
			Xposed23.handleLoadPackage(lpparam);
		}

	}

	private static String MODULE_PATH = null;
	public static int mFakeId = 0;
	public static int mFakeIdS = 0;
	public static int mFakeIdE = 0;
	public static int mFakeIdC;
	public static int mFakeIdH;

	public void initZygote(StartupParam startupParam) throws Throwable {
		MODULE_PATH = startupParam.modulePath;
	}

	public static int getNavBarHeight(Context context, int margin) {
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			// KitKat renders behind the navigation bar, jelly bean and below
			// cannot
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
				return resources.getDimensionPixelSize(resourceId) + margin;
			} else {
				return margin;
			}
		}
		return margin;
	}

	public void handleInitPackageResources(InitPackageResourcesParam resparam)
			throws Throwable {
		if (!resparam.packageName.equals("com.google.android.talk"))
			return;

		XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH,
				resparam.res);
		mFakeId = resparam.res
				.addResource(modRes, R.drawable.konami_background);
		mFakeIdS = resparam.res.addResource(modRes, R.drawable.shy_dino);
		mFakeIdE = resparam.res.addResource(modRes, R.drawable.ic_action_edit);
		mFakeIdC = resparam.res.addResource(modRes, R.string.copying_file);
		mFakeIdH = resparam.res.addResource(modRes, R.dimen.dino_height);
	}
}
