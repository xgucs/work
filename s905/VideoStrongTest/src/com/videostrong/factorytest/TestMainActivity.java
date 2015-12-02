package com.videostrong.factorytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.conn.util.InetAddressUtils;
import com.android.internal.util.ArrayUtils;
import  com.videostrong.factorytest.LocalAdapter;
import android.widget.BaseAdapter;

import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;
import android.os.SystemProperties;
import com.videostrong.commonutilits.CommonUtilits;
import android.net.ConnectivityManager;
import android.net.LinkProperties;

public class TestMainActivity extends Activity implements OnClickListener {
	private boolean isAudio = false;
	private VideoView mVideoView;
	private Button bt_video;
	private Button bt_audio;
	private Button bt_wifi_set;
	private Button bt_mac;
	private Button bt_burn;
	private String mUri;
     
	private TextView mFirmwareVersion;
	private TextView mCurrentModelTextView;
	private TextView mCurrentBuildDateTextView;
	private TextView internal_size;
	private TextView extro_size;
	private TextView tf_size;
	/* tablerow */
	private TextView txv_net_mac;
	private TextView txv_net_ip;
	private TextView txv_wifi_mac;
	private TextView txv_wifi_ip;
	private Button bluetooth;

	private String wifi_mac;
	private String wifi_ip;
	private ConnectivityManager mCM;
	private WifiManager mWifiManager;
	private String mSoftVersionStr;
	private IntentFilter wifiFilter;
	private boolean isRun = true;
	private Thread mThreadLAN;
	private Handler mHandler;
	private static final int LAN_UP=1;
	private static final int LAN_DOWN=0;
	private final String STORAGE_PATH ="/storage";
    private final String SDCARD_FILE_NAME ="sdcard";
    private final String UDISK_FILE_NAME ="udisk";
    private GridView lv_status;
	private static final int EVENT_UPDATE_CONNECTIVITY = 600;
    private static final String[] CONNECTIVITY_INTENTS = {
         ConnectivityManager.CONNECTIVITY_ACTION_IMMEDIATE,
         WifiManager.LINK_CONFIGURATION_CHANGED_ACTION,
         WifiManager.NETWORK_STATE_CHANGED_ACTION,
 };
		
    private static class MyHandler extends Handler {
        private WeakReference<TestMainActivity> mStatus;

        public MyHandler(TestMainActivity activity) {
            mStatus = new WeakReference<TestMainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
        	TestMainActivity status = mStatus.get();
            if (status == null) {
                return;
            }

            switch (msg.what) {              

                case EVENT_UPDATE_CONNECTIVITY:
                    status.updateConnectivity();
                    break;
            }
        }
    }

