package com.videostrong.commonutilits;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.Log;
import android.os.SystemProperties;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import java.io.OutputStream;
import android.app.SystemWriteManager;
/**
 * @author Jesse
 *
 */
public class CommonUtilits {
	private static final String TAG = "Factory.CommonUtilits";

	public static void chmodFilePermision(String filePath, int mode) {
		if (null != filePath) {
			try {
				String command = "chmod " + mode + " " + filePath;
				Runtime runtime = Runtime.getRuntime();
				Process proc = runtime.exec(command);
			} catch (IOException e) {
				Log.i(TAG, "chmod file permision fail!");
				e.printStackTrace();
			}
		}
	}

	public static void syncCmd() {
		try {
			String command = "sync";
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(command);
			//Log.i(TAG, "syncCmd() ");
		} catch (IOException e) {
			Log.i(TAG, "sync fail!");
			e.printStackTrace();
		}
	}

	public static String readFile(String file) {
		String content = "";
		CommonUtilits.chmodFilePermision(file, 777);
		File OutputFile = new File(file);
		if (!OutputFile.exists()) {
			return content;
		}

		try {
			InputStream instream = new FileInputStream(file);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				Log.i(TAG, "readFile() buffreader=" + buffreader.toString());
				String line;
				while ((line = buffreader.readLine()) != null) {
					content += line;
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.d(TAG, "The File doesn't not exist.");
		} catch (IOException e) {
			Log.i(TAG, "GOTECH func readFile error!");
			Log.d(TAG, e.getMessage());
		}
		//CommonUtilits.chmodFilePermision(file, 666);
		Log.i(TAG, "readFile() content=" + content);
		return content;
	}

	public static boolean writeFile(String file, String value) {
		boolean ret = true;
		File OutputFile = new File(file);
		if (!OutputFile.exists()) {
			Log.d(TAG, "--------writeFile() exit-----------");
			return false;
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile), 32);
			try {
				if (!file.contains("led_test"))
					Log.d(TAG, "writeFile() " + file + ": " + value);
				
				out.write(value);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IOException when write " + OutputFile);
			e.printStackTrace();
			ret = false;
		}

		return ret;
	}

	public static boolean writeLed(String file, String value) {
		boolean ret = true;
		File OutputFile = new File(file);
		if (!OutputFile.exists()) {
			Log.d(TAG, "--------writeFile() exit-----------");
			return false;
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile), 32);
			try {
				if (file.contains("led_test"))
					Log.d(TAG, "writeFile() " + file + ": " + value);
				
				out.write(value);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "IOException when write " + OutputFile);
			e.printStackTrace();
			ret = false;
		}

		return ret;
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
	public static String readSysFile(SystemWriteManager sw, String path) {
		if (sw == null) {
			return null;
		}

		if (path == null) {
			return null;
		}

		return sw.readSysfs(path);

	}
}
