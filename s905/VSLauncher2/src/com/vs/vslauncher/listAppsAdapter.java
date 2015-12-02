package com.vs.vslauncher;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//使用BaseAdapter来存储取得的文件
public class listAppsAdapter extends BaseAdapter
{
	private Context				mContext	= null;
	// 用于显示文件的列表
	private List<vsFile>	mItems		= new ArrayList<vsFile>();
	
	private LayoutInflater 		mInflater	= null;
	private static boolean visflag = false;
	private static boolean Allvisflag = false;
	
	private int selectIndex = -1;
	
	public listAppsAdapter(Context context)
	{
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setvisflag(boolean bvisflag){
		visflag = bvisflag;
	}
	
	public void setAllvisflag(boolean bvisflag){
		Allvisflag = bvisflag;
	}
	
	 public void setSelectIndex(int i){  
	        selectIndex = i;  
	    }  
	 
	//添加一项（一个文件）
	public void addItem(vsFile it) { mItems.add(it); }
	
	//设置文件列表
	public void setListItems(List<vsFile> lit) { mItems = lit; }
	
	//得到文件的数目,列表的个数
	public int getCount() { return mItems.size(); }
	
	//得到一个文件
	public Object getItem(int position) { return mItems.get(position); }
	
	//能否全部选中
	public boolean areAllItemsSelectable() { return false; }
	
	//判断指定文件是否被选中
	public boolean isSelectable(int position) 
	{ 
		//return mItems.get(position).isSelectable();	
		return false;
	}
	
	//得到一个文件的ID
	public long getItemId(int position) { return position; }
	
	//重写getView方法来返回一个IconifiedTextView（我们自定义的文件布局）对象
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null ;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_imagin, null);
			holder.icon = (ImageView)convertView.findViewById(R.id.ImageView01);
			convertView.setTag(holder);
		} 
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		vsFile mIconifiedText = mItems.get(position);
		

		holder.icon.setBackgroundResource(mIconifiedText.source_id);
		
		if (position == selectIndex){
			convertView.setSelected(true);
		}
		else{
			convertView.setSelected(false);
		}
		
		return convertView;
	}
	
	class ViewHolder    
    {    
        TextView file;
        ImageView icon;
    }
}

