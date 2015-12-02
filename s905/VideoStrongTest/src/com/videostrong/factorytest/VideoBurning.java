/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.videostrong.factorytest;
import android.widget.Toast;
import android.app.Activity;  
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;  
import android.net.Uri;  
import android.os.Bundle;  
import android.os.Environment;  
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.webkit.WebView.FindListener;
import android.widget.Button;  
import android.widget.MediaController;  
import android.widget.TextView;  
import android.widget.VideoView;  
import android.os.Handler;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.os.SystemProperties;

import com.videostrong.commonutilits.CommonUtilits;

public class VideoBurning extends Activity {  
	private static final String TAG = "Factory.VideoBurning";

	private static final String LED_TEST_NEW = "/sys/devices/aml_pm/led_test";//s905
	private static final String LED_TEST_OLD = "/sys/devices/platform/aml_pm_m8/led_test";//s812&s805
	private static String LED_TEST;
	public final static int IR_LED_CLEAR = 100;	
	public final static int POWER_LED_TEST_START = 101;
	
	VideoView mVideoState;  
	String mUri;  
	private int mPowerLedValue = 0;	
	private TextView tx_timer;
	private Timer timer,counter,led_control;
	private long time=0;
	private static final int UpdateTime=1;
	private String current="1";
	private long count=0;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UpdateTime:
				  tx_timer.setText(showTimeCount(time));
				break;

			default:
				break;
			}
			
		};
	};
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
		if (SystemProperties.get("ro.vshwcheck", "false").equals("false")) {
			return ;
		}

		if (SystemProperties.get("ro.board.platform", "").equals("meson8")) {
			LED_TEST = LED_TEST_OLD;
		}
		else if (SystemProperties.get("ro.board.platform", "").equals("meson8b")) {
			LED_TEST = LED_TEST_OLD;
		}
		else{
			LED_TEST = LED_TEST_NEW;
		}

		super.onCreate(savedInstanceState);  
		setContentView(R.layout.videoview); 		
		time=Long.valueOf(SystemProperties.get("persist.syu.time","0"));
		tx_timer=(TextView) findViewById(R.id.timer);
		tx_timer.setText(showTimeCount(time));
		led_control=new Timer();
		timer=new Timer();
		counter=new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				time=time+1000;					
				handler.sendEmptyMessage(UpdateTime);				
			}
		}, 0,1000);
		
		counter.schedule(new TimerTask() {
			
			@Override
			public void run() {
				SystemProperties.set("persist.syu.time",String.valueOf(time));
			}
		}, 0, 5000);

		led_control.schedule(new TimerTask() {
			@Override
			public void run() {
				if (current.equals("1")) {
				 CommonUtilits.writeLed("/sys/devices/aml_pm/led_test","0");				
				
				 current="0";
				}else if(current.equals("0")){
					  CommonUtilits.writeLed(LED_TEST,"1");
                      current="1";
				}			   
				}
		}, 0,500);
	} 

	public  void playVideoView()
	{
		mUri = "android.resource://" + getPackageName() + "/" + R.raw.testvideo;
		mVideoState.setVideoURI(Uri.parse(mUri)); 
		
		mVideoState.start(); 
		CommonUtilits.writeFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor","performance");

	        mVideoState.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  
	            @Override  
	            public void onPrepared(MediaPlayer mp) {  
	                mp.start();  
	                mp.setLooping(true);  
	            }  
	       });  

		mVideoState.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { 
			@Override  
			public void onCompletion(MediaPlayer mp) { 
				mVideoState.setVideoURI(Uri.parse(mUri)); 
				mVideoState.start();
			}  
		}); 
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode)
		{
			
			CommonUtilits.writeFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor","hotplug");
			if(mVideoState.isPlaying()){
                mVideoState.stopPlayback();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
	@Override  
	protected void onStop() {  
		Log.d(TAG, "onStop()" );
		//mHandler.removeMessages(IR_LED_CLEAR);				
		//mHandler.removeMessages(POWER_LED_TEST_START);			
		if(mVideoState!=null)
		mVideoState.stopPlayback();
		mVideoState = null;
		
		CommonUtilits.writeFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor","hotplug");
		timer.cancel();
		counter.cancel();
		led_control.cancel();
		CommonUtilits.writeLed("/sys/devices/aml_pm/led_test","1");		
	
		super.onStop();
	}
    
	@Override  
	protected void onResume() {  
		Log.d(TAG, "onResume()" + mVideoState); 
		if( mVideoState != null)
		{ 
			mUri = "android.resource://" + getPackageName() + "/" + R.raw.testvideo;
			mVideoState.setVideoURI(Uri.parse(mUri)); 
			mVideoState.start(); 
		}
		else
		{
			mVideoState = (VideoView) findViewById(R.id.vidoView);	
			playVideoView();
		}
		
		super.onResume();  
	}

	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//Log.d(TAG, "handleMessage() "+msg.what);
			switch (msg.what)
			{
				case IR_LED_CLEAR:
					//CommonUtilits.writeFile("/sys/class/led_control/led_test","5");//ir led off		
					//CommonUtilits.writeFile("/proc/net_state","off");
					mHandler.removeMessages(IR_LED_CLEAR);				
					mHandler.removeMessages(POWER_LED_TEST_START);
					mHandler.sendEmptyMessageDelayed(POWER_LED_TEST_START, 1000);
					break;
					
				case POWER_LED_TEST_START:
					PowerLedTest();
					mHandler.removeMessages(POWER_LED_TEST_START);		
					mHandler.sendEmptyMessageDelayed(POWER_LED_TEST_START, 1000); 
					break;	
			}
			super.handleMessage(msg);
		};
	};

	void PowerLedTest()
	{
		if (0 == mPowerLedValue || 2 == mPowerLedValue)
		{
			//CommonUtilits.writeFile("/sys/class/led_control/led_test","1");//pwoer led green
			CommonUtilits.writeFile("/proc/net_state","on");
			mPowerLedValue = 1;
		}
		else if (1 == mPowerLedValue)
		{
			//CommonUtilits.writeFile("/sys/class/led_control/led_test","2");//power led red
			CommonUtilits.writeFile("/proc/net_state","off");
			mPowerLedValue = 2;
		}
	}
	
 /* public String showTimeCount(long time) {
        System.out.println("time="+time);
        if(time >= 360000000){
            return "00:00:00";
        }
        String timeCount = "";
        long hourc = time/3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length()-2, hour.length());

        long minuec = (time-hourc*3600000)/(60000);
        String minue = "0" + minuec;
        System.out.println("minue="+minue);
        minue = minue.substring(minue.length()-2, minue.length());
        long secc = (time-hourc*3600000-minuec*60000)/1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length()-2, sec.length());
        System.out.println("sec2="+sec);
        timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }
	
 */

	public String showTimeCount(long time) {
        System.out.println("time="+time);
        
        String timeCount = "";
        long hourc = time/3600000;
        String hour;
        if(hourc<100){
            hour= "0" + hourc;
            hour = hour.substring(hour.length()-2, hour.length());
        }
        else{
             hour=Long.toString(hourc);
        }        

        long minuec = (time-hourc*3600000)/(60000);
        String minue = "0" + minuec;
        System.out.println("minue="+minue);
        minue = minue.substring(minue.length()-2, minue.length());
        long secc = (time-hourc*3600000-minuec*60000)/1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length()-2, sec.length());
        System.out.println("sec2="+sec);
        timeCount = hour + ":" + minue + ":" + sec;
        return timeCount;
    }
	

}


