package com.vs.vslauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.SystemProperties;
import android.util.Log;


public class MainActivity extends Activity implements OnItemClickListener, OnItemSelectedListener{
	private final String STORAGE_PATH = "/storage";
	private final String SDCARD_FILE_NAME = "sdcard1";
	private final String UDISK_FILE_NAME = "udisk";
	private final String net_change_action = "android.net.conn.CONNECTIVITY_CHANGE";
	private final String wifi_signal_action = "android.net.wifi.RSSI_CHANGED";

	private final String file_path_lcd = "/sys/devices/platform/aml_pm_m8/led_test";
	private final String file_path_aml = "/sys/devices/aml_pm/led_test";
	
	private ImageView home_ac_weather_icon;
	private ImageView iv_wifi, iv_usb, iv_eth,iv_sdcard;
	private ResolveInfo seleted_info;
	private int seleted_position = -1;
	private GridView gridview, list_apps;
//	private MyGridView gridview2;
	private AppsAdapter user_adapter;
	private UserAppsAdapter app_adapter;
	private UseraddAppsAdapter add_adapter;
	List<ResolveInfo> list_user_app = new ArrayList<ResolveInfo>();
	List<ResolveInfo> list_user_app_popu = new ArrayList<ResolveInfo>();
	//HorizontalListView list_apps;
	ImageView kodi;
	HorizontalScrollView hs;
	private IntentFilter wifiFilter;
	private final static String TAG="VSLauncher2.MainActivity";
	private boolean isShowSdcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//setContentView(R.layout.test_lists);

		// secret check
		checkExists(); 
		isShowSdcard=SystemProperties.getBoolean("ro.launcher.nosdcard",true);		
		initView();
		showLog();	
	    wifiFilter = new IntentFilter();
		wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

		//register filter
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addDataScheme("file");
		registerReceiver(mediaReceiver, filter);

		filter = new IntentFilter();
		filter.addAction(net_change_action);
		filter.addAction(wifi_signal_action);
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
		filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		registerReceiver(netReceiver, filter);

		filter = new IntentFilter(
				//Intent.ACTION_PACKAGE_ADDED);
		//filter.addAction(
				Intent.ACTION_PACKAGE_REMOVED);
		//filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(appReceiver, filter);
		 
		//list_apps = (HorizontalListView) findViewById(R.id.listHoView);
		list_apps = (GridView)findViewById(R.id.gridView);

		setListGridView();
		
		gridview = (GridView)findViewById(R.id.listHoView);
		user_adapter = new AppsAdapter(this);

		list_user_app = getResolveinfoList();
		if (list_user_app.size() == 0) {			
			Log.d("--------", "--------- no hostroy apps-----");
		} else {
			user_adapter.setListItems(list_user_app);
			
			int etedItem = list_user_app.size();
			if (etedItem >= 8)
			{
				bExist = false;
				
				if (etedItem > 8)
				{
					list_user_app.remove(8);
				}
			}
		}

		// String st = list.get(0).activityInfo.name;
		// String stt = list.get(0).activityInfo.packageName;

		setGridView(list_user_app.size());
		
		gridview.setAdapter(user_adapter);		
		gridview.setOnItemClickListener(this);
		gridview.setFocusable(true);
		gridview.setFocusableInTouchMode(true);
		gridview.setOnItemSelectedListener(this);
		//gridview.requestFocus();
		gridview.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				int ii = 0;
				ii = arg0.getId();
				