    void updateConnectivity() {
      
        setIpAddressStatus();
        displayStatus();
        updateStatus();
    }
    private void displayStatus() {
        LocalAdapter ad = new LocalAdapter(TestMainActivity.this,
                getStatusData(getWifiLevel(), isEthernetOn()),
                R.layout.homelist_item,
                new String[] {"item_type", "item_name", "item_sel"},
                new int[] {R.id.item_type, 0, 0});
        lv_status.setAdapter(ad);
    }
    public  List<Map<String, Object>> getStatusData(int wifi_level, boolean is_ethernet_on) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        if (wifi_level != -1) {
            switch (wifi_level + 1) {
                //case 0:
                //	map.put("item_type", R.drawable.wifi1);
                //	break;
                case 1:
                    map.put("item_type", R.drawable.wifi2);
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

        if (isExternelStorageExists(SDCARD_FILE_NAME) == true) {
            map = new HashMap<String, Object>();
            map.put("item_type", R.drawable.img_status_sdcard);
            list.add(map);
        }

        if (isExternelStorageExists(UDISK_FILE_NAME) == true) {
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

    private int getWifiLevel(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
            WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
            int wifi_rssi = mWifiInfo.getRssi();

            return WifiManager.calculateSignalLevel(wifi_rssi, 4);
        } else {
            return -1;
        }
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
    private void setIpAddressStatus() {
        String ipAddress = getDefaultIpAddresses(this.mCM);
		String status=getEthernetDevStatus("/sys/class/net/eth0/operstate");
        if (ipAddress != null && status!=null && status.equals("up")) {
        	txv_net_ip.setText(ipAddress);
        } else {
            txv_net_ip.setText("");
        }
    }
    public  String getDefaultIpAddresses(ConnectivityManager cm) {
        LinkProperties prop = cm.getActiveLinkProperties();
        return formatIpAddresses(prop);
    }
   private  String formatIpAddresses(LinkProperties prop) {
        if (prop == null) return null;
        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
        if (!iter.hasNext()) return null;
        String addresses = "";
        while (iter.hasNext()) {
            addresses += iter.next().getHostAddress();
            if (iter.hasNext()) addresses += "\n";
        }
        return addresses;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_main);
		CommonUtilits.send_led_msg("88:88");
		CommonUtilits.send_led_msg("dotall1");
		checkExists();
		 mCM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		  mConnectivityIntentFilter = new IntentFilter();
		  for (String intent: CONNECTIVITY_INTENTS) {
             mConnectivityIntentFilter.addAction(intent);
        }
        mHandler = new MyHandler(this);
		initView();
		playVideoView();
		mediaFilter=new IntentFilter();
		mediaFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		mediaFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		mediaFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mediaFilter.addDataScheme("file");
		wifiFilter = new IntentFilter();
		wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);



	}	

	private void checkExists(){
      File f1=new File("/sys/devices/platform/aml_pm_m8/led_test");
      File f2=new File("/sys/devices/aml_pm/led_test");
      if(!f1.exists() && !f2.exists()){
      	  TestMainActivity.this.finish();
      }
	}

	public boolean isExternelStorageExists(String media_name){
        File storage_file = new File(STORAGE_PATH);

        if (storage_file.exists() && storage_file.isDirectory()) {
            if (storage_file.listFiles() != null &&  storage_file.listFiles().length > 0) {
                for (File file : storage_file.listFiles()) {
                    String path = file.getAbsolutePath();
                    if (path.startsWith(STORAGE_PATH + "/" + media_name)
                        && Environment.getExternalStorageState(file).equals(Environment.MEDIA_MOUNTED)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

	public void playVideoView() {
		if (isAudio) {
			mUri = "android.resource://" + getPackageName() + "/"
					+ R.raw.vstrong;
		} else {
			mUri = "android.resource://" + getPackageName() + "/"
					+ R.raw.testvideo;
		}
		mVideoView.setVideoURI(Uri.parse(mUri));

		mVideoView.start();
		CommonUtilits.writeFile(
				"/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor",
				"performance");

		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mp.setLooping(true);
			}
		});

		mVideoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mVideoView.setVideoURI(Uri.parse(mUri));
						mVideoView.start();
					}
				});
	}

