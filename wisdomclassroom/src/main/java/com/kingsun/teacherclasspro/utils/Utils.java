package com.kingsun.teacherclasspro.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import com.kingsun.teacherclasspro.config.Configure;

/**
 * @ClassName: Utils
 * @Description: TODO(公共工具方法类)
 * @author LXL
 * @date 2016-1-6 上午9:33:34
 * 
 */
public class Utils {

	private static Context context;

	public static void Utils(Context contexts) {
		context = contexts;
	}


	public static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

	public static String listPayCallbackUrl;// 在列表页面进入购买页面成功后返回的url页面地址

	public static HashMap<String, String> hashMapSavadate = new HashMap<String, String>();

	public static HashMap<String, String> hashMapSaveCurUrl = new HashMap<String, String>();

	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	//上传文件格式
	public static String[] fileFormat = {
		"TXT", "HTM", "HTML", "RTF",  "pdf","log","text","xml",//文本格式
		"doc","docx","docm","dotx","dotm",//Word 后缀名
		"xls","xlsx","xlsm","xltx","xltm","xlsb","xlam",//Excel 文件扩展名
		"dps", "dpt", "pot","pps","PPt","pptx","wps","et"
		,"pptm","ppsx","ppsm","potx","potm","ppam", //PowerPoint 文件扩展名
		"JPG", "GIF", "BMP", "tif", "eps", "GIF","png",//图像格式
		"MP4", "MOV",  "WMV", "RM","AVI","MPG", "dat","vob","avi",//视频格式
		"WAV","MP3","WMA", "MID", "aif", "vqf", "aac","ac3","m4a",//音频格式
		"zip","rar"//压缩文件
	};
	//	protected static MessageDigest messagedigest = null;
	//	static {
	//		try {
	//			messagedigest = MessageDigest.getInstance("MD5");
	//		} catch (NoSuchAlgorithmException nsaex) {
	//			System.err.println("初始化失败，MessageDigest不支持MD5Util。");
	//			nsaex.printStackTrace();
	//		}
	//	}

	/**
	 * 是否能上传的文件类型
	 * @param targetValue
	 * @return
	 */
	public static boolean isCanUpload(String targetValue){
		for(String s: fileFormat){
			if(s.equalsIgnoreCase(targetValue))
				return true;
		}
		return false;
	}
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	//	public static String getFileMD5String(File file) throws IOException {
	//		InputStream fis;
	//		fis = new FileInputStream(file);
	//		byte[] buffer = new byte[1024];
	//		int numRead = 0;
	//		while ((numRead = fis.read(buffer)) > 0) {
	//			messagedigest.update(buffer, 0, numRead);
	//		}
	//		fis.close();
	//		return bufferToHex(messagedigest.digest());
	//	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4]; // 取字节中高 4 位的数字转换, >>>
		// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf]; // 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	// 判断两个MD5串是否相同，不区分大小写
	public static boolean checkMd5(String md5Str1, String md5Str2) {
		if (md5Str1 == null) {
			return false;
		} else {
			return md5Str1.equalsIgnoreCase(md5Str2);
		}
	}

