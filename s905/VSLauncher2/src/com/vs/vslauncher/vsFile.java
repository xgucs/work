package com.vs.vslauncher;


public class vsFile
{
	int source_id;  			//icon source id
	String		mPackageName;	//apk package name
	String		mActivityName;	//apk activity name
	String		mName;			//title name

	/* 文件的图标ICNO */
	private int		mIcon;
	/* 能否选中 */
	private boolean	mSelectable	= true;
	
	public vsFile(int id, String text, String size, String path)
	{
		source_id = id;
		mPackageName = text;
		mActivityName = size;
		mName = path;
	}
	
	//是否可以选中
	public boolean isSelectable()
	{
		return mSelectable;
	}
	//设置是否可用选中
	public void setSelectable(boolean selectable)
	{
		mSelectable = selectable;
	}
	public String getPackageName()
	{
		return mPackageName;
	}
	
	public void getPackageName(String name)
	{
		mPackageName = name;
	}
	//得到文件名
	public void setActivityName(String name)
	{
		mActivityName = name;
	}
	
	//得到文件名
	public String getActivityName()
	{
		return mActivityName;
	}
	public String getName()
	{
		return mName;
	}
	//设置文件名
	public void setName(String text)
	{
		mName = text;
	}
	//设置图标
	public void setIcon(int icon)
	{
		source_id = icon;
	}
	//得到图标
	public int getIcon()
	{
		return source_id;
	}
	//比较文件名是否相同
	public int compareTo(vsFile other)
	{
		if (this.mPackageName != null)
			return this.mPackageName.compareTo(other.getPackageName());
		else
			throw new IllegalArgumentException();
	}
}
