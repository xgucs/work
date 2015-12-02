package com.amlogic.mediaboxlauncher;
import android.content.pm.ActivityInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import android.graphics.Typeface;
import java.util.List;
import java.util.Map;

import android.opengl.Visibility;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.os.SystemProperties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.SystemProperties;
import android.os.Handler;
import android.os.Message;
import android.os.ServiceManager;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.net.wifi.WifiInfo;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.app.Instrumentation;
import android.widget.TextView;
import android.os.Environment;
import android.widget.GridView;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import java.util.Timer;
import java.util.TimerTask;
public class LauncherActivity extends Activity {
	public static boolean isRealOutputMode;	
	public static boolean isAddButtonBeTouched;
	public static boolean isInTouchMode;
	public static boolean dontRunAnim;
	public static boolean IntoCustomActivity;
	public static View pressedAddButton = null;
	public static boolean isShowHomePage;
	public static boolean dontDrawFocus;
	public static boolean ifChangedShortcut;
	public static boolean animIsRun;
	private boolean is24hFormart = false;
	
	public static boolean cantGetDrawingCache;
	public static boolean IntoApps;
	private String privitekey = "";
	public static View frameView;
	public static View trans_frameView;
    private final String outputmode_change_action = "android.amlogic.settings.CHANGE_OUTPUT_MODE";
	private static String strCity;
	private static String strWeather;
	public static String REAL_OUTPUT_MODE;
	public static float startX;
	public static int accessBoundaryCount = 0;
	public static RelativeLayout layoutScaleShadow;
	public static MyGridLayout recommendShortcutView = null;
	public static View prevFocusedView;
	public static View saveHomeFocusView = null;
	public static View viewHomePage = null;
	public static String current_shortcutHead = "Home_Shortcut:";
	public static MyGridLayout localShortcutView = null;
	private String result;
	private static final int WEATHER_SUCCESS = 4;
	private static View view = null;
    private final String net_change_action = "android.net.conn.CONNECTIVITY_CHANGE";
	private final String wifi_signal_action = "android.net.wifi.RSSI_CHANGED";
	// all views buttons
	public static MyGridLayout homeShortcutView = null;
	public static MyGridLayout appShortcutView = null;
    private Timer mTimer;
	
	
	
	// layout commonsappShortcutView
	private static TextView city;
	private static TextView wWeather;
	private static TextView description;
	
	private ImageView iv_wifi;
	private TextView times;
	public static TextView top_Title;
	public static MyRelativeLayout left;
	public static MyRelativeLayout right;
	// house
	public static ImageView house;
	private ImageView first_point;
	private ImageView second_point;
	private ImageView third_point;
	private ImageView fourth_point;
	// first_page
	private MyRelativeLayout re_iztv;
	private MyRelativeLayout re_iradio;
	private MyRelativeLayout re_yutu;
	private MyRelativeLayout re_airplay;
	private MyRelativeLayout re_ilure;
	private MyRelativeLayout re_google;
	// second
	private MyRelativeLayout re_bird;
	private MyRelativeLayout re_human;
	private MyRelativeLayout re_fire;
	private MyRelativeLayout re_sock;
	private MyRelativeLayout re_wire;
	// third
	public static List<Map<String, Object>> appShortCutList = new ArrayList<Map<String, Object>>();
	// fourth
	private MyRelativeLayout for_re_setting;
	private MyRelativeLayout for_re_explore;
	private MyRelativeLayout for_re_advance;
	private MyRelativeLayout for_re_clear;
	private MyRelativeLayout for_re_netspeed;
	private GridView lv_status;
	
    private final int time_freq = 180;
	private String[] list_homeShortcut;
	private static boolean updateAllShortcut;
	private List<Map<String, Object>> HomeShortCutList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newShortCutList = new ArrayList<Map<String, Object>>();

	public static Bitmap screenShot;
	public static Bitmap screenShot_keep;
	private int popWindow_top = -1;
	private int popWindow_bottom = -1;
	private int numberInGrid = -1;
	private int numberInGridOfShortcut = -1;

	private static PopupWindow menue;
	private static LayoutInflater mInflate;
	private static View mainview;
	// bottom
	private static ImageView iv_house;
	public static ImageView iv_point_one;
	public static ImageView iv_point_two;
	public static ImageView iv_point_three;
	public static MyViewFlipper viewMenu = null;
	private static int time_count = 0;
    public static boolean isAPPs=true;
	
	public static int SCREEN_HEIGHT;
	public static int SCREEN_WIDTH;
	public static boolean isNative4k2k;
	public static boolean isNative720;
	private static int wifi_level;
    private final String SD_PATH = "/storage/external_storage/sdcard1";
	private final String USB_PATH = "/storage/external_storage";
	private HomeKeyEventBroadCastReceiver receiver;
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case WEATHER_SUCCESS:
				city.setVisibility(View.VISIBLE);
				description.setVisibility(View.VISIBLE);
				wWeather.setVisibility(View.VISIBLE);
				city.setText(strCity+"  ");
				description.setText(strWeather);
				break;
			case 0:
                 updateView(0);
				break;
			case 1:
				 updateView(1);
				break;
			case 2:
				 updateView(2);
				break;
			case 3:
                  updateView(3);
				break;
			case 12:

