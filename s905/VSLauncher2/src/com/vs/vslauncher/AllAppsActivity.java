package com.vs.vslauncher;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.util.Log;
public class AllAppsActivity extends Activity implements OnItemClickListener,
		OnItemSelectedListener {

	private GridView gridview;
	private UserAllAppsAdapter adapter;
	List<ResolveInfo> list = new ArrayList<ResolveInfo>();
	private ResolveInfo seletedItem ;
	private int seletedId = -1;
	private final static String TAG="VSLauncher2.AllAppsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.all_app_grid);

		gridview = (GridView) findViewById(R.id.apps_gview);

		adapter = new UserAllAppsAdapter(this);

		list = Utils.loadAllApps(this);//getResolveinfoList();
		if (list.size() == 0) {

		} else {
			adapter.setListItems(list);
			seletedItem = list.get(0);
		}

		//String st = list.get(0).activityInfo.name;
		//String stt = list.get(0).activityInfo.packageName;

		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		gridview.setOnItemSelectedListener(this);
		gridview.setSelection(0);	
		adapter.notifyDataSetChanged();

		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(appReceiver, filter);
		
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(appReceiver);
			super.onDestroy();
	}

	@Override
	protected void onResume() {
		Utils.send_led_msg("dotapps1");
		super.onResume();
	}

	private void cleanUserData(String packageName){
		ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        boolean res = am.clearApplicationUserData(packageName, null);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU
				//|| keyCode == KeyEvent.KEYCODE_BACK
				) {
			// do something
			showPopupWindow(this.getCurrentFocus());			
			return false;
		} 
		return super.onKeyDown(keyCode, event);
	}
	
	private PopupWindow popupWindow;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getBaseContext()).inflate(
                R.layout.pop_window, null);
        
        final String packageName = seletedItem.loadLabel(getPackageManager()).toString();
        final Drawable icon = seletedItem.loadIcon(getPackageManager());
        
        TextView name = (TextView)contentView.findViewById(R.id.file_name);
        final TextView menu_title = (TextView)contentView.findViewById(R.id.textView1);
        ImageView image = (ImageView)contentView.findViewById(R.id.ImageView01);
        
        name.setText(packageName);
        image.setBackgroundDrawable(icon);
        menu_title.setText(getString(R.string.item_menu_uninstall_selected));
        
        // 设置按钮的点击事件
        Button button_uninstall = (Button) contentView.findViewById(R.id.button1);
        button_uninstall.setFocusable(true);
        button_uninstall.setFocusableInTouchMode(true);
        button_uninstall.requestFocus();
        
        button_uninstall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow != null){
					popupWindow.dismiss();
				}
				
				//uninstall apk
				String packageName2 = seletedItem.activityInfo.packageName;
				uninstall(packageName2);
				
				//refresh list
				//list.remove(seletedItem);
				//adapter.notifyDataSetChanged();
			}
        });
        
        button_uninstall.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus){
					menu_title.setText(getString(R.string.item_menu_uninstall_selected));
				}
			}});

        // 设置按钮的点击事件
        Button button_clean = (Button) contentView.findViewById(R.id.button2);
        button_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//PackageManager mPm = getPackageManager();
	            //mPm.deleteApplicationCacheFiles(packageName, null);
				//DataCleanManager.cleanApplicationData(getBaseContext());
				String packageName = seletedItem.activityInfo.packageName;
				cleanUserData(packageName);
				
				if (popupWindow != null){
					popupWindow.dismiss();
				}
			}

        });
        
        button_clean.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus){
					menu_title.setText(getString(R.string.item_menu_type_selected));
				}
			}});
                
        popupWindow = new PopupWindow(contentView,
                //LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
        		LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,
                true);

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
        //popupWindow.showAsDropDown(view);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

	private BroadcastReceiver appReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			final String action = intent.getAction();
			if (Intent.ACTION_PACKAGE_ADDED.equals(action))
			{
				updateList();
			}			
			else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) 
			{
				String ss = intent.getDataString();
				String dd = seletedItem.activityInfo.packageName;
				
				if (ss.contains(dd)){
					//refresh list
					list.remove(seletedId);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};
	
	private void updateList()
	{
		list = Utils.loadAllApps(this);
		adapter.setListItems(list);
		adapter.notifyDataSetChanged();
	}
	
    private void uninstall(String packageName)
    {		
		Intent intent = new Intent();
	    intent.setAction(Intent.ACTION_DELETE);
	    intent.setData(Uri.parse("package:"+packageName));
		startActivity(intent);
	}
	
	private List<ResolveInfo> getResolveinfoList() {
		return Utils.loadApps(this);
	}

	private void openUserActivity(String packageName, String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		
		startActivity(intent);

		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ResolveInfo info = (ResolveInfo) parent.getItemAtPosition(position);

		String a_name = info.activityInfo.name;
		String p_name = info.activityInfo.packageName;
		
		openUserActivity(p_name, a_name);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		seletedItem = (ResolveInfo) parent.getItemAtPosition(position);
		seletedId = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
