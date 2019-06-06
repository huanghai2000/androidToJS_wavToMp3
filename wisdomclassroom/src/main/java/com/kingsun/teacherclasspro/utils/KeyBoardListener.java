package com.kingsun.teacherclasspro.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

/**
 * 解决webView键盘遮挡问题的类  
 * Created by huanghai on 2017/06/14.  
 */
public class KeyBoardListener implements OnGlobalLayoutListener{
	private Activity activity;
	// private Handler mhanHandler;
	private int viewHeight = 0;
	private View mChildOfContent;
	private int usableHeightPrevious;
	private FrameLayout.LayoutParams frameLayoutParams;
	private static KeyBoardListener keyBoardListener;
	public static KeyBoardListener getInstance(Activity activity) {
		if(keyBoardListener == null){
			keyBoardListener = new KeyBoardListener(activity);
		}
		return keyBoardListener;
	}

	public KeyBoardListener(Activity activity) {
		super();
		// TODO Auto-generated constructor stub  
		this.activity = activity;
		// this.mhanHandler = handler;  
	}
	public void init() {
		FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this);
//		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(  
//				new ViewTreeObserver.OnGlobalLayoutListener() {  
//					public void onGlobalLayout() { 
//						//监听执行完一次就就删除，主要在登录页使用
////						mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//						possiblyResizeChildOfContent();  
//					}  
//				});  
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public  void clearOnGlobalLayoutListener(int height){
		if (mChildOfContent != null && mChildOfContent.getViewTreeObserver() != null) {
//			int usableHeightNow = computeUsableHeight();
//			int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//			int heightDifference = usableHeightSansKeyboard - usableHeightNow;
			frameLayoutParams.height = height;
			mChildOfContent.requestLayout();
			usableHeightPrevious = height;
			mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		}
	}

	private void possiblyResizeChildOfContent() {
		int usableHeightNow = computeUsableHeight();
//		Log.e("MainActivity","usableHeightNow = "+usableHeightNow);
		if (usableHeightNow != usableHeightPrevious) {
			int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
			int heightDifference = usableHeightSansKeyboard - usableHeightNow;
			if (heightDifference < 100){
				viewHeight = heightDifference;
			}
//			Log.e("MainActivity","heightDifference test="+heightDifference+"; usableHeightSansKeyboard = "+usableHeightSansKeyboard);
//			if (heightDifference > (usableHeightSansKeyboard / 4)) {  
//				// keyboard probably just became visible  
//				frameLayoutParams.height = usableHeightSansKeyboard- heightDifference;  
//			} else {  
//				// keyboard probably just became hidden  
//				frameLayoutParams.height = usableHeightSansKeyboard;  
//			}  
			frameLayoutParams.height = usableHeightSansKeyboard- heightDifference;
//			frameLayoutParams.height = usableHeightSansKeyboard;  
			mChildOfContent.requestLayout();
			usableHeightPrevious = usableHeightNow;
		}else{
//			Log.e("MainActivity","-----------");
		}
	}

	private int computeUsableHeight() {
		Rect r = new Rect();
		mChildOfContent.getWindowVisibleDisplayFrame(r);
		return (r.bottom - r.top);
	}

	@Override
	public void onGlobalLayout() {
		possiblyResizeChildOfContent();
	}
}  
