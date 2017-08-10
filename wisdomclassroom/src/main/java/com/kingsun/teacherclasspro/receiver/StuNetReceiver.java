package com.kingsun.teacherclasspro.receiver;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import com.kingsun.teacherclasspro.utils.NetWorkHelper;

public class StuNetReceiver extends BroadcastReceiver {

	WeakReference<Activity> mActivity;
	State wifiState = null;
	State mobileState = null;
	ConnectivityManager cm;
	NetworkInfo info;
	NetworkInfo info2;
	AlertDialog dlg;
	private String mType;
	private Handler mHandler;
	public StuNetReceiver(Activity activity){
		super();
		mActivity = new WeakReference<Activity>(activity);	
	}
	
	public StuNetReceiver(Activity activity,String type,Handler handler){
		super();
		mActivity = new WeakReference<Activity>(activity);
		this.mType = type;
		this.mHandler = handler;
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {		
		 cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		 info2 = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info!=null) {
			mobileState = info.getState();
		}
		if (info2!=null) {
			wifiState = info2.getState();
		}
		if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
			// 手机网络连接成功
			// Toast_Util.ToastString(getApplicationContext(), "手机网络连接成功");
//			Log.e(TAG, "手机网络连接成功");
			if (null != dlg && dlg.isShowing()) {// 如果用户没点或者在切换wifi消失
				if (null != dlg) {
					dlg.dismiss();
					dlg = null;
				}
			}
		} else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
			// 手机没有任何的网络
			// Toast_Util.ToastString(getApplicationContext(), "手机没有任何的网络");
			try {
//				Log.e(TAG, "手机没有任何的网络");
				if (mType.equals("StuDoWork") || mType.equals("reDone")) {
					if (null == dlg) {// 广播会调2次，这里增加判断只让出现一次
						dlg = NetWorkHelper.setNetwork(mActivity.get(), mHandler);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (wifiState != null && State.CONNECTED == wifiState) {
			// 无线网络连接成功
//			Log.e(TAG, "无线网络连接成功");
			if (null != dlg && dlg.isShowing()) {// 如果用户没点或者在切换wifi消失
				if (null != dlg) {
					dlg.dismiss();
					dlg = null;
				}
			}
			// Toast_Util.ToastString(getApplicationContext(), "无线网络连接成功");
		}
	}
}
