package com.kingsun.teacherclasspro.utils;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.config.Constant;

public class NetWorkHelper {

	private static int mwidth;
	private static int mheight;
	private static int textsize;
	private static float mdensity;
	public static boolean isdownload = false;
	private static final int CMNET = 3;
	private static final int CMWAP = 2;
	private static final int WIFI = 1;

	// 是否联网网络
	public static boolean IsHaveInternet(final Context context) {
		try {
			State wifiState = null;
			State mobileState = null;
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);					
			NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo info2 = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (info!=null) {
				mobileState = info.getState();
			}
			if (info2!=null) {
				wifiState = info2.getState();
			}
			if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
				// 手机网络连接成功
				// Toast_Util.ToastString(getApplicationContext(), "手机网络连接成功");
				return true;
			} else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
				// 手机没有任何的网络
				// Toast_Util.ToastString(getApplicationContext(), "手机没有任何的网络");
				return false;
			} else if (wifiState != null && State.CONNECTED == wifiState) {
				// 无线网络连接成功
				// Toast_Util.ToastString(getApplicationContext(), "无线网络连接成功");
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// 获取联网类型，0-断网;1-wifi;2-3G
	// public static int isConnecting(Context context) {
	// ConnectivityManager mConnectivity = (ConnectivityManager) context
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	// TelephonyManager mTelephony = (TelephonyManager) context
	// .getSystemService(Context.TELEPHONY_SERVICE);
	// NetworkInfo info = mConnectivity.getActiveNetworkInfo();
	// if (info == null || !mConnectivity.getBackgroundDataSetting()) {
	// return 0;
	// }
	//
	// int netType = info.getType();
	// int netSubtype = info.getSubtype();
	// if (netType == ConnectivityManager.TYPE_WIFI) {
	// return 1;
	// } else if (netType == ConnectivityManager.TYPE_MOBILE
	// && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
	// && !mTelephony.isNetworkRoaming()) {
	// return 2;
	// } else {
	// return 0;
	// }
	// }
	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络 2：wap网络 3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		System.out.println("networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}

	// ConnectivityManager manager;
	//
	// /**
	// * 检测网络是否连接
	// *
	// * @return
	// */
	// private boolean checkNetworkState() {
	// boolean flag = false;
	// // 得到网络连接信息
	// manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// // 去进行判断网络是否连接
	// if (manager.getActiveNetworkInfo() != null) {
	// flag = manager.getActiveNetworkInfo().isAvailable();
	// }
	// if (!flag) {
	// setNetwork();
	// }
	// return flag;
	// }

	/**
	 * 网络未连接时，调用设置方法
	 */
	public static AlertDialog setNetwork(final Context context,final Handler handler) {
		final AlertDialog dlg = new AlertDialog.Builder(context, R.style.CustomDialog).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.s_dialog_removebinding);
		TextView textView_title = (TextView) window.findViewById(R.id.textView_title);
		TextView textView_tips = (TextView) window.findViewById(R.id.textView_tips);
		Button button_confirm = (Button) window.findViewById(R.id.button_confirm);
		Button button_cancel = (Button) window.findViewById(R.id.button_cancel);
		textView_title.setText("网络提示信息");
		textView_tips.setText("网络不可用，如果继续，请先设置网络！");
		button_confirm.setText("设置");
		button_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = null;
				/**
				 * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
				 */
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
				if (dlg != null) {
					dlg.dismiss();
				}
				handler.sendEmptyMessage(Constant.NO_SUBMIT);
			}
		});

		button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (dlg != null) {
					dlg.dismiss();
				}
				handler.sendEmptyMessage(Constant.NO_SUBMIT);
			}
		});
		return dlg;
	}
}
