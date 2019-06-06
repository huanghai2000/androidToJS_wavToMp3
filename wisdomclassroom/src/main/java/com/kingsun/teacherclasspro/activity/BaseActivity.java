package com.kingsun.teacherclasspro.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.kingsun.teacherclasspro.R;
import com.kingsun.teacherclasspro.application.MyApplication;
import com.kingsun.teacherclasspro.bean.DownLoadBean;
import com.kingsun.teacherclasspro.bean.Question;
import com.kingsun.teacherclasspro.utils.DownloadService;
import com.kingsun.teacherclasspro.utils.ToastUtils;
import com.kingsun.teacherclasspro.utils.Utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BaseActivity extends FragmentActivity {
	private Activity currActivity;
	protected long exitTime = 0;
	final String TAG = "BaseActivity";
	public PowerManager pManager;
	//	public WakeLock mWakeLock;
	public String fileDownPath = Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 透明状态栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 将activity添加到list中
		MyApplication.getInstance().pushActivity(this);
	}

	public static void Dlog(String tag,String content){
		Log.d(tag, content);
	}

	public static void Elog(String tag,String content){
		Log.e(tag, content);
	}

	public static void Ilog(String tag,String content){
		Log.i(tag, content);
	}

	public static void Wlog(String tag,String content){
		Log.w(tag, content);
	}
	@Override
	public void onResume() {
		super.onResume();
		pManager = ((PowerManager) getSystemService(POWER_SERVICE));
//		mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
//				| PowerManager.ON_AFTER_RELEASE, TAG);
//		mWakeLock.acquire();
	}

	@Override
	public void onPause() {
		super.onPause();
//		if(null != mWakeLock){
//			mWakeLock.release();
//		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().popActivity(currActivity);
//		RefWatcher refWatcher = MyApplication.getRefWatcher(this);
//		refWatcher.watch(this);
	}

	// 获取历史列表
	public static ArrayList<String> loadHistoryUrls = new ArrayList<String>();

	public static boolean isFileExist(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" + fileName);
		file.isFile();
		return file.exists();
	}

	public static File createSDDir(String dirName) throws IOException {
		Ilog("MainActivity", "dirName = "+dirName);
		File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/KingSunSmart/Download/" + dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/** 打开文件 **/
	public void openFile(String path) {
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setAction(android.content.Intent.ACTION_VIEW);
//		File f = new File(path);
//		String type = FileUtil.getMIMEType(f.getName());
//		intent.setDataAndType(Uri.fromFile(f), type);
//		startActivity(intent);

		Intent intent = Utils.openFile(path);
		try {
			startActivity(intent);
		} catch (Exception e) {
			ToastUtils.showToast(getApplicationContext(), "安装相应应用后再打开");
		}
	}

	/**
	 * 下载
	 */
	public void downFile(final String url ,final Handler handler,final String fileName) {
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						if (!isFileExist(MyApplication.getInstance().getLoginName()+"/ppt")) {
							createSDDir(MyApplication.getInstance().getLoginName()+"/ppt");
						}
						File downFile = new File(fileName);
						fileOutputStream = new FileOutputStream(downFile);
						byte[] b = new byte[1024];
						int charb = -1;
						int count = 0;
						while ((charb = is.read(b)) != -1) {
							fileOutputStream.write(b, 0, charb);
							count += charb;
							Message msg = new Message();
							msg.what = 1;
							msg.obj = length;
							msg.arg2 = count;
							handler.sendMessage(msg);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


	/**
	 * 下载
	 */
	public void downFile(final DownLoadBean bean ,final Handler handler) {
//		Elog("MainActivity","downFile RUL = "+bean.getFileURL());
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(bean.getFileURL());
				HttpResponse response;
				try {
					response = client.execute(get);
					Header[] headers = response.getAllHeaders();
					String name = "";//文件后缀名
					String path = "";
					for (Header x :headers){
//						Elog("MainActivity","name "+x.getName()+";value "+x.getValue());
//						if (x.getName().equals("Content-Length")){
//							fileLength = x.getValue();
////							continue;
//						}
						if (x.getName().equals("Content-Type")){
							name = x.getValue().replace("application/",".");
							break;
						}
					}
					path = bean.getFilePath().replace("\\","/").substring(1);
					if (!isFileExist(path)) {
						createSDDir(path);
					}
					File downFile;
					if (isNULL(name)){
						downFile = new File(fileDownPath+"/"+path+"/"+bean.getFileID()+".zip");
					}else{
						downFile = new File(fileDownPath+"/"+path+"/"+bean.getFileID()+name);
					}
					if (!downFile.exists()){//不存在就下载
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();
						FileOutputStream fileOutputStream = null;
						if (is != null) {
							fileOutputStream = new FileOutputStream(downFile);
							byte[] b = new byte[1024];
							int charb = -1;
							int count = 0;
							while ((charb = is.read(b)) != -1) {
								fileOutputStream.write(b, 0, charb);
								count += charb;
//								Elog("MainActivity","下载进度 "+count+" ; size =" +
//										length+"; ==> "+(count/(length/100)));
								Message msg = new Message();
								msg.what = 1001;
								if (length == -1){
									msg.obj =  -1l;
								}else{
									msg.obj = (count/(length/100));
								}
								msg.arg2 = count;
								handler.sendMessage(msg);
							}
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
							if (length == -1){
								//表示未知大小的文件下载完毕
								Message msg = new Message();
								msg.what = 1001;
								msg.obj = 100l;
								handler.sendMessage(msg);
								Elog("MainActivity","获取大小异常，下载完成");
							}else {
								Elog("MainActivity","P0USH下载完成");
							}
						}
					}else{
						//存在就直接跳过
						Message msg = new Message();
						msg.what = 1001;
						msg.obj = 100l;
						msg.arg2 = 100;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Elog("MainActivity","下载出错");
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 下载
	 */
	public void downFile(final String url ,final Handler handler,final String path,final String fileName) {
		Elog("MainActivity","downFile RUL = "+url);
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					Header[] headers = response.getAllHeaders();
					String name = "";
					for (Header x :headers){
						Elog("MainActivity","name "+x.getName()+";value "+x.getValue());
						if (x.getName().equals("content-disposition")){
							name = x.getValue();
							name = name.substring(name.indexOf("filename="), name.length())
									.replace("filename=","")
									.replace("%20"," ");
							break;
						}
					}
					Elog("MainActivity","name END "+name);
					if (!isFileExist(path)) {
						createSDDir(path);
					}
					File downFile;
					if (isNULL(name)){
						downFile = new File(fileDownPath+path+fileName);
					}else{
						downFile = new File(fileDownPath+path+"/"+name);
					}
					if (!downFile.exists()){//不存在就下载
						HttpEntity entity = response.getEntity();
						long length = entity.getContentLength();
						InputStream is = entity.getContent();
						FileOutputStream fileOutputStream = null;
						if (is != null) {
							fileOutputStream = new FileOutputStream(downFile);
							byte[] b = new byte[1024];
							int charb = -1;
							int count = 0;
							while ((charb = is.read(b)) != -1) {
								fileOutputStream.write(b, 0, charb);
								count += charb;
								Message msg = new Message();
								msg.what = 1001;
								msg.obj = length;
								msg.arg2 = count;
								handler.sendMessage(msg);
							}
						}
						fileOutputStream.flush();
						if (fileOutputStream != null) {
							fileOutputStream.close();
						}
					}else{
						//存在就直接跳过
						Message msg = new Message();
						msg.what = 1001;
						msg.obj = 100l;
						msg.arg2 = 100;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Elog("MainActivity","下载出错");
					e.printStackTrace();
				}
			}
		}.start();
	}
	/**
	 * 判断文件的完整性
	 * @return
	 */
	public boolean isTureFile(final  String url, final long size,final Handler handler){
		final boolean sp = false;
		Ilog(TAG, "isTureFile ;size = "+size);
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(url);
					HttpResponse response;
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					Ilog(TAG, "length = "+length+";size = "+size);
					Message msg = new Message();
					if (length == size) {
						msg.what = 6;
					}else{
						msg.obj = url;
						msg.what = 7;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		return sp;
	}
	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				Elog("MainActivity",docId);
				final String[] split = docId.split(":");
				final String type = split[0];
				for (String xp :split) {
					Elog("MainActivity", xp);
				}
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
								String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * 上传文件
	 * @param fileName 文件绝对路径
	 */
	public  void upLoadFile(String  fileName,String upLoadUrl, final Handler handler){
		Ilog(TAG,"fileName = "+fileName);
		Ilog(TAG,"upLoadUrl = "+upLoadUrl);
		File file = new File(fileName);
		RequestParams params = new RequestParams(upLoadUrl);
		params.setConnectTimeout(10000);
		params.setMultipart(true);
		params.addBodyParameter("file",file);
		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {
				Log.i(TAG, "onCancelled = "+arg0);
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtils.showToast(getApplicationContext(),"上传失败");
				handler.sendEmptyMessage(3);
			}

			@Override
			public void onFinished() {
				Log.i(TAG, "onFinished = ");
			}

			@Override
			public void onSuccess(String arg0) {
				Log.i(TAG, "onSuccess ==》arg0 = "+arg0);
				Message msg = new Message();
				msg.what = 2;
				msg.obj = arg0;
				handler.sendMessage(msg);
			}
		});
	}

	public  interface doThing{
		public void changeIP(String head, String ip);
	}

	public long getFileSizes(File f) throws Exception{//取得文件大小
		long s=0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s= fis.available();
		} else {
			f.createNewFile();
		}
		return s;
	}

	@SuppressWarnings("deprecation")
	public void CheckActivityIn(){
		int version = Integer.valueOf(Build.VERSION.SDK);
		if(version  >= 5) {
			this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
		}
	}

	@SuppressWarnings("deprecation")
	public void CheckActivityStart(){
		int version = Integer.valueOf(Build.VERSION.SDK);
		if(version  >= 5) {
			this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}

	@SuppressWarnings("deprecation")
	public void CheckActivityOut(){
		int version = Integer.valueOf(Build.VERSION.SDK);
		if(version  >= 5) {
			this.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
		}
	}

	/***
	 * 回传数据
	 * @author hai.huang
	 *
	 */
	public  interface loadCallback{
		public void loadData(int po, Question qus);
	}

	/**
	 * 判断http地址是否存在
	 * @param url
	 * @return
	 */
	public void checkURL(final String url ,final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 11;
				try {
					HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
					int code = conn.getResponseCode();
					if (code == 200) {
						msg.what = 12;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}).start();
	}

	public interface PostOrCanser{
		//确定
		public void Post();
		//确定升级使用
		public void upLoad(Intent intent);
		//取消
		public void Canser();
	}

	public  String  getVersion(){
		String versionName = "";
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(getPackageName(),PackageManager.GET_CONFIGURATIONS);
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e){
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 有新版本时弹出的对话框
	 *
	 * @author huanghai
	 * @date 2014-9-30 上午10:23:13
	 * @param str
	 *            更新日志
	 * @param fileurl
	 *            apk下载链接
	 */
	public void upDataDialog(final Context context,String str, final String fileurl) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.upapp);
		builder.setMessage(str);
		builder.setPositiveButton(R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent = new Intent(context,DownloadService.class);
						System.out.println("come  url -> "+fileurl);
						intent.putExtra("url", fileurl);
						context.startService(intent);
					}
				});
		builder.create().show();
	}

	/**
	 * 递归删除 文件/文件夹
	 *
	 * @param file
	 */
	public void deleteFile(File file) {
		Ilog(TAG,"deleteFile = "+111);
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				Ilog(TAG,"deleteFile = "+files.length);
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}

	/***
	 * 读取本地文件
	 * @param
	 * @param fileName
	 * @return
	 */
	public static String readLocalJson(String fileName){
		Elog("MainActivity","readLocalJson "+fileName);
		String jsonString="";
		String resultString="";
		try {
			if (!new File(fileName).exists()){
				return resultString;
			}
//			InputStream inputStream=context.getResources().getAssets().open(fileName);
			InputStream inputStream=new FileInputStream(fileName);
			byte[] buffer=new byte[inputStream.available()];
			inputStream.read(buffer);
			resultString=new String(buffer,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			Elog("MainActivity","读取文件异常");
		}
		return resultString;
	}


	/***
	 * 字符串保存本地文件
	 * @param str
	 * @param fileName
	 */
	public void SaveFile(final  String str , final String fileName){
		new Thread() {
			public void run() {
				File myjson = new File(fileName);
				BufferedWriter bufferedWriter = null;
				try {
					bufferedWriter = new BufferedWriter(new FileWriter(myjson));
					for (int i = 0; i < str.length(); i++) {
						bufferedWriter.write(str.charAt(i));
					}
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}


	/***
	 * 字符串保存本地文件
	 * @param been
	 */
	public void SaveFileList(final ArrayList<DownLoadBean> been){
		new Thread() {
			public void run() {
				if (been != null && been.size() > 0){
					for (DownLoadBean loadBean : been){
						String fileName = "";
						String str = "";
						if (loadBean != null ){
							if (loadBean.getIsDownLoad() == 0){
								fileName = fileDownPath+"/"+loadBean.getFilePath().replace("\\","/");
								str = loadBean.getFileURL();
							}else if (loadBean.getIsDownLoad() == 2){
								fileName = fileDownPath+"/"+loadBean.getBookData().replace("\\","/");
								str = loadBean.getOther();
							}
						}
						File dir = new File(fileName.substring(0,fileName.lastIndexOf("/")));
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File myjson = new File(fileName);
						BufferedWriter bufferedWriter = null;
						try {
							bufferedWriter = new BufferedWriter(new FileWriter(myjson));
							for (int i = 0; i < str.length(); i++) {
								bufferedWriter.write(str.charAt(i));
							}
							bufferedWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	/**
	 * 判断是否为空
	 * @param info
	 * @return
	 */
	public boolean isNULL(String info){
		boolean sp = false;
		if (info == null || info.equals("") || info.equals("null") || info.equals("[]")){
			sp = true;
		}
		return  sp;
	}

	public static String getFileName(String path,String name){
		String fileName = "";
		File file = new File(path);
		if (file.exists()){
			File[] list = file.listFiles();
			if (list != null && list.length > 0){
				for (File f : list){
					if (f.isFile() && f.getName().startsWith(name)){
						fileName = f.getName() ;
					}
				}
			}
		}
		return fileName;
	}
}