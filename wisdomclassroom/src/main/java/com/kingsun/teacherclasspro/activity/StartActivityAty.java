package com.kingsun.teacherclasspro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivityAty extends BaseActivity{
	public SharedPreferences sp;
	public static  boolean isNet = false;
	public  String service_head;
	private Timer myTimer;
	private TimerTask myTask;
	private Handler mHandler = new MyHandler(StartActivityAty.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
        StringBuilder sb = new StringBuilder();  
        sb.append("\nDeviceId(IMEI) = " + Build.MODEL);  
        sb.append("\nDeviceSoftwareVersion = " + Build.MANUFACTURER);
        Ilog("info", sb.toString());
		sp = getSharedPreferences("KINGSUNTEACHER",MODE_PRIVATE);
		if (sp != null) {
			service_head = sp.getString("HEAD", "");
			if (service_head != null && !service_head.equals("")) {
				checkURL("http://"+service_head,mHandler);
			}
		}
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}

		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		myTimer = new Timer();
		myTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(3);
			}
		};
		myTimer.schedule(myTask, 1500);
//		Configure.init(StartActivityAty.this);
//		Ilog(TAG, "w = "+Configure.screenWidth+";h = "+Configure.screenHeight);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}

		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		mHandler.removeMessages(1);
		mHandler.removeMessages(2);
		mHandler.removeMessages(3);
		mHandler.removeCallbacksAndMessages(null);
	}

	private static  class MyHandler extends Handler {  
		private final WeakReference<StartActivityAty> mActivity;  
		public MyHandler(StartActivityAty activity) {  
			mActivity = new WeakReference<StartActivityAty>(activity);  
		}  

		@Override  
		public void handleMessage(Message msg) {  
			if (mActivity.get() == null) {  
				return;  
			}
			switch (msg.what) {
			case 11:
				isNet = false;
				break;
			case 12:
				isNet = true;
				break;
			case 3:
				mActivity.get().gotoHome();
				break;
			default:
				break;
			}
		}  
	} 

	public void gotoHome(){
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}

		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		Intent mainIntent = null;
		if (isNet) {
			//能访问
			MyApplication.getInstance().setServer_head(service_head);
			mainIntent = new Intent(StartActivityAty.this, MainActivity.class);
			mainIntent.putExtra("url", "http://"+service_head);
		}else{
			mainIntent = new Intent(this, TestMainAcitity.class);
		}
		//mainIntent = new Intent(StartActivityAty.this, JobActivity.class);
		startActivity(mainIntent);
		CheckActivityIn();
		finish();
	}
}