	private void initView() {
		bt_video = (Button) findViewById(R.id.bt_video);
		bt_video.setOnClickListener(this);
		bt_mac=(Button)findViewById(R.id.bt_mac);
		bt_mac.setOnClickListener(this);
		bt_audio = (Button) findViewById(R.id.bt_audio);
		bt_audio.setOnClickListener(this);
		bt_wifi_set = (Button) findViewById(R.id.bt_wifi_set);
		bt_wifi_set.setOnClickListener(this);
		bt_burn = (Button) findViewById(R.id.bt_burn);
		bt_burn.setOnClickListener(this);
		lv_status = (GridView)findViewById(R.id.list_status);
		mVideoView = (VideoView) findViewById(R.id.videoView);

		/* net test */
		txv_net_mac = (TextView) findViewById(R.id.net_mac);
		txv_net_ip = (TextView) findViewById(R.id.net_ip);
		txv_wifi_mac = (TextView) findViewById(R.id.wifi_mac);
		txv_wifi_ip = (TextView) findViewById(R.id.wifi_ip);
		bluetooth = (Button) findViewById(R.id.bt_bluetooth);
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH)) {
			bluetooth.setVisibility(View.VISIBLE);
		    bluetooth.setOnClickListener(this);
		}        
		mCurrentModelTextView = (TextView) this
				.findViewById(R.id.current_model_textview);
		mCurrentBuildDateTextView = (TextView) this
				.findViewById(R.id.current_builddate_textview);
		mFirmwareVersion = (TextView) this.findViewById(R.id.soft_version);
		mSoftVersionStr = getVersion();
		if (mSoftVersionStr != null)
			mFirmwareVersion.setText(mSoftVersionStr);
		mCurrentModelTextView
				.setText(SystemProperties.get("ro.product.model", "") + "-"
						+ SystemProperties.get("ro.platform.wifi.module", ""));
		mCurrentBuildDateTextView.setText(SystemProperties.get("ro.build.date",
				""));
	}

	private void initWifiView() {
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = mWifiManager.getConnectionInfo();
		if (info != null) {
			wifi_mac = info.getMacAddress();
			wifi_ip = String.valueOf(info.getIpAddress());
			if (wifi_mac != null) {
				txv_wifi_mac.setText(wifi_mac);
				txv_wifi_ip.setText(Formatter.formatIpAddress(Integer
						.valueOf(wifi_ip)));
			}
		}

	}

	private void initEthernetView() {

		String net = getLocalIpAddress();
		txv_net_ip.setText(net);
		txv_net_mac.setText(getMacAddress().toLowerCase());
	}
	
	private IntentFilter mediaFilter;
	private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
	        @Override
	            public void onReceive(Context context, Intent intent) {
	                String action = intent.getAction();
	                if (action == null)
	                    return;

	                if (Intent.ACTION_MEDIA_EJECT.equals(action)
	                        || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {	
	                	  displayStatus();
                          updateStatus();
	                      
	                }else if(Intent.ACTION_MEDIA_MOUNTED.equals(action)){
	               
                            
                       displayStatus();
                       updateStatus();
	                }
	            }
	    };
	    
	  private void updateStatus() {
        ((BaseAdapter) lv_status.getAdapter()).notifyDataSetChanged();
    }  

    private IntentFilter mConnectivityIntentFilter;
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ArrayUtils.contains(CONNECTIVITY_INTENTS, action)) {
                mHandler.sendEmptyMessage(EVENT_UPDATE_CONNECTIVITY);
            }
        }
    };


	private String getLocalIpAddress() {
		try {
			String ipv4;
			List netlist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			for (int index = 0; index < netlist.size(); index++) {
				List ialist = Collections.list(((NetworkInterface) netlist
						.get(index)).getInetAddresses());
				for (int count = 0; count < ialist.size(); count++) {
					if (!((InetAddress) ialist.get(count)).isLoopbackAddress()
							&& InetAddressUtils
									.isIPv4Address(ipv4 = ((InetAddress) ialist
											.get(count)).getHostAddress())) {
						return ipv4;
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	private String getEthernetDevStatus(String eth0_parm) {
		String mEhernet_dev_status = null;
		FileReader fileReader = null;

		try {
			fileReader = new FileReader(eth0_parm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(fileReader);
		try {
			mEhernet_dev_status = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mEhernet_dev_status;
	}

	public String getMacAddress() {
		String result = "";
		String Mac = "";
		result = callCmd("busybox ifconfig", "HWaddr");

		if (result == null) {
			return "没有读取到MAC";
		}

		// ???磺eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
		if (result.length() > 0 && result.contains("HWaddr") == true) {
			Mac = result.substring(result.indexOf("HWaddr") + 6,
					result.length() - 1);
			Log.i("test", "Mac:" + Mac + " Mac.length: " + Mac.length());

			if (Mac.length() > 1) {
				Mac = Mac.replaceAll(" ", "");
				result = "";
				String[] tmp = Mac.split(":");
				for (int i = 0; i < tmp.length; ++i) {
					result += tmp[i];
				}
			}
			Log.i("test", result + " result.length: " + result.length());
		}
		return Mac.trim();
	}

	public String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			while ((line = br.readLine()) != null
					&& line.contains(filter) == false) {
				Log.i("test", "line: " + line);
			}

			result = line;
			Log.i("test", "result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	

	public static String getVersion() {
		String ware = SystemProperties.get("ro.build.display.id", "");
		return ware;
	}

	@Override
	protected void onResume() {
		if(isExternelStorageExists(SDCARD_FILE_NAME) == true){
             
		}
		mVideoView = (VideoView) findViewById(R.id.videoView);
		playVideoView();
		initWifiView();
		 displayStatus();
         updateStatus();
		txv_net_mac.setText(getMacAddress().toLowerCase());
	//	initEthernetView();
		   updateConnectivity();
       registerReceiver(mConnectivityReceiver, mConnectivityIntentFilter,
                      android.Manifest.permission.CHANGE_NETWORK_STATE, null);
       this.registerReceiver(mediaReceiver, mediaFilter);
		this.registerReceiver(mWifiReceiver, wifiFilter);
		super.onResume();
	}

	@Override
	protected void onStop() {
		if (mVideoView != null) {
			mVideoView.stopPlayback();
			mVideoView = null;
		}
		this.unregisterReceiver(mConnectivityReceiver);
		this.unregisterReceiver(mWifiReceiver);
		this.unregisterReceiver(mediaReceiver);
		
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_PROG_BLUE) {
			System.out.println("========KEYCODE_PROG_BLUE===========");
			Intent msIntent=new Intent(TestMainActivity.this, ObbMountActivity.class);
			this.startActivity(msIntent);
		}
		else if(keyCode==KeyEvent.KEYCODE_MENU){
               Toast.makeText(this, "Recovery键被按下！", Toast.LENGTH_LONG).show();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		isRun = false;
		super.onDestroy();
	}

	private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

			} else if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				NetworkInfo info = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
					txv_wifi_mac.setText("WiFi断开");
					txv_wifi_ip.setText("请检查WiFi连接");
					displayStatus();
				} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					initView();
				}
				 displayStatus();
                 updateStatus();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_video:
			if (mVideoView != null && isAudio) {
				mVideoView.stopPlayback();
				isAudio = false;
			    playVideoView();
			}		
			break;
		case R.id.bt_audio:
			if (mVideoView != null && !isAudio) {
				mVideoView.stopPlayback();
				isAudio = true;
			    playVideoView();
			}		
		
			break;
		case R.id.bt_wifi_set:
			if (mVideoView != null) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			ComponentName cn=new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
			Intent inte =new Intent();
			inte.setComponent(cn);
			this.startActivity(inte);
			break;
		case R.id.bt_burn:
			if (mVideoView != null ) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			Intent mntent = new Intent(TestMainActivity.this,
					VideoBurning.class);
			startActivityForResult(mntent, 0);
			break;
		case R.id.bt_mac:
			if (mVideoView != null) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			Intent qqtent = new Intent(TestMainActivity.this,
					ObbMountActivity.class);
			startActivityForResult(qqtent, 0);
			
			break;
		case R.id.bt_bluetooth:
			if (mVideoView != null) {
				mVideoView.stopPlayback();
				mVideoView = null;
			}
			ComponentName bluetooth = new ComponentName("com.android.settings",
					"com.android.settings.bluetooth.BluetoothSettings");
			Intent intes = new Intent();
			intes.setComponent(bluetooth);
			this.startActivity(intes);
			break;
		default:
			break;
		}
	}
}
