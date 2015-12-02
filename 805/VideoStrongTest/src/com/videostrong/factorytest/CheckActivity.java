package com.videostrong.factorytest;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.os.Handler;
import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;

public class CheckActivity extends Activity {  
	private static final String TAG = "Factory.CheckActivity";

    final static int CHECK_OK = 1000;
    final static int CHECK_FAIL = 1001;
	
	private TextView mScanTextView;
	private TextView mDataTextView;	
	//private LinearLayout mResetLinearLayout;
	private Button mResetButton;
	private int mScanNum = 0;
	private String mScan ="";

	private String mCurrentSNStr="";
	private String mCurrentMACStr="";	
	private boolean mbScanEnterFlag = false; //for scan mac or  sn trigger button be pressed
	
	@Override  
	public void onCreate(Bundle savedInstanceState) { 
		Log.d(TAG, "onCreate() "); 	
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.check_layout); 

		Intent intent =this.getIntent();
		if (intent != null)
		{
			mCurrentSNStr = intent.getStringExtra("sn");
			String temp = intent.getStringExtra("mac");
			for (int i=0; i<temp.length(); i++)
			{
				if (temp.charAt(i) != ':')
				{
					mCurrentMACStr += temp.charAt(i);
				}
			}
		}
		
		mScanTextView = (TextView) this.findViewById(R.id.scan_textview);
		mDataTextView = (TextView) this.findViewById(R.id.data_textview);
		//mResetLinearLayout = (LinearLayout) this.findViewById(R.id.reset_linearlayout);
		mResetButton = (Button) this.findViewById(R.id.reset_button);	
		mResetButton.setOnClickListener(ResetBtnClickListener);		
	} 

	private Handler mClearScanHandler = new Handler();
	private Runnable mClearScanRunnable = new Runnable() 					
	{						
		public void run() 						
		{
			//Log.i(TAG, "---------onKeyDown-------mClearScanRunnable------------");
			mScan = "";	
			mScanNum = 0;	
			mbScanEnterFlag	= false;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.i(TAG, "----------------onKeyDown------------keyCode= "+keyCode);
		String scanStr = ObbMountActivity.processKey(keyCode);
		if(scanStr.length() > 0)
		{
			mScan = mScan+scanStr;
			mScanNum++;
			mbScanEnterFlag = true;
			
			//Log.i(TAG, "----------------onKeyDown------------mScanNum= "+mScanNum);					
			if (mScanNum >= 8) //hw length is 8, mac length is 12, sn length is 17
			{
				//Log.i(TAG, "----------------onKeyDown------------mScan= "+mScan);
				if (mScan.charAt(0) == 'D' && mScan.charAt(1) == 'M')//SN: DMA30104140740027
				{
					if (mScanNum == 17)
					{
						Log.d(TAG, "mCurrentSNStr " + mCurrentSNStr + " mScan " + mScan);
						mDataTextView.setText("Read: "+mCurrentSNStr+"  Scan: " + mScan);
						if (mCurrentSNStr != null && mCurrentSNStr.equals(mScan))
						{
							mHandler.sendEmptyMessageDelayed(CHECK_OK, 100);
						}
						else
						{
							mHandler.sendEmptyMessageDelayed(CHECK_FAIL, 100);	
						}
							
						mScan = ""; 
						mScanNum = 0;
					}
					else if (mScanNum > 17) //error count
					{
						mScan = ""; 
						mScanNum = 0;				
					}				
				}
				else if (mScan.charAt(0) == '1' && mScan.charAt(1) == '4'
						&& mScan.charAt(2) == '3' && mScan.charAt(3) == 'D')
				{
					if(mScanNum == 12) //mac
					{
						Log.d(TAG, "mCurrentMACStr " + mCurrentMACStr + " mScan " + mScan);
						mDataTextView.setText("Read: "+mCurrentMACStr+"  Scan: " + mScan);
						if (mCurrentMACStr != null && mCurrentMACStr.equals(mScan))
						{
							mHandler.sendEmptyMessageDelayed(CHECK_OK, 100);
						}
						else
						{
							mHandler.sendEmptyMessageDelayed(CHECK_FAIL, 100);	
						}
						
						mScan = "";	
						mScanNum = 0;
					}
				}
				/*else if (mScan.charAt(0) == '1' && mScan.charAt(1) == '4'
						&& mScan.charAt(2) == '2' && mScan.charAt(3) == '3')				
				{
					if(mScanNum == 8) //hw
					{
						mScan = "";	
						mScanNum = 0;
					}
				}*/				
				else //error first char
				{
					//mScan = ""; 
					//mScanNum = 0;	
					mClearScanHandler.removeCallbacks(mClearScanRunnable);
					mClearScanHandler.postDelayed(mClearScanRunnable, 50);
				}	
			}

			mClearScanHandler.removeCallbacks(mClearScanRunnable);
			mClearScanHandler.postDelayed(mClearScanRunnable, 500);

		}
		
		if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_BACK)
		{
			mScan = "";	
			mScanNum = 0;
			mbScanEnterFlag = false;
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
					|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
					|| keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN 
					|| keyCode == KeyEvent.KEYCODE_MENU)
		{
			mScan = "";	
			mScanNum = 0;	
			mbScanEnterFlag = false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override  
	protected void onDestroy() {  
		Log.d(TAG, "onDestroy()");
		mHandler.removeMessages(CHECK_OK);
		mHandler.removeMessages(CHECK_FAIL);
		mClearScanHandler.removeCallbacks(mClearScanRunnable);
		super.onDestroy(); 
	}

	@Override  
	protected void onPause() {  
		Log.d(TAG, "onPause()");
		mHandler.removeMessages(CHECK_OK);
		mHandler.removeMessages(CHECK_FAIL);
		super.onPause(); 
	}

	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.d(TAG, "handleMessage() " + msg.what);			
			switch (msg.what)
			{
				case CHECK_OK:
					mScanTextView.setText(getString(R.string.str_normal));
					mScanTextView.setBackgroundResource(R.color.green);
					
					mResetButton.setVisibility(View.VISIBLE);	
					mResetButton.requestFocus();					
					break;
					
				case CHECK_FAIL:
					mScanTextView.setText(getString(R.string.str_unnormal));
					mScanTextView.setBackgroundResource(R.color.red);

					mResetButton.setVisibility(View.INVISIBLE);
					break;
					
			}
			super.handleMessage(msg);
		};
	};

	OnClickListener ResetBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}
			
			sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
		}
	};	
}
