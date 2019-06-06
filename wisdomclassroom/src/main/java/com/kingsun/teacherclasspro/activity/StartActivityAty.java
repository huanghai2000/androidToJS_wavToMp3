package com.kingsun.teacherclasspro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.utils.AppConfig;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivityAty extends BaseActivity{
	public SharedPreferences sp;
	public static  boolean isNet = false;
	public  String service_head;
	private Timer myTimer;
	private TimerTask myTask;
	private static String  TAG = "StartActivityAty";
	private Handler mHandler = new MyHandler(StartActivityAty.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
//		Configure.init(StartActivityAty.this);
//		Elog(TAG,"W = "+ Configure.screenWidth+";h = "+Configure.screenHeight);
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceId(IMEI) = " + Build.MODEL);
//        sb.append("- DeviceSoftwareVersion = " + Build.MANUFACTURER);
        Ilog(TAG, sb.toString());
		if (!AppConfig.AppID.equals("SKS1916")){
			sp = getSharedPreferences("KINGSUNTEACHER",MODE_PRIVATE);
			if (sp != null) {
				service_head = sp.getString("HEAD", "183.47.42.221:4322/");
				if (service_head == null || service_head.equals("")){
					service_head = "183.47.42.221:4322/";
				}
				Log.e(TAG,"service_head "+service_head);
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
					Elog(TAG,"SSS");
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
		if (!AppConfig.AppID.equals("SKS1916")){
			if (Build.VERSION.SDK_INT <= 19 || isNet) {
				//能访问
				MyApplication.getInstance().setServer_head(service_head);
				mainIntent = new Intent(StartActivityAty.this, MainActivity.class);
//				mainIntent = new Intent(StartActivityAty.this, TestAty.class);
				mainIntent.putExtra("url", "http://"+service_head);
			}else{
				mainIntent = new Intent(this, TestMainAcitity.class);
			}
		}else{
			Elog(TAG,"xxx");
			mainIntent = new Intent(StartActivityAty.this, LoginActivity.class);
		}
		startActivity(mainIntent);
		CheckActivityIn();
		finish();
	}
}