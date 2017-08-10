package com.kingsun.teacherclasspro.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.google.myjson.Gson;
import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.CheckBean;
import com.kingsun.teacherclasspro.bean.UPBean;
import com.kingsun.teacherclasspro.bean.UpLoadBean;
import com.kingsun.teacherclasspro.bean.UpLoadBean.otherBean;
import com.kingsun.teacherclasspro.callback.MyHandlerCallBack;
import com.kingsun.teacherclasspro.config.Configure;
import com.kingsun.teacherclasspro.config.Constant;
import com.kingsun.teacherclasspro.config.HandlerStrings;
import com.kingsun.teacherclasspro.dialog.ExitDialog;
import com.kingsun.teacherclasspro.dialog.setIpAndPortDialog;
import com.kingsun.teacherclasspro.dialog.upLoadProDialog;
import com.kingsun.teacherclasspro.receiver.NetBroadcastReceiver;
import com.kingsun.teacherclasspro.utils.CheckPermissionUtils;
import com.kingsun.teacherclasspro.utils.Check_UnZip;
import com.kingsun.teacherclasspro.utils.HttpMultipartPost;
import com.kingsun.teacherclasspro.utils.KeyBoardListener;
import com.kingsun.teacherclasspro.utils.KingSoftParasJson;
import com.kingsun.teacherclasspro.utils.PhotoSelUtil;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.utils.Utils;
import com.kingsun.teacherclasspro.widgets.MyHandler;
import com.kingsun.teacherclasspro.widgets.WVJBWebViewClient;
import com.kingsun.teacherclasspro.widgets.WVJBWebViewClient.WVJBResponseCallback;
import com.torment.lame.LameUtils;
import com.xs.SingEngine;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements  MyHandlerCallBack ,OnKeyListener{
	MyHandler myHandler = new MyHandler(this);
	static MainActivity mManager = null;
	private static String TAG ="MainActivity";
	//下载文件名
	private String UPDATE_SERVERAPK = "";
	private String fileName = "";
	private static final int  REQUEST_FILE_PICKER = 100;
	private String  upLoadUrl = "";
	private upLoadProDialog upDialog ;
	private MediaRecorder mRecorder;
	private CheckPermissionUtils permissionUtils;
	private  String path = "";
	private NetBroadcastReceiver receiver;
	private int  upIndex = 0;
	private  setIpAndPortDialog portDialog;
	public SharedPreferences sp;
	private String url = "";
	private Button btn;
	private ProgressDialog dlgMixing;
	private boolean isDown = true;
	private HttpMultipartPost post;
	private String upLoadName = "";
	private Timer myTimer;
	private TimerTask myTask;
	private PhotoSelUtil photoSelUtil;
	private String loadPhotoUrl = "";
	private String ModeInfo = "HUAWEI";//设备类型
	private String zimu = "";
	//		public WebViewMod webView;
	public WebView webView;
	protected WVJBWebViewClient webViewClient;
	private String mp3File,wavFile;
	private SingEngine engine;
	public static final String ACTION_UPLOAD = "com.clovsoft.ik.RemoteRecorder.MEDIA_FILE";
	//修改IP地址
	public  doThing doThing = new doThing() {
		@Override
		public void changeIP(String head,String ip) {
			if (webView != null) {
				Editor e = sp.edit();
				e.putString("HEAD", ip);
				e.commit();
				MyApplication.getInstance().setServer_head(ip);
				url = head+ip;
				Ilog(TAG, "URL = "+url);
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
						downloadHandle.sendEmptyMessage(11);
					}
				};
				myTimer.schedule(myTask, 3000);
				checkURL(url,downloadHandle);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("KINGSUNTEACHER",MODE_PRIVATE);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		photoSelUtil = new PhotoSelUtil();
		ModeInfo = Build.MANUFACTURER;//用来区别各个厂商

		//		com.kingsun.teacherclasspro.utils.Configure.init(this);
		//		Ilog(TAG, "w = "+com.kingsun.teacherclasspro.utils.Configure.screenWidth+";h = "+
		//				com.kingsun.teacherclasspro.utils.Configure.screenHeight);

		intView(); //初始化界面
		//		AndroidBug5497Workaround.assistActivity(MainActivity.this);
		permissionUtils = CheckPermissionUtils.getinstance();
		String sp = getVersion();
		portDialog = new setIpAndPortDialog(MainActivity.this,doThing,sp);
		IntentFilter filter = new IntentFilter();
		filter.addDataScheme("file");
		filter.addAction(ACTION_UPLOAD);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(myReceiver, filter);

		receiver = new NetBroadcastReceiver();
		IntentFilter filters = new IntentFilter();
		filters.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filters);

		dlgMixing = new ProgressDialog(this);
		dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dlgMixing.setCancelable(false);
		dlgMixing.setCanceledOnTouchOutside(false);
	}

	@SuppressLint("NewApi")
	private void intView() {
		initSingEnge();
		setContentView(R.layout.activity_main);
		KeyBoardListener.getInstance(this).init();
		btn = (Button) findViewById(R.id.login);
		//		webView = (WebViewMod) findViewById(R.id.webView_main);

		webView = (WebView) findViewById(R.id.webView_main);

		webView.setBackgroundColor(getResources().getColor(R.color.app_bg));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
				// 开启app内调试的开关，开启后将可以在谷歌浏览器中调试js
				webView.setWebContentsDebuggingEnabled(Configure.isH5WebContentsDebuggingEnabled);
			}
		}
		webViewClient = new WVJBWebViewClient(webView);
		webViewClient.webViewLoadFinished(myHandler);
		webViewClient.enableLogging();

		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int progress){
			}
		});

		webView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});

		webView.setWebViewClient(webViewClient);

		webView.loadUrl(url);// 加载H5主页
		/**
		 * 启动易课+接口
		 */
		webViewClient.registerHandler("startAPP", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"startAPP =x==>"+datas);
				Utils.gotoXKJ(MainActivity.this,"com.kingsunedu.ik",datas.toString(),"com.clovsoft.ik.MainActivity");
				//返回给JS执行结果
				//								Intent intent = getPackageManager().getLaunchIntentForPackage("com.kingsun.stsunnytasktea");
				//								startActivity(intent);
			}
		});

		/**
		 * 角色扮演
		 */
		webViewClient.registerHandler("startRcord", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"startRcord =x==>"+datas);
