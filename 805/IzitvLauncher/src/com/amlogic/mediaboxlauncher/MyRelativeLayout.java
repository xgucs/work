/*-------------------------------------------------------------------------
    
-------------------------------------------------------------------------*/
package com.amlogic.mediaboxlauncher;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.graphics.Canvas;  
import android.graphics.Rect;
import android.graphics.Matrix;
import android.util.AttributeSet;

import java.lang.Character;


public class MyRelativeLayout extends RelativeLayout{
    private final static String TAG="MyRelativeLayout";    
    private Context mContext = null;
    private static Rect imgRect;
    private float scalePara = 1.1f;
    private float shortcutScalePara = 1.1f;
    private float framePara = 1.08f;
    private int animDuration;
    private int animDelay = 0;
    private final int MODE_HOME_RECT = 0;
    private final int MODE_HOME_SHORTCUT = 1;
    private final int MODE_CHILD_SHORTCUT = 2;

    public MyRelativeLayout(Context context){
        super(context); 
    }
    
    public MyRelativeLayout(Context context, AttributeSet attrs){
        super(context, attrs); 
        mContext = context;
        if (LauncherActivity.isRealOutputMode)
            animDuration = 30;
        else
            animDuration = 30;
    }
    
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle); 
    } 

    @Override  
    public void onDraw(Canvas canvas) {  
       super.onDraw(canvas);        
    }  

    @Override
    public boolean onTouchEvent (MotionEvent event){
        setAddShortcutHead();
   
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LauncherActivity.startX = -1f;

            setSurface();
            
            if (this.getChildAt(0) instanceof ImageView){
                ImageView img = (ImageView)this.getChildAt(0);    
                if(img != null && img.getDrawable() != null &&
                            img.getDrawable().getConstantState().equals(mContext.getResources().getDrawable(R.drawable.item_img_add).getConstantState())){                   
                         LauncherActivity.isAddButtonBeTouched = true;
                         LauncherActivity.pressedAddButton = this;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            return false;
        }
        return true;       
    }
    
	@Override
	protected void onFocusChanged (boolean gainFocus, int direction, Rect previouslyFocusedRect){
      
        setAddShortcutHead();
          
        if (gainFocus && !LauncherActivity.isInTouchMode && !LauncherActivity.dontDrawFocus){ 
           
            //Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ getfocus ="+ this);
         //   Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ getParent="+ this.getParent());
            
            if (LauncherActivity.prevFocusedView != null && (isParentSame(this,LauncherActivity.prevFocusedView)
                                                                    || LauncherActivity.isShowHomePage)){
                //Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ isShowHomePage ="+ LauncherActivity.isShowHomePage + " isParentSame="+isParentSame(this, LauncherActivity.prevFocusedView));

                if (!LauncherActivity.dontRunAnim && !LauncherActivity.IntoCustomActivity){
                    //Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ dontRunAnim ="+ LauncherActivity.dontRunAnim + " IntoCustomActivity="+LauncherActivity.IntoCustomActivity);
                    LauncherActivity.frameView.setVisibility(View.INVISIBLE);
                    LauncherActivity.layoutScaleShadow.setVisibility(View.INVISIBLE);
                    
                    Rect preRect = new Rect();
                    LauncherActivity.prevFocusedView.getGlobalVisibleRect(preRect);
                    
                    setShadowEffect();
                    startFrameAnim(preRect);    
                } else if(!(LauncherActivity.IntoCustomActivity && LauncherActivity.isShowHomePage && LauncherActivity.ifChangedShortcut)){
                    LauncherActivity.dontRunAnim = false;
                    setSurface();
                }
                
            } else{
                    //Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ isShowHomePage ="+ LauncherActivity.isShowHomePage + " dontRunAnim="+LauncherActivity.dontRunAnim + 
                    //                                        " IntoCustomActivity=" + LauncherActivity.IntoCustomActivity);
                if (LauncherActivity.isShowHomePage || LauncherActivity.dontRunAnim || LauncherActivity.IntoCustomActivity){
                    LauncherActivity.IntoCustomActivity = false;
                   setSurface();
                }
            }  
        }
        else if(!LauncherActivity.isInTouchMode){
            LauncherActivity.prevFocusedView = this;
            if (!LauncherActivity.dontRunAnim){
                ScaleAnimation anim = new ScaleAnimation(1.1f, 1f, 1.1f, 1f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setZAdjustment(Animation.ZORDER_TOP);
                anim.setDuration(animDuration);
                anim.setStartTime(animDelay);
             
                if (!(this.getParent() instanceof MyGridLayout)){
                    this.bringToFront();
                    ((View)this.getParent()).bringToFront();
                  //  LauncherActivity.viewHomePage.bringToFront();
               //   LauncherActivity.layoutScaleShadow.bringToFront();
               //   LauncherActivity.frameView.bringToFront();
               //  LauncherActivity.trans_frameView.bringToFront();
                }
                this.startAnimation(anim);
            }
        }
	}

    private void setAddShortcutHead(){
            
        LauncherActivity.current_shortcutHead = CustomAppsActivity.HOME_SHORTCUT_HEAD;
         
    }

   

    public class TransAnimationListener implements AnimationListener { 
        private Animation scaleAnim;
        private ViewGroup mView;
            
        public TransAnimationListener(Context context, ViewGroup view, Animation anim) { 
            scaleAnim = anim;
            mView = view;     
        } 
     
        @Override 
        public void onAnimationStart(Animation animation) {       
        } 
     
        @Override 
        public void onAnimationEnd(Animation animation) { 
            scaleAnim.reset();   
            if (!LauncherActivity.animIsRun){
                LauncherActivity.layoutScaleShadow.setVisibility(View.VISIBLE);
                LauncherActivity.frameView.setVisibility(View.VISIBLE);
            }
        } 
     
        @Override 
        public void onAnimationRepeat(Animation animation) {  
        } 
    } 

    public class ScaleAnimationListener implements AnimationListener { 
        @Override 
        public void onAnimationStart(Animation animation) {       
        } 
        @Override 
        public void onAnimationEnd(Animation animation) { 
            if (!LauncherActivity.animIsRun){
                LauncherActivity.layoutScaleShadow.setVisibility(View.VISIBLE);
                LauncherActivity.frameView.setVisibility(View.VISIBLE);
            }
        } 
        @Override 
        public void onAnimationRepeat(Animation animation) { 
        } 
    } 

    private void startFrameAnim(Rect preRect){
        imgRect = new Rect();
        this.getGlobalVisibleRect(imgRect);
       setTransFramePosition(preRect);
        
        /*AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, imgRect.left-preRect.left,0.0f, imgRect.top-preRect.top);
        translateAnimation.setDuration(animDuration);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, (float)(imgRect.right-imgRect.left)/(float)(preRect.right-preRect.left), 
                                                              1f, (float)(imgRect.bottom-imgRect.top)/(float)(preRect.bottom-preRect.top));
                                                             //   Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        scaleAnimation.setDuration(animDuration);
        scaleAnimation.setStartTime(animDelay);                                                    
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new TransAnimationListener(mContext, this, scaleAnimation));
        */
        
        ScaleAnimation shadowAnim = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        shadowAnim.setDuration(animDuration);
        shadowAnim.setStartTime(animDelay);
        shadowAnim.setAnimationListener(new ScaleAnimationListener());
        
        LauncherActivity.layoutScaleShadow.startAnimation(shadowAnim); 
        //LauncherActivity.trans_frameView.startAnimation(animationSet); 
    }
    
    public void setSurface(){
        setShadowEffect();
        if (!LauncherActivity.animIsRun){
            LauncherActivity.frameView.setVisibility(View.VISIBLE);
            LauncherActivity.layoutScaleShadow.setVisibility(View.VISIBLE);
        }
    }

    public void setShadowEffect(){
        float bgScalePara;
        Rect layoutRect;
        Bitmap scaleBitmap;
        Bitmap shadowBitmap;
        ViewGroup mView = this;
        ImageView scaleImage;
        TextView scaleText;
        int screen_mode;
        String text = null;

     
        LauncherActivity.trans_frameView.bringToFront();
        LauncherActivity.layoutScaleShadow.bringToFront();
        LauncherActivity.frameView.bringToFront();
       

        screen_mode = getScreenMode(mView);

        imgRect = new Rect();
        mView.getGlobalVisibleRect(imgRect);
       setFramePosition(imgRect);
                
        scaleImage = (ImageView)LauncherActivity.layoutScaleShadow.findViewById(R.id.img_focus_unit);
        
            
        if(screen_mode == MODE_HOME_SHORTCUT){
            bgScalePara = shortcutScalePara;
        } else {
            bgScalePara = scalePara;
        }  
                   
        ImageView img = (ImageView)(mView.getChildAt(0));
        img.buildDrawingCache();
        Bitmap bmp = img.getDrawingCache();
        if (bmp == null){
            LauncherActivity.cantGetDrawingCache = true;
            return;
        } else {
            LauncherActivity.cantGetDrawingCache = false;
        }
        
        scaleBitmap = zoomBitmap(bmp, (int)(imgRect.width()*bgScalePara),(int)(imgRect.height()*bgScalePara));
        img.destroyDrawingCache();  
        
              
        shadowBitmap = BitmapFactory.decodeResource(mContext.getResources(), getShadow(mView.getChildAt(0), screen_mode));
        int layout_width = (shadowBitmap.getWidth()) / 2;
        int layout_height = (shadowBitmap.getHeight()) / 2;
        layoutRect = new Rect(layout_width, layout_height, layout_width, layout_height);
        
        scaleImage.setImageBitmap(scaleBitmap);
        
      
        LauncherActivity.layoutScaleShadow.setBackgroundResource(getShadow(mView.getChildAt(0), screen_mode));
        setViewPosition(LauncherActivity.layoutScaleShadow, layoutRect);  
    }

    private void setTextWidth(TextView text, int width){
        android.view.ViewGroup.LayoutParams para;
        para = text.getLayoutParams();
        para.width = width;
        text.setLayoutParams(para);
    }

    private void setTextMarginAndSize(TextView text, int screen_mode){
        android.widget.RelativeLayout.LayoutParams para;
        para = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        para.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        para.addRule(RelativeLayout.CENTER_HORIZONTAL);
        if (screen_mode == MODE_HOME_RECT){
            /*if (LauncherActivity.REAL_OUTPUT_MODE.equals("4k2knative")){
                para.setMargins(0, 0, 0, 284);
            } else if(LauncherActivity.REAL_OUTPUT_MODE.equals("720p"))
                para.setMargins(0, 0, 0, 95);
            else
                para.setMargins(0, 0, 0, 142);*/
			para.setMargins(0, 0, 0, 55);
            text.setLayoutParams(para);
            text.setTextSize(27);
        } else {
           if (LauncherActivity.REAL_OUTPUT_MODE.equals("4k2knative")){
                para.setMargins(150, 0, 150, 154);
           }else if(LauncherActivity.REAL_OUTPUT_MODE.equals("720p"))
                para.setMargins(50, 0, 50, 55);
           else
                para.setMargins(75, 0, 75, 77);
           text.setLayoutParams(para);
           text.setTextSize(30);
        }
    }
   
    private void setTransFramePosition(Rect rect){
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
        int rectWidth = rect.right - rect.left;
        int rectHeight = rect.bottom - rect.top;

        lp.width = (int)(rectWidth* framePara);
        lp.height = (int)(rectHeight * framePara);
        lp.x = rect.left + (int)((rectWidth - lp.width)/2);
        lp.y = rect.top + (int)((rectHeight - lp.height)/2);
        LauncherActivity.trans_frameView.setLayoutParams(lp);
    }

    private void setFramePosition(Rect rect){
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
        int rectWidth = rect.right - rect.left;
        int rectHeight = rect.bottom - rect.top;

        lp.width = (int)(rectWidth)-5;
        lp.height = (int)(rectHeight)-5;
        lp.x = rect.left + (int)((rectWidth - lp.width)/2);
        lp.y = rect.top + (int)((rectHeight - lp.height)/2);
        LauncherActivity.frameView.setLayoutParams(lp);
    }

    private void setViewPosition(View view, Rect rect){
        android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);

        lp.width = rect.width();
        lp.height = rect.height();
        lp.x = rect.left;
        lp.y = rect.top;
        view.setLayoutParams(lp);
    }
    
    private int getScreenMode(ViewGroup view){
        View img = view.getChildAt(0);
        View tx = view.getChildAt(1);
        String path  = img.getResources().getResourceName(img.getId()); 
        String vName = path.substring(path.indexOf("/")+1);
        
        if (vName.equals("img_recommend") || vName.equals("img_video") || vName.equals("img_setting")
                || vName.equals("img_app") || vName.equals("img_music") || vName.equals("img_local")){
            return MODE_HOME_RECT;
        } else if(tx != null){
            framePara = 1.06f;
            return MODE_CHILD_SHORTCUT;
        } else {
            return MODE_HOME_SHORTCUT;
        }        
    }

    private boolean isParentSame(View view1, View view2){
        if (((ViewGroup)view1.getParent()).indexOfChild(view2) == -1){
            return false;
        } else {
            return true;
        }
    }
 private int getShadow(View img, int mode){
        String path  = img.getResources().getResourceName(img.getId()); 
        String vName = path.substring(path.indexOf("/")+1);
       // Log.d(" MyRelativeLayout", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ child 1="+ vName);

        if (vName.equals("iv_iztv")){
            return R.drawable.shadow_tv;
        } else if(vName.equals("iv_iradio") ||
		 vName.equals("tv_fire") || 
		 vName.equals("iv_setting") 
		 || vName.equals("iv_clear") ||vName.equals("iv_netspeed")){
            return R.drawable.shadow_vertical;
        } else if(vName.equals("tv_bird") || vName.equals("tv_human") 
		|| vName.equals("tv_sock") || 
		vName.equals("tv_wire") || 
		vName.equals("iv_explore") || vName.equals("iv_advance")){
            return R.drawable.shadow_hor;
        } else {
            return R.drawable.shadow_other;
        }      
    }
  
    public int getStringLength(String s)
    {
        int length = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;
                
        }
        return length;
       
    }

   public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
       int width = bitmap.getWidth();
       int height = bitmap.getHeight();
       Matrix matrix = new Matrix();
       float scaleWidht = ((float) w / width);
       float scaleHeight = ((float) h / height);
       matrix.postScale(scaleWidht, scaleHeight);
       Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
       return newbmp;
    }
}
