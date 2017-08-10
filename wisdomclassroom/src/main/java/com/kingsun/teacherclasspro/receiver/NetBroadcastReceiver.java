package com.kingsun.teacherclasspro.receiver;

import java.util.ArrayList;

import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetBroadcastReceiver extends BroadcastReceiver {
	private String TAG ="NetBroadcastReceiver";
	private ConnectivityManager manager  ;
	private NetworkInfo info;
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = manager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				String name = info.getTypeName();
			}else{
				ToastUtils.showToast(context, "网络未连接");
			}
		}
	}
	
	public static abstract interface netEventHandler {
		public abstract void onNetChange();
	}
}