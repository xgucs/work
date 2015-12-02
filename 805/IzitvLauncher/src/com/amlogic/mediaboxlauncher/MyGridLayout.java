
package com.amlogic.mediaboxlauncher;

import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

public class MyGridLayout extends GridLayout{
    private final static String TAG="MyGridLayout";
    private Context mContext;

    public MyGridLayout(Context context){
        super(context); 
    }
    
    public MyGridLayout(Context context, AttributeSet attrs){
        super(context, attrs); 
        mContext = context;
    }
    
    public MyGridLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle); 
    } 

    @Override  
    public void onDraw(Canvas canvas) {  
       super.onDraw(canvas);  
    }  

    public void setLayoutView(List<Map<String, Object>> list, int flag){
           int count=0;
    	
   
        if (this.getChildCount() > 0)
            this.removeAllViews();
   
        for (Map<String, Object> m : list) {
           
              count++;
            ViewGroup view; 
            if (flag==0) {
				
            	view = (ViewGroup)View.inflate(mContext,R.layout.homegrid_item, null);        
			}else {
				view = (ViewGroup)View.inflate(mContext,R.layout.my_app_item, null);  
			}
                  
          
            ImageView img_bg = (ImageView)view.getChildAt(0);
            view.setBackgroundResource(parseItemBackground(count, 0));
            if(m.get("file_path").toString().contains("com.all.apps")){
                img_bg.setImageResource(R.drawable.item_myapps);
            } 
            
            if(m.get("file_path").toString().contains("com.add.path")){
                img_bg.setImageResource(R.drawable.item_img_add);
            } 
            if (m.get("item_type") instanceof Drawable){
            	int resId = LauncherActivity.parseItemIcon(((ComponentName)m.get("item_symbol")).getPackageName());

            if (resId != -1){
                img_bg.setImageResource(resId);
            } else {
                img_bg.setImageDrawable((Drawable)(m.get("item_type")));
            }  }
            else {
            //   img_bg.setImageResource(R.drawable.item_img_add);

            }         
            
           view.setOnTouchListener(new MyOnTouchListener(mContext, m.get("file_path")));
           view.setOnKeyListener(new MyOnKeyListener(mContext, m.get("file_path")));
           view.setFocusable(true);
           view.setFocusableInTouchMode(true);
           view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
           this.addView(view);
            } 
       
        }
    
    private int  parseItemBackground(int num, int flag){
        if (flag == 0){
            switch (num % 10 + 1){
                case 1:
                    return R.drawable.item_1;
                case 2:
                    return R.drawable.item_2;
                case 3:
                    return R.drawable.item_3;
                case 4:
                    return R.drawable.item_4;
                case 5:
                    return R.drawable.item_5;
                case 6:
                    return R.drawable.item_6;
                case 7:
                    return R.drawable.item_7;
                case 8:
                    return R.drawable.item_8;
                case 9:
                    return R.drawable.item_9;
                case 10:
                    return R.drawable.item_10;
                default:
                    return R.drawable.item_1;
           }
        }
        
        return R.drawable.item_1;
    }
    
                 
    }

   
    


