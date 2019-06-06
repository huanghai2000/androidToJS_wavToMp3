package com.kingsun.teacherclasspro.widgets;

/**
 * Created by hai.huang on 2017/8/31.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView{
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }



    @Override
    public boolean zoomIn() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean zoomOut() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount()==1) {
            return super.onTouchEvent(event);
        }else {
            return false;
        }
    }
    @Override
    @Deprecated
    public boolean canZoomIn() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    @Deprecated
    public boolean canZoomOut() {
        // TODO Auto-generated method stub
        return false;
    }
}