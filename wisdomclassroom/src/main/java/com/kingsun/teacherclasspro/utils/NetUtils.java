package com.kingsun.teacherclasspro.utils;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetUtils {
	public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }
	/**
	 * 判断是否有网络
	 */
	public static boolean netState(Context context) {
		// 获取系统的网络管理对象
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn == null) {
			return false;
		} else {
			// 因为网络有wifi和gprs所以用数组来接受
			NetworkInfo[] infos = conn.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo info : infos) {
					// 判断网络是否连接了
					if (info.getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private NetUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity)
		{
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected())
			{
				if (info.getState() == State.CONNECTED)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity)
	{
		if (android.os.Build.VERSION.SDK_INT >10) {
			activity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
		}else {
			activity.startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
		}
//		Intent intent = new Intent("/");
//		ComponentName cm = new ComponentName("com.android.settings",
//				"com.android.settings.WirelessSettings");
//		intent.setComponent(cm);
//		intent.setAction("android.intent.action.VIEW");
//		activity.startActivityForResult(intent, 0);
	}
}