				break;
			}
		};

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (DesUtils.isAmlogicChip() == false) {
                finish();
            }
		IWindowManager mWindowManager = IWindowManager.Stub
				.asInterface(ServiceManager.getService("window"));
		try {
			mWindowManager.setAnimationScale(1, 0.0f);
		} catch (RemoteException e) {
		}
		initStaticVariable();
	     updateWifiLevel();
		view = this.getWindow().getDecorView();
		
		initView();
		mTimer=new Timer();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addDataScheme("file");
		registerReceiver(mediaReceiver, filter);

		filter = new IntentFilter();
		filter.addAction(net_change_action);
		filter.addAction(wifi_signal_action);
		filter.addAction(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
		filter.addAction(Intent.ACTION_TIME_TICK);
		// filter.addAction(weather_receive_action);
		filter.addAction(outputmode_change_action);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		registerReceiver(netReceiver, filter);

		filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(appReceiver, filter);
		setRectOnKeyListener();
		viewMenu = (MyViewFlipper) findViewById(R.id.newspp);
		receiver = new HomeKeyEventBroadCastReceiver();  
       registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
	   layoutScaleShadow = (RelativeLayout) findViewById(R.id.layout_focus_unit);
		frameView = findViewById(R.id.img_frame);
		trans_frameView = findViewById(R.id.img_trans_frame);
		displayShortcuts();
		if (isEthernetOn()==true || isWifiOn()==true) {
			 mTimer.schedule(new TimerTask() {
			int preNum = 0;

			@Override
			public void run() {
				try {
					strCity = getCity(getRequestResult("http://api.izinode.com/weather"));
					try {						
					strWeather = getWeather(getRequestResult("http://api.izinode.com/weather"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
					
				}
				if(strCity!=null && strWeather!=null){
					
					handler.sendEmptyMessage(WEATHER_SUCCESS);
				}
				
				
				
			}
		}, 0, 30*60*1000);
		}
		 
	}
		class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {

		static final String SYSTEM_REASON = "reason";
		static final String SYSTEM_HOME_KEY = "homekey";//home key
		static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_REASON);
				if (reason != null) {
					if (reason.equals(SYSTEM_HOME_KEY)) {
					
					LauncherActivity.viewMenu.setDisplayedChild(0);
					LauncherActivity.handler.sendEmptyMessage(0);	
					} else if (reason.equals(SYSTEM_RECENT_APPS)) {
						// long home key澶勭悊鐐�
					}
				}
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if (SystemProperties.getBoolean("ro.platform.has.mbxuimode", false)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if (isInTouchMode
				|| (IntoCustomActivity && isShowHomePage && ifChangedShortcut)) {
			LauncherActivity.dontRunAnim = true;
			layoutScaleShadow.setVisibility(View.INVISIBLE);
			frameView.setVisibility(View.INVISIBLE);
		}
        displayDate();
		displayShortcuts();
	    displayStatus();
		setHeight();
       
		if (cantGetDrawingCache) {
			resetShadow();
		}

		IntoCustomActivity = false;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	
	
		prevFocusedView = null;
	}

	@Override
	protected void onDestroy() {
	    unregisterReceiver(mediaReceiver);
		unregisterReceiver(netReceiver);
		unregisterReceiver(appReceiver);
		unregisterReceiver(receiver);
		super.onDestroy();
	}



	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
			if (Intent.ACTION_MAIN.equals(intent.getAction())) {
		    layoutScaleShadow.setVisibility(View.INVISIBLE);
			trans_frameView.setVisibility(View.INVISIBLE);
			frameView.setVisibility(View.INVISIBLE);
			isShowHomePage = true;
			IntoCustomActivity = false;
			updateAllShortcut = true;
		    dontRunAnim = true;
		}
}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startX = event.getX();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (pressedAddButton != null && isAddButtonBeTouched && !IntoCustomActivity
					) {
				Rect rect = new Rect();
				pressedAddButton.getGlobalVisibleRect(rect);
				
				popWindow_top = rect.top - 10;
				popWindow_bottom = rect.bottom + 10;
				new Thread(new Runnable() {
					public void run() {
						mHandler.sendEmptyMessage(1);
					}
				}).start();
				Intent intent = new Intent();
				intent.putExtra("top", popWindow_top);
				intent.putExtra("bottom", popWindow_bottom);
				intent.putExtra("left", rect.left);
				intent.putExtra("right", rect.right);
				intent.setClass(this, CustomAppsActivity.class);
				startActivity(intent);
				IntoCustomActivity = true;
				isAddButtonBeTouched = false;
					} else if (!isShowHomePage) {
				// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@ getX = " +event.getX() +
				// " startX = " + startX);
				if (event.getX() + 20 < startX && startX != -1f) {
					viewMenu.setInAnimation(this, R.anim.push_right_in);
					viewMenu.setOutAnimation(this, R.anim.push_right_out);
					viewMenu.showNext();
				} else if (event.getX() - 20 > startX && startX != -1f) {
					viewMenu.setInAnimation(this, R.anim.push_left_in);
					viewMenu.setOutAnimation(this, R.anim.push_left_out);
					viewMenu.showPrevious();
				}
			}
		}
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		int code = 0;
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			code = keyCode - 7;
			privitekey = privitekey + code;
			break;
		default:
			privitekey = "";
			break;
		}
		// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@ privitekey=" + privitekey);
		if (privitekey.equals("1120")) {
			privitekey = "";
			Intent mac_test = new Intent();
			try {
				mac_test.setComponent(new ComponentName(
						"com.videostrong.factorytest",
						"com.videostrong.factorytest.ObbMountActivity"));
				startActivity(mac_test);
			} catch (Exception e) {
				mac_test.setComponent(new ComponentName(
						"com.videostrong.factorytest",
						"com.videostrong.factorytest.ObbMountActivity"));
				startActivity(mac_test);
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@ KEYCODE_BACK");
			if (!isShowHomePage && !animIsRun) {
				viewMenu.setVisibility(View.GONE);
				viewMenu.clearFocus();
				viewHomePage.setVisibility(View.VISIBLE);
				isShowHomePage = true;
				IntoCustomActivity = false;
				if (saveHomeFocusView != null && !isInTouchMode) {
					prevFocusedView = null;
					dontRunAnim = true;
					saveHomeFocusView.clearFocus();
					dontRunAnim = true;
					saveHomeFocusView.requestFocus();
				}
			}else {
				viewMenu.requestFocus();
				int sm=LauncherActivity.viewMenu.getDisplayedChild();
				if (sm==0) {
					
				}else if (sm==1) {					
					LauncherActivity.handler.sendEmptyMessage(0);
					LauncherActivity.viewMenu.showPrevious();
				}else if (sm==2) {
					LauncherActivity.handler.sendEmptyMessage(1);
					LauncherActivity.viewMenu.showPrevious();
					
				}else if(sm==3){
					LauncherActivity.handler.sendEmptyMessage(2);
					LauncherActivity.viewMenu.showPrevious();
					
				}

					
				}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
                    	ViewGroup view = (ViewGroup) getCurrentFocus();
			if (view.getChildAt(0) instanceof ImageView) {
				ImageView img = (ImageView) view.getChildAt(0);
				if (img != null
						&& img.getDrawable()
								.getConstantState()
								.equals(getResources().getDrawable(
										R.drawable.item_img_add)
										.getConstantState())) {
			
					Rect rect = new Rect();
					view.getGlobalVisibleRect(rect);

					setHeight();
					popWindow_top = rect.top ;
					popWindow_bottom = rect.bottom + 10;
					setPopWindow(popWindow_top, popWindow_bottom);
					Intent intent = new Intent();
					intent.putExtra("top", popWindow_top);
					intent.putExtra("bottom", popWindow_bottom);
					intent.putExtra("left", rect.left);
					intent.putExtra("right", rect.right);
					intent.setClass(this, CustomAppsActivity.class);
					startActivity(intent);
					IntoCustomActivity = true;
					 if (saveHomeFocusView != null){
					 saveHomeFocusView.clearFocus();
					 }
				}}}
			
		   else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			ComponentName globalSearchActivity = searchManager
					.getGlobalSearchActivity();
			if (globalSearchActivity == null) {
				return false;
			}else if(keyCode == KeyEvent.KEYCODE_HOME){		
                  System.out.println("activity"+view);			
				 LauncherActivity.viewMenu.setDisplayedChild(0);
				return false;
			}
			Intent intent = new Intent(
					SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(globalSearchActivity);
			Bundle appSearchData = new Bundle();
			appSearchData.putString("source", "LauncherActivity-search");
			intent.putExtra(SearchManager.APP_DATA, appSearchData);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
		private void initStaticVariable() {
		isShowHomePage = true;
		dontRunAnim = false;
		dontDrawFocus = false;
		ifChangedShortcut = true;
		IntoCustomActivity = false;
		IntoApps = true;
		isAddButtonBeTouched = false;
		isInTouchMode = false;
		animIsRun = false;
		updateAllShortcut = true;
		animIsRun = false;
		cantGetDrawingCache = false;

		setHeight();
	}

	public static void updateView(int num) {
		if (num == 0) {
			LauncherActivity.top_Title.setText("Media");
			LauncherActivity.left.setVisibility(View.GONE);
			LauncherActivity.right.setVisibility(View.VISIBLE);
			LauncherActivity.iv_house.setImageResource(R.drawable.house_black);
			LauncherActivity.iv_point_one.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_two.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_three.setImageResource(R.drawable.point);
        
			
		} 
		if(num==1){
			LauncherActivity.top_Title.setText("Communication");
			LauncherActivity.left.setVisibility(View.VISIBLE);
			LauncherActivity.iv_house.setImageResource(R.drawable.house);
			LauncherActivity.iv_point_one
					.setImageResource(R.drawable.point_gray);
			LauncherActivity.iv_point_two.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_three.setImageResource(R.drawable.point);
		}else if (num == 2) {
			LauncherActivity.top_Title.setText("Apps");
			LauncherActivity.iv_house.setImageResource(R.drawable.house);
			LauncherActivity.right.setVisibility(View.VISIBLE);
			LauncherActivity.iv_point_two
					.setImageResource(R.drawable.point_gray);
			LauncherActivity.iv_point_one.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_three.setImageResource(R.drawable.point);
		} else if (num == 3) {
			LauncherActivity.top_Title.setText("Settings");
			LauncherActivity.right.setVisibility(View.GONE);
			LauncherActivity.iv_house.setImageResource(R.drawable.house);
			LauncherActivity.iv_point_two.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_one.setImageResource(R.drawable.point);
			LauncherActivity.iv_point_three
					.setImageResource(R.drawable.point_gray);

		}

	}

	private void setHeight() {
		
		String outputmode = SystemProperties.get("ubootenv.var.outputmode",
				"1080p");
		isRealOutputMode = SystemProperties.getBoolean(
				"ro.platform.has.realoutputmode", false);
		isNative4k2k = SystemProperties.getBoolean(
				"ro.platform.has.native4k2k", false);
		isNative720 = SystemProperties.getBoolean("ro.platform.has.native720",
				false);
		if (isNative4k2k && outputmode.startsWith("4k2k")) {
			REAL_OUTPUT_MODE = "4k2knative";
			CustomAppsActivity.CONTENT_HEIGHT = 900;
		} else if (isRealOutputMode && !isNative720) {
			REAL_OUTPUT_MODE = "1080p";
			CustomAppsActivity.CONTENT_HEIGHT = 450;
		} else {
			REAL_OUTPUT_MODE = "720p";
			CustomAppsActivity.CONTENT_HEIGHT = 300;
		}
		Display display = this.getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getRealSize(p);
		SCREEN_HEIGHT = p.y;
		SCREEN_WIDTH = p.x;
	
	}
	
	public List<Map<String, Object>> getStatusData(boolean is_ethernet_on) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		 if (wifi_level != -1){
    		switch (wifi_level + 1) {
    			//case 0:
    			 //	map.put("item_type", R.drawable.wifi1);				
    			//	break;
    			case 1:
    				map.put("item_type", R.drawable.wifi2);;
    				break;
    			case 2:
    			 	map.put("item_type", R.drawable.wifi3);
    				break;
    			case 3:
    			 	map.put("item_type", R.drawable.wifi4);
    				break;
    			case 4:
    			 	map.put("item_type", R.drawable.wifi5);
    				break;
    			default:
    				break;
    		}
    		list.add(map);
        }
		
		
		
		if (isSdcardExists() == true) {
			map = new HashMap<String, Object>();
			map.put("item_type", R.drawable.img_status_sdcard);
			list.add(map);
		}

		if (isUsbExists() == true) {
			map = new HashMap<String, Object>();
			map.put("item_type", R.drawable.img_status_usb);
			list.add(map);
		}

		if (is_ethernet_on == true) {
			map = new HashMap<String, Object>();
			map.put("item_type", R.drawable.img_status_ethernet);
			list.add(map);
		}
		
		return list;
	}

	public void setPopWindow(int top, int bottom) {
		View view = this.getWindow().getDecorView();
		view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		view.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
		view.destroyDrawingCache();

		screenShot = null;
		screenShot_keep = null;

		if (bottom > SCREEN_HEIGHT / 2) {
			if (top + 3 - CustomAppsActivity.CONTENT_HEIGHT > 0) {
				screenShot = Bitmap
						.createBitmap(bmp, 0, 0, bmp.getWidth(), top);
				screenShot_keep = Bitmap.createBitmap(bmp, 0,
						CustomAppsActivity.CONTENT_HEIGHT, bmp.getWidth(), top
								+ 3 - CustomAppsActivity.CONTENT_HEIGHT);
			} else {
				screenShot = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
						CustomAppsActivity.CONTENT_HEIGHT);
				screenShot_keep = null;
			}
		} else {
			screenShot = Bitmap.createBitmap(bmp, 0, bottom, bmp.getWidth(),
					SCREEN_HEIGHT - bottom);
			screenShot_keep = Bitmap.createBitmap(bmp, 0, bottom,
					bmp.getWidth(), SCREEN_HEIGHT
							- (bottom + CustomAppsActivity.CONTENT_HEIGHT));
		}
	
	}

	public static void getPopWindow() {

		View contentView = mInflate.inflate(R.layout.layout_custom_apps, null);
		menue = new PopupWindow(contentView,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, true);
		menue.showAtLocation(mainview, Gravity.CENTER_VERTICAL
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}
  private void updateStatus() {
		((BaseAdapter) lv_status.getAdapter()).notifyDataSetChanged();
	}

	private void initView() {
		city = (TextView) findViewById(R.id.country);
		wWeather = (TextView) findViewById(R.id.mWeather);
		description = (TextView) findViewById(R.id.weather);
		iv_wifi = (ImageView) findViewById(R.id.iv_wifi);
		times = (TextView) findViewById(R.id.time);
		top_Title = (TextView) findViewById(R.id.title);
		
        lv_status = (GridView) findViewById(R.id.list_status);
		re_iztv = (MyRelativeLayout) findViewById(R.id.re_iztv);

		re_iradio = (MyRelativeLayout) findViewById(R.id.re_iradio);
		re_yutu = (MyRelativeLayout) findViewById(R.id.re_yutu);
		re_airplay = (MyRelativeLayout) findViewById(R.id.re_airplay);
		re_ilure = (MyRelativeLayout) findViewById(R.id.re_ilure);
		re_google = (MyRelativeLayout) findViewById(R.id.re_google);
		// second
		re_bird = (MyRelativeLayout) findViewById(R.id.re_bird);
		re_human = (MyRelativeLayout) findViewById(R.id.re_human);
		re_fire = (MyRelativeLayout) findViewById(R.id.re_fire);
		re_sock = (MyRelativeLayout) findViewById(R.id.re_sock);
		re_wire = (MyRelativeLayout) findViewById(R.id.re_wire);
		// third
		// fourth
		for_re_setting = (MyRelativeLayout) findViewById(R.id.for_re_setting);
		for_re_explore = (MyRelativeLayout) findViewById(R.id.for_re_explore);
		for_re_advance = (MyRelativeLayout) findViewById(R.id.for_re_advance);
		for_re_clear = (MyRelativeLayout) findViewById(R.id.for_re_clear);
		for_re_netspeed = (MyRelativeLayout) findViewById(R.id.for_re_netspeed);

		left = (MyRelativeLayout) findViewById(R.id.re_sow_left);
		left.setVisibility(View.GONE);
		right = (MyRelativeLayout) findViewById(R.id.re_sow_right);
		initHousePoint();
		homeShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_home);
		layoutScaleShadow = (RelativeLayout) findViewById(R.id.layout_focus_unit);
	
		

		
	}

	private void initHousePoint() {
		iv_house = (ImageView) findViewById(R.id.imageview1);
		iv_point_one = (ImageView) findViewById(R.id.imageview2);
		iv_point_two = (ImageView) findViewById(R.id.imageview3);
		iv_point_three = (ImageView) findViewById(R.id.imageview4);

	}

	private void setRectOnKeyListener() {
		re_iztv.setOnKeyListener(new MyOnKeyListener(this, null));
		re_iradio.setOnKeyListener(new MyOnKeyListener(this, null));
		re_yutu.setOnKeyListener(new MyOnKeyListener(this, null));
		re_airplay.setOnKeyListener(new MyOnKeyListener(this, null));
		re_ilure.setOnKeyListener(new MyOnKeyListener(this, null));
		re_google.setOnKeyListener(new MyOnKeyListener(this, null));
		re_bird.setOnKeyListener(new MyOnKeyListener(this, null));
		re_human.setOnKeyListener(new MyOnKeyListener(this, null));
		re_fire.setOnKeyListener(new MyOnKeyListener(this, null));
		re_sock.setOnKeyListener(new MyOnKeyListener(this, null));
		re_wire.setOnKeyListener(new MyOnKeyListener(this, null));
		for_re_setting.setOnKeyListener(new MyOnKeyListener(this, null));
		for_re_explore.setOnKeyListener(new MyOnKeyListener(this, null));
		for_re_advance.setOnKeyListener(new MyOnKeyListener(this, null));
		for_re_clear.setOnKeyListener(new MyOnKeyListener(this, null));
		for_re_netspeed.setOnKeyListener(new MyOnKeyListener(this, null));

		re_iztv.setOnTouchListener(new MyOnTouchListener(this, null));
		re_iradio.setOnTouchListener(new MyOnTouchListener(this, null));
		re_yutu.setOnTouchListener(new MyOnTouchListener(this, null));
		re_airplay.setOnTouchListener(new MyOnTouchListener(this, null));
		re_ilure.setOnTouchListener(new MyOnTouchListener(this, null));
		re_google.setOnTouchListener(new MyOnTouchListener(this, null));
		re_bird.setOnTouchListener(new MyOnTouchListener(this, null));
		re_human.setOnTouchListener(new MyOnTouchListener(this, null));
		re_fire.setOnTouchListener(new MyOnTouchListener(this, null));
		re_sock.setOnTouchListener(new MyOnTouchListener(this, null));
		re_wire.setOnTouchListener(new MyOnTouchListener(this, null));
		for_re_setting.setOnTouchListener(new MyOnTouchListener(this, null));
		for_re_explore.setOnTouchListener(new MyOnTouchListener(this, null));
		for_re_advance.setOnTouchListener(new MyOnTouchListener(this, null));
		for_re_clear.setOnTouchListener(new MyOnTouchListener(this, null));
		for_re_netspeed.setOnTouchListener(new MyOnTouchListener(this, null));
		left.setOnTouchListener(new MyOnTouchListener(this, null));
		right.setOnTouchListener(new MyOnTouchListener(this, null));

	}
	
	public boolean isUsbExists() {
		File dir = new File(USB_PATH);
		if (dir.exists() && dir.isDirectory()) {
			if (dir.listFiles() != null) {
				if (dir.listFiles().length > 0) {
					for (File file : dir.listFiles()) {
						String path = file.getAbsolutePath();
						if (path.startsWith(USB_PATH + "/sd")
								&& !path.equals(SD_PATH)) {
							// if (path.startsWith("/mnt/sd[a-z]")){
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean isSdcardExists() {
		if (Environment.getExternalStorage2State().startsWith(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(SD_PATH);
			if (dir.exists() && dir.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	private void updateWifiLevel() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connectivity
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			WifiManager mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
			WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
			int wifi_rssi = mWifiInfo.getRssi();

			if (wifi_rssi <= -100)
				wifi_level = -1;
			else
				wifi_level = WifiManager.calculateSignalLevel(wifi_rssi, 4);
		} else {
			wifi_level = -1;
		}
	}

	private boolean isEthernetOn() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		if (info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isWifiOn(){
       	ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connectivity
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			return true;
		} else {
			return false;
		}

	}


	public String getTime() {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
 
		is24hFormart = DateFormat.is24HourFormat(this);
		if (!is24hFormart && hour >= 12) {
			hour = hour - 12;
		}

		String time = "";
		if (hour >= 10) {
			time += Integer.toString(hour);
		} else {
			time += "0" + Integer.toString(hour);
		}
		time += ":";

		if (minute >= 10) {
			time += Integer.toString(minute);
		} else {
			time += "0" + Integer.toString(minute);
		}

		return time;
	}

	

	private String getRequestResult(String str) {
		HttpGet httpGet = new HttpGet(str);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("MyApplication", result);
		return result;
	}

	private String getCity(String result) {
		String city_name = null;
		try {
			JSONObject jObject = new JSONObject(result);
			city_name = jObject.getString("city");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return city_name;
	}

	private String getWeather(String wethJson) {
		JSONObject object = null;
		JSONObject array = null;
		String dec = null;
		String temp=null;
		try {
			object = new JSONObject(wethJson);
			array = object.getJSONObject("weather");
			dec = array.getString("des");
			temp=array.getString("temp");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return dec+" "+temp;
	}
	
	public static int parseItemIcon(String packageName) {
		if (packageName.equals("com.app.myapps")) {
			return R.drawable.item_myapps;
		} 
		if (packageName.equals("com.gsoft.appinstall")) {
			return R.drawable.icon_appinstaller;
		}
		if (packageName.equals("com.google.android.apps.maps")) {
			return R.drawable.item_map_google;
		}

		return -1;
	}

	private void loadCustomApps(String path) {
		File mFile = new File(path);
		File default_file = new File(MyAppsActivity.DEFAULT_SHORTCUR_PATH);

		if (!mFile.exists()) {
			mFile = default_file;
			getShortcutFromDefault(MyAppsActivity.DEFAULT_SHORTCUR_PATH,
					MyAppsActivity.SHORTCUT_PATH);
		} else {
			try {
				BufferedReader b = new BufferedReader(new FileReader(mFile));
				if (b.read() == -1) {
					getShortcutFromDefault(
							MyAppsActivity.DEFAULT_SHORTCUR_PATH,
							MyAppsActivity.SHORTCUT_PATH);
				}
				if (b != null)
					b.close();
			} catch (IOException e) {
			}
		}
 
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(mFile));
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.startsWith(MyAppsActivity.HOME_SHORTCUT_HEAD)) {
					str = str.replaceAll(MyAppsActivity.HOME_SHORTCUT_HEAD, "");
										
										list_homeShortcut = str.split(";");
				}
			}

		} catch (Exception e) {
			Log.d("LauncherActivity", "" + e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}
	}

	

	public void getShortcutFromDefault(String srcPath, String desPath) {
		File srcFile = new File(srcPath);
		File desFile = new File(desPath);
		if (!srcFile.exists()) {
			return;
		}
		if (!desFile.exists()) {
			try {
				desFile.createNewFile();
			} catch (Exception e) {
			}
		}

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(srcFile));
			String str = null;
			List list = new ArrayList();

			while ((str = br.readLine()) != null) {
				list.add(str);
			}
			bw = new BufferedWriter(new FileWriter(desFile));
			for (int i = 0; i < list.size(); i++) {
				bw.write(list.get(i).toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
			}
		}
	}

	private List<Map<String, Object>> loadShortcutList(PackageManager manager,
			final List<ResolveInfo> apps, String[] list_custom_apps) {
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (list_custom_apps != null) {
			for (int i = 0; i < list_custom_apps.length; i++) {
				if (apps != null) {
					final int count = apps.size();
					for (int j = 0; j < count; j++) {
						ApplicationInfo application = new ApplicationInfo();
						ResolveInfo info = apps.get(j);

						application.title = info.loadLabel(manager);
						application
								.setActivity(
										new ComponentName(
												info.activityInfo.applicationInfo.packageName,
												info.activityInfo.name),
										Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						application.icon = info.activityInfo.loadIcon(manager);
						if (application.componentName.getPackageName().equals(
								list_custom_apps[i])) {
							if (application.componentName.getPackageName()
									.equals("com.android.gallery3d")
									&& application.intent.toString().contains(
											"camera"))
								continue;

							map = new HashMap<String, Object>();
							map.put("item_name", application.title.toString());
							map.put("file_path", application.intent);
							map.put("item_type", application.icon);
							map.put("item_symbol", application.componentName);
							list.add(map);
							break;
						}
					}
				}
			}
		}

		return list;
	}

	private Map<String, Object> getAddMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item_name", this.getResources().getString(R.string.str_add));
		map.put("file_path", "com.add.path");
		map.put("item_type", R.drawable.item_img_add);

		return map;
	}

	private Map<String, Object> getAllApps() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("file_path", "com.all.apps");
		map.put("item_type", R.drawable.item_myapps);
map.put("item_name", this.getResources().getString(R.string.all));

		return map;
	}

	
	

	private void loadApplications() {
		List<Map<String, Object>> HomeShortCutList = new ArrayList<Map<String, Object>>();

		PackageManager manager = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		HomeShortCutList.clear();
		loadCustomApps(MyAppsActivity.SHORTCUT_PATH);

		if (updateAllShortcut == true) {
			HomeShortCutList = loadShortcutList(manager, apps,
					list_homeShortcut);

			if (apps != null) {
				final int count = apps.size();
				for (int i = 0; i < count; i++) {

					ApplicationInfo application = new ApplicationInfo();
					ResolveInfo info = apps.get(i);

					application.title = info.loadLabel(manager);
					application
							.setActivity(
									new ComponentName(
											info.activityInfo.applicationInfo.packageName,
											info.activityInfo.name),
									Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					application.icon = info.activityInfo.loadIcon(manager);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("item_name", application.title.toString());
					map.put("file_path", application.intent);
					map.put("item_type", application.icon);
					map.put("item_symbol", application.componentName);
					// Log.d(TAG, ""+ application.componentName.getPackageName()
					// + " path="+application.intent);
					appShortCutList.add(map);
				}
			}

			Map<String, Object> all = getAllApps();
			HomeShortCutList.add(all);
			Map<String, Object> map = getAddMap();
			HomeShortCutList.add(map);

			homeShortcutView.setLayoutView(HomeShortCutList, 0);

			

			updateAllShortcut = false;
		}

		HomeShortCutList = loadShortcutList(manager, apps, list_homeShortcut);

		Map<String, Object> all = getAllApps();
			HomeShortCutList.add(0, all);
					HomeShortCutList.add(1,getAddMap());
		homeShortcutView.setLayoutView(HomeShortCutList, 0);

	}

		private void displayShortcuts() {
		if (ifChangedShortcut == true) {
			loadApplications();
			ifChangedShortcut = false;

			if (!isShowHomePage) {
				if (numberInGrid == -1) {
					new Thread(new Runnable() {
						public void run() {
							ViewGroup findGridLayout = null;
							while (findGridLayout == null) {
								findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
										.getCurrentView()).getChildAt(4))
										.getChildAt(0));
							}
							mHandler.sendEmptyMessage(3);
						}
					}).start();
				} else {
					new Thread(new Runnable() {
						public void run() {
							ViewGroup findGridLayout = null;
							while (findGridLayout == null) {
								findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
										.getCurrentView()).getChildAt(4))
										.getChildAt(0));
							}
							mHandler.sendEmptyMessage(4);
						}
					}).start();
				}
			} else if (numberInGridOfShortcut != -1) {
				new Thread(new Runnable() {
					public void run() {
						while (homeShortcutView
								.getChildAt(numberInGridOfShortcut) == null) {
						}
						mHandler.sendEmptyMessage(5);
					}
				}).start();

			} else if (IntoCustomActivity) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(200);
						} catch (Exception e) {
					
						}
						mHandler.sendEmptyMessage(6);
					}
				}).start();
			}
		}
	}
	
	private void sendKeyCode(final int keyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					Log.e("Exception when sendPointerSync", e.toString());
				}
			}
		}.start();
	}
	private void resetShadow() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					
				
				}
				// Message msg = new Message();
				// msg.what = 2;
				mHandler.sendEmptyMessage(2);
			}
		}).start();
	}
	private void updateAppList(Intent intent) {
		final boolean replacing = intent.getBooleanExtra(
				Intent.EXTRA_REPLACING, false);
		boolean isShortcutIndex = false;
		String packageName = null;

		if (intent.getData() != null) {
			packageName = intent.getData().getSchemeSpecificPart();
			if (packageName == null || packageName.length() == 0) {
				// they sent us a bad intent
				return;
			}
			if (packageName.equals("com.android.provision"))
				return;
		}
		if (getCurrentFocus() != null
				&& getCurrentFocus().getParent() instanceof MyGridLayout) {
			int parentId = ((MyGridLayout) getCurrentFocus().getParent())
					.getId();
			dontRunAnim = true;
			if (parentId != View.NO_ID) {
				String name = getResources().getResourceEntryName(parentId);
				if (name.equals("gv_shortcut")) {
					numberInGridOfShortcut = ((MyGridLayout) getCurrentFocus()
							.getParent()).indexOfChild(getCurrentFocus());
					isShortcutIndex = true;
				}
			}
			if (!isShortcutIndex) {
				numberInGrid = ((MyGridLayout) getCurrentFocus().getParent())
						.indexOfChild(getCurrentFocus());
			}
		} else {
			numberInGrid = -1;
		}

		updateAllShortcut = true;
		ifChangedShortcut = true;
		displayShortcuts();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// setPopWindow(popWindow_top, popWindow_bottom);
				break;
			case 2:
				MyRelativeLayout view = (MyRelativeLayout) getCurrentFocus();
				view.setSurface();
				break;
			case 3:
				ViewGroup findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
						.getCurrentView()).getChildAt(0)).getChildAt(0));
				int count = findGridLayout.getChildCount();
				LauncherActivity.dontRunAnim = true;
				findGridLayout.getChildAt(count - 1).requestFocus();
				LauncherActivity.dontRunAnim = false;
				break;
			case 4:
				if (numberInGrid != -1) {
					findGridLayout = ((ViewGroup) ((ViewGroup) ((ViewGroup) viewMenu
							.getCurrentView()).getChildAt(0)).getChildAt(0));
					LauncherActivity.dontRunAnim = true;
					findGridLayout.getChildAt(numberInGrid).requestFocus();
					LauncherActivity.dontRunAnim = false;
					numberInGrid = -1;
				}
				break;
			case 5:
				if (numberInGridOfShortcut != -1) {
					LauncherActivity.dontRunAnim = true;
					saveHomeFocusView = homeShortcutView
							.getChildAt(numberInGridOfShortcut);
					saveHomeFocusView.requestFocus();
					LauncherActivity.dontRunAnim = false;
					numberInGridOfShortcut = -1;
				}
				break;
			case 6:
				int i = homeShortcutView.getChildCount();
				LauncherActivity.dontRunAnim = true;
				homeShortcutView.getChildAt(i - 1).requestFocus();
				LauncherActivity.dontRunAnim = false;
				if (!isInTouchMode) {
					layoutScaleShadow.setVisibility(View.VISIBLE);
					frameView.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};
  private void displayDate() {

		is24hFormart = DateFormat.is24HourFormat(this);
		
		times.setText(getTime());
		times.setTypeface(Typeface.DEFAULT_BOLD);
	
	}
	private void displayStatus() {
		LocalAdapter ad = new LocalAdapter(LauncherActivity.this,
				getStatusData(isEthernetOn()), R.layout.homelist_item,
				new String[] { "item_type", "item_name", "item_sel" },
				new int[] { R.id.item_type, 0, 0 });
		lv_status.setAdapter(ad);
	}
	private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// Log.d(TAG, " mediaReceiver		  action = " + action);
			if (action == null)
				return;

			if (Intent.ACTION_MEDIA_EJECT.equals(action)
					|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
					|| Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
				displayStatus();
				updateStatus();
			}
		}
	};
	private BroadcastReceiver netReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action == null)
				return;		
			if (action.equals(outputmode_change_action)) {
				setHeight();
			}
			if (action.equals(Intent.ACTION_TIME_TICK)) {
				displayDate();
				time_count++;
				if (time_count >= time_freq) {				
					time_count = 0;
				}
			} else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE
					.equals(action)
					|| Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE
							.equals(action)) {
				updateAppList(intent);
			} else if(net_change_action.equals(action)){
                  ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
                 NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
                 if(mNetworkInfo==null){
                 	if(mTimer!=null){
                 		mTimer.cancel();
                 		mTimer=null;
                 	}
                      
                      updateWifiLevel();
                 	 displayStatus();
                 	 updateStatus();
                 }else{
                 	updateWifiLevel();
                 	displayStatus();
                 	updateStatus();
               mTimer=new Timer();  	
               mTimer.schedule(new TimerTask() {
			 int preNum = 0;

			@Override
			public void run() {
				try {
					strCity = getCity(getRequestResult("http://api.izinode.com/weather"));
					try {						
					strWeather = getWeather(getRequestResult("http://api.izinode.com/weather"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
					
				}
				if(strCity!=null && strWeather!=null){
					
					handler.sendEmptyMessage(WEATHER_SUCCESS);
				}				
				
			}
		}, 0, 30*60*1000);
                 }
                   
			}
			
		}
	};

	private BroadcastReceiver appReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			final String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
					|| Intent.ACTION_PACKAGE_REMOVED.equals(action)
					|| Intent.ACTION_PACKAGE_ADDED.equals(action)) {

				updateAppList(intent);
			}
		}
	};
}