	/**
	 * @author DW
	 * @Description 该方法是校验格式的，目前包括的校验功能为： 1、手机号码格式验证（11位数字）； 2、手机号是否为移动号码验证； 3、新、旧密码格式验证（6-18位数字、字母、标点符号）； 4、验证码格式（6位数字）正确性；
	 * @param string 要校验的字符串
	 * @param flag 校验类型： 0 表示验证手机号格式正确性； 1表示验证是否为移动号码； 2表示验证密码格式正确性； 3表示校验验证码格式正确性；4表示旧密码格式验证
	 * @return boolean isRight 校验结果
	 */
	public static boolean checkFormatting(String string, int flag) {
		boolean isRight = false;
		String patternStr = null;
		switch (flag) {
		case 0:
			patternStr = "^1[3|4|5|7|8][0-9]\\d{8}$";// 11位手机号码格式的正则表达式
			break;

		case 1:
			/*
			 * 目前中国移动号段正则表达式，包括: 134[0-8] +7位 13[5|6|7|8|9] +8位 147+8位 (3G上网卡) 15[0|1|2|7|8|9] +8位 1705 +7位 (移动虚拟运营商) 178 +8位 (4G号段) 18[2|3|4|7|8] +8位
			 */
			patternStr = "^1((3[5-9]|(47)|(5([0-2]|[7-9]))|(78)|(8([2-4]|[7-8])))\\d{8})" + "|(((34[0-8])|(705))\\d{7})$";
			break;

		case 2:
			/*
			 * 新密码格式必须是6-18位 可以包含数字、字母，可以是单纯的数字或字母
			 */
			patternStr = "^[A-Z0-9a-z]{6,18}$";
			break;

		case 4:
			/*
			 * 旧密码格式必须是6-18位 可以包含数字、字母、下划线，可以是单纯的数字或字母
			 */
			patternStr = "^.{6,18}$";
			break;

		case 3:
			patternStr = "^\\d{6}$";// 验证码格式，6位数字
			break;

		case 5:
			patternStr = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";// 邮箱
			break;

		case 6:
			patternStr = "^[A-Z0-9a-z_.]+$";// 用户名格式
			break;

		case 7:
			patternStr = "^\\w+$";// 学校和班级格式
			break;
		}

		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			isRight = true;
		}
		return isRight;
	}

	/**
	 * @Title: ReadFile
	 * @Description: TODO(从指定路径读取文件内容)
	 * @param @param Path
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String ReadFile(String path) {
		File file = new File(path);
		String laststr = "";
		if (sdCardExists() && file.exists()) {
			BufferedReader reader = null;
			try {
				FileInputStream fileInputStream = new FileInputStream(path);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					laststr += tempString;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return laststr;
	}

	/**
	 * @Title: dpToPx
	 * @Description: TODO(dp转px)
	 * @param @param res
	 * @param @param dp
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	/**
	 * @Title: getFilePathBySuffix
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param path
	 * @param @param suffix
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getFilePathBySuffix(String path, String suffix) {
		String filePath = null;
		File file = new File(path);
		if (sdCardExists() && file != null) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						if (!files[i].isDirectory() && (files[i].getName().trim().indexOf(suffix) != -1)) {
							filePath = files[i].getAbsolutePath();
						}
					}
				}
			}
		}
		return filePath;
	}

	/*
	 * 判断网络连接是否已开 2012-08-20true 已打开 false 未打开
	 */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}

	// 判断网络连接是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
		} else {
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 判断WiFi是否打开
	public static boolean isWiFi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	// 判断移动数据是否打开
	public static boolean isMobile(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 利用ShareedPreference保存必要信息
	 * 
	 * @param ctx 上下文Context
	 * @param name 要保存信息的名字
	 * @param value 要保存的值
	 */
	public static void sharePreSave(Context ctx, String name, String value) {
		if (null == value)
			return;
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		pref.edit().putString(name, value).commit();
	}

	public static void sharePreSaveInt(Context ctx, String name, int value) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		pref.edit().putInt(name, value).commit();
	}

	public static void sharePreSaveInt(Context ctx, String sharedName, String name, int value) {
		SharedPreferences pref = ctx.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		pref.edit().putInt(name, value).commit();
	}

	/**
	 * 利用ShareedPreference保存必要信息
	 * 
	 * @param ctx 上下文Context
	 * @param name 要保存信息的名字
	 * @param value 要保存的值
	 */
	public static void sharePreSaveBoolean(Context ctx, String name, Boolean value) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		pref.edit().putBoolean(name, value).commit();
	}

	public static void sharePreSaveBoolean(Context ctx, String sharedName, String name, Boolean value) {
		SharedPreferences pref = ctx.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		pref.edit().putBoolean(name, value).commit();
	}

	/**
	 * 获取ShareedPreference保存的信息
	 * 
	 * @param ctx 上下文Context
	 * @param name 所保存信息的名字
	 */
	public static String sharePreGet(Context ctx, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		return pref.getString(name, "");
	}

	public static int sharePreGet(Context ctx, String sharedName, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		return pref.getInt(name, -1);
	}

	/**
	 * 获取ShareedPreference保存的信息 int 类型
	 * 
	 * @param ctx 上下文Context
	 * @param name 所保存信息的名字
	 */

	public static int sharePreGetInt(Context ctx, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		return pref.getInt(name, -1);
	}

	/**
	 * 获取ShareedPreference保存的信息
	 * 
	 * @param ctx 上下文Context
	 * @param name 所保存信息的名字
	 */
	public static boolean sharePreGetBoolean(Context ctx, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		return pref.getBoolean(name, false);
	}

	public static boolean sharePreGetBoolean(Context ctx, String sharedName, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
		return pref.getBoolean(name, false);
	}

	/**
	 * 删除ShareedPreference保存的信息
	 * 
	 * @param ctx 上下文Context
	 * @param name 所要删除信息的名字
	 */
	public static void sharePreRemo(Context ctx, String name) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		pref.edit().remove(name).commit();
	}

	public static void sharePreDelete(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(Configure.preNaturallySpell, Context.MODE_PRIVATE);
		Editor ed = pref.edit();
		ed.clear();
		ed.commit();
	}





	/**
	 * 获取扩展SD卡存储目录
	 * 
	 * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录 否则：返回内置SD卡目录
	 * 
	 * @return
	 */
	public static String getExternalSdCardPath(String filePath) {
		if (sdCardExists()) {
			File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filePath);
			return sdCardFile.getAbsolutePath();
		} else {
			Log.e("U", "SD卡不存在.. 正在app 文件目录下创建文件夹");
			File filess = new File(context.getFilesDir(), filePath);
			return filess.getAbsolutePath();
		}
	}

	/**
	 * 判断SD卡是否挂载
	 * 
	 * @return
	 */
	public static boolean sdCardExists() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * @Description 获取版本方法
	 * @param ctx
	 * @return
	 */
	public static String getVersion(Context ctx) {
		String version;
		try {
			PackageManager manager = ctx.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			version = "1.0.0";
		}
		return version;
	}

	/**
	 * @Description 获取版本号方法
	 * @param ctx
	 * @return
	 */
	public static int getVersionCode(Context ctx) {
		int version;
		try {
			PackageManager manager = ctx.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
			version = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			version = -1;
		}
		return version;
	}

	public static void fileScan(Context context, String file) {
		Uri data = Uri.parse("file://" + file);
		Log.d("TAG", "file:" + file);
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
	}

	public static void folderScan(Context context, String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] array = file.listFiles();
			for (int i = 0; i < array.length; i++) {
				File f = array[i];
				if (f.isFile()) {// FILE TYPE
					String name = f.getName();
					if (name.contains(".mp3") || name.contains(".jpg")) {
						fileScan(context, f.getAbsolutePath());
					}
				} else {// FOLDER TYPE
					folderScan(context, f.getAbsolutePath());
				}
			}
		}
	}

	public static String getPercentString(double d) {
		String percent = "";
		// 获取格式化对象
		NumberFormat nt = NumberFormat.getPercentInstance();
		// 设置百分数精确度2即保留两位小数
		nt.setMinimumFractionDigits(2);
		percent = nt.format(d);
		if (percent.equalsIgnoreCase("NAN"))
			percent = "等待下载...";
		return percent;
	}

	// -----------------------------time -----------------------
	/**
	 * 
	 * getCurrentChuoLong(得到当前系统时间，并以long的形式)
	 * 
	 * @param @return 设定文件
	 * @return String DOM对象
	 * @Exception 异常对象
	 * @since CodingExample　Ver(编码范例查看) 1.1
	 */
	public final static String getManageDate() {
		String str = "";
		str = df.format(new Date()).toString();
		// System.out.println("当前日期"+df.format(new Date()));// new Date()为获取当前系统时间
		return str;
	}

	public static String addDate(String saveTime, int x) {
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		try {
			date = df.parse(saveTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		// System.out.println("输入的日期 :" + df.format(date)); // 显示输入的日期
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, x);// 加上 自动登陆的时限，例如：7天
		date = calendar.getTime();
		// System.out.println("免登陆日期 :" + df.format(date)); // 显示更新后的日期
		calendar = null;
		return df.format(date) + "";
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds 精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date 字符串日期
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * 返回字符串 例如：1秒前 1分钟后
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String convertTimeToFormatString(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = timeStamp - curTime;

		if (time < 60 && time >= 0) {
			return "已过期";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟后";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时后";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天后";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月后";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年后";
		} else {
			return "已过期";
		}
	}

	public static long convertTimeToFormatInt(long timeStamp) {
		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = timeStamp - curTime;
		if (0 == time / 3600 / 24) {
			return 1;
		}
		return time / 3600 / 24;
	}

	/**
	 * 对比时间戳（精确到秒）
	 * 
	 * @return true 表示还在使用期内，false表示已经过期
	 */
	public static boolean timeStamp(int hqtime) {
		long time = System.currentTimeMillis() / 1000;// 系统当前时间戳
		Long s = (hqtime - time);// 到期时间减去 当前时间 得到时间差
		// long betweenDays = (long)((hqtime - time) / ( 60 * 60 *24) + 0.5); //天数
		if (s > 0)
			return true;
		return false;
	}

	/**
	 * 控制全屏
	 * @param activity
	 * @param enable = true全屏，false 非全屏
	 */
	public static void fullActivityTitle(Activity activity, boolean enable) {
		if (!enable) {
			// 显示状态栏
			WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().setAttributes(attr);
			activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			// 隐藏状态栏,全屏
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			activity.getWindow().setAttributes(lp);
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 判断是否安装
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isAppInstalled(Context context,String packagename)
	{
		PackageInfo packageInfo;        
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		}catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			System.out.println("没有安装");
			return false;
		}else{
			System.out.println("已经安装");
			return true;
		}
	}

	/**
	 * 跳转到易课+
	 * @param Package
	 */
	public static void gotoXKJ(Activity activity,String packageName,String  info,
			String className ){
		boolean sp = isAppInstalled(activity, packageName);
		if (sp) {
			//系统安装了易课+
			//			Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
			//			intent.putExtra("data", info);
			//			activity.startActivity(intent);

			Intent intent = new Intent();
			ComponentName cn = new ComponentName(packageName,className);
			intent.putExtra("data", info);
			intent.setComponent(cn);
			activity.startActivity(intent);
		}else{
			//跳转应用市场
			ToastUtils.showToast(activity, "请安装教师助手");
			//			Uri uri = Uri.parse("market://details?id="+packageName);//id为包名 
			//			Intent it = new Intent(Intent.ACTION_VIEW, uri); 
			//			activity.startActivity(it); 
		}
	}

	public static void startAty(Activity activity,String packageName,String className,String url){
		Intent intent = new Intent();
		intent.setPackage(packageName);
		intent.setComponent(new ComponentName(packageName, className));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setData(Uri.fromFile(new File(url)));
		try {
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Intent openFile(String filePath){
		File file = new File(filePath);  
		if(!file.exists()) return null;  
		/* 取得扩展名 */  
		String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();   
		/* 依扩展名的类型决定MimeType */  
		if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||  
				end.equals("xmf")||end.equals("ogg")||end.equals("wav")){  
			return getAudioFileIntent(filePath);  
		}else if(end.equals("3gp")||end.equals("mp4")){  
			return getAudioFileIntent(filePath);  
		}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||  
				end.equals("jpeg")||end.equals("bmp")){  
			return getImageFileIntent(filePath);  
		}else if(end.equals("apk")){  
			return getApkFileIntent(filePath);  
		}else if(end.equals("ppt")){  
			return getPptFileIntent(filePath);  
		}else if(end.equals("xls")){  
			return getExcelFileIntent(filePath);  
		}else if(end.equals("doc")){  
			return getWordFileIntent(filePath);  
		}else if(end.equals("pdf")){  
			return getPdfFileIntent(filePath);  
		}else if(end.equals("chm")){  
			return getChmFileIntent(filePath);  
		}else if(end.equals("txt")){  
			return getTextFileIntent(filePath,false);  
		}else{  
			return getAllIntent(filePath);  
		}  
	}  

	//Android获取一个用于打开APK文件的intent  
	public static Intent getAllIntent( String param ) {  

		Intent intent = new Intent();    
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param ));  
		intent.setDataAndType(uri,"*/*");   
		return intent;  
	}  
	//Android获取一个用于打开APK文件的intent  
	public static Intent getApkFileIntent( String param ) {  

		Intent intent = new Intent();    
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param ));  
		intent.setDataAndType(uri,"application/vnd.android.package-archive");   
		return intent;  
	}  

	//Android获取一个用于打开VIDEO文件的intent  
	public static Intent getVideoFileIntent( String param ) {  

		Intent intent = new Intent("android.intent.action.VIEW");  
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		intent.putExtra("oneshot", 0);  
		intent.putExtra("configchange", 0);  
		Uri uri = Uri.fromFile(new File(param ));  
		intent.setDataAndType(uri, "video/*");  
		return intent;  
	}  

	//Android获取一个用于打开AUDIO文件的intent  
	public static Intent getAudioFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");  
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		intent.putExtra("oneshot", 0);  
		intent.putExtra("configchange", 0);  
		Uri uri = Uri.fromFile(new File(param ));  
		intent.setDataAndType(uri, "audio/*");  
		return intent;  
	}  

	//Android获取一个用于打开Html文件的intent     
	public static Intent getHtmlFileIntent( String param ){  

		Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();  
		Intent intent = new Intent("android.intent.action.VIEW");  
		intent.setDataAndType(uri, "text/html");  
		return intent;  
	}  

	//Android获取一个用于打开图片文件的intent  
	public static Intent getImageFileIntent( String param ) {  

		Intent intent = new Intent("android.intent.action.VIEW");  
		intent.addCategory("android.intent.category.DEFAULT");  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		Uri uri = Uri.fromFile(new File(param ));  
		intent.setDataAndType(uri, "image/*");  
		return intent;  
	}  

	//Android获取一个用于打开PPT文件的intent     
	public static Intent getPptFileIntent( String param ){    
		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		Uri uri = Uri.fromFile(new File(param ));     
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");     
		return intent;     
	}     

	//Android获取一个用于打开Excel文件的intent     
	public static Intent getExcelFileIntent( String param ){    

		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		Uri uri = Uri.fromFile(new File(param ));     
		intent.setDataAndType(uri, "application/vnd.ms-excel");     
		return intent;     
	}     

	//Android获取一个用于打开Word文件的intent     
	public static Intent getWordFileIntent( String param ){    

		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		Uri uri = Uri.fromFile(new File(param ));     
		intent.setDataAndType(uri, "application/msword");     
		return intent;     
	}     

	//Android获取一个用于打开CHM文件的intent     
	public static Intent getChmFileIntent( String param ){     

		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		Uri uri = Uri.fromFile(new File(param ));     
		intent.setDataAndType(uri, "application/x-chm");     
		return intent;     
	}     

	//Android获取一个用于打开文本文件的intent     
	public static Intent getTextFileIntent( String param, boolean paramBoolean){     

		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		if (paramBoolean){     
			Uri uri1 = Uri.parse(param );     
			intent.setDataAndType(uri1, "text/plain");     
		}else{     
			Uri uri2 = Uri.fromFile(new File(param ));     
			intent.setDataAndType(uri2, "text/plain");     
		}     
		return intent;     
	}    
	//Android获取一个用于打开PDF文件的intent     
	public static Intent getPdfFileIntent( String param ){     

		Intent intent = new Intent("android.intent.action.VIEW");     
		intent.addCategory("android.intent.category.DEFAULT");     
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     
		Uri uri = Uri.fromFile(new File(param ));     
		intent.setDataAndType(uri, "application/pdf");     
		return intent;     
	}  

