package com.videostrong.factorytest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.conn.util.InetAddressUtils;
import com.videostrong.commonutilits.CommonUtilits;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.ethernet.EthernetManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Environment;
import android.os.StatFs;
import android.net.NetworkInfo.State; 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.TableRow;
import android.widget.Toast;
import android.content.ComponentName;
import android.os.SystemProperties;
import android.text.format.Formatter;;
import android.os.StatFs;
import android.hardware.input.IInputManager;
import android.os.SystemClock;
import android.os.RemoteException;
import android.os.ServiceManager;

import java.io.OutputStream;
import java.net.Socket;  
import java.net.InetSocketAddress;
import java.net.UnknownHostException; 

import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.ethernet.EthernetManager;
import android.net.ethernet.EthernetDevInfo;
import android.net.ethernet.EthernetStateTracker;
import android.net.LinkProperties;
import android.net.DhcpInfo;
import java.net.ServerSocket;
import java.util.Iterator;
import java.lang.Integer;
import android.os.SystemProperties;

/**
 * @author Jesse
 *
 */
public class ObbMountActivity extends Activity {
	private static final String TAG = "Factory.ObbMountActivity";
    public final static int SD_MESSAGE_Ok = 0;
    public final static int SD_MESSAGE_ERROR = 1;
    public final static int USB1_MESSAGE_Ok = 2;
    public final static int USB1_MESSAGE_ERROR = 3;
    public final static int WIFI_MESSAGE_Ok = 4;
    public final static int WIFI_MESSAGE_ERROR = 5;
    public final static int LAN_MESSAGE_Ok = 6;
    public final static int LAN_MESSAGE_ERROR = 7;
    public final static int LAN_PACKAGE_LOSS = 8;	
	
    public final static int IR_LED_FLASH = 10;
    public final static int IR_LED_CLEAR = 11;	
    public final static int VERSION_CHECK = 12;
    public final static int VERSION_MESSAGE_Ok = 13;
    public final static int VERSION_MESSAGE_ERROR = 14;	
    public final static int STORAGE_MESSAGE_ERROR = 15;	
		

    public final static int EFUSE_INFO_SHOW = 16;
    public final static int RESET_BUTTON_SHOW = 17;
	public final static int BURNING_TEST_MESSAGE = 18; 	
    public final static int WIFI_MESSAGE_PROCESS = 19;	

	public final static int SN_SET = 30;
	public final static int SN_GET_FAILED = 31;
	public final static int MAC_SET = 32;
	public final static int MAC_GET_FAILED = 33;
	//public final static int MODEL_SET = 34;
	//public final static int HW_SET = 35;
	public final static int CONTROL_CHAR_ERR = 36;
	public final static int SOCKET_ERR = 37;
	public final static int HW_GET_FAILED = 38;
	public final static int SOCKET_THREAD_RESTART = 39;	

	public final static int CHECK_SUCESS = 40;
	public final static int CHECK_FAILED = 41;
	public final static int MAC_MATCHED = 42;
	public final static int MAC_NOT_MATCHED = 43;

	//SN
	public final static int MESSAGE_SCAN_STB_ID_OVER = 1024;
	public final static int MESSAGE_SCAN_MAC_OVER = 1025;
	public final static int MESSAGE_SN_GET_FAILED = 1030;
	public final static int MESSAGE_MAC_GET_FAILED = 1031;
	public final static int MESSAGE_CONTROL_CHAR_ERR = 1032;
	public final static int MESSAGE_SOCKET_ERR = 1033;

	public static final int STB_ID_LENGHT_NORMAL = 14;
	public static final String STB_ID_FRIST_TWO_BYTE = "00";
	public static final String SN_FRIST_TWO_BYTE_CHT = "30";
	
	private final static String MAC_NODE = "mac";
	private final static String MODEL_NODE = "model";
	private final static String HW_NODE = "hw";
	private final static String SN_NODE = "usid";

	private static final String HOST = "192.168.1.3";    
	private static final int PORT = 5050;  
	private String mRecieveSn ="";
	private String mRecieveMac ="";
	private String mRecieveHw ="";	
	private boolean mWriteModelSuccess =false;	
	private boolean mWriteSnSuccess =false;
	private boolean mWriteMacSuccess =false;
	private boolean mWriteHwSuccess =false;	
	private boolean mMacUpdate =false;	
	private int mSocketErrResId = 0;
	private String mSocketIpStr = HOST;
	private int mSocketPortId = PORT;

	private TableRow mUsbTableRow;	
	private TableRow mLanTableRow;
	private TableRow mWifiTableRow;
	private TableRow mPlayTableRow;	
	private TableRow mVideoTableRow;
	private Button mPlayButton;

	private TableRow mIrLedTableRow;	
	private TableRow mGreenLedTableRow;
	private TableRow mRedLedTableRow;
	private Button mIrLedButton; 
	private Button mGreenLedButton;
	private Button mRedLedButton;

	private TextView mNoticeTextView;
	private TableRow mCountTableRow;	
	private TableRow mStrengthTableRow;
	private TableRow mSsidTableRow;
	private TableRow mMemoryTableRow;
	private TableRow mVersionTableRow;
	private TableRow mDateTableRow;
	private TableRow mIpTableRow;

	//private TableRow mCurrentModelTableRow;	
	//private TableRow mCurrentHwTableRow;
	private TableRow mCurrentSnTableRow;
	private TableRow mCurrentMacTableRow;
	private TableRow mMacWriteTableRow;
	private TableRow mSnWriteTableRow;
	private TableRow mHwWriteTableRow;

	private TableRow mResetTableRow;

	private TextView mWifiState;
	private TextView mLanNetState;
	private TextView mUSB1State;
	private VideoView mVideoState;
	private TextView mMacBrunNote;
	
	private TextView mMulticastAddr;
	private TextView mMulticastPort;
	private TextView mSSID;
	private TextView mMemorySize;
	private TextView mFirmwareVersion;
	private TextView mReleaseDate;
	//private TextView mLanNetMac;
	private TextView mIP;

	private TextView mCurrentModelTextView;
	private TextView mCurrentHWTextView;
	private TextView mCurrentBuildDateTextView;
	private TextView mCurrentSNTextView;
	private TextView mCurrentMACTextView;
	//private String mCurrentModelStr;
	//private String mCurrentHWStr;
	private String mCurrentSNStr;
	private String mCurrentMACStr = "";
	private String mCurrentReleaseDate;
	private boolean mbScanEnterFlag = false; //for scan mac or  sn trigger button be pressed
	private final Object mSyncObject = new Object();
	
	private Button mBurnInTest;
	private Button mResetButton;
		
	private TextView mWriteMac;
	//private TextView mMacWriteStates;
	private TextView mSNWrite;
	private TextView mHWWrite;
	
	private TableLayout mLeftTable;
	private TableLayout mRightTable;
	
	private String mUri;
	
	private  EthernetManager mEthManager;
	private  WifiAdmin wifiAdmin;
	
	//private MediaPlayer mediaPlayer;
	
	private int mWifiCount = 0;
	private  int mLedColor = 0; //0 == off,1,green,2,blue
	private int restartTest = 0;
	private int wifiStrength = 0;
	private int wifiProcess = 0;
	private int mMacInput = 0;//0 none //1;change input
	private int mScanNum = 0;
	
	private String mSSIDString = "";
	private String mScan ="";
	
	private String mLanMacValue = "";
	private String mLanIPAddr = "";
	
	Thread threadSdUSB1;
	Thread threadLAN;
	Thread threadSocket;	
	
	private boolean mIsRun = true;
	private boolean mWifiTestOk = false;	
	private boolean mLanTestOk = false;	
	private boolean mPlayTestOk = false;
	private int mIrLedValue = 0;
	private String mReadVersion;
	private long mReadSize = 32;
	private String mSoftVersionStr;
	private String mMemoryStr;
	private String mReadSsidStr;	
	private String mBurningSsidStr;	
	private int mFactorySsidValue = 10000;	//this for can not read from file
	private int mMaxScanSsidLevel = 0;
	private int mWifiConnectIndex = 0;
	private boolean mIsManualTest = false;
	
	private USBExistFlag mUSBExistFlag = new USBExistFlag();
	class USBExistFlag{
		boolean mUSB1;
		boolean mUSB2;
		boolean mUSB3;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
     /*
		if (SystemProperties.get("ro.vshwcheck", "false").equals("false")) {
			return ;
		}
      */
		super.onCreate(savedInstanceState);
		
		//setDisplayFullHD(); gotech@jesse20131216 fix this because of jimh suggest me commont this,because the HDMI no problem but AV out can't display.
		//setSystemAudio();
		getSystemInfo();

		if (isTheFileExists("sn_mac_check.ini"))
		{			
			initCheckView();
			startCheckTest();
		}
		else if(isTheFileExists("auto_test.ini"))
		{
			mIsManualTest = false;		
			mIsAutoTest = true;
			
			initAutoTestView();
			startAutoTest();
		}
		else //if(isTheFileExists("sn_mac_burn.ini"))
		{
			initWriteTestView();
			startWriteTest();
		}
/*
		else
		{
			mIsAutoTest = false;
			mIsManualTest = true;
			initView();
			initData();
			
			startFactoryTest();
			mPlayButton.requestFocus();
			
			//getSystemInfo();
			if (mCurrentReleaseDate != null)
				mReleaseDate.setText(mCurrentReleaseDate);
			//if (mCurrentModelStr != null)
			//	mCurrentModelTextView.setText(mCurrentModelStr);
			//if (mCurrentHWStr != null)
			//	mCurrentHWTextView.setText(mCurrentHWStr);
			if (mCurrentSNStr != null)
				mCurrentSNTextView.setText(mCurrentSNStr);
			if (mCurrentMACStr != null)	
				mCurrentMACTextView.setText(mCurrentMACStr);
		}
*/

	}

	private void initView()
	{
		setContentView(R.layout.tabletest);
	}
	