//				onStartDub();
				if (datas != null && !datas.toString().equals("")){
					zimu = datas.toString();
					start();
				}else{
					Ilog(TAG,"ZIMU is null");
				}
			}
		});

		/**
		 * 角色扮演
		 */
		webViewClient.registerHandler("checkRcord", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"checkRcord =x==>"+datas);
				checkPrimiss();
			}
		});

		/**
		 * 设置IP端口号
		 */
		webViewClient.registerHandler("setIpAndPort", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"setIpAndPort =x==>"+datas);
				portDialog.showDialog(1, 1);
			}
		});

		/**
		 * upLoadPhoto
		 */
		webViewClient.registerHandler("upLoadPhoto", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"upLoadPhoto =x==>"+datas);
				if (datas != null) {
					loadPhotoUrl = (String) datas;
					photoSelUtil.showPhoto(MainActivity.this, "修改头像");
				}
			}
		});

		/**
		 * 保存登录用户名
		 */
		webViewClient.registerHandler("loginName", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"loginName =x==>"+datas);
				if (datas != null) {
					MyApplication.getInstance().setLoginName((String) datas);
				}
			}
		});

		/**
		 * 设置软硬件加速，主要兼容MTK平台
		 */
		webViewClient.registerHandler("setSoftMode", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG,"setSoftMode =x==>"+datas+";ModeInfo = "+ModeInfo);
				if (!ModeInfo.equalsIgnoreCase("HUAWEI")) {
					//表示不是华为平台
					if (datas != null) {
						if ((int)datas == 1) {
							//表示使用软件加速
							webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
						}else if ((int)datas == 2) {
							//硬件加速
							webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
						}
					}
				}
			}
		});

		/**
		 * 角色扮演
		 */
		webViewClient.registerHandler("stopRcord", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				upIndex = 1;
				Ilog(TAG,"stopRcord =x==>"+datas);
				stop();
				if (datas.toString() != null && !datas.toString() .equals("")){
					//表示需要上传
					wavToMp3();
				}
//				if (mRecorder != null) {
//					mRecorder.stop();
//					mRecorder.release();
//					mRecorder = null;
//				}
				//表示需要上传
				if (datas.toString() != null && !datas.toString() .equals("")) {
					try {
						//开始转码wav转mp3
						//						JSONObject jsonObject = new JSONObject(datas.toString());
						//						String pathString = jsonObject.optString("uploadPath");
						//						Ilog(TAG,"pathString =x==>"+pathString);
						UPBean upBean = (UPBean) KingSoftParasJson.getObjectByJson(datas.toString(), UPBean.class);
						HashMap<String, String> map = new HashMap<>();
						map.put("FileID", upBean.getFileID());
						map.put("ResourceStyle", upBean.getResourceStyle());
						map.put("UserName", upBean.getUserName());
						Gson gson = new Gson();
						String dateUrl  = gson.toJson(map);
						String  teString = upBean.getFilePath()+"?JsonFile="+dateUrl;
						if (teString != null && !teString.equals("")) {
							if(upDialog != null){
								if(!upDialog.isShowing()){
									upDialog.setTextPro("上传中");
									upDialog.setTextNotic("");
									upDialog.showDialog(1, 1);
								}
							}
							upLoadFile(path,teString,downloadHandle);
							//上传角色扮演录音
							//							post = new HttpMultipartPost(MainActivity.this, path,downloadHandle);
							//							post.execute(teString);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		/**
		 * 下载文件
		 */
		webViewClient.registerHandler("downLoadFile", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				Ilog(TAG, "downLoadFile =test=>"+datas.toString());
				String urlString = datas.toString().replace("?","/");
				String[] ttStrings = urlString.split("/");
				UPDATE_SERVERAPK = ttStrings[ttStrings.length -1];
				fileName = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
						MyApplication.getInstance().getLoginName()+"/ppt/"+UPDATE_SERVERAPK;
				//表明是PPT文件
				File file = new File(fileName);
				if (file.exists()) {
					long s ;
					FileInputStream fis;
					try {
						fis = new FileInputStream(file);
						s= fis.available();
						isTureFile(urlString, s,downloadHandle);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					if (upDialog != null && !upDialog.isShowing()) {
						upDialog.setTextNotic("下载中.....");
						upDialog.setTextPro(0+"%");
						upDialog.showDialog(1,1);
					}
					isDown = true;
					downFile(urlString,downloadHandle,fileName);
				}
			}
		});

		/**
		 * 上传载文件
		 */
		webViewClient.registerHandler("upLoadFile", new WVJBWebViewClient.WVJBHandler() {
			@Override
			public void request(Object datas, WVJBResponseCallback callbacks) {
				upLoadUrl = datas.toString();
				Ilog(TAG, "upLoadUrl ==> "+upLoadUrl);
				if (upLoadUrl.contains("?type=1")) {
					upIndex = 2;
					//表示上传图片文件
					upLoadUrl = upLoadUrl.replace("?type=1", "");
					try {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("*/*");
						startActivityForResult(Intent.createChooser(intent,"File Chooser"),REQUEST_FILE_PICKER);
					} catch (Exception e) {
						e.printStackTrace();
						ToastUtils.showToast(getApplicationContext(), "请先安装资源管理器");
					}
				}else if (upLoadUrl.contains("?type=2")) {
					upIndex = 3;
					//打开微课
					upLoadUrl = upLoadUrl.replace("?type=2", "");
					String recordName = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
							MyApplication.getInstance().getLoginName()+"/weike.mp4";
					Utils.startAty(MainActivity.this,"com.kingsunedu.ik","com.clovsoft.ik" +
							".RemoteRecorder",recordName);
				}
			}
		});
		upDialog = new upLoadProDialog(MainActivity.this,"");
		upDialog.setOnKeyListener(this);

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//				Intent intents = new Intent();
				//				intents.setClass(MainActivity.this, MyDownLoadAty.class);
				//				startActivity(intents);
				//				CheckActivityIn();

				//				webViewClient.callHandler("functionInJs", "data from Java", new WVJBResponseCallback() {
				//					@Override
				//					public void callback(Object data) {
				//						Ilog(TAG, "reponse data from js " + data);						
				//					}
				//				});
//				webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		});
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case HandlerStrings.MAIN_WEBVIEW_STARTAPP:// 检查更新请求成功
				break;
			case HandlerStrings.MAIN_WEBVIEW_404:// 网络连接失败
				ToastUtils.showToast(getApplicationContext(), "链接地址无法打开");
				Intent mainIntent = new Intent(MainActivity.this, TestMainAcitity.class);
				startActivity(mainIntent);
				CheckActivityIn();
				finish();
				break;
			case 100:
				webView.loadUrl("http://192.168.3.89:8079/demo.html");// 加载本地H5主页
				break;
			case -200:
				ToastUtils.showToast(getApplicationContext(), "初始化失败");
				break;
			case 101:
				//设置H5页面显示可以打开EK
				webView.loadUrl("javascript:openEK()");
				break;

			case 102:
				if (!ModeInfo.equalsIgnoreCase("HUAWEI")) {
					Ilog(TAG, "兼容修改密码界面1");
					//兼容华瑞安修改密码界面
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
							downloadHandle.sendEmptyMessage(14);
						}
					};
					myTimer.schedule(myTask, 500);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(upDialog != null && upDialog.isShowing()){
			upDialog.dismiss();
		}
		if (portDialog != null  && portDialog.isShowing()) {
			portDialog.dismiss();
		}
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
		if (myReceiver != null) {
			unregisterReceiver(myReceiver);
		}

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

		Editor e = sp.edit();
		e.putString("HEAD", MyApplication.getInstance().getServer_head());
		e.commit();
	}

	long exitTime =0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				ToastUtils.showToast(getApplicationContext(), "再按一次退出");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下载更新
	 */
	@SuppressLint("HandlerLeak")
	private Handler downloadHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					long total = (Long) msg.obj;
					long progress = (long) msg.arg2;
					float p = ((float) progress / (float) total);
					BigDecimal b = new BigDecimal(p);
					float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
					int pro = (int) (f1 * 100);
					if(upDialog != null && upDialog.isShowing()){
						upDialog.setTextNotic("下载中.....");
						upDialog.setTextPro(pro+"%");
					}
					if (pro == 100 && isDown ) {
						isDown = false;
						//					ToastUtils.showToast(MainActivity.this,"下载成功");
						if(upDialog != null){
							upDialog.setTextPro("100%");
							upDialog.dismiss();
						}
						pro = 0;
						if (fileName.endsWith(".zip")) {
							String  pptUrl =fileName.replace(".zip", "");
							try {
								dlgMixing.setMessage("正在解压PPT文件");
								dlgMixing.show();
								//解压PPT
								new Check_UnZip(fileName ,downloadHandle,pptUrl).start();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else  {
							try {
								Intent intent = Utils.openFile(fileName);
								if(intent == null){
									ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
									return;
								}
								startActivity(intent);
							} catch (Exception e) {
								ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
							}
						}
					}
					break;

				case 2:
					if (upDialog != null && upDialog.isShowing() ) {
						upDialog.dismiss();
					}
					final String  data = (String) msg.obj;
					Ilog(TAG, "obj ==> "+data);
					if (upIndex >1) {
						UpLoadBean bean = (UpLoadBean) KingSoftParasJson.getObjectByJson(data, UpLoadBean.class);
						if (bean != null && bean.isSuccess()) {
							ToastUtils.showToast(MainActivity.this,"上传成功");
							if (upIndex ==2) {
								//上传图片
								otherBean beans = bean.getData();
								if (beans != null) {
									beans.setType("1");
									beans.setFileName(upLoadName);
									bean.setData(beans);
								}
							}else if (upIndex == 3) {
								//上传微课
								otherBean beans = bean.getData();
								if (beans != null) {
									beans.setType("2");
									beans.setFileName(upLoadName);
									bean.setData(beans);
								}
							}
						}else {
							if(photoSelUtil != null){
								photoSelUtil.setImgString(null);
								photoSelUtil.delFile();
							}
							ToastUtils.showToast(MainActivity.this,"上传失败");
							return;
						}
						Gson gson = new Gson();
						final String upString = gson.toJson(bean);
						final String upString1 = gson.toJson(bean.getData());
//					Ilog(TAG, "upString1= "+upString1);
//					Ilog(TAG, "upString2= "+upString);
//					Ilog(TAG, "upString3= "+data);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (upIndex == 5) {
									if(photoSelUtil != null){
										photoSelUtil.setImgString(null);
										photoSelUtil.delFile();
									}
									webView.loadUrl("javascript:getAvatarUrl('"+ upString1 + "')");
								}else{
									webView.loadUrl("javascript:uploadfile('"+ upString + "')");
								}
							}
						});;
					}else if(upIndex == 1){
						try {
							JSONObject jsonObject = new JSONObject(data);
							JSONObject js = jsonObject.optJSONObject("Data");
							if(js != null){
								String pathString = js.optString("FilePath");
								webView.loadUrl("javascript:recordMessageHandler('"+ pathString + "')");
							}else{
								ToastUtils.showToast(MainActivity.this,"上传失败:"+jsonObject.optString("ErrorMsg"));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;

				case 3:
					if(upDialog != null && upDialog.isShowing() ){
						upDialog.dismiss();
					}
					break;

				case 4:
					//上传进度
					int pros = msg.arg2;
					if(upDialog != null && upDialog.isShowing()){
						upDialog.setTextNotic("上传中.....");
						upDialog.setTextPro(pros+"%");
					}
					break;

				case 5:
					final boolean  xp = (boolean) msg.obj;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							webView.loadUrl("javascript:recordMessageHandler('"+ xp + "')");
						}
					});;
					break;

				case 6:
					//文件相同
					Ilog(TAG, "6666");
					if (fileName.endsWith(".zip")) {
						String  pptUrl =fileName.replace(".zip", "");
						try {
							dlgMixing.setMessage("正在解压PPT文件");
							dlgMixing.show();
							//解压PPT
							new Check_UnZip(fileName,downloadHandle,pptUrl).start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						try {
							Intent intent = Utils.openFile(fileName);
							if(intent == null){
								ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
								return;
							}
							startActivity(intent);
						} catch (Exception e) {
							ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
						}
					}
					break;
				case 7:
					//文件不相同
					Ilog(TAG, "7777");
					final String  urlString = (String) msg.obj;
					if (upDialog != null && !upDialog.isShowing()) {
						upDialog.showDialog(1,1);
					}
					isDown = true;
					downFile(urlString,downloadHandle,fileName);
					break;

				case Constant.UNZIP_START:
					//正在解压
					//				dlgMixing.setMessage("正在解压PPT文件");
					//				dlgMixing.show();
					break;

				case Constant.DOWNLOAD_UNZIP_RESULT:
					//解压完成
					if (dlgMixing != null) {
						dlgMixing.cancel();
					}
					String openName = (String) msg.obj;
					//				ToastUtils.showToast(MainActivity.this,"解压成功");
					try {
						Intent intent2 = Utils.openFile(openName);
						if(intent2 == null){
							ToastUtils.showToast(getApplicationContext(), "无法打开该文件");
							return;
						}
						startActivity(intent2);
					} catch (Exception e) {
						ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
					}
					break;

				case Constant.UNZIP_NO_SPACE:
					//解压空间不足
					ToastUtils.showToast(MainActivity.this,(String) msg.obj);
					break;

				case Constant.UNZIP_ERROR:
					//解压出错
					ToastUtils.showToast(MainActivity.this,(String) msg.obj);
					break;

				case 11:
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
					ToastUtils.showToast(MainActivity.this,"输入地址无法访问");
					break;

				case 12:
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
				/*
				 * 清除缓存
				 * 
				 * */
					clearCache();
					webView.loadUrl(url);// 加载H5主页
					break;

				case 13:
					if(photoSelUtil != null){
						photoSelUtil.setImgString(null);
						photoSelUtil.delFile();
					}
					ToastUtils.showToast(MainActivity.this,"取消上传");
					break;

				case 14 :
					if (myTimer != null) {
						myTimer.cancel();
						myTimer = null;
					}

					if (myTask != null) {
						myTask.cancel();
						myTask = null;
					}
					Ilog(TAG, "兼容修改密码界面2");
					webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
					break;
				default:
					break;
			}
		};
	};

	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Ilog(TAG, "onActivityResult come = "+arg0);
		if (arg0 == REQUEST_FILE_PICKER) {
			if (arg2 != null && arg2.getData() != null) {
				String xp = getPath(getApplicationContext(), arg2.getData());
				Ilog(TAG, "xp = "+xp);
				File file = new File(xp);
				if (file.exists()) {
					String[] jkString = xp.split("/");
					String okString = jkString[jkString.length-1];
					if (!okString.contains(".")) {
						ToastUtils.showToast(MainActivity.this,"该文件类型不能被上传");
						return;
					}
					//保存文件名
					upLoadName= okString.substring(0, okString.indexOf("."));
					String  ttString = okString.substring(okString.indexOf(".")+1);
					if (ttString.contains(".")) {
						ttString = ttString.substring(ttString.indexOf(".")+1);
					}
					if (Utils.isCanUpload(ttString)) {
						try {
							long size = getFileSizes(file);
							if(size > 1024 *1024*500){
								//标示以G的形式显示
								ToastUtils.showToast(MainActivity.this,"上传文件不能超过500M");
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						if(upDialog != null){
							if(!upDialog.isShowing()){
								upDialog.setTextPro("上传中");
								upDialog.setTextNotic("");
								upDialog.showDialog(1, 1);
							}
						}
						//						upLoadFile(xp,upLoadUrl,downloadHandle);
						//上传用户选的课件
						post = new HttpMultipartPost(this, xp,downloadHandle);
						post.execute(upLoadUrl);
					}else{
						ToastUtils.showToast(MainActivity.this,"该文件类型不能被上传");
						return;
					}
				}
			}
		}else if (arg0 == 1 || arg0 == 2 || arg0 ==0 ) {
			if (photoSelUtil==null) {
				photoSelUtil = new PhotoSelUtil();
			}
			photoSelUtil.onActivityResult(this,  arg0,  arg1,  arg2);
			//表示是截取后的
			if (photoSelUtil.RESIZE_REQUEST_CODE==arg0 ) {
				Ilog(TAG, "imgURL = "+photoSelUtil.getImgString());
				if (photoSelUtil.getImgString() == null || photoSelUtil.getImgString().equals("")) {
					//上传取消
					downloadHandle.sendEmptyMessage(13);
				}else{
					File file = new File(photoSelUtil.getImgString());
					if (file.exists()) {
						//文件存在
						//开始上传
						upIndex = 5;
						post = new HttpMultipartPost(MainActivity.this, photoSelUtil.getImgString(),downloadHandle);
						post.execute(loadPhotoUrl);
					}else{
						Ilog(TAG, "文件不存在 ");
						downloadHandle.sendEmptyMessage(13);
					}
				}
			}
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
		{
			return true;
		}
		return false;
	};

	public void onStartDub() {
		try {
			if (!isFileExist("recordCache")) {
				createSDDir("recordCache");
			}
			path = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
					MyApplication.getInstance().getLoginName()+"recordCache/" + "record.mp3";
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
			mRecorder = new MediaRecorder();
			mRecorder.reset();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//定义音频来源
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//定义输出格式
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);//定义音频编码
			mRecorder.setAudioEncodingBitRate(96000);
			mRecorder.setAudioChannels(2);
			mRecorder.setAudioSamplingRate(44100);
			mRecorder.setOutputFile(path);
			mRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mRecorder.start();
	}

	/**
	 * 初始化评测SDK
	 */
	private void initSingEnge() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					//  获取引擎实例,设置测评监听对象
					engine = SingEngine.newInstance(MainActivity.this);
					engine.setListener(mResultListener);
					if (!isFileExist("recordCache/"+MyApplication.getInstance().getLoginName())) {
						createSDDir("recordCache/"+MyApplication.getInstance().getLoginName());
					}
					wavFile = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
							"recordCache/" +MyApplication.getInstance().getLoginName()+ "/record.wav";
					mp3File = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" +
							"recordCache/" +MyApplication.getInstance().getLoginName()+ "/record_new.mp3";
					engine.setWavPath(wavFile);
					//  设置引擎类型
					//引擎类型（在线 、 离线 、混合）,默认使用在线引擎
					//备注:   在线: "cloud"; 离线:"native"; 混合:"auto"
					engine.setServerType("native");
					//  设置是否开启VAD功能
					engine.setOpenVad(false, null);
//					engine.setOpenVad(true, "vad.0.1.bin");
//					engine.setFrontVadTime(3000);
//					engine.setBackVadTime(3000);
					//   构建引擎初始化参数
					JSONObject cfg_init = engine.buildInitJson("t135", "1eb07a38f3834b7ea666934cb0ce3085");
					//   设置引擎初始化参数
					engine.setNewCfg(cfg_init);
					//   引擎初始化
					engine.newEngine();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	private void start() {
		if (engine != null) {
			try {
				JSONObject request = new JSONObject();
				//段落评测
				request.put("coreType", "en.pred.score");
				request.put("refText", zimu);
				request.put("rank", 100);
				//构建评测请求参数
				JSONObject startCfg = engine.buildStartJson("guest", request);
				//设置评测请求参数
				engine.setStartCfg(startCfg);
				//开始测评
				engine.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	SingEngine.ResultListener mResultListener = new SingEngine.ResultListener() {
		@Override
		public void onBegin() {

		}

		@Override
		public void onResult(JSONObject jsonObject) {
			Ilog(TAG,"onResult come  = "+jsonObject.toString());
			//回调评测结果
			CheckBean bean = (CheckBean) KingSoftParasJson.getObjectByJson(jsonObject.toString(), CheckBean.class);
			if (bean != null) {
				CheckBean.otherBean oBean = bean.getResult();
				if (oBean != null) {
					final int score = oBean.getOverall();
					Ilog(TAG,"core = "+oBean.getOverall());
				}else {
					Ilog(TAG,"oBean is null ");
				}
			}else{
				Ilog(TAG,"bean is null ");
			}
		}

		@Override
		public void onEnd(int i, String s) {

		}

		@Override
		public void onUpdateVolume(int i) {

		}

		@Override
		public void onFrontVadTimeOut() {

		}

		@Override
		public void onBackVadTimeOut() {

		}

		@Override
		public void onRecordingBuffer(byte[] bytes) {

		}

		@Override
		public void onRecordLengthOut() {

		}

		@Override
		public void onReady() {

		}

		@Override
		public void onPlayCompeleted() {

		}
	};

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final Uri uri = intent.getData();
			String path = uri.getPath();
			//弹出提示框
			ExitDialog dialog =new ExitDialog(MainActivity.this, postOrCanser, path);
			dialog.showDialog(1, 0);
			dialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
						return true;
					else
						return false;
				}
			});
//			if(upDialog != null){
//				if(!upDialog.isShowing()){
//					upDialog.setTextPro("上传中");
//					upDialog.setTextNotic("");
//					upDialog.showDialog(1, 1);
//				}
//			}
//			//			upLoadFile(path,upLoadUrl,downloadHandle);
//			//上传微课
//			post = new HttpMultipartPost(MainActivity.this, path,downloadHandle);
//			post.execute(upLoadUrl);
		}
	};

	public void checkPrimiss(){
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		boolean xp = permissionUtils.isHasAudioRecordingPermission(MainActivity.this);
		Message msg = new Message();
		msg.what = 5;
		msg.obj = xp;
		downloadHandle.sendMessage(msg);
		return;
	}

	/**
	 * 清楚webView  缓存
	 */
	public void clearCache(){
		try {
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		webView.clearHistory();
	}

	private PostOrCanser postOrCanser = new PostOrCanser() {
		@Override
		public void upLoad(Intent intent) {
			if (intent != null) {
				String name = intent.getStringExtra("name");
				path = intent.getStringExtra("path");
				upLoadName = name;
				if(upDialog != null){
					if(!upDialog.isShowing()){
						upDialog.setTextPro("上传中");
						upDialog.setTextNotic("");
						upDialog.showDialog(1, 1);
					}
				}
				//			upLoadFile(path,upLoadUrl,downloadHandle);
				//上传微课
				post = new HttpMultipartPost(MainActivity.this, path,downloadHandle);
				post.execute(upLoadUrl);
			}
		}

		@Override
		public void Post() {

		}

		@Override
		public void Canser() {
			ToastUtils.showToast(MainActivity.this,"取消上传微课视频");
		}
	};

	public void audioCheckResult(int  result){
		Ilog(TAG, "audioCheckResult come "+result);
	}

	public static MainActivity getInstance() {
		if (mManager == null) {
			mManager = new MainActivity();
		}
		Ilog(TAG,"mManager=" + mManager);
		return mManager;
	}

	/**
	 *停止录音
	 */
	private void stop() {
		if (engine != null) {
			engine.stop();
		}
	}

	/**
	 * 将wav转Mp3
	 */
	private void wavToMp3(){
		new Thread() {
			@Override
			public void run() {
				LameUtils utils = new LameUtils();
				utils.initLame(16000, 16000, 1);
				utils.wavTomp3(wavFile,mp3File);
				utils.closeLame();
			}
		}.start();
	}
}