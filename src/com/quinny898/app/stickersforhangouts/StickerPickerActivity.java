package com.quinny898.app.stickersforhangouts;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.startapp.android.publish.StartAppSDK;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class StickerPickerActivity extends ActionBarActivity {
    SharedPreferences prefs;
    ViewPager mViewPager;
    String[] mViewPagerTabsTitles;
    Fragment[] mViewPagerFragments;
    static DisplayImageOptions options;
    static String[] gifs = new String[]{"angry.gif", "cake.gif", "cathappy.gif",
            "catheart.gif", "celebration.gif", "cheers.gif", "confused.gif",
            "crying.gif", "doublehighfive.gif", "fist_bump.gif",
            "flexedarm.gif", "frustrated.gif", "ghost.gif", "haloguy.gif",
            "happy.gif", "happyblushing.gif", "happycrying.gif",
            "hearteyed.gif", "joy.gif", "kiss.gif", "lol.gif", "meh.gif",
            "mumstheword.gif", "music.gif", "ohno.gif", "okay.gif",
            "omgterrified.gif", "poop.gif", "praying.gif", "princess.gif",
            "sad.gif", "scared.gif", "sick.gif", "silly.gif", "sleepy.gif",
            "smirk.gif", "sparklingheart.gif", "sunglasses.gif",
            "thumbsdown.gif", "thumbsup.gif", "thumpingheart.gif",
            "waving.gif", "wink.gif", "winter.gif", "worried.gif",
            "spring.gif", "valentine.gif", "newyear.gif", "irish.gif"};
    static String[] pngs = new String[]{"angry.png", "cake.png", "cathappy.png",
            "catheart.png", "celebration.png", "cheers.png", "confused.png",
            "crying.png", "doublehighfive.png", "fist_bump.png",
            "flexedarm.png", "frustrated.png", "ghost.png", "haloguy.png",
            "happy.png", "happyblushing.png", "happycrying.png",
            "hearteyed.png", "joy.png", "kiss.png", "lol.png", "meh.png",
            "mumstheword.png", "music.png", "ohno.png", "okay.png",
            "omgterrified.png", "poop.png", "praying.png", "princess.png",
            "sad.png", "scared.png", "sick.png", "silly.png", "sleepy.png",
            "smirk.png", "sparklingheart.png", "sunglasses.png",
            "thumbsdown.png", "thumbsup.png", "thumpingheart.png",
            "waving.png", "wink.png", "winter.png", "worried.png",
            "spring.png", "valentine.png", "newyear.png", "irish.png"};

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "102378373", "205305173", true);
        prefs = this.getSharedPreferences(
                getPackageName() + "_preferences", Context.MODE_PRIVATE);
        mViewPagerTabsTitles = new String[]{getString(R.string.stickers), getString(R.string.extras)};
        mViewPagerFragments = new Fragment[]{new StickerFragment(), new ExtraFragment()};

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_hangouts_ab));

        start();

    }

    public void about() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(getString(R.string.about));

        alertDialogBuilder.setMessage(
                Html.fromHtml(getString(R.string.about_text)))

                .setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                );

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void start() {
        setContentView(R.layout.activity_fragments);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(new StatePagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText(getString(R.string.stickers)).setTabListener(new TabListener() {

            @Override
            public void onTabReselected(Tab arg0, FragmentTransaction arg1) {


            }

            @Override
            public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
                mViewPager.setCurrentItem(arg0.getPosition());

            }

            @Override
            public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {


            }

        }));
        actionBar.addTab(actionBar.newTab().setText(getString(R.string.extras)).setTabListener(new TabListener() {

            @Override
            public void onTabReselected(Tab arg0, FragmentTransaction arg1) {


            }

            @Override
            public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
                mViewPager.setCurrentItem(arg0.getPosition());

            }

            @Override
            public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {


            }

        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sticker_picker, menu);
        /*
		 * May be re-implemented later
		 * 
		 * try{ 
		animateItem = menu.findItem(R.id.animate);
		animateItem.setChecked(prefs.getBoolean("animate", true));
		}catch(Exception e){}*/
        try {
            MenuItem showApp = menu.findItem(R.id.action_show);
            int state = getPackageManager().getComponentEnabledSetting(new ComponentName(this, MainActivity.class));
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                showApp.setVisible(true);
            }
        } catch (Exception e) {
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                about();
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.action_show:
                PackageManager pm = getPackageManager();
                pm.setComponentEnabledSetting(new ComponentName(this, MainActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                Toast.makeText(this, getString(R.string.reboot), Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public class StatePagerAdapter extends FragmentStatePagerAdapter {
        public StatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mViewPagerFragments[i];
        }

        @Override
        public int getCount() {
            return mViewPagerFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mViewPagerTabsTitles[position];
        }
    }

}