	private void initData()
	{
		mUsbTableRow = (TableRow) this.findViewById(R.id.usb_tablerow);
		mLanTableRow = (TableRow) this.findViewById(R.id.lan_tablerow);
		mWifiTableRow = (TableRow) this.findViewById(R.id.wifi_tablerow);
		mPlayTableRow = (TableRow) this.findViewById(R.id.play_tablerow);	
		mVideoTableRow = (TableRow) this.findViewById(R.id.video_tablerow);
		mPlayButton = (Button) this.findViewById(R.id.play_button);
		mPlayButton.setOnClickListener(PlayBtnClickListener);

		mIrLedTableRow = (TableRow) this.findViewById(R.id.ir_led_tablerow);
		mGreenLedTableRow = (TableRow) this.findViewById(R.id.green_led_tablerow);
		mRedLedTableRow = (TableRow) this.findViewById(R.id.red_led_tablerow);		
		mIrLedButton = (Button) this.findViewById(R.id.ir_led);
		mIrLedButton.setOnClickListener(IrLedBtnClickListener);
		mGreenLedButton = (Button) this.findViewById(R.id.green_led);
		mGreenLedButton.setOnClickListener(GreenLedBtnClickListener);
		mRedLedButton = (Button) this.findViewById(R.id.red_led);
		mRedLedButton.setOnClickListener(RedLedBtnClickListener);

		mNoticeTextView = (TextView) this.findViewById(R.id.notice_textview);
		mCountTableRow = (TableRow) this.findViewById(R.id.count_tablerow);
		mStrengthTableRow = (TableRow) this.findViewById(R.id.strength_tablerow);
		mSsidTableRow = (TableRow) this.findViewById(R.id.ssid_tablerow);	
		mMemoryTableRow = (TableRow) this.findViewById(R.id.memory_tablerow);
		mVersionTableRow = (TableRow) this.findViewById(R.id.version_tablerow);
		mDateTableRow = (TableRow) this.findViewById(R.id.date_tablerow);	
		mIpTableRow = (TableRow) this.findViewById(R.id.ip_tablerow);

		//mCurrentModelTableRow = (TableRow) this.findViewById(R.id.current_model_tablerow);
		//mCurrentHwTableRow = (TableRow) this.findViewById(R.id.current_hw_tablerow);
		mCurrentSnTableRow = (TableRow) this.findViewById(R.id.current_sn_tablerow);	
		mCurrentMacTableRow = (TableRow) this.findViewById(R.id.current_mac_tablerow);
		mMacWriteTableRow = (TableRow) this.findViewById(R.id.mac_write_tablerow);
		mSnWriteTableRow = (TableRow) this.findViewById(R.id.sn_write_tablerow);	
		mHwWriteTableRow = (TableRow) this.findViewById(R.id.hw_write_tablerow);
		
		mResetTableRow = (TableRow) this.findViewById(R.id.reset_tablerow);
		
		mUSB1State = (TextView) this.findViewById(R.id.usb);
		mLanNetState = (TextView) this.findViewById(R.id.line_net);
		mWifiState = (TextView) this.findViewById(R.id.wifi);
		mMacBrunNote = (TextView) this.findViewById(R.id.mac_brun_note);
		//mVideoState = (VideoView) this.findViewById(R.id.vido_view);
		
		mMulticastAddr = (TextView) this.findViewById(R.id.multicast_addr);
		mMulticastPort = (TextView) this.findViewById(R.id.multicast_port);
		mSSID = (TextView) this.findViewById(R.id.ssid);
		mMemorySize = (TextView) this.findViewById(R.id.stronge_size);
		mFirmwareVersion = (TextView) this.findViewById(R.id.soft_version);
		mReleaseDate = (TextView) this.findViewById(R.id.release_data);
		//mLanNetMac = (TextView) this.findViewById(R.id.lan_mac);
		mIP = (TextView) this.findViewById(R.id.ip);
		
		mBurnInTest = (Button) this.findViewById(R.id.burn_in_test);
		mResetButton = (Button) this.findViewById(R.id.reset_button);

		mWriteMac = (TextView) this.findViewById(R.id.write);
		//mMacWriteStates = (TextView) this.findViewById(R.id.read);
		mSNWrite = (TextView) this.findViewById(R.id.sn_write);
		mHWWrite = (TextView) this.findViewById(R.id.hw_write);
		
		mLeftTable = (TableLayout) this.findViewById(R.id.tableLayout);
		mRightTable = (TableLayout) this.findViewById(R.id.tableLayout1);
		
		mBurnInTest.setOnClickListener(burnInTestBtnClickListener);
		mResetButton.setOnClickListener(ResetBtnClickListener);

		//mCurrentModelTextView = (TextView) this.findViewById(R.id.current_model_textview);
		//mCurrentHWTextView = (TextView) this.findViewById(R.id.current_hw_textview);
		mCurrentSNTextView = (TextView) this.findViewById(R.id.current_sn_textview);
		mCurrentMACTextView = (TextView) this.findViewById(R.id.current_mac_textview);

	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what)
			{
				case USB1_MESSAGE_Ok:					
					/*if ((mCurrentSNStr != null && mCurrentSNStr.length() > 0)
						&& (mCurrentMACStr != null && mCurrentMACStr.length() > 0) 
						&& mReadVersion == null)
					{
						mUSBExistFlag.mUSB1 = true;
						Intent intent =new Intent(ObbMountActivity.this,CheckActivity.class); 
						intent.putExtra("sn", mCurrentSNStr);
						intent.putExtra("mac", mCurrentMACStr);
						startActivity(intent); 
						finish();
					}
					else*/
					{
						mUsbTableRow.setVisibility(View.VISIBLE);
						mUSB1State.setText(getString(R.string.str_normal_usb1));
						mUSB1State.setBackgroundResource(R.color.green);
			
						mLanTableRow.setVisibility(View.VISIBLE); //for lan display checking
						mUSBExistFlag.mUSB1 = true;
					}
					break;
					
				case USB1_MESSAGE_ERROR:
					mUsbTableRow.setVisibility(View.VISIBLE);
					mUSB1State.setText(getString(R.string.str_unnormal_usb1));
					mUSB1State.setBackgroundResource(R.color.red);
					mUSBExistFlag.mUSB1 = false;
					break;
					
				case LAN_MESSAGE_Ok:
					mLanTableRow.setVisibility(View.VISIBLE);
					mLanNetState.setText(getString(R.string.str_normal));
					mLanNetState.setBackgroundResource(R.color.green);

					mIP.setText(mLanIPAddr);
					mLanTestOk = true;
					break;
					
				case LAN_MESSAGE_ERROR:
					mLanTableRow.setVisibility(View.VISIBLE);
					mLanNetState.setText(getString(R.string.str_unnormal));
					mLanNetState.setBackgroundResource(R.color.red);

					mIP.setText(getString(R.string.str_lan_notice));
					mLanTestOk = false;
					break;
					
				case LAN_PACKAGE_LOSS:
					mLanTableRow.setVisibility(View.VISIBLE);
					mLanNetState.setText(getString(R.string.str_lan_package_loss));
					mLanNetState.setBackgroundResource(R.color.red);

					mLanTestOk = false;
					break;
					
				case WIFI_MESSAGE_ERROR:
					mWifiTableRow.setVisibility(View.VISIBLE);					
					//mWifiState.setText(getString(R.string.str_unnormal));
					mWifiState.setText(getString(R.string.str_unnormal)+", Max: "+mMaxScanSsidLevel+" dBm");
					mWifiState.setBackgroundResource(R.color.red);	

					mMulticastPort.setText(getString(R.string.str_wifi_notice));
					mMulticastAddr.setText(getString(R.string.str_wifi_notice));
					mSSID.setText(getString(R.string.str_wifi_notice));
					mWifiTestOk = false;
					break;
					
				case WIFI_MESSAGE_Ok:					
					mWifiTableRow.setVisibility(View.VISIBLE);
					//mWifiState.setText(getString(R.string.str_normal));
					mWifiState.setText(getString(R.string.str_normal)+", Max: "+mMaxScanSsidLevel+" dBm");
					mWifiState.setBackgroundResource(R.color.green);

					mMulticastPort.setText(wifiStrength+" dBm");
					mMulticastAddr.setText(String.valueOf(mWifiCount));
					mSSID.setText(mSSIDString);
					mWifiTestOk = true;

					mPlayTableRow.setVisibility(View.VISIBLE);
					mVideoTableRow.setVisibility(View.VISIBLE);
					if( mVideoState != null)
					{
						mVideoState.start();
					}
					//Log.d(TAG, "mPlayButton.hasFocus() "+mPlayButton.hasFocus() + " mIrLedTableRow.getVisibility() "+mIrLedTableRow.getVisibility());
					if (!mPlayButton.hasFocus() && mIrLedTableRow.getVisibility() != View.VISIBLE)
					{
						mPlayButton.requestFocus();
						sendKeyEvent(KeyEvent.KEYCODE_DPAD_UP); //this for reboot, ender apk then pluging udisk no focus problem
					}
					break;

				case WIFI_MESSAGE_PROCESS:
					mWifiTableRow.setVisibility(View.VISIBLE);	
					if (mFactorySsidValue > 100)
					{
						mWifiState.setText(getString(R.string.str_wifi_host_error));
						GetVersionAndSize();
					}
					else
					{
						mWifiState.setText(getString(R.string.str_wifi_signal_week)+", Max: "+mMaxScanSsidLevel+" dBm");
					}
					break;

				case IR_LED_FLASH:
					IrLedTest();
					handler.removeMessages(IR_LED_FLASH);
					handler.sendEmptyMessageDelayed(IR_LED_FLASH, 300);
					break;
					
				case IR_LED_CLEAR:
					handler.removeMessages(IR_LED_FLASH);
					//CommonUtilits.writeFile("/sys/class/led_control/led_test","5");//ir led off
					CommonUtilits.writeFile("/proc/net_state","on");
					//mIrLedButton.setBackgroundResource(R.color.transparent);
					mIrLedButton.setBackgroundResource(R.color.green);
					mIrLedButton.setText(getResources().getString(R.string.str_normal)); 
					//mIrLedButton.setTextColor(R.color.gray); 
					break;
					
				case VERSION_CHECK:
					if (mReadVersion != null)
					{
						mSoftVersionStr = getVersion();
						Log.d(TAG, "mSoftVersionStr " + mSoftVersionStr + " mReadVersion " + mReadVersion);
						if (mSoftVersionStr.equals(mReadVersion))
						{
							long size = getInternalMemorySize(ObbMountActivity.this);
							Log.d(TAG, "size " + size + " mReadSize " + mReadSize);
							if (size > mReadSize)
							{
								handler.sendEmptyMessage(VERSION_MESSAGE_Ok);		
							}
							else
							{
								handler.sendEmptyMessage(STORAGE_MESSAGE_ERROR);	
							}									
						}
						else
						{
							handler.sendEmptyMessage(VERSION_MESSAGE_ERROR);	
						}
					}
					else
					{
						handler.sendEmptyMessage(VERSION_MESSAGE_ERROR);	
					}

					break;

				case VERSION_MESSAGE_Ok:
					mNoticeTextView.setVisibility(View.VISIBLE);
					mCountTableRow.setVisibility(View.VISIBLE);
					mStrengthTableRow.setVisibility(View.VISIBLE);
					mSsidTableRow.setVisibility(View.VISIBLE);
					mMemoryTableRow.setVisibility(View.VISIBLE);
					mVersionTableRow.setVisibility(View.VISIBLE);
					mDateTableRow.setVisibility(View.VISIBLE);
					mIpTableRow.setVisibility(View.VISIBLE);

					mFirmwareVersion.setText(mSoftVersionStr);
					mMemorySize.setText(mMemoryStr);

					handler.sendEmptyMessage(EFUSE_INFO_SHOW);	
					break;

				case VERSION_MESSAGE_ERROR:
					mVersionTableRow.setVisibility(View.VISIBLE);
					mFirmwareVersion.setText(getResources().getString(R.string.str_unnormal));
					mFirmwareVersion.setBackgroundResource(R.color.red);
					Toast.makeText(ObbMountActivity.this, R.string.str_version_error_toast, Toast.LENGTH_LONG).show();
					break;

				case STORAGE_MESSAGE_ERROR:
					mMemoryTableRow.setVisibility(View.VISIBLE);
					mMemorySize.setText(getResources().getString(R.string.str_unnormal));
					mMemorySize.setBackgroundResource(R.color.red);
					Toast.makeText(ObbMountActivity.this, R.string.str_memory_error_toast, Toast.LENGTH_LONG).show();					
					break;

				case EFUSE_INFO_SHOW:
					//mCurrentModelTableRow.setVisibility(View.VISIBLE);
					//mCurrentHwTableRow.setVisibility(View.VISIBLE);
					mCurrentSnTableRow.setVisibility(View.VISIBLE);
					mCurrentMacTableRow.setVisibility(View.VISIBLE);
					mMacWriteTableRow.setVisibility(View.VISIBLE);
					mSnWriteTableRow.setVisibility(View.VISIBLE);
					mHwWriteTableRow.setVisibility(View.VISIBLE);	

					threadSocket= new SocketThread();
					threadSocket.start();
					break;

				case RESET_BUTTON_SHOW:
					//mResetTableRow.setVisibility(View.VISIBLE);	
					//mResetButton.requestFocus();
					break;

				case BURNING_TEST_MESSAGE:
					if (mbScanEnterFlag)
					{
						mbScanEnterFlag	= false;
						return;
					}

					if(mVideoState != null)
					{
						mVideoState.stopPlayback();
						mVideoState = null;
						mPlayTestOk = false;
					}	
					
					Intent intent =new Intent(ObbMountActivity.this,VideoBurning.class); 
					startActivity(intent); 
					finish();					
					break;
					
				case SN_SET:
					SNNodeWriteUpdateUI(mRecieveSn, mWriteSnSuccess);
					break;

				case SN_GET_FAILED:
					SNNodeWriteUpdateUI(mRecieveSn, false);
					Toast.makeText(ObbMountActivity.this, R.string.str_sn_error, Toast.LENGTH_LONG).show();
					break;

				case MAC_SET:
					MACNodeWriteUpdateUI(mRecieveMac, mWriteMacSuccess);
					break;			

				case MAC_GET_FAILED:
					MACNodeWriteUpdateUI(mRecieveMac, false);
					Toast.makeText(ObbMountActivity.this, R.string.str_mac_error, Toast.LENGTH_LONG).show();
					break;	

				case MAC_MATCHED:
					mMacBrunNote.setVisibility(View.VISIBLE);
					mMacBrunNote.setText(R.string.str_mac_match);
					mMacBrunNote.setBackgroundResource(R.color.green);
					mWriteMac.setText(mRecieveMac);
					break;			

				case MAC_NOT_MATCHED:
					mMacBrunNote.setVisibility(View.VISIBLE);
					mMacBrunNote.setText(R.string.str_mac_notmatch);
					mMacBrunNote.setBackgroundResource(R.color.red);
					mWriteMac.setText(mRecieveMac);
					//Toast.makeText(ObbMountActivity.this, R.string.str_mac_notmatch, Toast.LENGTH_LONG).show();
					break;	

				/*
				case MODEL_SET:
					ModelNodeWriteUpdateUI(mWriteModelSuccess);
					break;
					
				case HW_SET:
					HWNodeWriteUpdateUI(mRecieveHw, mWriteHwSuccess);
					break;
				
				case HW_GET_FAILED:
					HWNodeWriteUpdateUI(mRecieveHw, false);
					Toast.makeText(ObbMountActivity.this, R.string.str_hw_error, Toast.LENGTH_LONG).show();
					break;	
				*/
				case CONTROL_CHAR_ERR:
					Toast.makeText(ObbMountActivity.this, R.string.str_control_char_error, Toast.LENGTH_LONG).show();
					break;

				case SOCKET_ERR:
					if (mSocketErrResId > 0)
					{
						Toast.makeText(ObbMountActivity.this, mSocketErrResId, Toast.LENGTH_LONG).show();
					}
					break;

				case SOCKET_THREAD_RESTART:
					Log.d(TAG, "threadSocket.getState() "+threadSocket.getState());
					if (threadSocket != null && !threadSocket.isAlive())
					{
						threadSocket= new SocketThread();
						threadSocket.start();
					}
					break;	

				case CHECK_SUCESS:
					if (mCheckInfoTextView != null)
					{
						mCheckInfoTextView.setText(getString(R.string.str_normal));
						mCheckInfoTextView.setBackgroundResource(R.color.green);
					}
					break;

				case CHECK_FAILED:
					if (mCheckInfoTextView != null)
					{
						mCheckInfoTextView.setText(getString(R.string.str_unnormal));
						mCheckInfoTextView.setBackgroundResource(R.color.red);
					}
					break;
				
			}
			
