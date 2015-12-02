package com.amlogic.mediaboxlauncher;
import android.app.SearchManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.KeyEvent;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class MyAppsActivity extends Activity{
private static final String TAG = "CustomAppsActivity";
	
	private ImageView img_screen_shot = null;
    private ImageView img_screen_shot_keep = null;
    private ImageView img_arrow = null;
    private ImageView img_dim = null;
	private GridView gv = null;
    private Context mContext = null;
    private String[] list_homeShortcut;
	//private  AnimationDrawable anim_selector = null;

	private File mFile;
	private String[] list_custom_apps;
	private String str_custom_apps;
    public static  int CONTENT_HEIGHT;

    public final static String SHORTCUT_PATH = "/data/data/com.amlogic.mediaboxlauncher/shortcut.cfg";
    public final static String DEFAULT_SHORTCUR_PATH = "/system/etc/default_shortcut.cfg";
	public final static String HOME_SHORTCUT_HEAD = "Home_Shortcut:";
	List<Map<String, Object>> appShortCutList = new ArrayList<Map<String, Object>>();
    private int homeShortcutCount;
    private TranslateAnimation exitTransAnim;

    final static Object mLock = new Object[0];
    @Override
    	protected void onCreate(Bundle savedInstanceState) {
    		setContentView(R.layout.layout_new_apps);
    		super.onCreate(savedInstanceState);
    		
		gv = (GridView)findViewById(R.id.grid_add_apps);
		gv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(pos);
                      MyAppsActivity.this.startActivity((Intent)item.get("file_path")) ;
              
          
            }
        });

        mContext = this;
        img_screen_shot = (ImageView)findViewById(R.id.img_screenshot);
        img_screen_shot_keep = (ImageView)findViewById(R.id.img_screenshot_keep);
        img_arrow = (ImageView)findViewById(R.id.img_arrow);
        img_dim = (ImageView)findViewById(R.id.img_dim);
		//img_screen_shot.setVisibility(View.INVISIBLE); 
		//displayView();
	
    }

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "------onResume");

        str_custom_apps = "";
        displayView();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "------onPause");
		
		saveShortcut(SHORTCUT_PATH, str_custom_apps);
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "------onStop");
	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "------onDestroy");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
            
			MyAppsActivity.this.finish();
            
            return true;
	    }else if(keyCode == KeyEvent.KEYCODE_SEARCH){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            ComponentName globalSearchActivity = searchManager.getGlobalSearchActivity();
            if (globalSearchActivity == null) {
                return false;
            }
            Intent intent = new Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH);
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
	 
	private void displayView() {
	    homeShortcutCount = 0;
		LocalAdapter ad = new LocalAdapter(MyAppsActivity.this,
						 loadApplications(),
						 R.layout.add_apps_grid_item, 			 
						 new String[] {"item_type", "item_name", "item_bg"},
						 new int[] {R.id.item_type, R.id.item_name,  R.id.relative_layout});
		gv.setAdapter(ad); 
	}
	
	private void updateView() {
		 ((BaseAdapter) gv.getAdapter()).notifyDataSetChanged();
	}
    private int  parseItemBackground(int num){
        switch (num % 13){
            case 0:
                return R.drawable.item_1;
            case 1:
                return R.drawable.item_2;
            case 2:
                return R.drawable.item_3;
            case 3:
                return R.drawable.item_4;
            case 4:
                return R.drawable.item_5;
            case 5:
                return R.drawable.item_6;
            case 6:
                return R.drawable.item_7;
            case 7:
                return R.drawable.item_8;
            case 8:
                return R.drawable.item_9;
            case 9:
                return R.drawable.item_10;
            case 10:
                return R.drawable.item_11;
            case 11:
                return R.drawable.item_12;
            case 12:
                return R.drawable.item_13;
            default:
                return R.drawable.item_1;
        }
    }
    
	private List<Map<String, Object>> loadApplications() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();	
		Map<String, Object> map = new HashMap<String, Object>();

		PackageManager manager = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		str_custom_apps = loadCustomApps(SHORTCUT_PATH, LauncherActivity.current_shortcutHead);

		/*map = new HashMap<String, Object>(); 
		map.put("item_name",getString(R.string.str_exit));   
		map.put("file_path", null); 	
		map.put("item_type", R.drawable.item_img_exit);
		map.put("item_sel", R.drawable.item_img_unsel);	
		list.add(map);*/

        //delete the packages are not exist
        list_custom_apps = str_custom_apps.split(";");
        if(list_custom_apps != null){
            for (int i=0; i < list_custom_apps.length; i++){
                
                final int count = apps.size();
                for (int j = 0; j < count; j++) {
                    ResolveInfo info = apps.get(j);

                    if (info.activityInfo.applicationInfo.packageName.equals(list_custom_apps[i])){
                        break;
                    }
                    if (j == count -1){
                        str_custom_apps = str_custom_apps.replaceAll(list_custom_apps[i] + ";", "");
                    }           
                }         
            }
        }
        list_custom_apps = str_custom_apps.split(";");
        if (LauncherActivity.current_shortcutHead.equals(HOME_SHORTCUT_HEAD))
            homeShortcutCount = list_custom_apps.length;


        if (apps != null) {
            final int count = apps.size();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
		
                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = info.activityInfo.loadIcon(manager);
				
				map = new HashMap<String, Object>(); 
				map.put("item_name", application.title.toString());   
				map.put("file_path", application.intent); 
                if (LauncherActivity.parseItemIcon(application.componentName.getPackageName()) != -1){
				    map.put("item_type", LauncherActivity.parseItemIcon(application.componentName.getPackageName()));
                } else {
                    map.put("item_type", application.icon);
                }
			 	map.put("item_sel", R.drawable.item_img_unsel);	
                map.put("item_bg", parseItemBackground(i));
				map.put("item_symbol", application.componentName);

				if(list_custom_apps != null){
					for (int j=0; j < list_custom_apps.length; j++){
					
						if (application.componentName.getPackageName().equals(list_custom_apps[j])){
							 map.put("item_sel", R.drawable.item_img_sel);
							 break;
						} 
					}
				}
				list.add(map);
				// Log.i(TAG, ""+application.title.toString());  
			}
                
              

            
        }

		return list;
    }

    private void setViewPosition(int top, int bottom, int left, int right){
        TranslateAnimation translateAnimation;
        int arrow_x_center = left + (right - left) / 2;
        img_screen_shot.setImageBitmap(LauncherActivity.screenShot);

        if (bottom > LauncherActivity.SCREEN_HEIGHT/2){ 
            android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0); 
            lp.y = 0;            
            img_screen_shot.setLayoutParams(lp);
            translateAnimation = new TranslateAnimation(0.0f, 0.0f,0.0f, (float)(0 - CONTENT_HEIGHT));
            translateAnimation.setDuration(500);
            exitTransAnim = new TranslateAnimation(0.0f, 0.0f,(float)(0 - CONTENT_HEIGHT), 0.0f);
            exitTransAnim.setDuration(300);
            
            android.widget.AbsoluteLayout.LayoutParams lp1 = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
            lp1.height = CONTENT_HEIGHT;
            if (top - CONTENT_HEIGHT > 0){
                lp1.y = top - CONTENT_HEIGHT;
            } else {
                lp1.y = 0;
            }
            gv.setLayoutParams(lp1); 
            translateAnimation.setAnimationListener(new MyAnimationListener(lp1.y, arrow_x_center, 0));          
        } else {
            android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
            lp.y = bottom;
            img_screen_shot.setLayoutParams(lp);
            translateAnimation = new TranslateAnimation(0.0f, 0.0f,0.0f, (float)(CONTENT_HEIGHT));
            translateAnimation.setDuration(500);
            exitTransAnim = new TranslateAnimation(0.0f, 0.0f,(float)(CONTENT_HEIGHT), 0.0f);
            exitTransAnim.setDuration(300);
            
            android.widget.AbsoluteLayout.LayoutParams lp1 = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
            lp1.y = bottom;  
            lp1.height = CONTENT_HEIGHT;
            gv.setLayoutParams(lp1);
            translateAnimation.setAnimationListener(new MyAnimationListener(lp1.y, arrow_x_center, 1));
        }

        exitTransAnim.setAnimationListener(new exitAnimationListener());
        img_screen_shot.startAnimation(translateAnimation);       
    }

   private void setArrowPosition(int top, int x_center, int flag){
        android.widget.AbsoluteLayout.LayoutParams lp2 = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0); 
        lp2.x = x_center - 13;
        if (flag == 0){
            lp2.y = top + CONTENT_HEIGHT -4;

        } else {
            lp2.y = top - 15 + 4;
          
        }
        img_arrow.setLayoutParams(lp2);
   }

    private String loadCustomApps(String path, String shortcut_head){
		String[] list_custom_apps;		
		mFile = new File(path);
        
		if(!mFile.exists()) {
		    return null;
		}
		
        BufferedReader br = null;
		try {
            br = new BufferedReader(new FileReader(mFile));
            String str = null;
            while( (str=br.readLine()) != null ){
                if (str.startsWith(shortcut_head)){                  
                    //Log.d(TAG, "@@@@@@@@@@@@@@@@@@ get CustomApps" + str);
                    break;
                } 
            }
            str_custom_apps = str.replaceAll(shortcut_head, "");
			//list_custom_apps = str_custom_apps.split(";");
            //homeShortcutCount = list_custom_apps.length;
		}
		catch (Exception e) {
			return null;
		} finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
            }
        }
		return str_custom_apps;

	}


    public void saveShortcut(String path, String str_apps){     
        mFile = new File(path);
        if(!mFile.exists()) {
			try {
				mFile.createNewFile();

			} catch (Exception e) {
				Log.e(TAG, e.getMessage().toString());
			}
		}
        
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(mFile));
            String str = null;
            List list = new ArrayList();
            
            while( (str=br.readLine()) != null ){
                list.add(str);
            }

            if (list.size() == 0){
                list.add(HOME_SHORTCUT_HEAD);
             
            }
            //Log.d(TAG, "@@@@@@@@@@@@@ size" + list.size());
                 
            bw = new BufferedWriter(new FileWriter(mFile));
            for( int i = 0;i < list.size(); i++ ){
                 if (list.get(i).toString().startsWith(LauncherActivity.current_shortcutHead)){
                    str_apps = LauncherActivity.current_shortcutHead + str_apps;
                    bw.write(str_apps);
                    //Log.d(TAG, "@@@@@@@@@@@@@@@@@@ wirte " + str_apps);
                 } else {          
                    bw.write(list.get(i).toString());
                    //Log.d(TAG, "@@@@@@@@@@@@@@@@@@ wirte " + list.get(i).toString());
                 }
                 bw.newLine();
            }
            bw.flush();
            bw.close();
        }
		catch (Exception e) {
			Log.d(TAG, "   " + e);
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

    public void getShortcutFromDefault(String srcPath, String desPath){     
        File srcFile = new File(srcPath);
        File desFile = new File(desPath);
        if(!srcFile.exists()) {
		    return;
		}
		
        BufferedReader br = null;
        BufferedWriter bw = null;
		try {
            br = new BufferedReader(new FileReader(srcFile));
            String str = null;
            List list = new ArrayList();
            
            while( (str=br.readLine()) != null ){
                list.add(str);
            }
            bw = new BufferedWriter(new FileWriter(mFile));
            for( int i = 0;i < list.size(); i++ ){ 
                 bw.write(list.get(i).toString());
                 bw.newLine();
            }
            bw.flush();
            bw.close();
        }
		catch (Exception e) {
			Log.d(TAG, "   " + e);
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
      
    private class MyAnimationListener implements AnimationListener { 
       private int mTop;
       private int up_or_down;
       private int arrow_x_center;
       //private Animation mAnim;
                
        public MyAnimationListener(int top, int x_center,int flag) { 
           mTop = top;
           up_or_down = flag;
           arrow_x_center = x_center;
        } 
     
        @Override 
        public void onAnimationStart(Animation animation) {     
            //setArrowPosition(mTop, arrow_x_center, up_or_down);
        } 
     
        @Override 
        public void onAnimationEnd(Animation animation) { 
          android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
          img_screen_shot_keep.setImageBitmap(LauncherActivity.screenShot_keep);
            if (up_or_down == 0){
                lp.y = 0;
            } else {
                lp.y = mTop + CONTENT_HEIGHT;
            }
            img_screen_shot_keep.setLayoutParams(lp);
       //  mAnim.reset();
         img_screen_shot.setVisibility(View.INVISIBLE); 
         Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_show);
         anim.setAnimationListener(new dimAnimationListener());
         img_dim.startAnimation(anim);
         //img_dim.setVisibility(View.VISIBLE);
         gv.bringToFront();
        } 
     
        @Override 
        public void onAnimationRepeat(Animation animation) { 
     
        } 
     
    } 

    private class dimAnimationListener implements AnimationListener {        
        @Override 
        public void onAnimationStart(Animation animation) {     
        } 
     
        @Override 
        public void onAnimationEnd(Animation animation) { 
            img_dim.setVisibility(View.VISIBLE);
        } 
     
        @Override 
        public void onAnimationRepeat(Animation animation) { 
        }   
    } 

   private class exitAnimationListener implements AnimationListener {        
        @Override 
        public void onAnimationStart(Animation animation) {     
        } 
     
        @Override 
        public void onAnimationEnd(Animation animation) { 
            img_screen_shot.setVisibility(View.VISIBLE);
            finish();
        } 
     
        @Override 
        public void onAnimationRepeat(Animation animation) { 
        }   
    } 
}
