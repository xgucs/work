package com.amlogic.mediaboxlauncher;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.KeyEvent;
import android.view.FocusFinder;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.util.Log;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;

public class MyOnTouchListener implements OnTouchListener {
	private final static String TAG = "MyOnTouchListener";

	private Context mContext;
	private Object appPath;

	public MyOnTouchListener(Context context, Object path) {
		mContext = context;
		appPath = path;
	}

	public boolean onTouch(View view, MotionEvent event) {		  
    
        LauncherActivity.isInTouchMode = true;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
               System.out.println("onkey:down"+view);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
               System.out.println("oKey:up"+view);
			Intent intent = new Intent();
			
			switch (view.getId()) {
			case R.id.re_iztv:
				intent.setComponent(new ComponentName("com.izitv.android",
						"com.izitv.android.MainActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_iradio:
				intent.setComponent(new ComponentName("com.iziradio.android",
						"com.iziradio.android.MainActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_yutu:
				intent.setComponent(new ComponentName(
						"com.google.android.youtube",
						"com.google.android.youtube.app.honeycomb.Shell$HomeActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_airplay:
				intent.setComponent(new ComponentName("com.waxrain.airplaydmr",
						"com.waxrain.ui.WaxPlayerSetting"));
				mContext.startActivity(intent);
				break;
			case R.id.re_ilure:
				intent.setComponent(new ComponentName("com.izi.ilurer",
						"com.izi.ilurer.iLurer"));
				mContext.startActivity(intent);
				break;
			case R.id.re_google:
				intent.setComponent(new ComponentName("com.android.chrome",
						"com.google.android.apps.chrome.Main"));
				mContext.startActivity(intent);
				break;

			case R.id.re_bird:
				intent.setComponent(new ComponentName("com.twitter.android",
						"com.twitter.android.StartActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_human:
				intent.setComponent(new ComponentName("ru.ok.android",
						"ru.ok.android.ui.activity.main.OdklActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_fire:
				intent.setComponent(new ComponentName("com.facebook.katana",
						"com.facebook.katana.LoginActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.re_sock:
				intent.setComponent(new ComponentName("com.skype.rover",
						"com.skype.raider.Main"));
				mContext.startActivity(intent);
				break;
			case R.id.re_wire:
				intent.setComponent(new ComponentName("com.vkontakte.android",
						"com.vkontakte.android.MainActivity"));
				mContext.startActivity(intent);
				break;

			case R.id.for_re_setting:
				intent.setComponent(new ComponentName("com.android.settings",
						"com.android.settings.Settings"));
				mContext.startActivity(intent);
			
				break;
			case R.id.for_re_explore:
				intent.setComponent(new ComponentName("com.fb.FileBrower",
						"com.fb.FileBrower.FileBrower"));
				mContext.startActivity(intent);
				break;
			case R.id.for_re_advance:
				intent.setComponent(new ComponentName("com.mbx.settingsmbox",
						"com.mbx.settingsmbox.SettingsMboxActivity"));
				mContext.startActivity(intent);
				break;
			case R.id.for_re_clear:
				 new Thread(){
					  public void run(){
						  Intent intent=new Intent();
						  intent.setComponent(new ComponentName(
							"com.speed.fast.clean",
							"com.speed.fast.clean.activity.MainActivity"));
					mContext.startActivity(intent);
					  }
				  }.start();
				break;
			case R.id.for_re_netspeed:
			new Thread(){
					  public void run(){
						  Intent intent=new Intent();
						intent.setComponent(new ComponentName(
						"org.zwanoo.android.speedtest",
						"com.ookla.speedtest.softfacade.MainActivity"));
				       mContext.startActivity(intent);
					  }
				  }.start();
				
				break;

			case R.id.re_sow_left:
			    LauncherActivity.frameView.setVisibility(View.INVISIBLE);
				
				LauncherActivity.viewMenu.setInAnimation(mContext,
						R.anim.push_left_in);
				LauncherActivity.viewMenu.setOutAnimation(mContext,
						R.anim.push_left_out);
				LauncherActivity.viewMenu.showPrevious();
				int m=LauncherActivity.viewMenu.getDisplayedChild();
				if (m==0) {
					LauncherActivity.handler.sendEmptyMessage(0);
				}else if (m==1) {
					LauncherActivity.handler.sendEmptyMessage(1);
				}else if (m==2) {
					LauncherActivity.handler.sendEmptyMessage(2);
				}else {
					LauncherActivity.handler.sendEmptyMessage(3);
				}

				break;
			case R.id.re_sow_right:
			     LauncherActivity.frameView.setVisibility(View.INVISIBLE);
				LauncherActivity.viewMenu.setInAnimation(mContext,
						R.anim.push_right_in);
				LauncherActivity.viewMenu.setOutAnimation(mContext,
						R.anim.push_right_out);
				LauncherActivity.viewMenu.showNext();
				int sm=LauncherActivity.viewMenu.getDisplayedChild();
				if (sm==0) {
					LauncherActivity.handler.sendEmptyMessage(0);
				}else if (sm==1) {
					LauncherActivity.handler.sendEmptyMessage(1);
				}else if (sm==2) {
					LauncherActivity.handler.sendEmptyMessage(2);
				}else {
					LauncherActivity.handler.sendEmptyMessage(3);
				}

				break;
			default:
				if (appPath != null) {
					if (appPath.equals("com.add.path")) {
						return false;
					} else if (appPath.equals("com.all.apps")) {
						Intent mIntent=new Intent(mContext, MyAppsActivity.class);
                        mContext.startActivity(mIntent);
					} else {
						mContext.startActivity((Intent) appPath);
					}

				}

				break;
			}
		}

		return false;
	}

}