			super.handleMessage(msg);
		};
	};

	
	OnClickListener PlayBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if( mVideoState != null)
			{ 
				Log.d(TAG, "PlayBtnClickListener() "+mPlayTestOk);
				if (mVideoTableRow.getVisibility() == View.VISIBLE && !mPlayTestOk)
				{
					//mVideoState.start();
					mPlayTestOk = true;
					//mPlayButton.setBackgroundResource(R.color.transparent);
					mPlayButton.setBackgroundResource(R.color.green);
					mPlayButton.setText(getResources().getString(R.string.str_normal)); 
					//mPlayButton.setTextColor(R.color.gray); 
					
					mIrLedTableRow.setVisibility(View.VISIBLE);
					mIrLedButton.requestFocus();
					//CommonUtilits.writeFile("/sys/class/led_control/led_test","2");//red
					//CommonUtilits.writeFile("/proc/power_state","pon");
					handler.removeMessages(IR_LED_FLASH);
					handler.sendEmptyMessageDelayed(IR_LED_FLASH, 300);
				}
			}
		}
	};

	void IrLedTest()
	{
			if (0 == mIrLedValue || 2 == mIrLedValue)
			{
	   			//CommonUtilits.writeFile("/sys/class/led_control/led_test","4");//ir led green
	   			CommonUtilits.writeFile("/proc/net_state","on");
	   			mIrLedButton.setBackgroundResource(R.color.blue);
	   			mIrLedValue = 1;
			}
			else if (1 == mIrLedValue)
			{
	   			//CommonUtilits.writeFile("/sys/class/led_control/led_test","5");//ir led off
	   			CommonUtilits.writeFile("/proc/net_state","off");
				mIrLedButton.setBackgroundResource(R.color.red);
	   			//mIrLedButton.setBackgroundResource(R.color.transparent);
	   			mIrLedValue = 2;
			}	
	}

	OnClickListener IrLedBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}

			if (mIrLedTableRow.getVisibility() == View.VISIBLE)
			{
				handler.removeMessages(IR_LED_FLASH);
				handler.sendEmptyMessageDelayed(IR_LED_CLEAR, 100);
				
				mGreenLedTableRow.setVisibility(View.VISIBLE);
				mGreenLedButton.requestFocus();
				//CommonUtilits.writeFile("/sys/class/led_control/led_test","1");//green
				//mGreenLedButton.setBackgroundResource(R.color.green);
				//CommonUtilits.writeFile("/proc/power_state","pon");
	   			mGreenLedButton.setBackgroundResource(R.color.blue);
			}
		}
	};	

	OnClickListener GreenLedBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}

			if (mGreenLedTableRow.getVisibility() == View.VISIBLE)
			{
				handler.sendEmptyMessageDelayed(IR_LED_CLEAR, 100);
				
	   			//CommonUtilits.writeFile("/sys/class/led_control/led_test","2");//red
				//mGreenLedButton.setBackgroundResource(R.color.transparent);
				mGreenLedButton.setBackgroundResource(R.color.green);
				CommonUtilits.writeFile("/proc/power_state","poff");				
				mGreenLedButton.setText(getResources().getString(R.string.str_normal)); 
				//mGreenLedButton.setTextColor(R.color.gray); 

				mRedLedTableRow.setVisibility(View.VISIBLE);
				mRedLedButton.setBackgroundResource(R.color.red);
				mRedLedButton.requestFocus();
			}
		}
	};

	
	OnClickListener RedLedBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}
			
			if (mRedLedButton.getVisibility() == View.VISIBLE)
			{
				handler.sendEmptyMessageDelayed(IR_LED_CLEAR, 100);
				
	   			//CommonUtilits.writeFile("/sys/class/led_control/led_test","1");//green
				//mRedLedButton.setBackgroundResource(R.color.transparent);
				mRedLedButton.setBackgroundResource(R.color.green);
				//CommonUtilits.writeFile("/proc/power_state","pon");			
				mRedLedButton.setText(getResources().getString(R.string.str_normal)); 
				//mRedLedButton.setTextColor(R.color.gray); 	

				handler.sendEmptyMessageDelayed(VERSION_CHECK, 200);
			}
		}
	};

	public static String getVersion(){
		//String ware = SystemProperties.get("ro.product.firmware", "");
		String ware = SystemProperties.get("ro.build.display.id", "");
		//StringBuffer buffer = new StringBuffer();
		//buffer.append(ware.substring(0,2)).append(".").append(ware.substring(2,4)).append(".").append(ware.substring(4,6)).append(".").append(ware.substring(6,8));
		//ware = buffer.toString();
		return ware;
	}

	private String getSoftDate(){
		mCurrentReleaseDate = SystemProperties.get("ro.build.date", "");
		
		return mCurrentReleaseDate;
	}

	public long getInternalMemorySize(Context context) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		long availableBlocks = stat.getAvailableBlocks();
		
		File path2 = Environment.getExternalStorageDirectory();
		StatFs stat2 = new StatFs(path2.getPath());
		long blockSize2 = stat2.getBlockSize();
		long totalBlocks2 = stat2.getBlockCount();
		long availableBlocks2 = stat2.getAvailableBlocks();
		
		String usedSize = Formatter.formatShortFileSize(context, (totalBlocks - availableBlocks) * blockSize + (totalBlocks2 - availableBlocks2) * blockSize2);
		String totalSize = Formatter.formatShortFileSize(context, totalBlocks * blockSize + totalBlocks2 * blockSize2);
		mMemoryStr = usedSize + " / " + totalSize;
		//return internalMemory;
		Log.d(TAG, "getInternalMemorySize() "+mMemoryStr + " value "+(totalBlocks * blockSize + totalBlocks2 * blockSize2));
		
		return (totalBlocks * blockSize + totalBlocks2 * blockSize2);
	}

	private String getFactoryHostIniFile(String path)
	{
		File[] files = new File(path).listFiles();
		String name;
		if (files != null)
		{
			for (File file : files) 
			{
				name = file.getName();
				//Log.d(TAG, "getFactoryHostIniFile: " + name);
				if (name.startsWith("factory_host") && name.endsWith(".ini")) 
				{
					return name;
				}
			}	

		}


		return null;
	}

	private void GetVersionAndSize()
	{
		File[] files = new File("/storage/external_storage").listFiles();
		String path;
		if (files != null) {
			for (File file : files) {
				path = file.getPath();
				if (path.startsWith("/storage/external_storage/sd") && !path.startsWith("/storage/external_storage/sdcard")) {
					Log.d(TAG,"usb device path: "+file.getAbsoluteFile());
					String hostname = getFactoryHostIniFile(path); //do this for version control
					if (hostname != null)
					{
						//File hostFile = new File(file.getAbsoluteFile()+"/factory_host.ini");
						File hostFile = new File(file.getAbsoluteFile()+"/"+hostname);
						BufferedReader reader = null;
						int line = 0;
						try {
							reader = new BufferedReader(new FileReader(hostFile));
							String tempString = null;
							while ((tempString = reader.readLine()) != null) {
								Log.d(TAG, "GetVersionAndSize() line " + line + ": " + tempString);
								if (line == 0)
								{
									if (tempString.startsWith("FFS:"))
									{
										mReadVersion = tempString.substring(4);
									}
								}
								else if (line == 1)
								{
									if (tempString.startsWith("FLASH:"))
									{
										int size = Integer.parseInt(tempString.substring(6));
										if (size >= 0 && size <= 32)
										{
											mReadSize = size*1024*1024*1024L;
										}
									}
								}
								else if (line == 2)
								{
									if (tempString.startsWith("WIFI:"))
									{
										int value = Integer.parseInt(tempString.substring(5));
										if (value >= -100 && value <= 100)
										{
											mFactorySsidValue = value;
										}
									}
								}
								else if (line == 3)
								{
									if (tempString.startsWith("SSID:"))
									{
										mReadSsidStr = tempString.substring(5);
									}
									break;

								}							
								line++;
							}
						} 
						catch (IOException e) {
							e.printStackTrace();
						} 
						finally {
							if (reader != null) {
								try {
									reader.close();
								} 
								catch (IOException e1) {
								}
							}
						}

						if (line > 0) //already find host file
							break;
					}
				} 
			}	
		}	
	}
		
	OnClickListener burnInTestBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}

			if(mVideoState != null)
			{
				mVideoState.stopPlayback();
				mVideoState = null;
				mPlayTestOk = false;
			}
			Intent intent =new Intent(ObbMountActivity.this,VideoBurning.class); 
			startActivity(intent); 
			finish();
		}
	};
	
	OnClickListener ResetBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mbScanEnterFlag)
			{
				mbScanEnterFlag	= false;
				return;
			}
		
			//finish();
			/*if (!mUSBExistFlag.mUSB1)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_unnormal_usb1, Toast.LENGTH_LONG).show();
			}
			else if (!mWifiTestOk)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_wifi_error, Toast.LENGTH_LONG).show();
			}
			else if (!mLanTestOk)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_lan_error, Toast.LENGTH_LONG).show();
			}			
			else *//*if (mCurrentModelStr == null || mCurrentModelStr.length() == 0)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_model_null, Toast.LENGTH_LONG).show();	
			}
			else if (mCurrentHWStr == null || mCurrentHWStr.length() == 0)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_hw_null, Toast.LENGTH_LONG).show();	
			}
			else*/ if (mCurrentSNStr == null || mCurrentSNStr.length() == 0)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_sn_null, Toast.LENGTH_LONG).show();	
			}
			else if (mCurrentMACStr == null || mCurrentMACStr.length() == 0)
			{
				Toast.makeText(ObbMountActivity.this, R.string.str_mac_null, Toast.LENGTH_LONG).show();	
			}			
			else
			{
				sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
			}
		}
	};
/*
	private boolean ModelNodeWrite()
	{
		processCommandKeyName(MODEL_NODE);
		String value = CHexConver.str2HexStr(getResources().getString(R.string.str_model_value));
		boolean success = processCommandKeyWrite(value);

		return success;
	}	

	private void ModelNodeWriteUpdateUI(boolean success)
	{
		if (success)
		{
			//mMacBrunNote.setVisibility(View.VISIBLE);
			//mMacBrunNote.setText(R.string.str_model_write_success);
			//mMacBrunNote.setBackgroundResource(R.color.green);
			ModelNodeRead(true);
		}
		else
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_model_write_failed);
			mMacBrunNote.setBackgroundResource(R.color.red);
		}
	}	

	private boolean HWNodeWrite(String hw)
	{
		processCommandKeyName(HW_NODE);
		//String value = CHexConver.str2HexStr(getResources().getString(R.string.str_hw_version_value));
		String value = CHexConver.str2HexStr(hw);
		boolean success = processCommandKeyWrite(value);
		
		return success;
	}

	private void HWNodeWriteUpdateUI(String hw, boolean success)
	{		
		if (success)
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_hw_write_success);
			mMacBrunNote.setBackgroundResource(R.color.green);
			HWNodeRead(true);
		}
		else
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_hw_write_failed);
			mMacBrunNote.setBackgroundResource(R.color.red);
		}		
		mHWWrite.setText(hw);
	}
*/
	private boolean SNNodeWrite(String sn)
	{
		processCommandKeyName(SN_NODE);
		String value = CHexConver.str2HexStr(sn);
		boolean success = processCommandKeyWrite(value);
		
		return success;		
	}

	private void SNNodeWriteUpdateUI(String sn, boolean success)
	{
		if (success)
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_sn_write_success);
			mMacBrunNote.setBackgroundResource(R.color.green);
			SNNodeRead(true);
		}
		else
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_sn_write_failed);
			mMacBrunNote.setBackgroundResource(R.color.red);
		}		
		mSNWrite.setText(sn);	
	}	

	private boolean MACNodeWrite(String mac)
	{
		processCommandKeyName(MAC_NODE);
		String value = convertMacToAsciiString(mac);
		boolean success = processCommandKeyWrite(value);
		
		return success;
	}

	private void MACNodeWriteUpdateUI(String mac, boolean success)
	{
		if (success)
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_mac_write_success);
			mMacBrunNote.setBackgroundResource(R.color.green);
			MACNodeRead(true);
		}
		else
		{
			mMacBrunNote.setVisibility(View.VISIBLE);
			mMacBrunNote.setText(R.string.str_mac_write_failed);
			mMacBrunNote.setBackgroundResource(R.color.red);
		}
		mWriteMac.setText(mac);
	}
