package com.kingsun.teacherclasspro.widgets;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kingsun.teacherclasspro.activity.BaseActivity;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.config.Configure;
import com.kingsun.teacherclasspro.config.HandlerStrings;
import com.kingsun.teacherclasspro.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class WVJBWebViewClient extends WebViewClient {

	private static final String kTag = "WVJB";
	private static final String kInterface = kTag + "Interface";
	private static final String kCustomProtocolScheme = "wvjbscheme";
	private static final String kQueueHasMessage = "__WVJB_QUEUE_MESSAGE__";
	private static boolean logging = true;
	protected WebView webView;
	private ArrayList<WVJBMessage> startupMessageQueue = null;
	private Map<String, WVJBResponseCallback> responseCallbacks = null;
	private Map<String, WVJBHandler> messageHandlers = null;
	private long uniqueId = 0;
	private WVJBHandler messageHandler;
	private MyJavascriptInterface myInterface = new MyJavascriptInterface();
	private Handler handler;

	public interface WVJBResponseCallback {
		public void callback(Object data);
	}

	public interface WVJBHandler {
		public void request(Object data, WVJBResponseCallback callback);
	}

	public WVJBWebViewClient(WebView webView) {
		this(webView, null);
	}

	public WVJBWebViewClient(WebView webView, WVJBHandler msgHandler) {
		this.webView = webView;
		this.webView.getSettings().setJavaScriptEnabled(true);


		/***打开本地缓存提供JS调用**/  
		this.webView.getSettings().setDomStorageEnabled(true);  
		// Set cache size to 8 mb by default. should be more than enough  
//		this.webView.getSettings().setAppCacheMaxSize(1024*1024*8);  
		// This next one is crazy. It's the DEFAULT location for your app's cache  
		// But it didn't work for me without this line.  
		// UPDATE: no hardcoded path. Thanks to Kevin Hawkins  
		this.webView.getSettings().setAllowFileAccess(true);  
		this.webView.getSettings().setAppCacheEnabled(true); 
		this.webView.getSettings().setDefaultTextEncodingName("utf-8");
		
		//设置自适应屏幕，两者合用
//		this.webView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小 
//		this.webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//		this.webView.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//		this.webView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//		this.webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		this.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//		this.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		if(Build.VERSION.SDK_INT > 19) {
			/*对系统API在19以上的版本作了兼容。因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时，会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。*/
			webView.getSettings().setLoadsImagesAutomatically(true);
		} else {  
			webView.getSettings().setLoadsImagesAutomatically(false);  
		}  

//		this.webView.addJavascriptInterface(myInterface, kInterface);
		this.responseCallbacks = new HashMap<String, WVJBResponseCallback>();
		this.messageHandlers = new HashMap<String, WVJBHandler>();
		this.startupMessageQueue = new ArrayList<WVJBMessage>();
		this.messageHandler = msgHandler;
	}

	public void enableLogging() {
		logging = true;
	}

	public void send(Object data) {
		send(data, null);
	}

	public void webViewLoadFinished(Handler _handler) {
		this.handler = _handler;
	}

	public void send(Object data, WVJBResponseCallback responseCallback) {
		sendData(data, responseCallback, null);
	}

	public void callHandler(String handlerName) {
		callHandler(handlerName, null, null);
	}

	public void callHandler(String handlerName, Object data) {
		callHandler(handlerName, data, null);
	}

	/**
	 * 
	 * @param handlerName JS方法名
	 * @param data 参数
	 * @param responseCallback 回调函数
	 */
	public void callHandler(String handlerName, Object data, WVJBResponseCallback responseCallback) {
		Log.i(kTag, "callHandler - come" );
		sendData(data, responseCallback, handlerName);
	}

	public void registerHandler(String handlerName, WVJBHandler handler) {
		Log.i(kTag, "registerHandler - handlerName = "+handlerName );
		if (handlerName == null || handlerName.length() == 0 || handler == null)
			return;
		messageHandlers.put(handlerName, handler);
	}

	private void sendData(Object data, WVJBResponseCallback responseCallback, String handlerName) {
		Log.i(kTag, "sendData - come" );
		if (data == null && (handlerName == null || handlerName.length() == 0))
			return;
		WVJBMessage message = new WVJBMessage();
		if (data != null) {
			message.data = data;
		}
		if (responseCallback != null) {
			String callbackId = "objc_cb_" + (++uniqueId);
			responseCallbacks.put(callbackId, responseCallback);
			message.callbackId = callbackId;
		}
		if (handlerName != null) {
			message.handlerName = handlerName;
		}
		queueMessage(message);
	}

	private void queueMessage(WVJBMessage message) {
		if (startupMessageQueue != null) {
			startupMessageQueue.add(message);
		} else {
			dispatchMessage(message);
		}
	}

	private void dispatchMessage(WVJBMessage message) {
		String messageJSON = message2JSONObject(message).toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\'", "\\\\\'").replaceAll("\n", "\\\\\n").replaceAll("\r", "\\\\\r").replaceAll("\f", "\\\\\f");
		log("SEND", messageJSON);
		executeJavascript("WebViewJavascriptBridge._handleMessageFromObjC('" + messageJSON + "');");
	}

	private JSONObject message2JSONObject(WVJBMessage message) {
		JSONObject jo = new JSONObject();
		try {
			if (message.callbackId != null) {
				jo.put("callbackId", message.callbackId);
			}
			if (message.data != null) {
				jo.put("data", message.data);
			}
			if (message.handlerName != null) {
				jo.put("handlerName", message.handlerName);
			}
			if (message.responseId != null) {
				jo.put("responseId", message.responseId);
			}
			if (message.responseData != null) {
				jo.put("responseData", message.responseData);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

	private WVJBMessage JSONObject2WVJBMessage(JSONObject jo) {
		WVJBMessage message = new WVJBMessage();
		try {
			if (jo.has("callbackId")) {
				message.callbackId = jo.getString("callbackId");
			}
			if (jo.has("data")) {
				message.data = jo.get("data");
			}
			if (jo.has("handlerName")) {
				message.handlerName = jo.getString("handlerName");
			}
			if (jo.has("responseId")) {
				message.responseId = jo.getString("responseId");
			}
			if (jo.has("responseData")) {
				message.responseData = jo.get("responseData");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}

	private void flushMessageQueue() {
		Log.i(kTag, "flushMessageQueue - come" );
		String script = "WebViewJavascriptBridge._fetchQueue()";
		executeJavascript(script, new JavascriptCallback() {
			public void onReceiveValue(String messageQueueString) {
				if (messageQueueString == null || messageQueueString.length() == 0)
					return;
				Log.i(kTag, "flushMessageQueue - come2 = "+ messageQueueString);
				processQueueMessage(messageQueueString);
			}
		});
	}

	private void processQueueMessage(String messageQueueString) {
		Log.i(kTag, "processQueueMessage - come = "+messageQueueString );
		try {
			JSONArray messages = new JSONArray(messageQueueString);
			for (int i = 0; i < messages.length(); i++) {
				JSONObject jo = messages.getJSONObject(i);
				log("RCVD", jo);
				WVJBMessage message = JSONObject2WVJBMessage(jo);
				if (message == null) {
					return;
				}
				Log.i(kTag,"handlerName = "+message.handlerName);
				if (message.handlerName!= null &&message.handlerName.startsWith("loginName")) {
					handler.sendEmptyMessage(103);
					MyApplication.getInstance().setLoginName((String)message.data);
				}
				if (message.handlerName!= null &&message.handlerName.startsWith("Islogin")) {
					handler.sendEmptyMessage(102);
				}
				if (message.responseId != null) {
					WVJBResponseCallback responseCallback = responseCallbacks.remove(message.responseId);
					if (responseCallback != null) {
						responseCallback.callback(message.responseData);
					}
				} else {
					WVJBResponseCallback responseCallback = null;
					if (message.callbackId != null) {
						final String callbackId = message.callbackId;
						responseCallback = new WVJBResponseCallback() {
							@Override
							public void callback(Object data) {
								WVJBMessage msg = new WVJBMessage();
								msg.responseId = callbackId;
								msg.responseData = data;
								queueMessage(msg);
							}
						};
					}
					WVJBHandler handler;
					if (message.handlerName != null) {
						handler = messageHandlers.get(message.handlerName);
					} else {
						handler = messageHandler;
					}
					if (handler != null) {
						handler.request(message.data, responseCallback);
					}
				}
			}
		} catch (Exception e) {
			Log.i(kTag,"json 解析异常");
			e.printStackTrace();
		}
	}

	void log(String action, Object json) {
		if (!logging)
			return;
		String jsonString = String.valueOf(json);
		if (jsonString.length() > 500) {
			Log.i(kTag, action + ": " + jsonString.substring(0, 500) + " [...]");
		} else {
			Log.i(kTag, action + ": " + jsonString);
		}
	}

	public void executeJavascript(String script) {
		executeJavascript(script, null);
	}

	public void executeJavascript(final String script, final JavascriptCallback callback) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webView.evaluateJavascript(script, new ValueCallback<String>() {
				@Override
				public void onReceiveValue(String value) {
					Log.i(kTag, "onReceiveValue  - come  value= "+value );
					if (callback != null) {
						if (value != null && value.startsWith("\"") && value.endsWith("\"")) {
							value = value.substring(1, value.length() - 1).replaceAll("\\\\", "");
						}
						Log.i(kTag, "onReceiveValue  - come2  value= "+value );
						callback.onReceiveValue(value);
					}
				}
			});
		} else {
			if (callback != null) {
				myInterface.addCallback(++uniqueId + "", callback);
				webView.loadUrl("javascript:window." + kInterface + ".onResultForScript(" + uniqueId + "," + script + ")");
			} else {
				webView.post(new Runnable() {
					@Override
					public void run() {
						webView.loadUrl("javascript:" + script);
					}
				});
			}
		}
	}

	/**
	 * 
	 * loginOut( 注入js代码，在注入代码的同时需要清除本地缓存的数据) TODO(这里描述这个方法适用条件 – 使用挤下线等异常通知) TODO(这里描述这个方法的执行流程 – 收到心跳包-解析 -根据解析内容进行广播) TODO(这里描述这个方法的使用方法 – 直接调用) TODO(这里描述这个方法的注意事项 – 注意，loginOut.js.txt 不能清空，不需要对内容进行修改)
	 * 
	 * @param
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public void loginOut() {
		try {
			InputStream is = webView.getContext().getAssets().open("loginOut.js.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String js = new String(buffer);
			executeJavascript(js);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadHtml() {
		try {
			InputStream is = webView.getContext().getAssets().open("loadHtml.js.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String js = new String(buffer);
			executeJavascript(js);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
  
	@Override
	public void onPageFinished(WebView view, String url) {
		Log.i(kTag, "onPageFinished  - come  url= "+url );
		if(!webView.getSettings().getLoadsImagesAutomatically()) {  
			webView.getSettings().setLoadsImagesAutomatically(true);  
		} 
		try {
			InputStream is = webView.getContext().getAssets().open("WebViewJavascriptBridge.js.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String js = new String(buffer);
			executeJavascript(js);
			//			loadHtml();
			if (url.contains("setting.html")) // 遇到登陆页面，title 改变颜色
				Utils.hashMapSavadate.remove("backUrl");
			if (url.contains("index.html"))// 页面加载完毕
				handler.sendEmptyMessage(HandlerStrings.MAIN_WEBVIEW_LOADFINISHED_BG);
			if (url.contains("SelectBook.aspx")|| url.contains("Index.aspx")|| url.contains("index.aspx")) {
				handler.sendEmptyMessage(101);
			}if (url.contains("ChangePswd.aspx")) {
				handler.sendEmptyMessage(102);
			}if (url.contains("PersonCenter/Index1.aspx")){
				//返回了个人中心
				handler.sendEmptyMessage(103);
			}else{
				//告诉页面用户密码
				handler.sendEmptyMessage(10000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (startupMessageQueue != null) {
			for (int i = 0; i < startupMessageQueue.size(); i++) {
				dispatchMessage(startupMessageQueue.get(i));
			}
			startupMessageQueue = null;
		}
		super.onPageFinished(view, url);

		if (url.contains(Configure.HTML_SOURCE)) {
			webView.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:function myFunction(){x=document.getElementById(\"SoundEffect.sound.play()\");}");
					webView.loadUrl("javascript:function myFunction(){x=document.getElementById(\"talkAnimateControler.talkSound.play()\");}");
					webView.loadUrl("javascript:function myFunction(){x=document.getElementById(\"talkAnimateControler.effectSound.play()\");}");
					webView.loadUrl("javascript:function myFunction(){x=document.getElementById(\"talkAnimateControler.bgSound.play()\");}");

					// webView.loadUrl("javascript:" + "SoundEffect.sound.play()");
					// webView.loadUrl("javascript:" + "talkAnimateControler.talkSound.play()");
					// webView.loadUrl("javascript:" + "talkAnimateControler.effectSound.play()");
					// webView.loadUrl("javascript:" + "talkAnimateControler.bgSound.play()");
				}
			});
		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		Log.i(kTag, "onReceivedError  - come  errorCode= "+errorCode );
		// view.stopLoading();
		// // 载入本地assets文件夹下面的错误提示页面404.html
		webView.clearHistory();
		handler.sendEmptyMessage(HandlerStrings.MAIN_WEBVIEW_404);
//		webView.loadUrl(Configure.HTML_404);
		if (failingUrl.contains(Configure.HTML_SOURCE)) {
			//			CourseInforProcess processer = new CourseInforProcess(SysApplication.getInstance());
			//			try {
			//				String k = failingUrl.substring(failingUrl.indexOf(Configure.HTML_SOURCE) + Configure.HTML_SOURCE.length(), failingUrl.indexOf(Configure.HTML_SOURCE) + Configure.HTML_SOURCE.length() + 1);
			//				if (k.contains("1") || k.contains("2")) {
			//					CourseInfo info = processer.get(k);// 从数据库查询数据，如果返回为空，则没有数据
			//					if (null != info) {
			////						info.setDownloadingState(CourseDownloadManager.STATE_NONE);
			////						new CourseInforProcess(SysApplication.getInstance()).update(info);
			//					}
			//				}
			//			} catch (Exception e) {
			//				e.printStackTrace();
			//			}
		} else {
//			handler.sendEmptyMessage(HandlerStrings.MAIN_WEBVIEW_404);
			//			Toast_Util.ToastStringLong(SysApplication.getInstance().currentActivity(), "检测到程序文件被恶意删除，自然拼读正在自动修复");
			//			Intent intentPay = new Intent(SysApplication.getInstance().currentActivity(), MainActivity.class);
			//			intentPay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//			SysApplication.getInstance().currentActivity().startActivity(intentPay);
		}
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.i(kTag, "shouldOverrideUrlLoading  - come  url= "+url );
		if (url.startsWith(kCustomProtocolScheme)) {
			if (url.indexOf(kQueueHasMessage) > 0) {
				flushMessageQueue();
			}
			return true;
		}
		if (url.contains(Configure.HTML_SOURCE)) {
			BaseActivity.loadHistoryUrls.add(url);
		}
		if (url.contains(Configure.HTML_LIST)) {
			BaseActivity.loadHistoryUrls.clear();
		}
		webView.clearCache(true);// 清除网页访问留下的缓存，由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		super.onScaleChanged(view, oldScale, newScale);
	}

	public static class WVJBMessage {
		Object data = null;
		String callbackId = null;
		String handlerName = null;
		String responseId = null;
		Object responseData = null;
	}

	private class MyJavascriptInterface {
		Map<String, JavascriptCallback> map = new HashMap<String, JavascriptCallback>();

		public void addCallback(String key, JavascriptCallback callback) {
			map.put(key, callback);
		}

		@JavascriptInterface
		public void onResultForScript(String key, String value) {
			Log.i(kTag, "onResultForScript: " + value);
			JavascriptCallback callback = map.remove(key);
			if (callback != null)
				callback.onReceiveValue(value);
		}
	}

	public interface JavascriptCallback {
		public void onReceiveValue(String value);
	};

	public String createSDDir(String dirName) throws IOException {
		String url = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" + dirName;
		File dir = new File(url);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
		}
		return url;
	}
}
