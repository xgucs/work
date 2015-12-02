package com.amlogic.mediaboxlauncher;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
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
import android.graphics.Rect;

public class MyOnKeyListener implements OnKeyListener {
	private final static String TAG = "MyOnKeyListener";

	// private int NUM_VIDEO = 0;
	private int NUM_APP = 0;
	private int NUM_RECOMMEND = 1;
	// private int NUM_MUSIC = 3;
	private int NUM_LOCAL = 2;

	private Context mContext;
	private Object appPath;

	public MyOnKeyListener(Context context, Object path) {
		mContext = context;
		appPath = path;
	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {
		 LauncherActivity.isInTouchMode = false;  
        if (LauncherActivity.animIsRun)
            return true;

		if (keyCode != KeyEvent.KEYCODE_BACK)
               
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {

				Intent intent = new Intent();
				System.out.println(view);
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
					intent.setComponent(new ComponentName(
							"com.waxrain.airplaydmr",
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
					intent.setComponent(new ComponentName(
							"com.twitter.android",
							"com.twitter.android.StartActivity"));
					mContext.startActivity(intent);
					break;
				case R.id.re_human:
					intent.setComponent(new ComponentName("ru.ok.android",
							"ru.ok.android.ui.activity.main.OdklActivity"));
					mContext.startActivity(intent);
					break;
				case R.id.re_fire:
					intent.setComponent(new ComponentName(
							"com.facebook.katana",
							"com.facebook.katana.LoginActivity"));
					mContext.startActivity(intent);
					break;
				case R.id.re_sock:
					intent.setComponent(new ComponentName("com.skype.rover",
							"com.skype.raider.Main"));
					mContext.startActivity(intent);
					break;
				case R.id.re_wire:
					intent.setComponent(new ComponentName(
							"com.vkontakte.android",
							"com.vkontakte.android.MainActivity"));
					mContext.startActivity(intent);
					break;

				case R.id.for_re_setting:
					intent.setComponent(new ComponentName(
							"com.android.settings",
							"com.android.settings.Settings"));
					mContext.startActivity(intent);
					mContext.startActivity(intent);
					break;
				case R.id.for_re_explore:
					intent.setComponent(new ComponentName("com.fb.FileBrower",
							"com.fb.FileBrower.FileBrower"));
					mContext.startActivity(intent);
					break;
				case R.id.for_re_advance:
					intent.setComponent(new ComponentName(
							"com.mbx.settingsmbox",
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
					}else if(m==3){
						
						LauncherActivity.handler.sendEmptyMessage(3);
					}

					break;
				case R.id.re_sow_right:
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
					}else if(sm==3){
						LauncherActivity.handler.sendEmptyMessage(3);
					}
 
					break;

				default:
					if (appPath != null) {					
						if (appPath.equals("com.add.path")) {
							return false;
						} else if (appPath.equals("com.all.apps")) {
							Intent mIntent = new Intent(mContext,
									MyAppsActivity.class);
							LauncherActivity.saveHomeFocusView=view;
							mContext.startActivity(mIntent);
						} else {
							mContext.startActivity((Intent) appPath);
							LauncherActivity.IntoApps = true;
							LauncherActivity.saveHomeFocusView=view;
						}

					}

					break;
				}

				return true;

			}
            
			 if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && view.getId()!=R.id.re_iztv){
            ImageView img = (ImageView)((ViewGroup)view).getChildAt(0);
            String path  = img.getResources().getResourceName(img.getId()); 
            String vName = path.substring(path.indexOf("/")+1);

            if (checkNextFocusedIsNull(view, View.FOCUS_LEFT)){
				
               LauncherActivity.accessBoundaryCount = 0;
                Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
                Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.push_left_out);
                animIn.setAnimationListener(new MyAnimationListener(0));
                animOut.setAnimationListener(new MyAnimationListener(1));
               LauncherActivity.viewMenu.setInAnimation(animIn);
               LauncherActivity.viewMenu.setOutAnimation(animOut);
               LauncherActivity.viewMenu.showPrevious();
			   int sm=LauncherActivity.viewMenu.getDisplayedChild();
                if (sm==0) {
					LauncherActivity.handler.sendEmptyMessage(0);
				}else if (sm==1) {
					LauncherActivity.handler.sendEmptyMessage(1);
				}else if (sm==2) {
					LauncherActivity.handler.sendEmptyMessage(2);
				}else if(sm==3){
					LauncherActivity.handler.sendEmptyMessage(3);
				}			   
                return true;
            }    
            
        }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT&&view.getId()!=R.id.for_re_netspeed ){
            ImageView img = (ImageView)((ViewGroup)view).getChildAt(0);
            String path  = img.getResources().getResourceName(img.getId()); 
            String vName = path.substring(path.indexOf("/")+1); 

            if(checkNextFocusedIsNull(view,View.FOCUS_RIGHT)){
				
               LauncherActivity.accessBoundaryCount = 0;  
                Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
                Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
                animIn.setAnimationListener(new MyAnimationListener(2));
                animOut.setAnimationListener(new MyAnimationListener(3));
               LauncherActivity.viewMenu.setInAnimation(animIn);
               LauncherActivity.viewMenu.setOutAnimation(animOut);
			   LauncherActivity.viewMenu.showNext(); 
			    int sm=LauncherActivity.viewMenu.getDisplayedChild();
                if (sm==0) {
					LauncherActivity.handler.sendEmptyMessage(0);
				}else if (sm==1) {
					LauncherActivity.handler.sendEmptyMessage(1);
				}else if (sm==2) {
					LauncherActivity.handler.sendEmptyMessage(2);
				}else if(sm==3){
					LauncherActivity.handler.sendEmptyMessage(3);
				}			   
                return true;
            }
        }
        return false;   
	}

	private void showMenuView(int num, View view) {
		LauncherActivity.saveHomeFocusView = view;
		LauncherActivity.isShowHomePage = false;
		LauncherActivity.layoutScaleShadow.setVisibility(View.INVISIBLE);
		LauncherActivity.frameView.setVisibility(View.INVISIBLE);

		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		ScaleAnimation scaleAnimationIn = new ScaleAnimation(1.0f, 1.0f, 1.0f,
				1.0f, getPiovtX(rect), getPiovtY(rect));
		
		scaleAnimationIn.setDuration(200);
	
		scaleAnimationIn.setAnimationListener(new MyAnimationListener(4));
		LauncherActivity.viewMenu.setInAnimation(scaleAnimationIn);
		LauncherActivity.viewMenu.setOutAnimation(null);

		LauncherActivity.viewHomePage.setVisibility(View.GONE);
		LauncherActivity.viewMenu.setVisibility(View.VISIBLE);
		LauncherActivity.viewMenu.setDisplayedChild(num);
		LauncherActivity.viewMenu.getChildAt(num).requestFocus();
	}

	private float getPiovtX(Rect rect) {
		return (float) ((rect.left + rect.width() / 2));
	}

	private float getPiovtY(Rect rect) {
		return (float) ((rect.top + rect.height() / 2));
	}

	private boolean checkNextFocusedIsNull(View view, int dec) {
		ViewGroup gridLayout = (ViewGroup) view.getParent();
		if (FocusFinder.getInstance().findNextFocus(gridLayout,
				gridLayout.findFocus(), dec) == null) {
			LauncherActivity.accessBoundaryCount++;
		} else {
			LauncherActivity.accessBoundaryCount = 0;
		}

		if (LauncherActivity.accessBoundaryCount <= 1)
			return false;
		else {
           LauncherActivity.dontRunAnim = true;
           LauncherActivity.animIsRun = true;
           LauncherActivity.layoutScaleShadow.setVisibility(View.INVISIBLE);
           LauncherActivity.frameView.setVisibility(View.INVISIBLE);
			return true;
		}
	}

	private class MyAnimationListener implements AnimationListener {
		private int in_or_out;

		public MyAnimationListener(int flag) {
			in_or_out = flag;
		}

		public void onAnimationStart(Animation animation) {
           LauncherActivity.animIsRun = true;
           LauncherActivity.layoutScaleShadow.setVisibility(View.INVISIBLE);
           LauncherActivity.frameView.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// Log.d(TAG,
			// "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ flag="+
			// in_or_out);
			if (in_or_out == 1) {
				if (((ViewGroup)LauncherActivity.viewMenu.getCurrentView())
						.getChildAt(0) instanceof MyScrollView) {
					ViewGroup findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup)LauncherActivity.viewMenu
							.getCurrentView()).getChildAt(0)).getChildAt(0));
					int count = findGridLayout.getChildCount() < 6 ? findGridLayout
							.getChildCount() - 1 : 5;
					findGridLayout.getChildAt(count).requestFocus();
					 LauncherActivity.dontRunAnim = true;
                     LauncherActivity.dontRunAnim = false;
				}
			} else if (in_or_out == 3) {
				//LauncherActivity.dontDrawFocus = false;
				LauncherActivity.dontRunAnim = true;
				LauncherActivity.viewMenu.clearFocus();
				LauncherActivity.dontRunAnim = true;
				LauncherActivity.viewMenu.requestFocus();
				LauncherActivity.dontRunAnim = false;
			} else if (in_or_out == 4) {
				LauncherActivity.dontRunAnim = true;
				LauncherActivity.viewMenu.clearFocus();
				LauncherActivity.dontRunAnim = true;
				LauncherActivity.viewMenu.requestFocus();
				LauncherActivity.dontRunAnim = false;
			}
           LauncherActivity.animIsRun = false;
           LauncherActivity.layoutScaleShadow.setVisibility(View.VISIBLE);
           LauncherActivity.frameView.setVisibility(View.VISIBLE);
		}

		public void onAnimationRepeat(Animation animation) {
		}

	}

}