/*
	private void ModelNodeRead(boolean updateUI)
	{
		processCommandKeyName(MODEL_NODE);
		String value = processCommandKeyRead();
		if (value != null && value.length() > 0)
		{
			mCurrentModelStr = CHexConver.hexStr2Str(value);
			if (updateUI)
				mCurrentModelTextView.setText(mCurrentModelStr);
		}
		else
		{
			if (updateUI)
			{
				mCurrentModelTextView.setText(getString(R.string.str_unnormal));
				mCurrentModelTextView.setBackgroundResource(R.color.red);

			}
		}
	}	

	private void HWNodeRead(boolean updateUI)
	{
		processCommandKeyName(HW_NODE);
		String value = processCommandKeyRead();
		if (value != null && value.length() > 0)
		{
			mCurrentHWStr = CHexConver.hexStr2Str(value);	
			if (updateUI)
				mCurrentHWTextView.setText(mCurrentHWStr);
		}
	}
*/
	private void SNNodeRead(boolean updateUI)
	{
		processCommandKeyName(SN_NODE);
		String value = processCommandKeyRead();
		if (value != null && value.length() > 0)
		{
			mCurrentSNStr = CHexConver.hexStr2Str(value);
			if (updateUI)
				mCurrentSNTextView.setText(mCurrentSNStr);
		}
	}

	private void MACNodeRead(boolean updateUI)
	{
		processCommandKeyName(MAC_NODE);
		String value = processCommandKeyRead();
		if (value != null && value.length() > 0)
		{
			mCurrentMACStr = convertAsciiStringToMac(value);
			//mCurrentMACStr = CHexConver.hexStr2Str(value);	
			if (updateUI)
				mCurrentMACTextView.setText(mCurrentMACStr);
		}
	}

	private Handler mClearScanHandler = new Handler();
	private Runnable mClearScanRunnable = new Runnable() 					
	{						
		public void run() 						
		{
			Log.i(TAG, "---------onKeyDown-------mClearScanRunnable------------");
			mScan = "";	
			mScanNum = 0;
			mbScanEnterFlag = false;
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "----------------onKeyDown------------keyCode= "+keyCode);
        if (isAcceptGetIrCode && mAutoTestOutput != null)
        {
			mCurrentkeyCode= keyCode;
        	byte []data={0x30,0x10,0x0c,0,0,0,0,0,0,0,0,(byte)(0xfe)}; //GET_IR_CODE
      		executeinStruction(mAutoTestOutput, data);
        	isAcceptGetIrCode = false;	

        }

//		if (KeyEvent.isModifierKey(keyCode))
//			return true;

		String scanStr = processKey(keyCode);
		//if(scanStr.length() > 0 && threadSocket != null)
		if(scanStr.length() > 0)
		{
			mScan = mScan+scanStr;
			mScanNum++;
			mbScanEnterFlag = true;

			Log.i(TAG, "----------------onKeyDown------------mScanNum= "+mScanNum);					
			if (mScanNum == 12) //hw length is 8, mac length is 12, sn length is 17
			{
				mMacBrunNote.setVisibility(View.INVISIBLE);
			
/*
				if (mScan.charAt(0) == 'D' && mScan.charAt(1) == 'M')//SN: DMA30104140740027
				{

					if (mScanNum == 17)
					{
						mRecieveSn = mScan;
						mWriteSnSuccess = SNNodeWrite(mRecieveSn);
						handler.sendEmptyMessage(SN_SET);
						
						mScan = ""; 
						mScanNum = 0;
					}
					else if (mScanNum > 17) //error count
					{
						mScan = ""; 
						mScanNum = 0;				
					}				
				}
				else 
*/
				//correct mac address
				if (Integer.parseInt(mScan.substring(1,2) )%2 == 0 )
				{		
						Log.i(TAG, " mac string = "+mScan);	
						//correct mac address
						mRecieveMac = mScan;
						MACNodeRead(false);
						if (mCurrentMACStr == null || mCurrentMACStr.length() == 0)
						{
							Log.i(TAG, " first write mac");
							mMacUpdate = false;
							mWriteMacSuccess = MACNodeWrite(mRecieveMac);
							handler.sendEmptyMessage(MAC_SET);
						}
							else if(mRecieveMac.equals(mCurrentMACStr.replace(":","")))
						{
							mMacUpdate = false;
							handler.sendEmptyMessage(MAC_MATCHED);
						}
						else if(mMacUpdate) //second scan 
						{
							mMacUpdate = false;
							mWriteMacSuccess = MACNodeWrite(mRecieveMac);
							handler.sendEmptyMessage(MAC_SET);
						}
						else
						{
							mMacUpdate = true;
							handler.sendEmptyMessage(MAC_NOT_MATCHED);
						}
						mScan = "";
						mScanNum = 0;
				}
				/*else if (mScan.charAt(0) == '1' && mScan.charAt(1) == '4'
						&& mScan.charAt(2) == '2' && mScan.charAt(3) == '3')				
				{
					if(mScanNum == 8) //hw
					{
						mRecieveHw = mScan;
						mWriteHwSuccess = HWNodeWrite(mRecieveHw);
						handler.sendEmptyMessage(HW_SET);
						
						mScan = "";	
						mScanNum = 0;
					}
				}*/				
				else //error mac 
				{
					Log.i(TAG, "err mac string = "+mScan);	
/*					
					//mScan = ""; 
					//mScanNum = 0;	
					MAC_GET_FAILED
					mClearScanHandler.removeCallbacks(mClearScanRunnable);
					mClearScanHandler.postDelayed(mClearScanRunnable, 50);
*/
					//correct mac address
					mRecieveMac = mScan;
					//mWriteMacSuccess = MACNodeWrite(mRecieveMac);
					handler.sendEmptyMessage(MAC_GET_FAILED);
					mScan = "";
					mScanNum = 0;
					mMacUpdate = false;
				}	

				if (mWriteHwSuccess && mWriteMacSuccess && mWriteSnSuccess)
				{
					handler.sendEmptyMessageDelayed(RESET_BUTTON_SHOW, 100);	
				}
			}

			mClearScanHandler.removeCallbacks(mClearScanRunnable);
			mClearScanHandler.postDelayed(mClearScanRunnable, 500);

		}
		else if(!(keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT))
		{
			mScan = "";	
			mScanNum = 0;		
			mbScanEnterFlag = false;
		}
		
		if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER /*|| keyCode == KeyEvent.KEYCODE_BACK*/)
		{
			//mWriteMac.setText(R.string.str_mac_reading);
			//mSNWrite.setText(R.string.str_mac_reading);
			//mHWWrite.setText(R.string.str_mac_reading);
			mScan = "";	
			mScanNum = 0;
			mbScanEnterFlag = false;
			mMacUpdate = false;
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
			mMacUpdate = false;
		}

		else if (keyCode == KeyEvent.KEYCODE_FACTORY_FACTORY_MODE )
		{
			mScan = "";	
			mScanNum = 0;		
			mbScanEnterFlag = false;
			mMacUpdate = false;
			Intent intent =new Intent(ObbMountActivity.this,VideoBurning.class); 
			startActivity(intent); 
			finish();			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void startFactoryTest()
	{	
		threadSdUSB1 =  new Thread()  
	    {  
	         public void run()  
	         {   
	        	 while (mIsRun)
	        	 {
	        		 File myFilePath = new File("/sys/bus/usb/devices/1-1");  	 
	        		 try {  
	        			 if (!myFilePath.exists()) {
	        				 //Log.d(TAG,"TEST USB fail!!!"); 
	        				 handler.sendEmptyMessage(USB1_MESSAGE_ERROR); 
	        			 }	
	        			 else
	        			 {
	        				 handler.sendEmptyMessage(USB1_MESSAGE_Ok);
	        			 }	
	        		 }
	        		 catch (Exception e) {  
	        			 handler.sendEmptyMessage(USB1_MESSAGE_ERROR);
	        			 Log.e(TAG, "create new file fail!!!");
	        			 e.printStackTrace();
	        		 } 
	        	 
	        		 try {
						Thread.sleep(100);
	        		 } 
	        		 catch (Exception e) {
						// TODO: handle exception
	        		 }
	        	}
	        }	 
	    };
	    
	    threadLAN =  new Thread()  
	    {  
	        public void run()  
	        { 
	        	while (mIsRun)
	        	{
		        	String ETHERNET_DEV_STATUS = "/sys/class/net/eth0/operstate";
		        	String status = getEthernetDevStatus(ETHERNET_DEV_STATUS);
					int wcg_id;

					if (mUSBExistFlag.mUSB1)
					{
			        	if(status == null)
			        	{
			        		handler.sendEmptyMessage(LAN_MESSAGE_ERROR);
			        	}
			        	else
			        	{
			        		if(status.equals("down"))
			        		{
			        			handler.sendEmptyMessage(LAN_MESSAGE_ERROR);
			        		}
			        		else if(status.equals("up"))
			        		{
					        	mLanIPAddr = getLocalIpAddress();
			        			if (pingLanNetwork())
			        				handler.sendEmptyMessage(LAN_MESSAGE_Ok);
								else
									handler.sendEmptyMessage(LAN_PACKAGE_LOSS);
			        		}
			        		else
			        		{
			        			handler.sendEmptyMessage(LAN_MESSAGE_ERROR);
			        		}
			        	}
					}

		        	mEthManager = (EthernetManager) getSystemService(Context.ETH_SERVICE);       
		        	mEthManager.setEthEnabled(true); 
	
					try {
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 
		        	wifiAdmin = new WifiAdmin(ObbMountActivity.this); 
		            wifiAdmin.openWifi(); 
		            
		            wifiAdmin.startScan();
		            List<ScanResult> wifiList = wifiAdmin.getWifiList();
					if (wifiList.size() > 0)
		            {
		            	mWifiCount = wifiList.size();
		            	//mSSIDString = wifiList.get(0).SSID;
		            	//wifiStrength = WifiManager.calculateSignalLevel(wifiAdmin.getRssi(), 100); 

						boolean isSend = false;
						int strength = 0;
						mMaxScanSsidLevel = -100;
						mWifiConnectIndex = 0;
						for (int i=0; i<wifiList.size(); i++)
						{
							if (mReadSsidStr != null && mReadSsidStr.length() > 0 && !wifiList.get(i).SSID.equals(mReadSsidStr))
							{
								//already set ssid in factory_host.ini, so skip unused ssid
								if (i == 0)
								{
									//Log.d(TAG, "no match ssid: " + wifiList.get(i).SSID + " mReadSsidStr: " + mReadSsidStr);
								}
								continue;
							}
								
							strength = wifiList.get(i).level;//WifiManager.calculateSignalLevel(wifiList.get(i).level, 100);
							if (strength > mMaxScanSsidLevel)
							{
								mMaxScanSsidLevel = strength;
							}
							
							//Log.d(TAG, "mFactorySsidValue:" + mFactorySsidValue + " level: "+ wifiList.get(i).level+ " strength: "+strength+" SSID: " +wifiList.get(i).SSID + " isSend "+isSend);
							if (mLanTestOk && mUSBExistFlag.mUSB1)
							{
								if (!isSend && (strength > mFactorySsidValue))
								{
									isSend = true;								
									wifiStrength = strength;
									mMaxScanSsidLevel = strength;
									mWifiConnectIndex = i;
									mSSIDString = wifiList.get(i).SSID;
			            			handler.sendEmptyMessage(WIFI_MESSAGE_Ok);
									//wifiAdmin.connectConfiguration(mWifiConnectIndex);
								}
							}

							if (!mUSBExistFlag.mUSB1)
							{
								if (wifiList.get(i).SSID.equals("Burn-in-Testing") || (mBurningSsidStr != null && wifiList.get(i).SSID.equals(mBurningSsidStr)))

								{
									handler.sendEmptyMessage(BURNING_TEST_MESSAGE);
									break;

								}
							}
						}

						if (mLanTestOk && !isSend && !mWifiTestOk && mUSBExistFlag.mUSB1)
						{
		            		handler.sendEmptyMessage(WIFI_MESSAGE_PROCESS);
						}
		            }
		            else
		            {
		            	if (mLanTestOk)
						{
		            		handler.sendEmptyMessage(WIFI_MESSAGE_ERROR); 
		            	}
		            }

 
	        		try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
	        	}
	        }  
	    };

	    threadSdUSB1.start();
        //threadLAN.start();
		
		//threadSocket= new SocketThread();
		//threadSocket.start();  //will start at version check ok
	}

	private class SocketThread extends Thread 
	{  
		@Override  
		public void run() 
		{
			Socket socket = null;			
			OutputStream out = null;
			BufferedReader br = null;
			String line = null; 
			int success = 0;
			boolean failedOnce = false;
			boolean isConnected = false;
	
			//first always write model, all key write must in the same thread
			//mWriteModelSuccess = ModelNodeWrite();
			//handler.sendEmptyMessage(MODEL_SET); 
			try {
				Thread.sleep(1000);
			} catch (Exception e){
				//TODO: handle exception
			}
			
			try 
			{  
				GetSocketIpAndPort();
				//socket = new Socket(mSocketIpStr, mSocketPortId); 				
				for (int i=0; i<60 && mIsRun; i++) //wait 30s for network ok
				{
					try
					{
						socket = new Socket();
						socket.connect(new InetSocketAddress(mSocketIpStr, mSocketPortId), 10000);
					} catch (UnknownHostException e) {
						//e.printStackTrace(); 
						if (i > 0 && (i%4 == 0))
						{
							mSocketErrResId = R.string.str_server_connection_failed;
							handler.sendEmptyMessage(SOCKET_ERR);
						}
					} catch (IOException e) {
						//e.printStackTrace();		
						if (i > 0 && (i%4 == 0))
						{
							mSocketErrResId = R.string.str_io_exception_failed;
							handler.sendEmptyMessage(SOCKET_ERR); 
						}
					}
	
					if (socket.isConnected()) {
						isConnected = true;
						break;
					}
					
					if (isInterrupted()) {
						Log.e(TAG, "TCP socket connect interrupted ");
						return;
					}
	
					if (socket != null)
					{
						try {
							socket.close(); 
						} catch (IOException e) {
							e.printStackTrace();
						}										

					}	
					
					try {
						Thread.sleep(2000);
					} catch (Exception e){
						//TODO: handle exception
					}					
					Log.d(TAG, "Socket connect timeout "+System.currentTimeMillis() + " retry "+i);

				}
	
				if (socket != null && isConnected)
				{
					out = socket.getOutputStream(); 
					//send control char to server
					Log.d(TAG, "Socket send 0x1000 "+System.currentTimeMillis());

					{
						byte  b[]  =  {0x10, 0x00}; 
						out.write(b);  
						out.flush(); 
					}
	
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
					while(mIsRun)
					{
						line = br.readLine();
						if (line != null)
						{
							Log.d(TAG, "--------Socket line = "+line+" length= "+line.length());
							if (line.length() >= 8)
							{
								if (line.charAt(0) == 0x10 && line.charAt(1) == 0x01) //sn
								{
									if (line.charAt(2) == 17 /*&& line.charAt(3) == 'D' && line.charAt(4) == 'M'*/)//SN: DMA30104140740027
									{
										mRecieveSn = line.substring(3);
										mWriteSnSuccess = SNNodeWrite(mRecieveSn);
										//Log.d(TAG, "Socket sn write "+mWriteSnSuccess + " time "+System.currentTimeMillis());

										if (!failedOnce)
										{
											handler.sendEmptyMessage(SN_SET);
											failedOnce = mWriteSnSuccess ? false : true;
										}
									}
									else
									{
										failedOnce = true;
										handler.sendEmptyMessage(SN_GET_FAILED); 

									}
									success++;
								}
								else if (line.charAt(0) == 0x10 && line.charAt(1) == 0x02) //mac: 143DF2119700
								{
									if (line.charAt(2) == 12/* && line.charAt(3) == '1' && line.charAt(4) == '4'
									&& line.charAt(5) == '3' && line.charAt(6) == 'D'*/)
									{
										mRecieveMac = line.substring(3);
										mWriteMacSuccess = MACNodeWrite(mRecieveMac);
										//Log.d(TAG, "Socket mac write "+mWriteMacSuccess + " time "+System.currentTimeMillis());
										if (!failedOnce)
										{
											handler.sendEmptyMessage(MAC_SET);
											failedOnce = mWriteMacSuccess ? false : true;
										}											
									}
									else
									{
										failedOnce = true;
										handler.sendEmptyMessage(MAC_GET_FAILED); 

									}
									success++;
								}
								/*else if (line.charAt(0) == 0x10 && line.charAt(1) == 0x03) //hw: 14230711 or 14230721
								{
									if (line.charAt(2) >= 8  && line.charAt(3) == '1' && line.charAt(4) == '4'
									&& line.charAt(5) == '2' && line.charAt(6) == '3')
									{
										mRecieveHw= line.substring(3);
										mWriteHwSuccess = HWNodeWrite(mRecieveHw);
										//Log.d(TAG, "Socket hw write "+mWriteHwSuccess + " time "+System.currentTimeMillis());
										if (!failedOnce)
										{
											handler.sendEmptyMessage(HW_SET);
											failedOnce = mWriteHwSuccess ? false : true;
										}											
									}
									else
									{
										failedOnce = true;
										handler.sendEmptyMessage(HW_GET_FAILED); 

									}									
									success++;
								}*/								
								else
								{
									handler.sendEmptyMessage(CONTROL_CHAR_ERR); 
	

								}
	
								if (success >= 3)
								{
									if (mWriteHwSuccess && mWriteMacSuccess && mWriteSnSuccess)
									{
										byte  b[]  =  {0x20, 0x20}; 
										out.write(b);  
										out.flush(); 
										Log.d(TAG, "Socket send 0x2020 ");

										handler.sendEmptyMessageDelayed(RESET_BUTTON_SHOW, 100);	
									}
									else
									{
										byte  b[]  =  {0x21, 0x21}; 
										out.write(b); 
										out.flush(); 
										Log.d(TAG, "Socket send 0x2121 ");
 										
										handler.sendEmptyMessageDelayed(SOCKET_THREAD_RESTART, 2000); 
									}
									CommonUtilits.syncCmd();
									break;

								}
							}
						}
						else
						{
							Log.d(TAG, "Socket read timeout "+System.currentTimeMillis());

						}
	
						try {
							Thread.sleep(1000);
						} catch (Exception e){
							//TODO: handle exception
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();  
				mSocketErrResId = R.string.str_server_connection_failed;
				handler.sendEmptyMessage(SOCKET_ERR); 
			} catch (IOException e) {
				e.printStackTrace();
				mSocketErrResId = R.string.str_io_exception_failed;
				handler.sendEmptyMessage(SOCKET_ERR);					
			}catch (SecurityException e) {
				e.printStackTrace();
				mSocketErrResId = R.string.str_server_permission_denies;
				handler.sendEmptyMessage(SOCKET_ERR);					
			}finally {		
				if (out != null)
				{
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}
	
				if (br != null)
				{
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}
	
				if (socket != null)
				{
					try {
						socket.close(); 
					} catch (IOException e) {
						e.printStackTrace();
					}										

				}
				Log.d(TAG, "Socket close "+System.currentTimeMillis());

			}
		}
	};

	private void GetSocketIpAndPort()
	{
		File[] files = new File("/storage/external_storage").listFiles();
		String path;
		if (files != null) {
			for (File file : files) {
				path = file.getPath();
				if (path.startsWith("/storage/external_storage/sd") && !path.startsWith("/storage/external_storage/sdcard")) {
					Log.d(TAG,"usb device path: "+file.getAbsoluteFile());

					File hostFile = new File(file.getAbsoluteFile()+"/gotech_host.ini");
					BufferedReader reader = null;
					int line = 0;
					try {
						reader = new BufferedReader(new FileReader(hostFile));
						String tempString = null;
						while ((tempString = reader.readLine()) != null) {
							Log.d(TAG, "GetSocketIpAndPort() line " + line + ": " + tempString);
							if (line == 0 && isIpAddress(tempString))
							{
								mSocketIpStr = tempString;
							}
							else if (line == 1)
							{
								int port = Integer.parseInt(tempString);
								if (port >= 1000 && port <= 10000)
								{
									mSocketPortId = port;
								}

								//break;
							}
							else if (line == 2)
							{
								mBurningSsidStr	= tempString;
								break;
							}
							line++;
						}
					} 
					catch (IOException e) {
						e.printStackTrace();
					} 
					finally {
						if (reader != null) {
							try {
								reader.close();
							} 
							catch (IOException e1) {
							}
						}
					}

					if (line > 0) //already find host file
						break;
				} 
			}	
		}	

	}

	private boolean isIpAddress(String value) 
	{
		int start = 0;
		int end = value.indexOf('.');
		int numBlocks = 0;

		while (start < value.length()) 
		{
			if (end == -1) 
			{
				end = value.length();
			}

			try 
			{
				int block = Integer.parseInt(value.substring(start, end));
				if ((block > 255) || (block < 0)) 
				{
					return false;
				}
			} 
			catch (NumberFormatException e) 
			{
				return false;
			}

			numBlocks++;

			start = end + 1;
			end = value.indexOf('.', start);
		}
		
		return numBlocks == 4;
	}

	private boolean pingLanNetwork() 
	{
		boolean ret = false;
		String line = null;
		BufferedReader bufferedReader=null;

		for (int i=0; i<2; i++)
		{
			try 
			{
				//String pingStr = "ping -c 1 -l 922 "+mSocketIpStr;
				String pingStr = "ping -c 1 "+mSocketIpStr;
				Process pro = Runtime.getRuntime().exec(pingStr);
				Log.d(TAG, "pingStr: "+pingStr);

				bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while ((line = bufferedReader.readLine()) != null) 
				{
					int position = 0;
					//Log.d(TAG, "ping: "+line);

					if ((position = line.indexOf(" 0% packet loss")) >= 0)
					{
						ret = true;
						break;
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				Log.d(TAG, "pingLanNetwork failed! ");

			}finally
			{
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (ret)
				break;
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}				
		}

		return ret;
	}
	
	private void getSystemInfo()
	{
		GetVersionAndSize();
		GetSocketIpAndPort();
		getSoftDate();
		
		//ModelNodeRead(false);
		//HWNodeRead(false);
		SNNodeRead(false);
		MACNodeRead(false);
	}
	
	private  static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }
	
    private static String formatKernelVersion(String rawKernelVersion) {
    // Example (see tests for more):
    // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
    //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
    //     Thu Jun 28 11:02:39 PDT 2012

	    final String PROC_VERSION_REGEX =
	        "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
	        "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
	        "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
	        "(#\\d+) " +              /* group 3: "#1" */
	        "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
	        "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            Log.e(TAG, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            Log.e(TAG, "Regex match on /proc/version only returned " + m.groupCount()+ " groups");
            return "Unavailable";
        }
//        return m.group(1) + "\n" +                 // 3.0.31-g6fb96c9
//        m.group(2) + " " + m.group(3) + "\n" + // x@y.com #1
//        m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
        return m.group(4);
    }
	
	public static String processKey(int keycode)
	{ 
		    String sRet = "";
		    int code = 0;
		   switch(keycode)
		   {
		   case 7:
		   case 8:
		   case 9:
		   case 10:
		   case 11:
		   case 12:
		   case 13:
		   case 14:
		   case 15:
		   case 16:
			   code = keycode - 7;
			   sRet = sRet+code;
			   break;
		   case 29:
			   sRet = "A";
			   break;
		   case 30:
			   sRet = "B";
			   break;
		   case 31:
			   sRet = "C";
			   break;
		   case 32:
			   sRet = "D";
			   break;
		   case 33:
			   sRet = "E";
			   break;
		   case 34:
			   sRet = "F";
			   break;

		   case 41:
			   sRet = "M";
			   break;
			   
		   }
		   return sRet;
	}
	
	private void processCommandInitVersion()
	{
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/version", 777);
		
		try {
			CommonUtilits.writeFile("/sys/class/aml_keys/aml_keys/version","nand3");
		}catch (Exception e) {
	    	e.printStackTrace();
		}
		
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/version", 666);
	}
	
	private void processCommandKeyName(String value)
	{
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/key_name", 777);
		
		String resultString;
		synchronized (mSyncObject)

		{		
			try {
				CommonUtilits.writeFile("/sys/class/aml_keys/aml_keys/key_name",value);  
			}catch (Exception e) {
				Log.i(TAG, "GOTECH func processCommandKeyName error!");
		    	e.printStackTrace();
			}
		}		
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/key_name", 666);
	}
	
	private boolean processCommandKeyWrite(String value)
	{
		boolean success = true;
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/key_write", 777);
		synchronized (mSyncObject)

		{
			String resultString;
			try {
				success = CommonUtilits.writeFile("/sys/class/aml_keys/aml_keys/key_write",value);   
			}catch (Exception e) {
		    	e.printStackTrace();
				success = false;
			}
		}
		
		//CommonUtilits.chmodFilePermision("/sys/class/aml_keys/aml_keys/key_write", 666);
		
		return success;
	}

	private String processCommandKeyRead()
	{
		String resultString = null;
		
		synchronized (mSyncObject)

		{	
			try {
				resultString = CommonUtilits.readFile("/sys/class/aml_keys/aml_keys/key_read");
			}catch (Exception e) {
				Log.i(TAG, "GOTECH func processCommandKeyRead error!");
		    	e.printStackTrace();
			}
		}

		return resultString;
	}

	private String convertMacToAsciiString(String strMac)
	{
		int i = 0;
		Boolean bconvert = true;
		char a = ':';
		String ascii = Integer.toHexString(a);
		
		//Log.d(TAG,"ascii : =" + ascii);
		String asciiA="";
		String sRet="";
		while(bconvert)
		{
			if((i>0) && (i%2 == 0))		
				sRet = sRet + ascii;
			
			a = strMac.charAt(i);
		
		//	asciiA = (int) a;
			asciiA= Integer.toHexString(a);
			
			 sRet = sRet + asciiA;
			i++;
			if(i==12)
				bconvert = false;
		}

		
		Log.d(TAG,"convertMacToAsciiString sRet= " + sRet);
		return sRet;
	}
	//jesse add method for convert module.
	private String convertAsciiStringToMac(String strAscii)
	{
		int length = strAscii.length();
		Log.i(TAG, "length= "+length + ", src= " + strAscii);
		if (length <= 0)
		{
			return "";
		}
		
		Boolean bconvert = true;
		int i = 0;
		String ret = "";
		int asciiValue;
		char AsciiChange;
		String temp = "";
		while(bconvert)
		{
			temp += strAscii.charAt(i);
			i++;

			if (i%2 == 0 && i != 0)
			{
				char tempH = temp.charAt(0);
				int intH = 0;
				if (tempH >= '0' && tempH <= '9')
				{
					intH = tempH - '0';
					intH *= 16;
				}
				else if (tempH >= 'a' && tempH <= 'z')
				{
					intH = tempH - 'a' + 10;
					intH *= 16;
				}
				
				char tempL = temp.charAt(1);
				int intL = 0;
				if (tempL >= '0' && tempL <= '9')
				{
					intL = tempL - '0';
				}
				else if (tempL >= 'a' && tempL <= 'z')
				{
					intL = tempL - 'a' + 10;
				}
				asciiValue = intH + intL;
				
				AsciiChange = (char)asciiValue;
				ret += String.valueOf(AsciiChange);
				temp = "";
			}
			
			if(i==length)
				bconvert = false;
		}

		Log.i(TAG, "convertAsciiStringToMac ret= "+ret);		
		return ret;
	}
	
	private String getLocalIpAddress() { 
        try { 
        	String ipv4; 
            List  netlist = Collections.list(NetworkInterface.getNetworkInterfaces()); 
            for (int index=0; index<netlist.size(); index++)
            {
            	List  ialist = Collections.list(((NetworkInterface) netlist.get(index)).getInetAddresses()); 
            	for (int count=0; count<ialist.size(); count++)
            	{
                	if (!((InetAddress) ialist.get(count)).isLoopbackAddress() &&                          
                    	InetAddressUtils.isIPv4Address(ipv4=((InetAddress) ialist.get(count)).getHostAddress()))  
                    {  
                		return ipv4; 
                    } 
            	}
            }
        } 
        catch (SocketException ex) { 
            Log.e(TAG, ex.toString()); 
        } 
        return null; 
    }
	
	private String getEthernetDevStatus(String eth0_parm)
	{
		String mEhernet_dev_status=null;
		FileReader fileReader = null;
		   
		try
        {
			fileReader = new FileReader(eth0_parm);
        }
		catch (FileNotFoundException e)
        {
			e.printStackTrace();
        }
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(fileReader);
        try
        {
        	mEhernet_dev_status = bufferedReader.readLine();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }                    
        try
        {
        	bufferedReader.close();		  
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }          
        try
        {
           fileReader.close();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        
        //Log.i(TAG,"mEhernet_dev_status = "+ mEhernet_dev_status);
        return mEhernet_dev_status;  
	}
	
	private void playVideoView()
	{
		mUri = "android.resource://" + getPackageName() + "/" + R.raw.testvideo;
		mVideoState.setVideoURI(Uri.parse(mUri)); 

		//mVideoState.start(); 

		mVideoState.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { 
			@Override  
			public void onCompletion(MediaPlayer mp) { 
				mVideoState.seekTo(0); 
			}  
		}); 
	}
	
	private void setSystemAudio()
	{
		AudioManager audioManager;

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,0);
	}
	
    private void setDisplayFullHD()
    {
    	CommonUtilits.writeFile("/sys/class/display/mode","1080p60");
		   try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   CommonUtilits.writeFile("/sys/class/ppmgr/ppscaler_rect","0 0 1920 1080 0"); 
    }

	private void sendKeyEvent(int keyCode) 
	{
		Log.d(TAG," sendKeyEvent() "+keyCode);	
		int eventCode = keyCode;
		long now = SystemClock.uptimeMillis();
		try {
			KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, eventCode, 0);
			KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, eventCode, 0);
			(IInputManager.Stub.asInterface(ServiceManager.getService("input"))).injectInputEvent(down, 0);
			(IInputManager.Stub.asInterface(ServiceManager.getService("input"))).injectInputEvent(up, 0);
		} catch (RemoteException e) {
			Log.i(TAG, "DeadOjbectException");
		}
	}
	
	 @Override  
     protected void onStop() {  
        Log.d(TAG, "onStop()------------" );  
		if(mVideoState!=null)
			mVideoState.stopPlayback();
		mVideoState = null;
		mPlayTestOk = false;

		//if(wifiAdmin!=null)
		//	wifiAdmin.closeWifi();
		if(mEthManager!=null)
			mEthManager.setEthEnabled(true);

		if (mIsAutoTest)
		{
			mIsRun = false;
		}
		 Log.d(TAG, "onStop()---------ok---" );  
		super.onStop();  
    }
	 
    @Override  
	protected void onResume() {
		Log.d(TAG, "onResume() " + mVideoState);  
		if (mIsAutoTest)
		{

			if( mVideoState != null)
			{ 
				mVideoState.start(); 
			}
			else
			{
				mVideoState = (VideoView) ObbMountActivity.this.findViewById(R.id.auto_test_vidoView);
				playAutoTestVideoView();
			}		
		}
		else if (mIsManualTest)
		{
			if( mVideoState != null)
			{ 
				//mVideoState.start(); 
			}
			else
			{
				mVideoState = (VideoView) ObbMountActivity.this.findViewById(R.id.vido_view);
				playVideoView();
			}
		}
		
		super.onResume();  
	}

	@Override  
	protected void onPause() {  
		Log.d(TAG, "onPause()");
		handler.removeMessages(BURNING_TEST_MESSAGE);
		super.onPause(); 
	}
   
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy()---------pre--" );  
	   	handler.removeMessages(SD_MESSAGE_Ok);
	   	handler.removeMessages(SD_MESSAGE_ERROR);
	   	handler.removeMessages(USB1_MESSAGE_Ok);
	   	handler.removeMessages(USB1_MESSAGE_ERROR);
	   	handler.removeMessages(LAN_MESSAGE_Ok);
	   	handler.removeMessages(LAN_MESSAGE_ERROR);
	   	handler.removeMessages(LAN_PACKAGE_LOSS);
	   	handler.removeMessages(WIFI_MESSAGE_Ok);
	   	handler.removeMessages(WIFI_MESSAGE_ERROR);
		
		handler.removeMessages(IR_LED_FLASH);
		handler.removeMessages(IR_LED_CLEAR);		
		handler.removeMessages(VERSION_CHECK);

		handler.removeMessages(VERSION_MESSAGE_Ok);
		
		handler.removeMessages(VERSION_MESSAGE_ERROR);
		
		handler.removeMessages(STORAGE_MESSAGE_ERROR);

		
		handler.removeMessages(EFUSE_INFO_SHOW);

		handler.removeMessages(RESET_BUTTON_SHOW);
	
		handler.removeMessages(BURNING_TEST_MESSAGE);
		
		handler.removeMessages(WIFI_MESSAGE_PROCESS);

		
	   	handler.removeMessages(SN_SET);
	   	handler.removeMessages(SN_GET_FAILED);
	   	handler.removeMessages(MAC_SET);
	   	handler.removeMessages(MAC_GET_FAILED);
	   	handler.removeMessages(MAC_MATCHED);
	   	handler.removeMessages(MAC_NOT_MATCHED);
	   	//handler.removeMessages(MODEL_SET);
	   	//handler.removeMessages(HW_SET);
	   	handler.removeMessages(HW_GET_FAILED);		
	   	handler.removeMessages(CONTROL_CHAR_ERR);
		handler.removeMessages(SOCKET_ERR);	
		handler.removeMessages(SOCKET_THREAD_RESTART);
		handler.removeMessages(CHECK_SUCESS);
		handler.removeMessages(CHECK_FAILED);
		mClearScanHandler.removeCallbacks(mClearScanRunnable);
		mIsRun = false;
		Log.d(TAG, "onDestroy()---------ok--" );  
	   super.onDestroy();
   }


	//////////////////////////////////////////////////////////////////////////////////
	/////////// Auto test part
	//////////////////////////////////////////////////////////////////////////////////
	private static final int AUTOTEST_PORT = 6003;	
	private static final int BUFFER_SIZE = 12;	
	private static final String INTERFACE_NAME = "eth0";
	private boolean mIsAutoTest = false;
	private boolean isAcceptGetIrCode = false;
	private OutputStream mAutoTestOutput;

    private boolean mFirstCmdFlag = false;
	private int mCurrentkeyCode = KeyEvent.KEYCODE_UNKNOWN;
	private String usb1MountPath = "";
	private String configUsbFileName = "factory_auto_test_usb_read_file.ini";
	private int wifiStateBeforeTest = WifiManager.WIFI_STATE_ENABLED;
	private int wifiRssi = 50;
	
	public class DeviceProfile
	{
		public String ipAddress;
		public String netMask;
		public String gateWay;
		public String DNS1;
		public String DNS2;
		public String macAddress;
		public int conn_type;
		public int deviceEnable;
		public boolean isLinkUp;

		public DeviceProfile()
		{
			this.ipAddress = "0.0.0.0";
			this.netMask = "0.0.0.0";
			this.gateWay = "0.0.0.0";
			this.DNS1 = "0.0.0.0";
			this.DNS2 = "0.0.0.0";
			this.macAddress = "00:00:00:00:00:00";
			this.deviceEnable = 0;
			this.conn_type = 0;
			this.isLinkUp = false;
		}
	}


	private boolean isTheFileExists(String strFileName)
	{
		File[] files = new File("/storage/external_storage").listFiles();
		String path;
		if (files != null) {
			for (File file : files) {
				path = file.getPath();
				if (path.startsWith("/storage/external_storage/sd") && !path.startsWith("/storage/external_storage/sdcard")) {
					//Log.d(TAG,"isAutoTestFileExist() path: "+path);
					usb1MountPath = path;
					File hostFile = new File(file.getAbsoluteFile()+"/"+strFileName);
					if(hostFile != null && hostFile.canRead())
					{
						return true;	

					}
				} 
			}	
		}	


		return false;
	}

	public void initAutoTestView()  
	{
		wifiAdmin = new WifiAdmin(ObbMountActivity.this);
		wifiStateBeforeTest = wifiAdmin.checkState();
		setStaticIP();

		setContentView(R.layout.auto_test_or_burn_write);
		mVideoState = (VideoView) findViewById(R.id.auto_test_vidoView);
 		mVideoState.setVisibility(View.VISIBLE);
		playAutoTestVideoView();
	}

	private void playAutoTestVideoView()
	{
		mUri = "android.resource://" + getPackageName() + "/" + R.raw.clrbrs720p;
		mVideoState.setVideoURI(Uri.parse(mUri)); 

		//mVideoState.start(); 

		mVideoState.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { 
			@Override  
			public void onCompletion(MediaPlayer mp) { 
				mVideoState.seekTo(0); 
			}  
		}); 
	}	

	private void readSystemProp(DeviceProfile profile, String DEV_NAME)
	{
		if (profile == null)
		{
			return;
		}
		String propIpAddress = "dhcp." + DEV_NAME + ".ipaddress";
		String propMask = "dhcp." + DEV_NAME + ".mask";
		String propDns1 = "dhcp." + DEV_NAME + ".dns1";
		// String propDns2 = "dhcp."+ currentEth +".dns2";
		String propGateway = "dhcp." + DEV_NAME + ".gateway";

		profile.ipAddress = SystemProperties.get(propIpAddress);
		Log.d(TAG, "readSystemProp() "+profile.ipAddress);
		if (profile.ipAddress == null || profile.ipAddress.length() == 0)
		{
			profile.ipAddress = "192.168.0.12";
		}
		
		profile.netMask = SystemProperties.get(propMask);
		if (profile.netMask == null || profile.netMask.length() == 0)
		{
			profile.netMask = "255.255.255.0";
		}
		profile.netMask = "255.255.252.0";
		
		profile.DNS1 = SystemProperties.get(propDns1);
		if (profile.DNS1 == null || profile.DNS1.length() == 0)
		{
			profile.DNS1 = "192.168.0.1";
		}
		profile.DNS1 = "221.5.88.88";
		
		profile.gateWay = SystemProperties.get(propGateway);
		if (profile.gateWay == null || profile.gateWay.length() == 0)
		{
			profile.gateWay = "192.168.0.1";
		}
		profile.gateWay = "192.168.12.1";
	}


	private void setStaticIP()
	{
		mEthManager = (EthernetManager) getSystemService(Context.ETH_SERVICE); 
		DeviceProfile deviceInfo = new DeviceProfile();
		readSystemProp(deviceInfo,INTERFACE_NAME);
		deviceInfo.ipAddress = "192.168.0.12";
		EthernetDevInfo info = new EthernetDevInfo();
		info.setIfName(INTERFACE_NAME);
		info.setConnectMode(EthernetDevInfo.ETH_CONN_MODE_MANUAL);
		info.setIpAddress(deviceInfo.ipAddress);
		info.setRouteAddr(deviceInfo.gateWay);
		info.setDnsAddr(deviceInfo.DNS1);
		info.setNetMask(deviceInfo.netMask);
		//mEthernetManager.manualIp(info);
		mEthManager.updateEthDevInfo(info);
		mEthManager.setEthEnabled(true);
	}

	public boolean AutoTestGetSN(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		Log.d(TAG, "AutoTestGetSN() "+mCurrentSNStr);
		if (mCurrentSNStr != null && mCurrentSNStr.length() > 0)
		{
			byte []VersionData = new byte[mCurrentSNStr.length()+5];
			VersionData[0] = data[0];
			VersionData[1] = data[1];
			VersionData[2] = (byte) (mCurrentSNStr.length()+5);
			byte ch;
			for(int i=0; i<mCurrentSNStr.length();i++)
			{
				ch = (byte) mCurrentSNStr.charAt(i);
				VersionData[3+i] = ch;
			}
			VersionData[VersionData[2]-2]=getFWCSData(VersionData);
			VersionData[VersionData[2]-1] = (byte) 0xFE;
			try {
				out.write(VersionData);
			} catch (IOException e) {
				e.printStackTrace();
				ret = false;
			}
		}
		else
		{
			data[3] = 0;
			try {
				data[10] = getCSData(data);
				out.write(data);
			} catch (Exception e) {
				e.printStackTrace();
				ret = false;
			}			

		}

		return ret;
	}

	public boolean AutoTestGetFwVersion(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		String strFwVersion = getVersion();//"FF.30.01.15";
		Log.d(TAG, "AutoTestGetFwVersion() "+strFwVersion);		
		byte []VersionData = new byte[strFwVersion.length()+5];
		VersionData[0] = data[0];
		VersionData[1] = data[1];
		VersionData[2] = (byte) (strFwVersion.length()+5);
		byte ch;
		for(int i=0; i<strFwVersion.length();i++)
		{
			ch = (byte) strFwVersion.charAt(i);
			VersionData[3+i] = ch;
		}
		VersionData[VersionData[2]-2]=getFWCSData(VersionData);
		VersionData[VersionData[2]-1] = (byte) 0xFE;
		try {
			out.write(VersionData);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}
	
	public byte getFWCSData(byte[] data)//计算软件版本号cs值
	{
		int cs = 0;
		for(int i=1;i<data.length-2;i++)
		{
			cs += data[i];
		}
		return (byte) (0xff-(byte)cs+1) ;
		
	}

	public boolean AutoTestGetFlashSize(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		//int TotalSize = (int) (getTotalInternalMemorySize()*1024);
		int TotalSize = (int) (getInternalMemorySize(ObbMountActivity.this)/(1024*1024));
		Log.d(TAG, "AutoTestGetFlashSize() "+TotalSize);
		data[3]=(byte) (TotalSize>>8&0xff);
		data[4]=(byte) (TotalSize&0x00ff);
		try {
			data[10] = getCSData(data);
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}
	
	private double getTotalDirectoryMemorySize(File path) {
		// File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		// Byte--->G jesse
		return (double) (totalBlocks * blockSize) / 1000 / 1000 / 1000;
	}

	private double getTotalInternalMemorySize() {
		return (double) (getTotalDirectoryMemorySize(Environment.getDataDirectory()) + getTotalDirectoryMemorySize(Environment
				.getRootDirectory()));

	}

	private double getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		// Byte--->G jesse
		return (double) (availableBlocks * blockSize) / 1000 / 1000 / 1000;
	}

	public boolean AutoTestGetUSBStatus(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		int iUsbCount = 1;
		data[3] = (byte)iUsbCount;
		try {
			data[10] = getCSData(data);
			out.write(data);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public boolean AutoTestGetSdcardStatus(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		data[3] = 0;
		try {
			data[10] = getCSData(data);
			out.write(data);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public boolean AutoTestReadUsbFile(OutputStream out,byte []data)
	{
		boolean ret = true;	
		boolean ReadUsbFileSate = false;
		ReadUsbFileSate = ReadUsbFile(usb1MountPath+"/"+configUsbFileName);
		//if(!ReadUsbFileSate)
		//	ReadUsbFileSate = ReadUsbFile(usb2MountPath+configUsbFileName);
		
		if(ReadUsbFileSate)
		{
			data[3] = 1;
		}
		else
		{
			data[3] = 0;
		}
		
		try {
			data[10] = getCSData(data);
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	//读取usb文件
	public boolean ReadUsbFile(String configUsbFileName)
	{
		Log.d(TAG, "ReadUsbFile() "+configUsbFileName);
		boolean ReadUsbFileSate = false;
		try {
			File readFile = new File(configUsbFileName);
			InputStream instream = new FileInputStream(readFile);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				//Log.d(TAG, "GOTECH func buffreader=" + buffreader.toString());
				String line;
				int TotalLen = 0;
				int ireadLen = 0;
				while ((ireadLen=buffreader.read())!=-1) {
					TotalLen +=ireadLen;
					if(TotalLen>1048576)//读1M的数据
					{
						ReadUsbFileSate = true;
						break;
					}
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.d(TAG, "ReadUsbFile()FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "ReadUsbFile()IOException!");
		}
		return ReadUsbFileSate;
	}


	public boolean AutoTestSetLedLight(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		Log.d(TAG,"AutoTestSetLedLight() data[3] led = "+data[3]+" data[4] value = "+data[4]);
		if (data[3] == 1)
		{
			if (data[4] == 1)
				//CommonUtilits.writeFile("/sys/class/led_control/led_test","4");//ir led green
				CommonUtilits.writeFile("/proc/net_state","on");
			else
				//CommonUtilits.writeFile("/sys/class/led_control/led_test","5");//ir led off
				CommonUtilits.writeFile("/proc/net_state","off");

			try {
				data[10] = getCSData(data);
				out.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				ret = false;
			}				
		}
		else if (data[3] == 2)
		{
			if (data[4] == 1)
				//CommonUtilits.writeFile("/sys/class/led_control/led_test","2");//power red led
				CommonUtilits.writeFile("/proc/power_state","poff");
			else
				//CommonUtilits.writeFile("/sys/class/led_control/led_test","1");//power green led

				CommonUtilits.writeFile("/proc/power_state","pon");

			try {
				data[10] = getCSData(data);
				out.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				ret = false;
			}				
		}

		return ret;
	}

	public void GetWifiSignalRssiOrPercent()
	{
		if (wifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED) {
			wifiAdmin.startScan();

			List<ScanResult> wifiList = wifiAdmin.getWifiList();

			try {
				if (wifiList.size() > 0) {
					mWifiCount = wifiList.size();
					mSSIDString = wifiList.get(0).SSID;
					wifiStrength = WifiManager.calculateSignalLevel(wifiList.get(0).level, 100);
					wifiRssi =Math.abs(wifiList.get(0).level);
					Log.d(TAG, "GetWifiSignalRssiOrPercent() wifiStrength= "+wifiStrength+" wifiRssi "+wifiRssi+" mSSIDString "+mSSIDString);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			wifiAdmin.openWifi();
		}
	}

	public boolean AutoTestGetWifiPercent(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		GetWifiSignalRssiOrPercent();
		try {
			data[3] = (byte) wifiStrength;
			data[10] = getCSData(data);
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public boolean AutoTestGetWifiSignalRssi(OutputStream out,byte []data)
	{
		boolean ret = true;
		
		GetWifiSignalRssiOrPercent();
		try {
			data[3] = (byte) wifiRssi;
			data[10] = getCSData(data);
			out.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}
	

	public boolean executeinStruction(OutputStream out,byte[] data)//处理接收的指令
	{
		boolean ret = true;
		
		switch (data[1]) {
		
		case DVBConstants.SET_FACTORY_TEST_MODE:
			byte SystemStateCmd[]={0x30,0x02,0x0c,0x01,0,0,0,0,0,0,0,(byte)(0xfe)};
			mFirstCmdFlag = true;
			try {
				SystemStateCmd[10] = getCSData(SystemStateCmd);
				out.write(SystemStateCmd);
			} catch (IOException e1) {
				e1.printStackTrace();
				ret = false;
			}
			break;
		case DVBConstants.GET_SN:
			ret = AutoTestGetSN(out,data);
			break;			
		case DVBConstants.GET_FW_VERSION:
			ret = AutoTestGetFwVersion(out,data);
			break;
		case DVBConstants.GET_FLASH_SIZE:
			ret = AutoTestGetFlashSize(out,data);
			break;
		case DVBConstants.TEST_IR:
			try {
				data[10] = getCSData(data);
				out.write(data);
			} catch (IOException e1) {
				e1.printStackTrace();
				ret = false;
			}
			break;
		case DVBConstants.GET_IR_CODE:
			if (mCurrentkeyCode != KeyEvent.KEYCODE_UNKNOWN)
			{
				data[3] =  (byte) (mCurrentkeyCode);
				try {
					data[10] = getCSData(data);
					out.write(data);
				} catch (IOException e1) {
					e1.printStackTrace();
					ret = false;
				}

				mCurrentkeyCode = KeyEvent.KEYCODE_UNKNOWN;
				isAcceptGetIrCode = false;
			}
			else
			{
				isAcceptGetIrCode
= true;
			}
			break;
		case DVBConstants.GET_USB_STATUS:
			ret = AutoTestGetUSBStatus(out,data);
			break;
		case DVBConstants.GET_SDCARD_STATUS:
			ret = AutoTestGetSdcardStatus(out,data);
			break;
		case DVBConstants.READ_USB_FILE:
			ret = AutoTestReadUsbFile(out,data);
			break;
		case DVBConstants.SET_LED_LIGHT:
			ret = AutoTestSetLedLight(out,data);
			break;
			
		case DVBConstants.GET_WIFI_SIGNAL_RSSI:
			ret = AutoTestGetWifiSignalRssi(out,data);
			break;
			
		case DVBConstants.GET_WIFI_SIGNAL_PERCENT:
			ret = AutoTestGetWifiPercent(out,data);
			break;
			
		case (byte) DVBConstants.AUTO_TEST_FINISH:
			//editor.putInt("AutomatedTestCompletedFalg", 1);
			//editor.commit();
			try {
				data[10] = getCSData(data);
				out.write(data);
			} catch (IOException e) {
				e.printStackTrace();
				ret = false;
			}
			break;

		default:
			byte NotSupportCmd[]={0x30,(byte) 0xfe,0x0c,0,0,0,0,0,0,0,0,(byte)(0xfe)};
			try {
				out.write(NotSupportCmd);
			} catch (IOException e) {
				e.printStackTrace();
				ret = false;
			}
			break;
		}

		return ret;
	}

	private byte getCSData(byte []data)//计算cs值
	{
		return (byte) (0xff-(data[1]+data[2]+data[3]+data[4]+data[5]+data[6]+data[7]+data[8]+data[9])+1);
	}

	public void startAutoTest()
	{
		Thread autoThread = new Thread()
		{
			ServerSocket server;
			Socket socket;
			InputStream input;	
			String line = null;

			@Override
			public void run() {
			  try {
				  //创建服务端
					Log.d(TAG,"startAutoTest() start");
					server=new ServerSocket(AUTOTEST_PORT);
					socket = server.accept();
					socket.setSendBufferSize(BUFFER_SIZE);
					socket.setReceiveBufferSize(BUFFER_SIZE);
					mAutoTestOutput = socket.getOutputStream();  
					input = socket.getInputStream();	
					byte []factoryTestModeCmd={0x30,1,0x0c,0,0,0,0,0,0,0,0,(byte)(0xfe)};
					//byte factoryTestModeCmd[]={0x30,0x01,0x0c,0x01,0,0,0,0,0,0,0,(byte)(0xfe)};					
					factoryTestModeCmd[10] = getCSData(factoryTestModeCmd);
					mAutoTestOutput.write(factoryTestModeCmd);
					Log.d(TAG,"startAutoTest() start receive while ");	 
					boolean ret = true;
					while(ret)
					{
						byte[] data = new byte[BUFFER_SIZE]; 
						input.read(data);
						if (data[1] != 0)
						Log.d(TAG,"cmd = "+(int)data[1]);

						if(data[1] != DVBConstants.GET_IR_CODE)
						{
							isAcceptGetIrCode = false;
						}
						
						ret = executeinStruction(mAutoTestOutput,data);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();  
					mSocketErrResId = R.string.str_server_connection_failed;
					handler.sendEmptyMessage(SOCKET_ERR); 
					Log.d(TAG,"startAutoTest() UnknownHostException");
				} catch (IOException e) {
					e.printStackTrace();
					mSocketErrResId = R.string.str_io_exception_failed;
					handler.sendEmptyMessage(SOCKET_ERR);
					Log.d(TAG,"startAutoTest() IOException");
				}catch (SecurityException e) {
					e.printStackTrace();
					mSocketErrResId = R.string.str_server_permission_denies;
					handler.sendEmptyMessage(SOCKET_ERR);
					Log.d(TAG,"startAutoTest() SecurityException");
				}finally {	
					if (mAutoTestOutput != null)
					{
						try {
							mAutoTestOutput.close();
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}

					if (input != null)
					{
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}

					if (socket != null)
					{
						try {
							socket.close(); 
						} catch (IOException e) {
							e.printStackTrace();
						}										

					}
					if (server != null)
					{
						try {
							server.close(); 
						} catch (IOException e) {
							e.printStackTrace();
						}										

					}	

					isAcceptGetIrCode = false;
					Log.d(TAG, "startAutoTest() Socket close "+mIsRun);

				}
			}
		};
		autoThread.start();
	}


	//////////////////////////////////////////////////////////////////////////////////
	/////////// sn and mac checking
	//////////////////////////////////////////////////////////////////////////////////
	private TextView mCheckInfoTextView;
	private String mWifiIpStr = "";
	
	public void initCheckView()  
	{
		setContentView(R.layout.check_view_layout);
		mCheckInfoTextView = (TextView) this.findViewById(R.id.checkinfo_textview);
	}

	public void startCheckTest()
	{
		CheckThread checkThread = new CheckThread();
		checkThread.start(); 

		//open wifi for read wifi addr
		wifiAdmin = new WifiAdmin(ObbMountActivity.this);
		wifiAdmin.openWifi();
		
		mWifiIpStr = getWifiMacAddress(ObbMountActivity.this);
		Log.d(TAG, "startCheckTest() mWifiIpStr: "+mWifiIpStr);
	}

	private class CheckThread extends Thread 
	{  
		@Override  
		public void run() 
		{
			Socket socket = null;			
			OutputStream out = null;
			BufferedReader br = null;
			String line = null; 
			int success = 0;
			boolean failedOnce = false;
			boolean isConnected = false;

			try 
			{  
				GetSocketIpAndPort();
				//socket = new Socket(mSocketIpStr, mSocketPortId); 				
				for (int i=0; i<120 && mIsRun; i++) //wait 30s for network ok
				{
					try
					{
						socket = new Socket();
						socket.connect(new InetSocketAddress(mSocketIpStr, mSocketPortId), 10000);
					} catch (UnknownHostException e) {
						//e.printStackTrace(); 
						if (i > 0 && (i%10 == 0))
						{
							mSocketErrResId = R.string.str_server_connection_failed;
							handler.sendEmptyMessage(SOCKET_ERR);
						}
					} catch (IOException e) {
						//e.printStackTrace();		
						if (i > 0 && (i%10 == 0))
						{
							mSocketErrResId = R.string.str_io_exception_failed;
							handler.sendEmptyMessage(SOCKET_ERR); 
						}
					}

					if (socket.isConnected()) {
						isConnected = true;
						break;
					}
					
					if (isInterrupted()) {
						Log.e(TAG, "CheckThread() TCP socket connect interrupted ");
						return;
					}

					if (socket != null)
					{
						try {
							socket.close(); 
						} catch (IOException e) {
							e.printStackTrace();
						}										

					}	
					
					try {
						Thread.sleep(2000);
					} catch (Exception e){
						//TODO: handle exception
					}					
					Log.d(TAG, "CheckThread() Socket connect timeout "+System.currentTimeMillis() + " retry "+i);

				}

				if (socket != null && isConnected)
				{
					out = socket.getOutputStream(); 
					//send control char to server
					Log.d(TAG, "CheckThread() Socket send 0x1000 "+System.currentTimeMillis());

					if ((mCurrentSNStr != null && mCurrentSNStr.length() > 0)
						&& (mCurrentMACStr != null && mCurrentMACStr.length() > 0))
					{
						byte  b[]  =  {0x10, 0x00}; 
						out.write(b);  
						out.flush(); 

						try {
							Thread.sleep(1000);
						} catch (Exception e){
							//TODO: handle exception
						}

						//send sn
						int i, count;
						byte  sendbuf[] = new byte[32];
						sendbuf[0] = 0x10;
						sendbuf[1] = 0x01;
						sendbuf[2] = 12;
						count=3;
						for (i=0; i<mCurrentSNStr.length(); i++)
						{
							if (mCurrentSNStr.charAt(i) != ':')
							{
								sendbuf[count++] = (byte)mCurrentSNStr.charAt(i);
							}
						}
						sendbuf[count++]='\n';
						sendbuf[count++]=0;
						out.write(sendbuf);  
						out.flush();
						
						try {
							Thread.sleep(1000);
						} catch (Exception e){
							//TODO: handle exception
						}

						//send mac
						sendbuf[0] = 0x10;
						sendbuf[1] = 0x02;
						sendbuf[2] = 12;
						count=3;
						for (i=0; i<mCurrentMACStr.length(); i++)
						{
							if (mCurrentMACStr.charAt(i) != ':')
							{
								sendbuf[count++] = (byte)mCurrentMACStr.charAt(i);
							}
						}
						sendbuf[count++]='\n';
						sendbuf[count++]=0;
						out.write(sendbuf);  
						out.flush();	
						
						try {
							Thread.sleep(1000);
						} catch (Exception e){
							//TODO: handle exception
						}

						//send wifi mac
						sendbuf[0] = 0x10;
						sendbuf[1] = 0x03;
						sendbuf[2] = 12;
						count=3;
						for (i=0; i<mWifiIpStr.length(); i++)
						{
							if (mWifiIpStr.charAt(i) != ':')
							{
								sendbuf[count++] = (byte)mWifiIpStr.charAt(i);
							}
						}
						sendbuf[count++]='\n';
						sendbuf[count++]=0;
						out.write(sendbuf);  
						out.flush();						
					}
					else
					{

						byte  b[]  =  {0x30, 0x30}; 
						out.write(b); 
						out.flush();
						
						mCheckInfoTextView.setText(getString(R.string.str_mac_sn_not_write));
						mCheckInfoTextView.setBackgroundResource(R.color.red);
					}

					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
					while(mIsRun)
					{
						line = br.readLine();
						if (line != null)
						{
							Log.d(TAG, "CheckThread() Socket line = "+line+" length= "+line.length());
							if (line.charAt(0) == 0x20 && line.charAt(1) == 0x20) //success
							{
								handler.sendEmptyMessage(CHECK_SUCESS); 
								sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
								break;
							}
							else if (line.charAt(0) == 0x21 && line.charAt(1) == 0x21) //failed
							{
								handler.sendEmptyMessage(CHECK_FAILED); 
							}
						}
						else
						{
							Log.d(TAG, "CheckThread() Socket read timeout "+System.currentTimeMillis());

						}

						try {
							Thread.sleep(1000);
						} catch (Exception e){
							//TODO: handle exception
						}
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();  
				mSocketErrResId = R.string.str_server_connection_failed;
				handler.sendEmptyMessage(SOCKET_ERR); 
			} catch (IOException e) {
				e.printStackTrace();
				mSocketErrResId = R.string.str_io_exception_failed;
				handler.sendEmptyMessage(SOCKET_ERR);					
			}catch (SecurityException e) {
				e.printStackTrace();
				mSocketErrResId = R.string.str_server_permission_denies;
				handler.sendEmptyMessage(SOCKET_ERR);					
			}finally {		
				if (out != null)
				{
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}

				if (br != null)
				{
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}

				if (socket != null)
				{
					try {
						socket.close(); 
					} catch (IOException e) {
						e.printStackTrace();
					}										

				}
				Log.d(TAG, "CheckThread() Socket close "+System.currentTimeMillis());

			}
		}
	};

	public static String getWifiMacAddress(Context context)
	{
		try 
		{
			return loadFileAsString("/sys/class/net/wlan0/address").toUpperCase().substring(0, 17);
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
			return  "";
		}
	}

	public static String loadFileAsString(String filePath) throws java.io.IOException
	{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1)
		{
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}

	//////////////////////////////////////////////////////////////////////////////////
	/////////// sn and mac write
	//////////////////////////////////////////////////////////////////////////////////
	public void initWriteTestView()  
	{

		Log.d(TAG, "initWriteTestView ");


		setContentView(R.layout.write_view_layout);

		mFirmwareVersion = (TextView) this.findViewById(R.id.soft_version);
		mIP = (TextView) this.findViewById(R.id.ip);
		mMacBrunNote = (TextView) this.findViewById(R.id.mac_brun_note);		
		mCurrentModelTextView = (TextView) this.findViewById(R.id.current_model_textview);
		mCurrentHWTextView = (TextView) this.findViewById(R.id.current_hw_textview);
		mCurrentBuildDateTextView = (TextView) this.findViewById(R.id.current_builddate_textview);
		
		mCurrentSNTextView = (TextView) this.findViewById(R.id.current_sn_textview);
		mCurrentMACTextView = (TextView) this.findViewById(R.id.current_mac_textview);	

		mWriteMac = (TextView) this.findViewById(R.id.write);
		mSNWrite = (TextView) this.findViewById(R.id.sn_write);
		mHWWrite = (TextView) this.findViewById(R.id.hw_write);
		
		mSoftVersionStr = getVersion();
		if (mSoftVersionStr != null)
			mFirmwareVersion.setText(mSoftVersionStr);
		
		mLanIPAddr = getLocalIpAddress();
		if (mLanIPAddr != null)
			mIP.setText(mLanIPAddr);

		mCurrentModelTextView.setText(SystemProperties.get("ro.product.model", "")+"-"+SystemProperties.get("ro.platform.wifi.module", ""));
		mCurrentHWTextView.setText(SystemProperties.get("ro.product.board", ""));
		mCurrentBuildDateTextView.setText(SystemProperties.get("ro.build.date", ""));

		if (mCurrentSNStr != null)
			mCurrentSNTextView.setText(mCurrentSNStr);
		if (mCurrentMACStr != null)	
			mCurrentMACTextView.setText(mCurrentMACStr);	

	}

	public void startWriteTest()
	{
		//threadSocket= new SocketThread();
		//threadSocket.start();	

	}

}
