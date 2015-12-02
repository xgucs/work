package com.vs.vslauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import java.io.OutputStream;
import android.os.SystemProperties;

public class Utils {

	private static List<ResolveInfo> Utils_list = new ArrayList<ResolveInfo>();
	private static List<ResolveInfo> Utils_user_list = new ArrayList<ResolveInfo>();
	private static String[] package_data = { "cn.com.etronics.ipfox.vs",
	                       "com.private_live_tv.android"
			 };
	private final static String TAG="VSLauncher2.Utils";
	public final static String DEFAULT_SHORTCUR_PATH = "/system/etc/default_shortcut.cfg";
	public final static String HOME_SHORTCUT_HEAD = "Home_Shortcut:";
	/*
	 * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过 appInfo.publicSourceDir =
	 * apkPath;来修正这个问题，详情参见:
	 * http://code.google.com/p/android/issues/detail?id=9151
	 */
	public static Drawable getApkIcon(Context context, String apkPath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadIcon(pm);
			} catch (OutOfMemoryError e) {
				Log.e(TAG, e.toString());
			}
		}
		return null;
	}

	private static void loadCustomApps(String path) {
		File mFile = new File(path);
		if (!mFile.exists()) {
			return ;
		} else {
			try {
				BufferedReader b = new BufferedReader(new FileReader(mFile));
				if (b.read() == -1) {
					return ;
				}
				if (b != null)
				{
					b.close();
				}
			} catch (IOException e) {
			}
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(mFile));
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.startsWith(Utils.HOME_SHORTCUT_HEAD)) {
					str = str.replaceAll(Utils.HOME_SHORTCUT_HEAD,
							"");
					package_data = str.split(";");
				}
			}

		} catch (Exception e) {
			Log.d(TAG, "" + e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}
		return ;
	}
	private static boolean getSharePreValue(Context context){

		SharedPreferences sh = context.getSharedPreferences("my_db", Activity.MODE_PRIVATE);
		boolean  used = sh.getBoolean("db_used", false);

		return used;
	}

	private static void setSharePreValue(Context context){

		SharedPreferences sh = context.getSharedPreferences("my_db", Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = sh.edit();
		if (ed != null){
			ed.putBoolean("db_used", true);
			ed.commit();
		}
	}

	public static List<ResolveInfo> loadUserApps(Context context) {
		if (Utils_user_list.size() == 0) {
			initDataBase(context);

			Utils_list = loadAllApps(context);
			Utils_user_list = getDateInfo();

			if (Utils_user_list.size() == 0 && !getSharePreValue(context)) {
				setSharePreValue(context);
				Utils_user_list = getDafaultDateInfo();
			}

			// Utils_list = loadApps(context);
			// for (int i = 0; i < Utils_list.size(); i++)
			// {
			// Utils_user_list.add(Utils_list.get(i));
			// }

			ResolveInfo info = new ResolveInfo();
			info.activityInfo = new ActivityInfo();
			info.activityInfo.icon = R.drawable.user_app_add;
			info.activityInfo.labelRes = R.string.add_app_title;
			info.activityInfo.packageName = context
					.getString(R.string.add_app_title);

			Utils_user_list.add(info);

		}

		return Utils_user_list;
	}

	private static List<ResolveInfo> getDafaultDateInfo() {
		loadCustomApps(DEFAULT_SHORTCUR_PATH);
		List<ResolveInfo> list = new ArrayList<ResolveInfo>();
		for (int j = 0; j < package_data.length; j++) {
			for (int i = 0; i < Utils_list.size(); i++) {
				if (Utils_list.get(i).activityInfo.packageName
						.equals(package_data[j])) 
				{
					list.add(Utils_list.get(i));
					
					insetDateInfo(0, "", package_data[j], "");
					break;
				}
			}
		}

		return list;
	}

	public static List<ResolveInfo> loadApps(Context context) {
		if (Utils_list.size() == 0) {
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			Utils_list = context.getPackageManager().queryIntentActivities(
					mainIntent, 0);
		}

		return Utils_list;
	}
	

	public static List<ResolveInfo> loadAllApps(Context context) {
		//if (Utils_list.size() == 0) 
		{
			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			return context.getPackageManager().queryIntentActivities(
					mainIntent, 0);
		}
	}
	
	

	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	private static dataBase mDateBase;

	public static void initDataBase(Context context) {
		mDateBase = new dataBase(context);
	}


	public static void insetDateInfo(int source_id, String activityname,
			String packagename, String name) {

		mDateBase.insert(source_id, activityname, packagename, name);
	}

	
	public static void delDateInfo(String packagename) {

		mDateBase.delete(packagename);
	}

	public static void updateDateInfo(int source_id, String activityname,
			String packagename, String name) {

		mDateBase.update(source_id, activityname, packagename, name);

	}

	public static List<ResolveInfo> getDateInfo() {

		List<ResolveInfo> list = new ArrayList<ResolveInfo>();

		Cursor result = mDateBase.select();

		result.moveToFirst();
		while (!result.isAfterLast()) {

			String packagename = result.getString(0);
			Log.d("----", "-----packeagename="+packagename);
			
			for (int i = 0; i < Utils_list.size(); i++) {
				if (Utils_list.get(i).activityInfo.packageName
						.equals(packagename)) {
					list.add(Utils_list.get(i));
					break;
				}
			}

			// do something useful with these
			result.moveToNext();
		}
		result.close();

		return list;
	}

	public static void send_led_msg(String msg)
	{
		boolean mVfdDisplay = SystemProperties.getBoolean("hw.vfd", false);
		if(mVfdDisplay)
		{
			Log.d(TAG ,"send_led_msg() = " + msg);
			// if we need to show the channel number, just send its number to socket "led".
			String SOCKET_NAME = "led";
			LocalSocket so =  null;
			LocalSocketAddress addr;
			so = new LocalSocket();
			addr = new LocalSocketAddress(SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
			try {
				so.connect(addr);
				so.setSendBufferSize(msg.length());
				OutputStream out = so.getOutputStream();
				out.write(msg.getBytes());
				out.close();
				so.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