				Log.d("----", "---"+ii+", arg1="+arg1);
				
			}});
		
		user_adapter.notifyDataSetChanged();
		gridview.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				int ii = -1;
				ii = arg1;
				int jj = -1;
				jj = arg2;
				
				//Log.d("-----", "arg1="+ii+", arg2="+jj +", arg3="+ arg3);
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
				Log.d("-----", "---------------arg1="+arg1);
			}});
	}

    /**设置GirdView参数，绑定数据*/
    private void setGridView(int size) {
        //int size = cityList.size();
    	size = 8;
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 50) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridview.setColumnWidth(180); // 设置列表项宽
        gridview.setHorizontalSpacing(10); // 设置列表项水平间距
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size); // 设置列数量=列表集合数

        
    }
	private void showLog() {
		File f2 = null;
		FileInputStream fis = null;
		Bitmap bitmap = null;
		f2 = new File("/system/etc/launcher_logo.png");
		if (f2.exists()) {
			try {
				fis = new FileInputStream(f2);
				bitmap = BitmapFactory.decodeStream(fis);

				DisplayMetrics dm = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(dm);
		        float density = dm.density;
				//Log.d(TAG, "layout density: " + density);

				Matrix matrix = new Matrix();
				matrix.postScale(density,density);
				Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
				home_ac_weather_icon.setImageBitmap(resizeBmp);

			} catch (FileNotFoundException  e) {
				home_ac_weather_icon.setImageResource(R.drawable.ki_plus);
			}
		} else {
			home_ac_weather_icon.setImageResource(R.drawable.ki_plus);
		}
	}

    private void setListGridView() {
        int size = 5; 
        int length = 120;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        list_apps.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        list_apps.setColumnWidth(itemWidth); // 设置列表项宽
        list_apps.setHorizontalSpacing(90); // 设置列表项水平间距
        list_apps.setStretchMode(GridView.NO_STRETCH);
        list_apps.setNumColumns(size); // 设置列数量=列表集合数

      //生成动态数组，并且转入数据  
        ArrayList<vsFile> lstImageItem = new ArrayList<vsFile>();  
        //for(int i=0;i<10;i++)  
        {  
        	
          lstImageItem.add(new vsFile(R.drawable.home_icon_kodi,
        		 "org.xbmc.kodi",
        		  "org.xbmc.kodi.Splash",
        		  ""
        		  ));
          lstImageItem.add(new vsFile(R.drawable.home_icon_local,
        		  "com.droidlogic.FileBrower",
        		  "com.droidlogic.FileBrower.FileBrower",
        		  ""
        		  ));
          lstImageItem.add(new vsFile(R.drawable.home_icon_apps,
        		  "com.vs.vslauncher",
        		  "com.vs.vslauncher.AllAppsActivity",
        		  ""
        		  ));
          lstImageItem.add(new vsFile(R.drawable.home_icon_set,
        		  "com.android.tv.settings",
        		  "com.android.tv.settings.MainSettings",
        		  ""
        		  ));
          lstImageItem.add(new vsFile(R.drawable.home_icon_web,
        		  "com.android.browser",
        		  "com.android.browser.BrowserActivity",
        		  ""
        		  ));

        }  
        
        listAppsAdapter ad = new listAppsAdapter(this);
        ad.setListItems(lstImageItem);
        
        list_apps.setAdapter(ad);
        list_apps.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				vsFile info = (vsFile)arg0.getItemAtPosition(arg2);
				
				openUserActivity(info.mPackageName, info.mActivityName);
				
			}});
        
        list_apps.setFocusable(true);
        list_apps.setFocusableInTouchMode(true);        
       // list_apps.requestFocus();
        
        ad.notifyDataSetChanged();
    }
    
   private void checkExists(){
      File f1=new File("/sys/devices/platform/aml_pm_m8/led_test");
      File f2=new File("/sys/devices/aml_pm/led_test");
      if(!f1.exists() && !f2.exists()){
      	  MainActivity.this.finish();
      }
	}
    
	private void initView(){
		iv_wifi = (ImageView)findViewById(R.id.status_wifi);
		iv_usb = (ImageView)findViewById(R.id.status_usb);
		iv_eth = (ImageView)findViewById(R.id.status_net);
		iv_sdcard=(ImageView)findViewById(R.id.status_sdcard);
		home_ac_weather_icon=(ImageView)findViewById(R.id.home_ac_weather_icon);
		displayDate();
		dispalyEth();
		displayWifi();
		if(isShowSdcard==false){
			iv_sdcard.setVisibility(View.VISIBLE);
			displaySdcard();
		}
		
		displayUsb();
	
		
	
		//ColorDrawable dw = new ColorDrawable(Color.argb(120, 0, 120, 0));		
		/*
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.home_ac_kodi);
		layout.setFocusable(true);
		layout.setFocusableInTouchMode(true);
		layout.requestFocus();
		//layout.setOnFocusChangeListener(this);
		layout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                 intent.setClassName("org.xbmc.kodi", "org.xbmc.kodi.Splash");

		         startActivity(intent);

				
			}});
			
		RelativeLayout layout2 = (RelativeLayout)findViewById(R.id.home_ac_local);
		layout2.setFocusable(true);
		layout2.setFocusableInTouchMode(true);
		//layout2.setOnFocusChangeListener(this);
		layout2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}});
		
		RelativeLayout layout3 = (RelativeLayout)findViewById(R.id.home_ac_apps);
		layout3.setFocusable(true);
		layout3.setFocusableInTouchMode(true);
		//layout3.setOnFocusChangeListener(this);
		layout3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openUserActivity("com.vs.vslauncher",
						"com.vs.vslauncher.AllAppsActivity");
			}});
		
		RelativeLayout layout4 = (RelativeLayout)findViewById(R.id.home_ac_set);
		layout4.setFocusable(true);
		layout4.setFocusableInTouchMode(true);
		//layout4.setOnFocusChangeListener(this);
		layout4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				openUserActivity("com.android.settings",
						"com.android.settings.Settings");
			}});
		
		RelativeLayout layout5 = (RelativeLayout)findViewById(R.id.home_ac_web);
		layout5.setFocusable(true);
		layout5.setFocusableInTouchMode(true);
		//layout5.setOnFocusChangeListener(this);
		layout5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openUserActivity("com.android.browser",
						"com.android.browser.BrowserActivity");
			}});
			*/
		
	
		
	}
	
	
	 
	@Override
	protected void onResume() {	
	 RelativeLayout layout=(RelativeLayout)findViewById(R.id.home_main_layout);	
	 Utils.send_led_msg("dotappoff");
	    layout.setBackgroundResource(R.drawable.app_bg);
		this.registerReceiver(mWifiReceiver, wifiFilter);	
		super.onResume();
	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mediaReceiver);
		unregisterReceiver(netReceiver);
		unregisterReceiver(appReceiver);
		unregisterReceiver(mWifiReceiver);
		System.gc();
		super.onDestroy();
	}

	private List<ResolveInfo> getResolveinfoList() {
		return Utils.loadUserApps(this);
	}

	public void onButtonClick(View view) {
		int id = view.getId();

	}

	private void openUserActivity(String packageName, String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);

		startActivity(intent);

		//overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU
				//|| keyCode == KeyEvent.KEYCODE_BACK
				) {
			// do something
			View view = this.getCurrentFocus();
			int ii = view.getId();
			
			if (ii == R.id.listHoView){
				if (!seleted_info.activityInfo.packageName.equals(getString(R.string.add_app_title))){
					if (list_user_app.size() > 1)
						showPopupWindow(view, false);
				}
			}
			
			return false;
		} 
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		ResolveInfo vs = (ResolveInfo) parent.getItemAtPosition(position);

		String packageName = vs.activityInfo.packageName;
		String activityName = vs.activityInfo.name;

		if (packageName.equals(getString(R.string.add_app_title))) {
			showPopupWindow(view);
		} else {
			openUserActivity(packageName, activityName);
		}

		user_adapter.setSelectIndex(position);
		user_adapter.notifyDataSetChanged();
	}



	private List<ResolveInfo> getUserAppList() {

		List<ResolveInfo> list = Utils.loadAllApps(this);//Utils.loadApps(this);

		int length = list_user_app.size();

		for (int j = 0; j < length; j++) {
			for (int i = 0; i < list.size(); i++){
				if (list.get(i).activityInfo.packageName.equals(list_user_app.get(j).activityInfo.packageName))
				{
					list.remove(i);
					break;
				}
					//			if (list.contains(list_user_app.get(j))) 
//			{
//				list.remove(list_user_app.get(j));
//			}
			}
		}

		return list;
	}

	private PopupWindow popupWindow;
	private static boolean bExist = true;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void showPopupWindow(View view, boolean flag) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(getBaseContext()).inflate(
				R.layout.user_lists, null);

		ListView list_user = (ListView) contentView
				.findViewById(R.id.listView1);
		
		add_adapter = new UseraddAppsAdapter(this);
		
		list_user_app_popu = getUserAppList();

		list_user_app_popu.add(0, seleted_info);	
		
		if (list_user_app_popu.size() == 0) {

		} else {
			add_adapter.setListItems(list_user_app_popu);
		}

		list_user.setAdapter(add_adapter);
		list_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ResolveInfo rinfo = (ResolveInfo) parent
						.getItemAtPosition(position);

				if (position == 0){

					Utils.delDateInfo(rinfo.activityInfo.packageName);
					list_user_app.remove(seleted_position);
					if(list_user_app.size()>0)
					{
						seleted_info = list_user_app.get(0);
					}
					if (seleted_position>0)
					{
						seleted_position -= 1;
						gridview.setSelection(seleted_position);
					}
					
					if(7 == list_user_app.size() && !bExist)
					{
						ResolveInfo info = new ResolveInfo();
						info.activityInfo = new ActivityInfo();
						info.activityInfo.icon = R.drawable.user_app_add;
						info.activityInfo.labelRes = R.string.add_app_title;
						info.activityInfo.packageName = getString(R.string.add_app_title);
						
						list_user_app.add(list_user_app.size(), info);
						bExist = true;
					}
				}
				else
				{
				
					
				Utils.insetDateInfo(rinfo.getIconResource(), "", rinfo.activityInfo.packageName, "");

				Utils.delDateInfo(seleted_info.activityInfo.packageName);
				list_user_app.remove(seleted_position);
				list_user_app.add(seleted_position, rinfo);
				gridview.setSelection(seleted_position);
				seleted_info = rinfo;
				}
				
				user_adapter.setListItems(list_user_app);
				// list_apps.setAdapter(user_adapter);

				user_adapter.notifyDataSetChanged();

				list_user_app_popu.remove(position);

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});

		popupWindow = new PopupWindow(contentView,
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug

		ColorDrawable dw = new ColorDrawable(Color.argb(120, 0, 0, 0));
		popupWindow.setBackgroundDrawable(dw);

		// 设置好参数之后再show
		// popupWindow.showAsDropDown(view);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(getBaseContext()).inflate(
				R.layout.user_lists, null);

		ListView list_user = (ListView) contentView
				.findViewById(R.id.listView1);
		app_adapter = new UserAppsAdapter(this);
		list_user_app_popu = getUserAppList();

		if (list_user_app_popu.size() == 0) {

		} else {
			app_adapter.setListItems(list_user_app_popu);
		}

		list_user.setAdapter(app_adapter);
		list_user.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ResolveInfo rinfo = (ResolveInfo) parent
						.getItemAtPosition(position);

				
				// list_user_app.add(rinfo);
				Utils.insetDateInfo(rinfo.getIconResource(), "", rinfo.activityInfo.packageName, "");

				list_user_app.add(list_user_app.size() - 1, rinfo);

				list_user_app_popu.remove(position);

				if(list_user_app.size() == 9)
				{
					list_user_app.remove(8);
					bExist = false;
					gridview.setSelection(6);
				}
				else
					gridview.setSelection(list_user_app.size() - 1);

				user_adapter.setListItems(list_user_app);
				// list_apps.setAdapter(user_adapter);

				user_adapter.notifyDataSetChanged();

				

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});

		popupWindow = new PopupWindow(contentView,
		// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug

		ColorDrawable dw = new ColorDrawable(Color.argb(120, 0, 0, 0));
		popupWindow.setBackgroundDrawable(dw);

		// 设置好参数之后再show
		// popupWindow.showAsDropDown(view);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}
	
	private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			Log.d("-----", " mediaReceiver		  action = " + action);
			if (action == null)
				return;

			if (Intent.ACTION_MEDIA_EJECT.equals(action)
					|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)){
				if(isShowSdcard==false){
				   iv_sdcard.setVisibility(View.VISIBLE);
                   displaySdcard();
				}				
			    displayUsb();
			}
			else if( Intent.ACTION_MEDIA_MOUNTED.equals(action)) 
			{
				if(isShowSdcard==false){
					iv_sdcard.setVisibility(View.VISIBLE);
					displaySdcard();
				}				
				displayUsb();
			}
		}
	};

	private BroadcastReceiver netReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action == null)
				return;

			// Log.d(TAG, "netReceiver         action = " + action);

			if (action.equals(Intent.ACTION_TIME_CHANGED)) {
				displayDate();
			}else if (action.equals(Intent.ACTION_TIME_TICK)) {
				displayDate();
			
			}else if(action.equals(net_change_action)){
				dispalyEth();
				displayWifi();
			}
			else if (Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE
					.equals(action)
					|| Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE
							.equals(action)) {
				updateAppList(intent);
			} else {

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
					//|| Intent.ACTION_PACKAGE_ADDED.equals(action)
					) {

				updateAppList(intent);
			}
		}
	};

  private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				displayWifi();

			} else if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				
				displayWifi();
			}
		}
	};


	public boolean isExternelStorageExists(String media_name){
        File storage_file = new File(STORAGE_PATH);

        if (storage_file.exists() && storage_file.isDirectory()) {
            if (storage_file.listFiles() != null &&  storage_file.listFiles().length > 0) {
                for (File file : storage_file.listFiles()) {
                    String path = file.getAbsolutePath();
                    if (path.startsWith(STORAGE_PATH + "/" + media_name)
                        && Environment.getExternalStorageState(file).equals(Environment.MEDIA_MOUNTED)
                        ) 
                    {
                    	return true;
                    }
                }
            }
        }

        return false;
    }

	private void dispalyEth(){
		if (isEthernetOn()){
			iv_eth.setBackgroundResource(R.drawable.juyuw_1);
		}
		else{
			iv_eth.setBackgroundResource(R.drawable.juyuw);
		}
	}
	
	private void displayWifi()
	{
		if (isWifiOn()){
			iv_wifi.setBackgroundResource(R.drawable.wifi_1);
		}
		else{
			iv_wifi.setBackgroundResource(R.drawable.wifi);
		}
	}
	
	

	private void displaySdcard(){
        if (isExternelStorageExists(SDCARD_FILE_NAME) == true) {
           iv_sdcard.setBackgroundResource(R.drawable.up_sdcard);
		Utils.send_led_msg("dotcard1");
        }else{
        	iv_sdcard.setBackgroundResource(R.drawable.down_sdcard);
		Utils.send_led_msg("dotcard0");
        }          

	}

    private void displayUsb(){
        if (isExternelStorageExists(UDISK_FILE_NAME) == true) {
		iv_usb.setBackgroundResource(R.drawable.upan_1);
		Utils.send_led_msg("dotusb1");

        }else{
		iv_usb.setBackgroundResource(R.drawable.upan);
		Utils.send_led_msg("dotusb0");
        }          

	}



	private void updateAppList(Intent intent) {

		String data = intent.getDataString();
			
		String packageName = data.substring(8);		
		
		for (int i = 0; i < list_user_app.size(); i++)
		{
			if (list_user_app.get(i).activityInfo.packageName.equals(packageName))
			{
				list_user_app.remove(i);
				
				Utils.delDateInfo(packageName);
			}
		}
		
		user_adapter.notifyDataSetChanged();
	}

	private void displayDate() {

		// is24hFormart = DateFormat.is24HourFormat(this);

		TextView time = (TextView) findViewById(R.id.home_ac_weather_time);
		// TextView date = (TextView)findViewById(R.id.tx_date);
		time.setText(getTime());
	//	time.setTypeface(Typeface.DEFAULT_BOLD);
		// date.setText(getDate());
	}

	public String getTime() {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		boolean is24hFormart = DateFormat.is24HourFormat(this);
		if (!is24hFormart && hour > 12) {
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

    private boolean isWifiOn(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null && mWifi.isConnected()) {
            return true;
        } 

        return false;
    }
    private boolean isEthernetOn(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		seleted_info = (ResolveInfo)arg0.getItemAtPosition(arg2);
		seleted_position = arg2;
		
		if (arg2==6)
		{
			//hs.fling(500);
			//hs.scrollTo(500, 0);
		}
		else if (list_user_app.size()>6 && arg2<=5){
			//hs.fling(-500);
			//hs.scrollTo(-500, 0);
		}
		
		if (list_user_app.size() < 7)
			gridview.setScrollingCacheEnabled(false);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