//	public static int getImageId(String url){
//		int  id = R.drawable.def;
//		if (url.endsWith(".ppt")) {
//			id = R.drawable.ppt;
//		}else if (url.endsWith(".mp3") || url.endsWith(".aac")||
//				url.endsWith(".ac3") || url.endsWith(".mp4a")) {
//			id = R.drawable.mp3;
//		}else if (url.endsWith(".mp4") || url.endsWith(".avi")||
//				url.endsWith(".wmv") || url.endsWith(".mov")) {
//			id = R.drawable.mp4;
//		}else if (url.endsWith(".pdf")) {
//			id = R.drawable.pdf;
//		}else if (url.endsWith(".doc") || url.endsWith(".docx")||
//				url.endsWith(".docm") || url.endsWith(".dotx")||
//				url.endsWith(".dotm")) {
//			id = R.drawable.doc;
//		}else if (url.equalsIgnoreCase(".txt") || url.equalsIgnoreCase(".log")) {
//			id = R.drawable.txt;
//		}else if (url.equalsIgnoreCase(".zip") || url.equalsIgnoreCase(".rar")) {
//			id = R.drawable.zip;
//		}else if (url.equalsIgnoreCase(".html") || url.equalsIgnoreCase(".htm")||
//				url.equalsIgnoreCase(".xml")) {
//			id = R.drawable.htm;
//		}
//		return id;
//	}
}
