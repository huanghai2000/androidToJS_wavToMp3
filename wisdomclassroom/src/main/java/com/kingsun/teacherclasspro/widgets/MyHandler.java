package com.kingsun.teacherclasspro.widgets;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

import com.kingsun.teacherclasspro.callback.MyHandlerCallBack;

public class MyHandler extends Handler {
    protected WeakReference<MyHandlerCallBack> weakReferenceContext;
    		
    public MyHandler(MyHandlerCallBack myHandlerCallBack) {
    	this.weakReferenceContext = new WeakReference<MyHandlerCallBack>(myHandlerCallBack);
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		MyHandlerCallBack myHandlerCallBack = weakReferenceContext.get();
		if (myHandlerCallBack!=null) {
			myHandlerCallBack.handleMessage(msg);
		}
	}
}
