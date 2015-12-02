package com.videostrong.factorytest;
import android.util.Log;
import com.videostrong.factorytest.ObbMountActivity;

public class ValidCheckUtils {
	public static boolean isMacAddressCorrect(String mac) {
		boolean isMacOk = false;
		if (null != mac && mac.length() == 12) {
			String secondByte = mac.substring(1, 2);
			Log.d(null, secondByte);
			if (secondByte.equals("0") || secondByte.equals("2") || secondByte.equals("4") || secondByte.equals("6") || secondByte.equals("8")
					|| secondByte.equals("A") || secondByte.equals("a") || secondByte.equals("C") || secondByte.equals("c") || secondByte.equals("E") 
					|| secondByte.equals("e")) {
				isMacOk = true;
				for (int index=0; index<mac.length(); index++) {
					int byteFlag = mac.charAt(index);
					if ((byteFlag < '0' || byteFlag > '9') && (byteFlag < 'A' || byteFlag > 'F')) {
						isMacOk = false;
						break;
					}
				}
			}
		}
		return isMacOk;
	}
	
	public static boolean isStbIdCorrect(String stbid) {
		boolean ret = false;
		
		if (Platform.AMLOGIC_CHT == Platform.CURRENT_PLATFORM){
			Log.e(null, "stbid = " + stbid);
			if (stbid != null && (stbid.length() == ObbMountActivity.STB_ID_LENGHT_NORMAL) && stbid.substring(0, 2).equals(ObbMountActivity.SN_FRIST_TWO_BYTE_CHT)) {
				return true;
			}
		}else if(Platform.AMLOGIC_S805_CHT == Platform.CURRENT_PLATFORM){
			Log.e(null, "stbid = " + stbid);
			if (stbid != null && (stbid.length() == ObbMountActivity.STB_ID_LENGHT_NORMAL)) {
				return true;
			}
		}
		else{
			if (stbid != null &&  stbid.substring(0, 2).equals(ObbMountActivity.STB_ID_FRIST_TWO_BYTE)) {
				ret = true;
			}
		}
		
		return ret;
	}
}
