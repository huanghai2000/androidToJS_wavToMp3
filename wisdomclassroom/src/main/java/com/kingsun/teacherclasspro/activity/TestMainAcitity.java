package com.kingsun.teacherclasspro.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.dialog.ExitDialog;
import com.kingsun.teacherclasspro.receiver.NetBroadcastReceiver;
import com.kingsun.teacherclasspro.receiver.NetBroadcastReceiver.netEventHandler;
import com.kingsun.teacherclasspro.utils.NetUtils;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.widgets.AtuoCompleteTextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class TestMainAcitity  extends BaseActivity implements netEventHandler {
	private  AtuoCompleteTextView  text,ed_yuming;
	private TextView  tv_login;
	public SharedPreferences sp;
	private String service_head = "";
	private String TAG = "MainActivity";
	private NetBroadcastReceiver receiver;
	private Timer myTimer;
	private TimerTask myTask;
	private String url = "";
	public static  boolean isNet = false;
	private Handler mHandler = new MyHandler(TestMainAcitity.this);
	//	private String[] emailSufixs = new String[]{
//			"http://",
//			"https://"
//	};

	private PostOrCanser postOrCanser = new PostOrCanser() {
		@Override
		public void upLoad(Intent intent) {
		}

		@Override
		public void Post() {
			NetUtils.openSetting(TestMainAcitity.this);
		}

		@Override
		public void Canser() {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_ip_port_layout);
		text = (AtuoCompleteTextView) findViewById(R.id.ed_ip);
		ed_yuming =(AtuoCompleteTextView) findViewById(R.id.ed_yuming);
//		ed_yuming.setAdapterString(emailSufixs);
		sp = getSharedPreferences("KINGSUNTEACHER",MODE_PRIVATE);
		if (sp != null) {
			service_head = sp.getString("HEAD", "");
		}
		Log.e(TAG,"service_head1 = "+service_head);
		if (service_head == null || service_head.equals("") || service_head.equals("null")) {
//			service_head = "192.168.3.2:4322/";
			service_head = "183.47.42.221:4322/";

			MyApplication.getInstance().setServer_head(service_head);
		}
		Log.e(TAG,"service_head = "+service_head);
		text.setText(service_head);
		text.setSelection(service_head.length());
		tv_login = (TextView) findViewById(R.id.login);
		tv_login.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				String xp = text.getText().toString();
				Ilog(TAG, "xp = "+xp+";service_head = "+service_head);
				if (xp.equals("")) {
//					MyApplication.getInstance().setServer_head("192.168.3.2:4322/");
					MyApplication.getInstance().setServer_head("183.47.42.221:4322/");
				}else{
					MyApplication.getInstance().setServer_head(xp);
				}
				Editor e = sp.edit();
				e.putString("HEAD", MyApplication.getInstance().getServer_head());
				e.commit();
				url = ed_yuming.getText().toString()+MyApplication.getInstance().getServer_head();
				Ilog(TAG, "url =>"+url);
				if (!NetUtils.isConnected(getApplicationContext())) {
					//没有链接网络
					new ExitDialog(TestMainAcitity.this, postOrCanser, "去设置打开网络").showDialog(0,0);
				}else{
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
							mHandler.sendEmptyMessage(11);
						}
					};
					myTimer.schedule(myTask, 3000);
					checkURL(url,mHandler);
				}
			}
		});

		receiver = new NetBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);
	}

	public void onNetChange() {
		if (NetUtils.getNetworkState(this) == NetUtils.NETWORN_NONE) {
			ToastUtils.showToast(getApplicationContext(), "网络未连接");
		}else{
			ToastUtils.showToast(getApplicationContext(), "网络已经链接");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (myTimer != null) {
			myTimer.cancel();
			myTimer = null;
		}

		if (myTask != null) {
			myTask.cancel();
			myTask = null;
		}
		if (mHandler != null ){
			mHandler.removeMessages(11);
			mHandler.removeMessages(12);
			mHandler.removeMessages(3);
			mHandler.removeCallbacksAndMessages(null);
		}
	}


	private class MyHandler extends Handler {
		private final WeakReference<TestMainAcitity> mActivity;
		public MyHandler(TestMainAcitity activity) {
			mActivity = new WeakReference<TestMainAcitity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mActivity.get() == null) {
				return;
			}
			switch (msg.what) {
				case 11:
					isNet = false;
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
					ToastUtils.showToast(TestMainAcitity.this,"输入地址无法访问");
					break;
				case 12:
					isNet = true;
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
					finish();
					break;
				default:
					break;
			}
		}
	}
}