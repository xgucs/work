package com.videostrong.mac.factorytest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
/**
 * @author Jesse 
 *	write this class for gotech all amlogic platform launcher factory test line using.
 *	such as ott\dvb\iptv all can use this class.
 */
public class JudgeMacAndStartFactoryTest {
	private static String TAG = "JudgeMacAndStartFactoryTest";
	private static boolean mDebug = false;
	
	public static void function(Context context)
	{
		String retVal = null;
		
		processCommandKeyName();
		retVal = readFile("/sys/class/aml_keys/aml_keys/key_read");
		if (retVal == null || retVal.length() <= 0 || "".equals(retVal))
		{
			if (mDebug){
				Toast.makeText(context, "MAC NO!", Toast.LENGTH_LONG).show();
			}
			
			Intent intent = new Intent();
            try{
                intent.setComponent(new ComponentName("com.videostrong.factorytest", "com.videostrong.factorytest.ObbMountActivity"));
                context.startActivity(intent);
            }catch(Exception e){
                intent.setComponent(new ComponentName("com.videostrong.factorytest", "com.videostrong.factorytest.ObbMountActivity"));
                context.startActivity(intent);
            }
		}
		else if (mDebug)
		{
			Toast.makeText(context, "MAC OK!", Toast.LENGTH_LONG).show();
		}
	}
	
	private static void processCommandKeyName()
	{
		chmodFilePermision("/sys/class/aml_keys/aml_keys/key_name", 777);
		
		try {
			writeFile("/sys/class/aml_keys/aml_keys/key_name","mac");  
		}catch (Exception e) {
			Log.i(TAG, "GOTECH func processCommandKeyName error!");
	    	e.printStackTrace();
		}
		
		chmodFilePermision("/sys/class/aml_keys/aml_keys/key_name", 755);
	}
	
    public static void writeFile(String file, String value){
		File OutputFile = new File(file);
		if(!OutputFile.exists()) { 
			Log.d(TAG, "--------writeFile exit-----------");
        	return ;
        }
    	try {
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile), 32);
    		try {
				Log.d(TAG, "set" + file + ": " + value);
    			out.write(value);    
    		} 
			finally {
				out.close();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IOException when write "+OutputFile);
		}
	} 
	
	 public static String readFile(String file)
	 {
		 String content = "";
		 chmodFilePermision(file, 777);
		 File OutputFile = new File(file);
		 if(!OutputFile.exists()) 
		 {    
			 return content;    
		 }
		 
		 try 
		 {
			 InputStream instream = new FileInputStream(file);
			 if (instream != null)
			 {
				 InputStreamReader inputreader = new InputStreamReader(instream);
				 BufferedReader buffreader = new BufferedReader(inputreader);
				 Log.i(TAG, "GOTECH func buffreader="+buffreader.toString());
				 String line;
				 while (( line = buffreader.readLine()) != null)
				 {
					 content += line ;
				 }                
				 instream.close();
			 }
		 }
		 catch (java.io.FileNotFoundException e)
		 {
			 Log.d("TestFile", "The File doesn't not exist.");
		 }
		 catch (IOException e)
		 {
			 Log.i(TAG, "GOTECH func readFile error!");
			 Log.d("TestFile", e.getMessage());
        }
		 chmodFilePermision(file, 755);
		 return content;    
	 } 
	 
		private static void chmodFilePermision(String filePath, int mode)
		{
			if (null != filePath)
			{
				try {
					String command = "chmod "+ mode +" "+ filePath;
					Runtime runtime = Runtime.getRuntime();
					Process proc = runtime.exec(command);
				} catch (IOException e) {
					// TODO: handle exception
					Log.i(TAG, "chmod file permision fail!");
					e.printStackTrace();
				}
			}
		}
}
