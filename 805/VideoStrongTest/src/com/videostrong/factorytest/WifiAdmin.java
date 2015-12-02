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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.net.wifi.ScanResult;
/**
 * This class provides a basic demonstration of how to manage an OBB file. It
 * provides two buttons: one to mount an OBB and another to unmount an OBB. The
 * main feature is that it implements an OnObbStateChangeListener which updates
 * some text fields with relevant information.
 */
 public class WifiAdmin {  
		    // 定义WifiManager对象   
		    private WifiManager mWifiManager;  
		    // 定义WifiInfo对象   
		    private WifiInfo mWifiInfo;  
		    // 扫描出的网络连接列表   
		    private List<ScanResult> mWifiList;  
		    // 网络连接列表   
		    private List<WifiConfiguration> mWifiConfiguration;  

		    WifiLock mWifiLock;  
		 
		  
		    public WifiAdmin(Context context) {  
		        // 取得WifiManager对象   
		        mWifiManager = (WifiManager) context  
		                .getSystemService(Context.WIFI_SERVICE);  
		        // 取得WifiInfo对象   
		        mWifiInfo = mWifiManager.getConnectionInfo();  
		    }  
		  
		    // 打开WIFI   
		    public void openWifi() {  
				int wifiApState = mWifiManager.getWifiApState();					
				if ((wifiApState == WifiManager.WIFI_AP_STATE_ENABLING) ||(wifiApState == WifiManager.WIFI_AP_STATE_ENABLED)) 
				{						
					mWifiManager.setWifiApEnabled(null, false);
				}				
				
		        if (!mWifiManager.isWifiEnabled()) {  
		            mWifiManager.setWifiEnabled(true);  
		        }  
		    }  
		  
		    // 关闭WIFI   
		    public void closeWifi() {  
		        if (mWifiManager.isWifiEnabled()) {  
		            mWifiManager.setWifiEnabled(false);  
		        }  
		    }  
		  
		    public int checkState() {  
		        return mWifiManager.getWifiState();  
		    }  
		  
		    // 锁定WifiLock   
		    public void acquireWifiLock() {  
		        mWifiLock.acquire();  
		    }  
		  
		    // 解锁WifiLock   
		    public void releaseWifiLock() {  
		        if (mWifiLock.isHeld()) {  
		            mWifiLock.acquire();  
		        }  
		    }  
		  
		    public void creatWifiLock() {  
		        mWifiLock = mWifiManager.createWifiLock("Test");  
		    }  
		  
		    // 得到配置好的网络   
		    public List<WifiConfiguration> getConfiguration() {  
		        return mWifiConfiguration;  
		    }  
		  
		    // 指定配置好的网络进行连接   
		    public void connectConfiguration(int index) {  
		        // 索引大于配置好的网络索引返回   
		        if (index > mWifiConfiguration.size()) {  
		            return;  
		        }  
		        // 连接配置好的指定ID的网�?  
		        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,  
		                true);  
		    }  
		  
		    public void startScan() {  
		        mWifiManager.startScan();  
		        // 得到扫描结果   
		        mWifiList = mWifiManager.getScanResults();  
		        // 得到配置好的网络连接   
		        mWifiConfiguration = mWifiManager.getConfiguredNetworks();  
		    }  
		  
		    // 得到网络列表   
		    public List<ScanResult> getWifiList() {  
		        return mWifiList;  
		    }  
		  
		    // 查看扫描结果   
		    public StringBuilder lookUpScan() {  
		        StringBuilder stringBuilder = new StringBuilder();  
		        for (int i = 0; i < mWifiList.size(); i++) {  
		            stringBuilder  
		                    .append("Index_" + new Integer(i + 1).toString() + ":");  
		            // 将ScanResult信息转换成一个
		            // 其中把包括：BSSID、SSID、capabilities、frequency、level   
		            stringBuilder.append((mWifiList.get(i)).toString());  
		            stringBuilder.append("/n");  
		        }  
		        return stringBuilder;  
		    } 

		      public int getRssi()
		    {
		    	 mWifiInfo=mWifiManager.getConnectionInfo();
		    	
		    	return mWifiInfo.getRssi();
		    }
			  
		    // 得到MAC地址   
		    public String getMacAddress() {  
		        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();  
		    }  
		  
		    // 得到接入点的BSSID   
		    public String getBSSID() {  
		        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();  
		    }  
		  
		    // 得到IP地址   
		    public int getIPAddress() {  
		        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();  
		    }  
		  
		    // 得到连接的ID   
		    public int getNetworkId() {  
		        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();  
		    }  
		  
		    // 得到WifiInfo的所有信息包   
		    public String getWifiInfo() {  
		        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();  
		    }   
		    
		    public int addNetwork(WifiConfiguration wcg) {  
		    	int wcgID = mWifiManager.addNetwork(wcg);  
		     boolean b =  mWifiManager.enableNetwork(wcgID, true);  
		     
		     
		     System.out.println("a--" + wcgID); 
		     System.out.println("b--" + b); 
		     return wcgID;
		    }  
		    
		    public int getConfiguredNetworksStatus(int wcgID)
		    {
		    	return mWifiManager.getConfiguredNetworks().get(wcgID).status;
		    }
		  
		    public void disconnectWifi(int netId) {  
		        mWifiManager.disableNetwork(netId);  
		        mWifiManager.disconnect();  
		    }  
		  
		//然后是一个实际应用方法，只验证过没有密码的情况： 
		  
		    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)  
		    {  
		          WifiConfiguration config = new WifiConfiguration();    
		           config.allowedAuthAlgorithms.clear();  
		           config.allowedGroupCiphers.clear();  
		           config.allowedKeyManagement.clear();  
		           config.allowedPairwiseCiphers.clear();  
		           config.allowedProtocols.clear();  
		          config.SSID = "\"" + SSID + "\"";    
		           
		          WifiConfiguration tempConfig = this.IsExsits(SSID);            
		          if(tempConfig != null) {   
		              mWifiManager.removeNetwork(tempConfig.networkId);   
		          } 
		           
		          if(Type == 1) //WIFICIPHER_NOPASS 
		          {  
		              // config.wepKeys[0] = "";  
		               config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
		              // config.wepTxKeyIndex = 0;  
		          }  
		          if(Type == 2) //WIFICIPHER_WEP 
		          {  
		              config.hiddenSSID = true; 
		              config.wepKeys[0]= "\""+Password+"\"";  
		              config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);  
		              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);  
		              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);  
		              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);  
		              config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);  
		              config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  
		              config.wepTxKeyIndex = 0;  
		          }  
		          if(Type == 3) //WIFICIPHER_WPA 
		          {  
		          config.preSharedKey = "\""+Password+"\"";  
		          config.hiddenSSID = true;    
		          config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
		          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);                          
		          config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
		          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
		          //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
		          config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP); 
		          config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
		          config.status = WifiConfiguration.Status.ENABLED;    
		          } 
		           return config;  
		    }  
		    
		    
		  //  wifi_manager.getConfiguredNetworks().get(wifi_configID).status; //huanghanjing
		     
		    private WifiConfiguration IsExsits(String SSID)   
		    {   
		        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();  

				   if(existingConfigs == null )
				   	return null;
		           for (WifiConfiguration existingConfig : existingConfigs)    
		           {   
		             if (existingConfig.SSID.equals("\""+SSID+"\""))   
		             {   
		                 return existingConfig;   
		             }   
		           }   
		        return null;    
		    } 
		   
		} 

