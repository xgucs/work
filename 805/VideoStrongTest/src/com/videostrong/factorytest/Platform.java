package com.videostrong.factorytest;
/**
 * @author Jesse
 *
 */
public interface Platform {
	//--------------platform-------------------
	public static final int AMLOGIC_S805_BARCODE_32 = 0;
	public static final int AMLOGIC_S805_BARCODE_12 = 1;
	public static final int AMLOGIC_MX_COMMON = 2;
	public static final int AMLOGIC_S805_STB_ID_MAC_SEPARAGE = 3;
	public static final int AMLOGIC_CHT = 4;
	public static final int AMLOGIC_S805_CHT = 5;
	
	//-----------current platfrom--------------
	public static final int CURRENT_PLATFORM = AMLOGIC_S805_BARCODE_12;
}
